package team3_Sub_3.featurealgo;


import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.classic.ParseException;
import java.util.Scanner;
import java.util.Set;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class EntityLinking {

/* This program combines has multiple features like- gender predictor,
which uses a keyword list of malewords and femalewords and 
the entity's inlink anchor text is considered as an alias name for the character */

	static HashMap<String, EntityCharacteristics> finalCSVMap = new HashMap<>();

	public static void usage() {
		System.out.println("Command line parameters: (header|pages|outlines|paragraphs) FILE");
		System.exit(-1);
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("file.encoding", "UTF-8");
		EntityGeneration entityGeneration = new EntityGeneration();

		Map<String, Entity> entityMap = new HashMap<>();
		List<Entity> entityPopularity = new ArrayList<>();
		if (args.length < 2)
			usage();

		String mode = args[0];
		if (mode.equals("header")) {
			final String pagesFile = args[1];
			final FileInputStream fileInputStream = new FileInputStream(new File(pagesFile));
			System.out.println(DeserializeData.getTrecCarHeader(fileInputStream));
			System.out.println();
		} else if (mode.equals("pages")) {
			final String pagesFile = args[1];
			final FileInputStream fileInputStream = new FileInputStream(new File(pagesFile));
			for (Data.Page page : DeserializeData.iterableAnnotations(fileInputStream)) {
				Entity entity = entityGeneration.generateEntity(page);
				entityMap.put(entity.getPageID(), entity);
				entityPopularity.add(PageRankPopularity.inlinkPageCount(page));
			}
		} else if (mode.equals("outlines")) {
			final String pagesFile = args[1];
			final FileInputStream fileInputStream3 = new FileInputStream(new File(pagesFile));
			for (Data.Page page : DeserializeData.iterableAnnotations(fileInputStream3)) {
				for (List<Data.Section> sectionPath : page.flatSectionPaths()) {
					System.out.println(Data.sectionPathId(page.getPageId(), sectionPath) + "   \t "
							+ Data.sectionPathHeadings(sectionPath));
				}
				System.out.println();
			}
		} else if (mode.equals("paragraphs")) {
			final String paragraphsFile = args[1];
			final FileInputStream fileInputStream2 = new FileInputStream(new File(paragraphsFile));
			for (Data.Paragraph p : DeserializeData.iterableParagraphs(fileInputStream2)) {
				System.out.println(p);
				System.out.println();
			}
		} else {
			usage();
		}

//	        final FileInputStream fileInputStream4 = new FileInputStream(new File("release.articles"));
//	        for(Data.Page page: DeserializeData.iterableAnnotations(fileInputStream4)) {
//	            for (Data.Page.SectionPathParagraphs line : page.flatSectionPathsParagraphs()){
//	                System.out.println(line.getSectionPath()+"\t"+line.getParagraph().getTextOnly());
//	            }
//	            System.out.println();
//	        }

		createEntityInlinkMapping(entityMap);
		PageRankPopularity.createEntityInlinkMapping(entityPopularity, finalCSVMap);
	}
/*
Create entity linking mapping essential to store the inlink ids as well thee anchor texts using 
which the gender predictor algorithm is based on.*/

	/**
	 * @param entities
	 * @throws IOException
	 */
	private static void createEntityInlinkMapping(Map<String, Entity> entityMap) throws IOException {
		Map<String, List<String>> entityInlinkMap = new HashMap<>();
		for (Map.Entry<String, Entity> entityEntry : entityMap.entrySet()) {
			entityInlinkMap.put(entityEntry.getValue().getPageName(), entityEntry.getValue().getAnchorTexts());
			// entityInlinkMap.put(e.getPageName(), e.getRedirectNames());

		}
	//	System.out.println();
		List<String> characterNames = mapEntityToCharacterNames(entityMap);
		// classifyGender(characterNames,entityInlinkMap);
	}
/* Constant method to be used wherever required, to get the mapping between entity and the actual character names */

	/**
	 * @param entityInlinkMap
	 * @return
	 * @throws IOException
	 */
	private static List<String> mapEntityToCharacterNames(Map<String, Entity> entityMap) throws IOException {
		GetCharacterName character = new GetCharacterName();
		List<String> characterNames = character.getActualNamesMap1();
		Map<String, List<String>> anchorTextsToCharacterNameMap = new HashMap<>();
		linkCharacter(entityMap, characterNames, anchorTextsToCharacterNameMap);
		return characterNames;
	}
/*Method writtern to link the character names and their anchor texts map created earlier
Useful for accessing the anchortexts mentioned in the entity 
better and efficiently. This method also predicts the gender of the character using the entity linker created with
the anchor texts.*/

	/**
	 * @param entityInlinkMap
	 * @param characterNames
	 * @param entitytoCharacterNameMap
	 */
	private static void linkCharacter(Map<String, Entity> entityMap, List<String> characterNames,
			Map<String, List<String>> anchorTextsToCharacterNameMap) throws IOException {

        BufferedWriter writer1 = new BufferedWriter(new FileWriter("data/GenderFeatureFinal2.csv"));
		Map<String, String> genderMap = new HashMap<>();
		Map<String, List<String>> paraMap = new HashMap<>();
		for (Map.Entry<String, Entity> entityEntry : entityMap.entrySet()) {
			String entityPageId = entityEntry.getKey();
			Entity entity = entityEntry.getValue();
			if (!anchorTextsToCharacterNameMap.containsKey(entityPageId)) {
				anchorTextsToCharacterNameMap.put(entityPageId, new ArrayList<>());
			}
			anchorTextsToCharacterNameMap.get(entityPageId).addAll(entity.getAnchorTexts());
			anchorTextsToCharacterNameMap.get(entityPageId).addAll(entity.getRedirectNames());

//			Commented for now. Will use further
//			for (String characterName : characterNames) {
//				boolean condition = entity.getPageName().trim().contains(characterName.trim());
//				if (condition) {
//					if (!anchorTextsToCharacterNameMap.containsKey(characterName)) {
//						anchorTextsToCharacterNameMap.put(characterName, new ArrayList<>());
//					}
//					
//				} else {
//					for (String redirectName : entity.getRedirectNames()) {
//						if (redirectName.contains(characterName)) {
//							if (!anchorTextsToCharacterNameMap.containsKey(characterName)) {
//								anchorTextsToCharacterNameMap.put(characterName, new ArrayList<>());
//							}
//							anchorTextsToCharacterNameMap.get(characterName).addAll(entity.getAnchorTexts());
//							anchorTextsToCharacterNameMap.get(characterName).addAll(entity.getRedirectNames());
//							break;
//						}
//					}
//				}
		}
		Scanner sc = new Scanner(
				new File("data/maleWords.txt"));
		CreateIndex c1 = new CreateIndex();
		/*Accessing the map of pagenames and the corresponding text*/
		paraMap = c1.createIndex();
		paraMap = c1.createIndex();
		SearchEng ss = new SearchEng();
		ss.choosesimilarity();
		Map<String, List<String>> pageIdCharacterNameMap = new HashMap<>();
		try {
			pageIdCharacterNameMap = ss.displayresults("runfile.run", entityMap);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		TDIDFDisambiguation tt = new TDIDFDisambiguation();
//		//ss.choosesimilarity();
//		Map<String, List<String>> TFIDFCharacterNameMap = new HashMap<>();
//		try {
//			TFIDFCharacterNameMap = tt.displayresults("runfile.run", entityMap);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		//resolveDisambiguations(entityMap, ss, pageIdCharacterNameMap);

		List<String> maleWords = new ArrayList<>();
		List<String> femaleWords = new ArrayList<>();
		while (sc.hasNext()) {
			String line = sc.next();
			maleWords.add(line);
		}

		Scanner sc1 = new Scanner(
				new File("data/femaleWords.txt"));
		while (sc1.hasNext()) {
			String line1 = sc1.next();
			femaleWords.add(line1);
		}
		for (Map.Entry<String, List<String>> entityInlink : anchorTextsToCharacterNameMap.entrySet()) {
			int maleCount = 0;
			int femaleCount = 0;
			for (String inLinkName : entityInlink.getValue()) {
				// System.out.println("InLinkName:" + inLinkName);
				// String line = sc.next();
				// System.out.println("male word:" + line);
				String a = inLinkName.toLowerCase().replaceAll("\\s+", "");
				// System.out.println(a);
				for (String maleWord : maleWords) {
					if (inLinkName.toLowerCase().replaceAll("\\s+", "").contains(maleWord.trim()))
						maleCount++;
				}
				// }

				// while (sc1.hasNext()) {
				// String line1 = sc1.next();
				for (String femaleWord : femaleWords) {
					if (inLinkName.toLowerCase().replaceAll("\\s+", "").contains(femaleWord.trim()))
						femaleCount++;
				}
				// }
			}
			if (maleCount > femaleCount) {
				genderMap.put(entityInlink.getKey(), "1");
//				finalCSVMap.put(entityInlink.getKey(), new EntityCharacteristics("1", null));
			} else if (maleCount < femaleCount) {
				genderMap.put(entityInlink.getKey(), "0");
//				finalCSVMap.put(entityInlink.getKey(), new EntityCharacteristics("0", null));
			} else if (maleCount == femaleCount) {

				for (String paraText : paraMap.get(entityInlink.getKey())) {
					// System.out.println("para text" + paraText);
					// String line = sc.next();
					// System.out.println("male word:" + line);
					String a = paraText.toLowerCase().replaceAll("\\s+", "");
					// System.out.println(a);
					for (String maleWord : maleWords) {
						if (paraText.toLowerCase().replaceAll("\\s+", "").contains(maleWord.trim()))
							maleCount++;
					}
					// }

					// while (sc1.hasNext()) {
					// String line1 = sc1.next();
					for (String femaleWord : femaleWords) {
						if (paraText.toLowerCase().replaceAll("\\s+", "").contains(femaleWord.trim()))
							femaleCount++;
					}
				}

				if (maleCount > femaleCount) {
					genderMap.put(entityInlink.getKey(), "1");
//					finalCSVMap.put(entityInlink.getKey(), new EntityCharacteristics("1", null));
				} else if (maleCount < femaleCount) {
					genderMap.put(entityInlink.getKey(), "0");
//					finalCSVMap.put(entityInlink.getKey(), new EntityCharacteristics("0", null));
				} else {
					genderMap.put(entityInlink.getKey(), "0");
//					finalCSVMap.put(entityInlink.getKey(), new EntityCharacteristics("Undetermined", null));
				}
			}
		}


		for (String characterName : characterNames) {
			for (Map.Entry<String, Entity> entityEntry : entityMap.entrySet()) {
				String pageId = entityEntry.getKey();
				Entity entity = entityEntry.getValue();
				if (findNameSimilarity(characterName, entity.getPageName())) {
					if (!finalCSVMap.containsKey(characterName)) {
						finalCSVMap.put(characterName, new EntityCharacteristics());
					}
					finalCSVMap.get(characterName).setGender(genderMap.get(pageId));
					break;
				}
				if (!finalCSVMap.containsKey(characterName)) {
					for(String redirectName: entity.getRedirectNames()) {
						if (findNameSimilarity(characterName, redirectName)) {
							finalCSVMap.put(characterName, new EntityCharacteristics());
							finalCSVMap.get(characterName).setGender(genderMap.get(pageId));
							break;
						}	
					}
				}
			}
			if (!finalCSVMap.containsKey(characterName)) {
				finalCSVMap.put(characterName, new EntityCharacteristics());
				finalCSVMap.get(characterName).setGender("0");
			}
		}
		sc.close();
		sc1.close();
		GetCharacterName c = new GetCharacterName();
		int count = 0;
		int actualco = 0;
		BufferedWriter writer = new BufferedWriter(new FileWriter("GenderFeature.csv"));
		HashMap<String, String> actualMap = c.getActualNamesMap();
		for (Map.Entry<String, String> actual : actualMap.entrySet()) {
			actualco++;
			for (Map.Entry<String, EntityCharacteristics> finalCSVEntry : finalCSVMap.entrySet()) {
				if (actual.getKey().equalsIgnoreCase(finalCSVEntry.getKey())) {
					// System.out.println(gender.getKey() + actual.getKey());
					if (actual.getValue().equals(finalCSVEntry.getValue().getGender())) {
						// System.out.println(gender.getKey() + actual.getKey());
						writer.append(actual.getKey() + "," + finalCSVEntry.getValue().getGender() + "\n");
						count++;
					}
				}

			}
		}
		writer.close();
	//	System.out.println(count + "match" + " out of" + actualco);
		// System.out.println(genderMap);
		
			getParagraph(c1, entityMap, pageIdCharacterNameMap, writer1);
	
	}
	
	/**
	 * @param entityMap
	 * @param ss
	 * @param pageIdCharacterNameMap
	 * @throws IOException
	 */
	public static void resolveDisambiguations(Map<String, Entity> entityMap, SearchEng ss,
			Map<String, List<String>> pageIdCharacterNameMap) throws IOException {
	//	Disambiguate dis = new Disambiguate();
	//	ss.choosesimilarity();
	///	Map<String, List<String>> pageIDNames = new HashMap<>();
	//	pageIDNames = dis.createRedirectNames(entityMap,pageIdCharacterNameMap);
	}
	

   /* A common method to do string comparison between the 
    character name and the page name in the entity */

	public static boolean findNameSimilarity(String characterName, String pageName) {
		if (pageName.toLowerCase().contains(characterName.toLowerCase())) {
			return true;
		}
		int count = 0;
		for (String characterSubString : characterName.split(" ")) {
			if (pageName.toLowerCase().contains(characterSubString.toLowerCase())) {
				count++;
			}
		}
		if (count >= 1) {
			return true;
		}
		return false;
	}

/* Created to match the male words with the text in the page */

/**
	 * @param characterNames
	 * @param genderMap
	 * @param paraMap
	 * @param maleWords
	 * @param femaleWords
	 */
	private static void toBeUsed(List<String> characterNames, Map<String, String> genderMap,
			Map<String, List<String>> paraMap, List<String> maleWords, List<String> femaleWords) {
		for (Map.Entry<String, List<String>> paraCharacterEntry : paraMap.entrySet()) {
			int maleCount = 0;
			int femaleCount = 0;
			for (String paraText : paraCharacterEntry.getValue()) {
				// System.out.println("para text" + paraText);
				// String line = sc.next();
				// System.out.println("male word:" + line);
				String a = paraText.toLowerCase().replaceAll("\\s+", "");
				// System.out.println(a);
				for (String maleWord : maleWords) {
					if (paraText.toLowerCase().replaceAll("\\s+", "").contains(maleWord.trim()))
						maleCount++;
				}
				// }

				// while (sc1.hasNext()) {
				// String line1 = sc1.next();
				for (String femaleWord : femaleWords) {
					if (paraText.toLowerCase().replaceAll("\\s+", "").contains(femaleWord.trim()))
						femaleCount++;
				}
			}

			if (!genderMap.containsKey(paraCharacterEntry.getKey())) {
			//	System.out.println(paraCharacterEntry.getKey());
				for (String characterName : characterNames) {
					if (findNameSimilarity(characterName, paraCharacterEntry.getKey())) {
						if (maleCount > femaleCount) {
							genderMap.put(characterName, "1");
							//finalCSVMap.put(characterName, new EntityCharacteristics("1", null));
						} else if (maleCount < femaleCount) {
							genderMap.put(characterName, "0");
							//finalCSVMap.put(characterName, new EntityCharacteristics("0", null));
						} else if (maleCount == femaleCount) {
							genderMap.put(characterName, "p");
							//finalCSVMap.put(characterName, new EntityCharacteristics("p", null));
						}
					}
//				else
//				{
//				genderMap.put(characterName, "--");
//				finalCSVMap.put(characterName,new EntityCharacteristics("--",null) );
//				}
					// System.out.println(finalCSVMap);
				}
			}

		}
	}
	
	/**
	 * @param c1
	 * @param entityInlink
	 * @throws IOException
	 */
	private static void getParagraph(CreateIndex c1, Map<String, Entity> entityMap,
			Map<String, List<String>> pageIdCharMap, BufferedWriter writer) throws IOException {
		TFIDFCalculator calculator = new TFIDFCalculator();
		GetCharacterName character = new GetCharacterName();
		HashMap<String, GenderClass> GenderCSVMap = new HashMap<String, GenderClass>();
		HashMap<String, String> trainingNamesMap = new HashMap<String, String>();
		trainingNamesMap = character.getActualNamesMap();
		HashMap<String, String> trainingMap = new HashMap<String, String>();
		trainingMap = character.getTrainNamesMap();
		HashSet<String> document = new HashSet<String>();
		HashSet<String> words;
		HashSet<Set<String>> documents = new HashSet<Set<String>>();
		for (Map.Entry<String, List<String>> pageIdCharsEntry : pageIdCharMap.entrySet()) {
			HashMap<String, Map<String, Double>> tfIDFScoreMapFinal = new HashMap<>();
			Map<String, Map<String, Double>> characterTfIdf = new HashMap<>();
			Map<String, String> paraMap = c1.getPageParaMap().get(pageIdCharsEntry.getKey());
			for (Map.Entry<String, String> paraMapEntry1 : paraMap.entrySet()) {
				document = tokenizerr(paraMapEntry1.getValue());
				documents.add(document);
			}
			for (Map.Entry<String, String> paraMapEntry : paraMap.entrySet()) {
				if (!documents.isEmpty()) {
					for (String characterName : pageIdCharsEntry.getValue()) {
						words = new HashSet<String>();
						DecimalFormat df = new DecimalFormat("#.##");
						words = tokenizerr(paraMapEntry.getValue());
						double tfidf = calculator.tfIdf(words, documents, characterName);
						if (!characterTfIdf.containsKey(characterName)) {
							characterTfIdf.put(characterName, new HashMap<>());
						}
					characterTfIdf.get(characterName).put(paraMapEntry.getKey(),Double.valueOf(df.format(Double.isNaN(tfidf) ? 0.0 : tfidf)));
					}
				}
			}
			for (Map.Entry<String, Map<String, Double>> chartfidfEntry : characterTfIdf.entrySet()) {
				characterTfIdf.put(chartfidfEntry.getKey(), chartfidfEntry.getValue().entrySet().stream()
						.sorted(Map.Entry.<String, Double>comparingByValue().reversed()).limit(100).collect(Collectors
								.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)));
			}
			for (Map.Entry<String, String> characterName1 : trainingNamesMap.entrySet()) {
				if (!characterTfIdf.isEmpty()) {	
					if (characterTfIdf.containsKey(characterName1.getKey())) {
						Map.Entry<String, Double> tfIDFScoreMapFinal1 = characterTfIdf.get(characterName1.getKey())
								.entrySet().iterator().next();
						if (!GenderCSVMap.containsKey(characterName1.getKey())) {
							GenderCSVMap.put(characterName1.getKey(), new GenderClass());
						}
						GenderCSVMap.get(characterName1.getKey()).setGender(characterName1.getValue());
						if(!tfIDFScoreMapFinal1.getValue().isNaN())
						GenderCSVMap.get(characterName1.getKey()).setTfidfscore(tfIDFScoreMapFinal1.getValue());
						else
						  GenderCSVMap.get(characterName1.getKey()).setTfidfscore(0.0);	
					}
				}
			}
		}
		HashMap<String, String> actualMap = character.getActualNamesMap();
		writer.append("Character Name" + "," + "Gender" + ","
				+ "TFIDF" + "\n");
		for (Map.Entry<String, GenderClass> finalCSVEntry : GenderCSVMap.entrySet()) {
			//if(trainingMap.containsKey(finalCSVEntry.getKey())){
		
			writer.append(finalCSVEntry.getKey() + "," + finalCSVEntry.getValue().getGender() + ","
					+ finalCSVEntry.getValue().getTfidfscore() + "\n");
			//System.out.println(finalCSVEntry.getKey() + "," + finalCSVEntry.getValue().getGender() + ","
					//+ finalCSVEntry.getValue().getTfidfscore() + "\n");
			//}
		 }
		
		for (Map.Entry<String, String> actual : actualMap.entrySet()) {
			if(!GenderCSVMap.containsKey(actual.getKey()))
			{
		      writer.append(actual.getKey() + "," + actual.getValue() + ","
					+ 0.0 + "\n");
			//System.out.println(actual.getKey() + "," + actual.getValue() + ","
			//	+ 0.0 + "\n");
		}
		}
		writer.close();
		//System.out.println();
	}
	public static List<String> StopWordString(String incoming) throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		Tokenizer tokenizer = new StandardTokenizer();
		final StandardFilter standardFilter = new StandardFilter(tokenizer);
		final StopFilter stopFilter = new StopFilter(standardFilter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
		final CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
		stopFilter.reset();
		while (stopFilter.incrementToken()) {
			final String token = charTermAttribute.toString().toString();
			list.add(token);
		}
		stopFilter.close();
		return list;
	}
	public static HashSet<String> tokenizerr(String str) throws IOException {
		HashSet<String> list = new HashSet<String>();
		Analyzer analyzer = new StandardAnalyzer();
		TokenStream stream = analyzer.tokenStream(null, new StringReader(str));
		CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
		stream.reset();
		while (stream.incrementToken()) {
			// System.out.println(cattr.toString());
			String termString = stream.getAttribute(CharTermAttribute.class).toString();
			list.add(termString);
		}
		stream.end();
		stream.close();
		return list;
	}

	
	/**
	 * @param entities
	 * @throws IOException
	 */
	private static void classifyGender(List<String> characterNames, Map<String, List<String>> entityInlinkMap)
			throws IOException {
		Scanner sc = new Scanner(new File("maleWords.txt"));
		Scanner sc1 = new Scanner(new File("femaleWords.txt"));
		while (sc.hasNext() || sc1.hasNext()) {
			String line = sc.nextLine();
			String line1 = sc1.nextLine();

		}

	}

}


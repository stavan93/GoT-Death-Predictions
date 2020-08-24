package team3_Sub_3.featurealgo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.TestPrediction;
import team3_Sub_3.utils.Utility;

public class FeatureCoreferenceNobility {

	private static List<String> nounForNobility = new ArrayList<>();

	static {

		/**
		 * Here we search the exact match for these nouns if the character is associated
		 * with these nouns then we can easily determine whether the character is noble
		 * or not.
		 */

		nounForNobility.add("King");
		nounForNobility.add("Lord");
		nounForNobility.add("Prince");
		nounForNobility.add("queen");
		nounForNobility.add("lord");
		nounForNobility.add("Princess");
		nounForNobility.add("highness");
		nounForNobility.add("lordlings");
		nounForNobility.add("majesty");
		nounForNobility.add("Honorable");

	}

	public Map<String, Integer> runNobilityRelation(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> nobilityPredictionMapTrain = new LinkedHashMap<String, Integer>();

		Map<String, Integer> nobilityPredictionMapTest = new LinkedHashMap<String, Integer>();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);
		
		cf.santizieCharNames(charNamesFromDeathTrain);

		// Reading Character relation from All Books.

//		 Set<String> nobilityForDeathTrain = extractNobilityFromBook(charNamesFromDeathTrain);
//
//		 System.out.println(new ArrayList<String>(nobilityForDeathTrain).size());
//		 
//		 Utility.printList(new ArrayList<String>(nobilityForDeathTrain));
//		 
//		 // Map for final prediction for Train Data 
//		 nobilityPredictionMapTrain.putAll(nobilityPrediction(charNamesFromDeathTrain, nobilityForDeathTrain));

		Map<String, Integer> nobilityForDeathTest = extractNobilityFromBook(charNamesFromDeathTest);
		
		Map<String, Integer> nobilityForDeathTrain = extractNobilityFromBook(charNamesFromDeathTrain);

		Map<String, Integer> nobilityFinalMap = prepareFinalNobleMap(charNamesFromDeathTest, nobilityForDeathTest);
		
		Map<String, Integer> nobilityFinalMap1 = prepareFinalNobleMap(charNamesFromDeathTrain, nobilityForDeathTrain);

		// Utility.printList(new ArrayList<String>(nobilityForDeathTest));

		// Map for final prediction for Test Data

//		System.out.println("Map Size" + nobilityFinalMap.size());

		// Utility.printStringIntegerMap(nobilityForDeathTest);

		//TestPrediction tp = new TestPrediction();
		//Map<String, Integer> actualnobilitydata = tp.getActualNobilityFromCsvFile(Constants.DEATHS_TEST);

//		Utility.comparePredictionWithActualData(actualnobilitydata, nobilityFinalMap);
		// return finalGenderMap for train set.
		return nobilityFinalMap1;
	}
	
	public Map<String, Integer> runNobilityRelation1(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);
		
		cf.santizieCharNames(charNamesFromDeathTrain);

		Map<String, Integer> nobilityForDeathTest = extractNobilityFromBook(charNamesFromDeathTest);
		
		Map<String, Integer> nobilityFinalMap = prepareFinalNobleMap(charNamesFromDeathTest, nobilityForDeathTest);
	
		return nobilityFinalMap;
	}

	/**
	 * This Tree will contain node of noun of noun (nobility noun word) with
	 * character name combination.
	 * 
	 * @param uniqueCharName
	 * @return
	 */
	private TreeMap<String, List<String>> nounMatchTree(List<String> uniqueCharName) {

		TreeMap<String, List<String>> nounMatchTree = new TreeMap<String, List<String>>();

		for (String pr : nounForNobility) {

			nounMatchTree.put(pr, uniqueCharName);

		}

		return nounMatchTree;

	}

	private Map<String, Integer> extractNobilityFromBook(List<String> charNamesFromDeathTrain) {

		Map<String, Integer> nobility = new LinkedHashMap<>();

		readForRelationExtraction(Constants.BOOK1, charNamesFromDeathTrain, nobility);
		readForRelationExtraction(Constants.BOOK2, charNamesFromDeathTrain, nobility);
		readForRelationExtraction(Constants.BOOK3, charNamesFromDeathTrain, nobility);
		readForRelationExtraction(Constants.BOOK4, charNamesFromDeathTrain, nobility);
		readForRelationExtraction(Constants.BOOK5, charNamesFromDeathTrain, nobility);

		return nobility;
	}

	/**
	 * Searching the relation phrase in the book in 2 steps: - 1. Using Syntactical
	 * extraction by searching the all possible relation for nobility. 2. If result
	 * is not found then we apply the argument extraction to find the nobility.
	 * 
	 * @param book
	 * @param uniqueCharName
	 * @param nobility
	 * @return
	 */
	private Map<String, Integer> readForRelationExtraction(String book, List<String> uniqueCharName,
			Map<String, Integer> nobilityMap) {

		// nn : - noun and noun combination.
		TreeMap<String, List<String>> pnMatchTree = nounMatchTree(uniqueCharName);

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			/**
			 * We are building a sentence for current paragraph. Algo will search the
			 * coreference nobility if searching noun and noun belongs to the current
			 * paragraph.
			 */

			StringBuilder currentSentence = new StringBuilder();

			/**
			 * We are building a sentence for previous paragraph. Algo will search the
			 * coreference nobility if searching noun and noun belongs to the previous
			 * paragraph.
			 */

			StringBuilder previousSentence = new StringBuilder();

			/**
			 * just as a buffer sentence to keep moving forward. This will temporarily store
			 * the previous sentence.
			 */
			StringBuilder tempSentence = new StringBuilder();

			while ((line = reader.readLine()) != null) {

				tempSentence.append(line);

				if (line.contains(".")) {

					previousSentence = tempSentence;
					// clearing the temp sentence.
					tempSentence = new StringBuilder();
				}

				for (String fn : nounForNobility) {

					boolean paraFlag = false;

					if (line.toLowerCase().contains(fn.toLowerCase())) {

						if (!exactNobleMatch(line, uniqueCharName, nobilityMap)) {
							currentSentence.append(line);
							paraFlag = true;
						}
					}

					if (paraFlag) {

						currentSentence.append(line);
					}

					if (line.contains(".") && paraFlag) {

						currentSentence.append(line);

						paraFlag = false;

						if (!preciseMatchForNoble(uniqueCharName, currentSentence.toString(), nobilityMap)) {
							preciseMatchForNoble(uniqueCharName, previousSentence.toString(), nobilityMap);
						}

						currentSentence = new StringBuilder();
					}
				}

				depthFirstSearch(line, pnMatchTree, nobilityMap);
			}

		} catch (FileNotFoundException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();
		}

		return nobilityMap;
	}

	private Map<String, Integer> prepareFinalNobleMap(List<String> charName, Map<String, Integer> predictedMap) {

		Map<String, Integer> finalNobleMap = new LinkedHashMap<>();

		for (String chName : charName) {

			if (predictedMap.containsKey(chName)) {

				finalNobleMap.put(chName, predictedMap.get(chName));
			} else {

				finalNobleMap.put(chName, 0);
			}
		}

		return finalNobleMap;

	}

	/**
	 * Searching the nobility in a paragraph using Tree which has the combination of
	 * noun as charName and noun as prince/princess/queen etc. In dfs approach we
	 * are searching PN(noun,noun) node of tree into the paragraph and as per the
	 * noun identifier we are distinguishing whether the characters is noble or
	 * not-noble.
	 * 
	 * @param line
	 * @param pnTreeMap
	 * @param nobilityMap
	 * @return
	 */
	private boolean depthFirstSearch(String line, TreeMap<String, List<String>> pnTreeMap,
			Map<String, Integer> nobilityMap) {

		// Using flag to check whether the character is present or not. If the character
		// is not present
		// then we return false else we return true.
		boolean flag = false;

		for (String nobilityIdentifier : pnTreeMap.keySet()) {

			if (line.toLowerCase().contains(nobilityIdentifier.toLowerCase())) {

				String charNameForNobleChar = Utility.fetchWordInString(line, pnTreeMap.get(nobilityIdentifier));

				if (charNameForNobleChar != null) {

					nobilityMap.put(charNameForNobleChar, 1);
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * Here we are searching for precise match for the characters name with their
	 * noble noun keywords.
	 * 
	 * @param charName
	 * @param paragraph
	 * @param nobilityMap
	 */
	private boolean preciseMatchForNoble(List<String> charName, String paragraph, Map<String, Integer> nobilityMap) {

		for (String ch : charName) {

			if (paragraph.toLowerCase().contains(ch.toLowerCase())
					|| Utility.checkWordInString(paragraph, nounForNobility)) {

				nobilityMap.put(ch, 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * In this methods we are just checking the character name with the possible
	 * relation. If the character name is present with the relation we directly add
	 * to the nobility character list.
	 * 
	 * @param line
	 * @param uniqueCharName
	 * @param nobilityMap
	 * @return
	 */

	private boolean exactNobleMatch(String line, List<String> uniqueCharName, Map<String, Integer> nobilityMap) {

		String charPresence = Utility.fetchWordInString(line, uniqueCharName);

		if (charPresence != null) {

			nobilityMap.put(charPresence, 1);
			return true;
		}

		return false;
	}

}

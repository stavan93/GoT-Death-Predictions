/**
 * 
 */
package team3_Sub_3.featurealgo;



import edu.unh.cs.treccar_v2.Data;
import edu.unh.cs.treccar_v2.Data.ItemWithFrequency;
import edu.unh.cs.treccar_v2.Data.Page;
import edu.unh.cs.treccar_v2.Data.PageMetadata;
import edu.unh.cs.treccar_v2.Data.PageSkeleton;
import edu.unh.cs.treccar_v2.read_data.DeserializeData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Scanner;

/**
 * @author Gayathri
 *
 */
 
 
public class PageRankPopularity {

	static List<Entity> entities = new ArrayList<>();
	static Map<String, Integer> entityInlinkCountMap = new HashMap<>();
	static Map<String, Integer> entityOutlinkCountMap = new HashMap<>();
	static Map<String, List<String>> entityOutlinkIdMap = new HashMap<>();
	static Map<String, List<String>> entityInlinkMap = new HashMap<>();
	static Map<String, Entity> entitiesMap = new HashMap<>();
	static Double nodeCount;

	public static void usage() {
		System.out.println("Command line parameters: (header|pages|outlines|paragraphs) FILE");
		System.exit(-1);
	}

	public static void mapEntity(String[] val) throws Exception {
		System.setProperty("file.encoding", "UTF-8");
		EntityGeneration entityGeneration = new EntityGeneration();
		List<Entity> entities = new ArrayList<>();
		if (val.length < 2)
			usage();

		String mode = val[0];
		if (mode.equals("pages")) {
			final String pagesFile = val[1];
			final FileInputStream fileInputStream = new FileInputStream(new File(pagesFile));
			for (Data.Page page : DeserializeData.iterableAnnotations(fileInputStream)) {
				entities.add(inlinkPageCount(page));
			}
		} else {
			usage();
		}
	//	createEntityInlinkMapping(entities);

	}

	public static Entity inlinkPageCount(Page page) {
		PageMetadata pageMetadata = page.getPageMetadata();
		String skeleton = page.getSkeleton().toString();
		List<String> inLinkAnchorTexts = pageMetadata.getInlinkAnchors().stream().map(ItemWithFrequency::getItem)
				.collect(Collectors.toList());
		Entity entity = new Entity(page.getPageName(), pageMetadata.getRedirectNames(), pageMetadata.getInlinkIds(),
				inLinkAnchorTexts, skeleton.toString(), page.getPageId());
		for (String inlinkPageId : entity.getInlinkIds()) {
			if (!entityOutlinkIdMap.containsKey(inlinkPageId)) {
				entityOutlinkIdMap.put(inlinkPageId, new ArrayList<>());
				entityOutlinkCountMap.put(inlinkPageId, 0);
			}
			entityOutlinkIdMap.get(inlinkPageId).add(entity.getPageID());
			entityOutlinkCountMap.put(inlinkPageId, entityOutlinkCountMap.get(inlinkPageId) + 1);
		}
		entityInlinkMap.put(entity.getPageID(), entity.getInlinkIds());
		entityInlinkCountMap.put(entity.getPageID(), entity.getInlinkIds().size());
		entitiesMap.put(entity.getPageID(), entity);
		return entity;
	}


/* Method to create a mapping between the enities and their inlink ids */
	/**
	 * @param entities
	 * @throws IOException
	 */
	public static void createEntityInlinkMapping(List<Entity> entities,HashMap<String,EntityCharacteristics> finalCSVMap) throws IOException {

		// Map<String, List<String>> entityInlinkMap = new HashMap<>();
		List<String> outlinkId = new ArrayList<String>();

//		for (Map.Entry<String, List<String>> inlinkIdMap : entityInlinkMap.entrySet()) {
//			String pageName = inlinkIdMap.getKey();
//			List<String> outLinksList = new ArrayList<String>();
//			int count = 0;
//			for (String inlink : inlinkIdMap.getValue()) {
//				for (Entity e : entities) {
//					
//					//System.out.println(e.getSkeleton());
//					if (e.getSkeleton().toString().contains(inlink)) {
//						count++;
//						outLinksList.add(inlink); // Create a list of all the outlinks for that particular entity
//					}
//					if(!outLinksList.isEmpty())
//					entityOutlinkIdMap.put(e.getPageID(), outLinksList);
//				}
//			}
//			entityOutlinkCountMap.put(inlinkIdMap.getKey(), count); // Calculated outlinks
//
//		}
		nodeCount = (double) entityInlinkMap.keySet().size();
		createPageRank(entities,finalCSVMap);
		System.out.println();
	}
	/*Main method that calculates the pagerank
	by making use of the inlink and outlink ids mentioned above. For ease of computation, an iteration count of 15 has been taken into consideration. */
	

	public static void createPageRank(List<Entity> entities,HashMap<String,EntityCharacteristics> finalCSVMap) throws IOException {

		Double initValue = 1 / nodeCount;
		int iterations = 0;
		double DampingFactor = 0.85;
		HashMap<String, Double> initialMapValues = new HashMap<String, Double>();
		Map<String, Double> pageRankMap = new HashMap<String, Double>();
		Map<String, Double> finalPageRankMap = new HashMap<String, Double>();
		for (Entry<String, Entity> entityMapEntry : entitiesMap.entrySet()) {
			// Put the pageId(gameofthrones%20wiki) and the
			// initial value as 1/n
			initialMapValues.put(entityMapEntry.getKey(), initValue);
		}

		while (iterations <= 15) {
			for (Entry<String, Entity> entityMapEntry : entitiesMap.entrySet()) {
				Double i = 0.0;
				for (String inlinkPageId : entityMapEntry.getValue().getInlinkIds()) {
					i += initialMapValues.get(inlinkPageId) / entityOutlinkCountMap.get(inlinkPageId);
				}
				pageRankMap.put(entityMapEntry.getKey(), i);
			}

			for (Entry<String, Entity> entityMapEntry : entitiesMap.entrySet()) {
					initialMapValues.put(entityMapEntry.getKey(), pageRankMap.get(entityMapEntry.getKey()));
			}
//			for (Entry<String, List<String>> inlinkEntry : entityInlinkMap.entrySet()) {
//				for (String inlink : inlinkEntry.getValue()) {
//					initialMapValues.put(inlink, pageRankMap.get(inlinkEntry.getKey()));
//				}
//			}
			iterations++;
		}

		Double totalMean = addDampingFactor(DampingFactor, pageRankMap, finalPageRankMap);
		GetCharacterName go = new GetCharacterName();
		HashMap<String, String> characterNames = go.getActualNamesMap();
		Map<String, String> characterNamePopularity = new HashMap<>();
		for(Map.Entry<String, String> characterName : characterNames.entrySet()) {
			characterNamePopularity.put(characterName.getKey(), "0");
			if(finalCSVMap.containsKey(characterName.getKey())) {
				finalCSVMap.get(characterName.getKey()).setPopularity("0");	
			}
		}
		System.out.println();
		for (Map.Entry<String, String> characterName : characterNames.entrySet()) {
			for (Entity entity : entities) {
				if (finalPageRankMap.containsKey(entity.getPageID())) {
					if (findNameSimilarity(characterName.getKey(), entity.getPageName())) {
						if (finalPageRankMap.get(entity.getPageID()) > totalMean) {
							characterNamePopularity.put(characterName.getKey(), "1");
							if(finalCSVMap.containsKey(characterName.getKey())) {
								finalCSVMap.get(characterName.getKey()).setPopularity("1");
							}
						}
					}
				}
			}
		}
	//	for(Map.Entry<String, String> characterPolularity : characterNamePopularity.entrySet()) {
		//	System.out.println("Character Name:" + characterPolularity.getKey() + " is " + characterPolularity.getValue());
		//}
		 GetCharacterName go1 = new GetCharacterName();
		HashMap<String,String> deaths= go1.getDeaths();
// 		for(Map.Entry<String, String> characterPolularity : characterNamePopularity.entrySet()) {
// 			System.out.println("Character Name:" + characterPolularity.getKey() + " is " + characterPolularity.getValue());
// 		}
		  BufferedWriter writer = new BufferedWriter(new FileWriter("data/finalCSV.csv"));
		  writer.write("Character Name" + "," + "Gender" + "," + "Popularity" + "," + "Death" +  "\n");
		  //System.out.println("Character Name" + "," + "Gender" + "," + "Popularity" + "," + "Death" + "\n");
		for(Map.Entry<String, EntityCharacteristics> finalVal : finalCSVMap.entrySet())
		{
			writer.write(finalVal.getKey() + "," + finalVal.getValue().getGender() + "," + finalVal.getValue().getPopularity() + "," + deaths.get(finalVal.getKey()) + "\n");
			//System.out.println(finalVal.getKey() + "," + finalVal.getValue().getGender() + "," + finalVal.getValue().getPopularity() + "," + deaths.get(finalVal.getKey()) + "\n");
		}
		writer.close();
//		int co = 0;
//		int tot = 0;
//		HashMap<String, String> charactersPredictionMap = go.getActualNamesPredictions();
//		for (Map.Entry<String, String> newval : charactersPredictionMap.entrySet()) {
//			tot++;
//			if (characterNamePopularity.containsKey(newval.getKey()))
//				if (newval.getValue()
//						.equals(characterNamePopularity.get(newval.getKey())))
//					co++;
//		}
//		System.out.println(co + "match" + " " + "from" + tot);
	}
/*Method used to add a damping factor to the actual pagerank score computed
in the previous step. A standard damping factor of 0.85 has been considered.*/

	/**
	 * @param DampingFactor
	 * @param pageRankMap
	 * @param finalPageRankMap
	 * @return
	 */
	private static Double addDampingFactor(double DampingFactor, Map<String, Double> pageRankMap,
			Map<String, Double> finalPageRankMap) {
		for (Map.Entry<String, Double> pageRankEntry : pageRankMap.entrySet()) {
			finalPageRankMap.put(pageRankEntry.getKey(),
					0.15 + DampingFactor * pageRankMap.get(pageRankEntry.getKey()));
		}
		Double mean = 0.0;
		Double keySize = 0.0;
		for (Map.Entry<String, Double> finalPageRankEntry : finalPageRankMap.entrySet()) {
		//	System.out.println("PageRank for Node:" + finalPageRankEntry.getKey() + " " + "is" + " "
		//			+ finalPageRankEntry.getValue());
			mean += finalPageRankEntry.getValue();
			keySize++;
		}
		Double totalMean = 0.0;

		totalMean = mean / keySize;
		return totalMean;
	}
	
	public static boolean findNameSimilarity(String characterName, String pageName) {
		for(String characterSubString: characterName.split(" ")) {
			if(pageName.toLowerCase().contains(characterSubString.toLowerCase())) {
				return true;
			}
		}
		return false;	
	}

}

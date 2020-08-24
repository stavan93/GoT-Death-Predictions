package team3_Sub_3.featurealgo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.Utility;

public class FeatureRelationOrphan {

	private static List<String> relation = new ArrayList<>();

	private static List<String> reverseRelation = new ArrayList<>();

	static {

		relation.add(" mother of ");
		relation.add (" father of ");
		
		reverseRelation.add(" son of ");
		reverseRelation.add(" daughter of ");
		
	}

	public Map<String, Integer> runRelationOrphan(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> orphanPredictionTestMap = new LinkedHashMap<String, Integer>();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);

		Map<String,String> Relation = extractOrphanFromBook(charNamesFromDeathTest);

		return orphanPredictionTestMap;
	}

	private Map<String, Integer> killerPrediction(List<String> charName, Set<String> killer) {

		Map<String, Integer> killerPredictionMap = new LinkedHashMap<String, Integer>();

		for (String ch : charName) {

			if (killer.contains(ch)) {

				/**
				 * Logic here is that the person who kills will have value as 1 and the person
				 * who did not kill any one will have value 0.
				 */

				killerPredictionMap.put(ch, 1);

			} else {

				killerPredictionMap.put(ch, 0);
			}
		}
		return killerPredictionMap;
	}

	private Map<String,String> extractOrphanFromBook(List<String> charNamesFromDeathTrain) {

		Map<String,String> orphanRelation = new LinkedHashMap<>();

		readForRelationExtraction(Constants.BOOK1, charNamesFromDeathTrain,orphanRelation);
		readForRelationExtraction(Constants.BOOK2, charNamesFromDeathTrain,orphanRelation);
		readForRelationExtraction(Constants.BOOK3, charNamesFromDeathTrain,orphanRelation);
		readForRelationExtraction(Constants.BOOK4, charNamesFromDeathTrain,orphanRelation);
		readForRelationExtraction(Constants.BOOK5, charNamesFromDeathTrain,orphanRelation);

		return orphanRelation;
	}

/**
 * 
 * @param book
 * @param uniqueCharName
 * @param orphanRelation 
 * @return
 */
	private Map<String,String> readForRelationExtraction(String book, List<String> uniqueCharName, Map<String, String> characterRelation) {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {

				for (String sc : relation) {

					boolean paraFlag = false;

					if (line.contains(sc)) {
					
						int temp = line.indexOf(sc);
						String childString = line.substring(temp);
						String parentString = line.substring(0,temp); 
						String child = Utility.fetchWordInString(childString, uniqueCharName);
						String parent = Utility.fetchWordInString(parentString, uniqueCharName);
						if( child!=null && parent!=null) {
							characterRelation.put(child, parent);
						}
						}

				}
				
				for (String sc : reverseRelation) {

					boolean paraFlag = false;

					if (line.contains(sc)) {
					
						int temp = line.indexOf(sc);
						String parentStr = line.substring(temp);
						String childStr = line.substring(0,temp); 
						String child = Utility.fetchWordInString(childStr, uniqueCharName);
						String parent = Utility.fetchWordInString(parentStr, uniqueCharName);
						if( child!=null && parent!=null) {
							characterRelation.putIfAbsent(child, parent);
						}
						}

				}
			}
		} catch (FileNotFoundException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();
		}

		return characterRelation;
	}

}

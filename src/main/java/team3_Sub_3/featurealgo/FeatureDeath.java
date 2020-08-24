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
import java.util.Map.Entry;
import java.util.TreeMap;

import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.TestPrediction;
import team3_Sub_3.utils.Utility;

public class FeatureDeath {

	private static List<String> deathRelation = new ArrayList<>();

	static {

		/**
		 * Here we search the exact match for these nouns if the character is associated
		 * with these nouns then we can easily determine whether the character is death
		 * or not.
		 */

		deathRelation.add("dead");
		deathRelation.add("die");

	}

	public Map<String, Integer> rundeathRelation(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		Map<String, Integer> deathForDeathTrain = extractdeathFromBook(charNamesFromDeathTrain);

		List<String> deadList = new ArrayList<String>();

		List<String> aliveList = new ArrayList<String>();

		getDeadAndAliveList(deathForDeathTrain, deadList, aliveList, charNamesFromDeathTrain);

		Map<String, List<Double>> deadCharTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(deadList));

//		System.out.println("Death Map Term Freq Map Size " + deadCharTFMap.size());

		/**
		 * Here we are calculating the term frequency of the characters having least
		 * used death.This is for Death_Train.
		 */

		Map<String, List<Double>> aliveCharTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(aliveList));

		double[] meanDeadChar = cf.calculateMeanOfChar(deadCharTFMap);

		double[] meanAliveChar = cf.calculateMeanOfChar(aliveCharTFMap);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);

		Map<String, List<Double>> deadCharNameTFMapForDeathTest = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);

		Map<String, Integer> finalDeathPrediction = predictCharForDeath(deadCharNameTFMapForDeathTest, meanDeadChar,
				meanAliveChar);

		TestPrediction tp = new TestPrediction();
		Map<String, Integer> actualDeathMap = tp.getActualDeathForChar(Constants.DEATHS_TEST);

//		Utility.comparePredictionWithActualData(actualDeathMap, finalDeathPrediction);

//		Utility.printStringIntegerMap(finalDeathPrediction);

		return deathForDeathTrain;
	//	return finalDeathPrediction;
	}

	public Map<String, Integer> rundeathRelation1(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		Map<String, Integer> deathForDeathTrain = extractdeathFromBook(charNamesFromDeathTrain);

		List<String> deadList = new ArrayList<String>();

		List<String> aliveList = new ArrayList<String>();

		getDeadAndAliveList(deathForDeathTrain, deadList, aliveList, charNamesFromDeathTrain);

		Map<String, List<Double>> deadCharTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(deadList));

//		System.out.println("Death Map Term Freq Map Size " + deadCharTFMap.size());

		/**
		 * Here we are calculating the term frequency of the characters having least
		 * used death.This is for Death_Train.
		 */

		Map<String, List<Double>> aliveCharTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(aliveList));

		double[] meanDeadChar = cf.calculateMeanOfChar(deadCharTFMap);

		double[] meanAliveChar = cf.calculateMeanOfChar(aliveCharTFMap);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);

		Map<String, List<Double>> deadCharNameTFMapForDeathTest = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);

		Map<String, Integer> finalDeathPrediction = predictCharForDeath(deadCharNameTFMapForDeathTest, meanDeadChar,
				meanAliveChar);

		return finalDeathPrediction;
	}

	
	private Map<String, Integer> predictCharForDeath(Map<String, List<Double>> charNamesTFMapDeathTest,
			double[] meanOfdeathChar, double[] meanOfAliveChar) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> charDeathPredictionMap = new LinkedHashMap<String, Integer>();

		for (Entry<String, List<Double>> deathIterator : charNamesTFMapDeathTest.entrySet()) {

			List<Double> listMapIterator = deathIterator.getValue();

			double[] charVectorforDeathChar = new double[meanOfAliveChar.length];
			for (int i = 0; i < meanOfAliveChar.length; i++) {
				charVectorforDeathChar[i] = 0.0;
			}

			int count = 0;
			for (Double itr : listMapIterator) {
				charVectorforDeathChar[count] = itr;
				count++;
			}

			double[] charVectorForAliveChar = new double[meanOfdeathChar.length];
			for (int i = 0; i < meanOfdeathChar.length; i++) {
				charVectorForAliveChar[i] = 0.0;
			}

			count = 0;

			for (Double itr : listMapIterator) {
				charVectorForAliveChar[count] = itr;
				count++;
			}

			double deadCharCosineValue = cf.cosineSimilarity(charVectorForAliveChar, meanOfdeathChar);

			double aliveCharCosineValue = cf.cosineSimilarity(charVectorforDeathChar, meanOfAliveChar);

			if (deadCharCosineValue > aliveCharCosineValue) {
				charDeathPredictionMap.put(deathIterator.getKey(), 1);
			} else {
				charDeathPredictionMap.put(deathIterator.getKey(), 0);
			}
		}
		return charDeathPredictionMap;
	}

	private void getDeadAndAliveList(Map<String, Integer> deadthMap, List<String> deadChar, List<String> aliveChar,
			List<String> charNamesFromDeathTrain) {

		for (String charName : charNamesFromDeathTrain) {

			if (deadthMap.get(charName) != null && deadthMap.get(charName) == 1) {
				deadChar.add(charName);
			} else {
				aliveChar.add(charName);
			}
		}
	}

	/**
	 * This Tree will contain node of noun of noun (death noun word) with character
	 * name combination.
	 * 
	 * @param uniqueCharName
	 * @return
	 */
	private TreeMap<String, List<String>> nounMatchTree(List<String> uniqueCharName) {

		TreeMap<String, List<String>> nounMatchTree = new TreeMap<String, List<String>>();

		for (String pr : deathRelation) {

			nounMatchTree.put(pr, uniqueCharName);
		}
		return nounMatchTree;
	}

	private Map<String, Integer> extractdeathFromBook(List<String> charNamesFromDeathTrain) {

		Map<String, Integer> death = new LinkedHashMap<>();

		readForRelationExtraction(Constants.BOOK1, charNamesFromDeathTrain, death);

		readForRelationExtraction(Constants.BOOK2, charNamesFromDeathTrain, death);

		readForRelationExtraction(Constants.BOOK3, charNamesFromDeathTrain, death);

		readForRelationExtraction(Constants.BOOK4, charNamesFromDeathTrain, death);

		readForRelationExtraction(Constants.BOOK5, charNamesFromDeathTrain, death);

		return death;
	}

	/**
	 * Searching the relation phrase in the book in 2 steps: - 1. Using Syntactical
	 * extraction by searching the all possible relation for death. 2. If result is
	 * not found then we apply the argument extraction to find the death.
	 * 
	 * @param book
	 * @param uniqueCharName
	 * @param death
	 * @return
	 */
	private Map<String, Integer> readForRelationExtraction(String book, List<String> uniqueCharName,
			Map<String, Integer> deathMap) {

		// nn : - noun and noun combination.
		TreeMap<String, List<String>> pnMatchTree = nounMatchTree(uniqueCharName);

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			/**
			 * We are building a sentence for current paragraph. Algo will search the
			 * coreference death if searching noun and noun belongs to the current
			 * paragraph.
			 */

			StringBuilder currentSentence = new StringBuilder();

			/**
			 * We are building a sentence for previous paragraph. Algo will search the
			 * coreference death if searching noun and noun belongs to the previous
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

				for (String fn : deathRelation) {

					boolean paraFlag = false;

					if (line.toLowerCase().contains(fn.toLowerCase())) {

						if (!exactdeathMatch(line, uniqueCharName, deathMap)) {
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

						if (!preciseMatchFordeath(uniqueCharName, currentSentence.toString(), deathMap)) {
							preciseMatchFordeath(uniqueCharName, previousSentence.toString(), deathMap);
						}

						currentSentence = new StringBuilder();
					}
				}

				depthFirstSearch(line, pnMatchTree, deathMap);
			}

		} catch (FileNotFoundException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();
		}

		return deathMap;
	}

	private Map<String, Integer> prepareFinaldeathMap(List<String> charName, Map<String, Integer> predictedMap) {

		Map<String, Integer> finaldeathMap = new LinkedHashMap<>();

		for (String chName : charName) {

			if (predictedMap.containsKey(chName)) {

				finaldeathMap.put(chName, predictedMap.get(chName));
			} else {

				finaldeathMap.put(chName, 0);
			}
		}

		return finaldeathMap;

	}

	private boolean depthFirstSearch(String line, TreeMap<String, List<String>> pnTreeMap,
			Map<String, Integer> deathMap) {

		// Using flag to check whether the character is present or not. If the character
		// is not present
		// then we return false else we return true.
		boolean flag = false;

		for (String deathIdentifier : pnTreeMap.keySet()) {

			if (line.toLowerCase().contains(deathIdentifier.toLowerCase())) {

				String charNameFordeathChar = Utility.fetchWordInString(line, pnTreeMap.get(deathIdentifier));

				if (charNameFordeathChar != null) {

					deathMap.put(charNameFordeathChar, 1);
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * Here we are searching for precise match for the characters name with their
	 * death noun keywords.
	 * 
	 * @param charName
	 * @param paragraph
	 * @param deathMap
	 */
	private boolean preciseMatchFordeath(List<String> charName, String paragraph, Map<String, Integer> deathMap) {

		for (String ch : charName) {

			if (paragraph.toLowerCase().contains(ch.toLowerCase())
					|| Utility.checkWordInString(paragraph, deathRelation)) {

				deathMap.put(ch, 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * In this methods we are just checking the character name with the possible
	 * relation. If the character name is present with the relation we directly add
	 * to the death character list.
	 * 
	 * @param line
	 * @param uniqueCharName
	 * @param deathMap
	 * @return
	 */

	private boolean exactdeathMatch(String line, List<String> uniqueCharName, Map<String, Integer> deathMap) {

		String charPresence = Utility.fetchWordInString(line, uniqueCharName);

		if (charPresence != null) {

			deathMap.put(charPresence, 1);
			return true;
		}

		return false;
	}

}

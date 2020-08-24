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

public class FeatureCoreferenceGender {

	private static List<String> nounForMale = new ArrayList<>();

	private static List<String> nounForFemale = new ArrayList<>();

	private static List<String> pronounsForMale = new ArrayList<>();

	private static List<String> pronounsForFemale = new ArrayList<>();

	/**
	 * Using Static block here For loading the pronouns and the lexical constraints
	 * with the class in static scope as these are constant values.
	 */

	static {

		/**
		 * 
		 * Adding male and female pronouns list. To determine the make or female we will
		 * find the coreference of this pronoun in the current sentence as well as
		 * previous sentence. Then character found with these coreference will be male
		 * for male pronouns and female for female pronouns respectively.
		 * 
		 */

		pronounsForMale.add("his");
		pronounsForMale.add("him");
		pronounsForMale.add("himself");

		pronounsForFemale.add("her");
		pronounsForFemale.add("herself");

		/**
		 * 
		 * Here we search the exact match for these nouns if the character is associated
		 * with these nouns then we can easily determine whether the character is male
		 * or female.
		 * 
		 */

		nounForFemale.add("mother");
		nounForFemale.add("sister");
		nounForFemale.add("wife");
		nounForFemale.add("princess");
		nounForFemale.add("queen");

		nounForMale.add("father");
		nounForMale.add("brother");
		nounForMale.add("husband");
		nounForMale.add("king");
		nounForMale.add("lord");
		nounForMale.add("prince");

	}

	public Map<String, Integer> runGenderRelation(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> genderPredictionMapTrain = new LinkedHashMap<String, Integer>();

		Map<String, Integer> genderPredictionMapTest = new LinkedHashMap<String, Integer>();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);

		cf.santizieCharNames(charNamesFromDeathTrain);
		// Reading Character relation from All Books.

//		 Set<String> genderForDeathTrain = extractGenderFromBook(charNamesFromDeathTrain);
//
//		 System.out.println(new ArrayList<String>(genderForDeathTrain).size());
//		 
//		 Utility.printList(new ArrayList<String>(genderForDeathTrain));
//		 
//		 // Map for final prediction for Train Data 
//		 genderPredictionMapTrain.putAll(genderPrediction(charNamesFromDeathTrain, genderForDeathTrain));

		Map<String, Integer> genderForDeathTest = extractGenderFromBook(charNamesFromDeathTest);
		
		Map<String, Integer> genderForDeathTrain = extractGenderFromBook(charNamesFromDeathTrain);

		Map<String, Integer> finalGenderPredMap = prepareFinalGenderMap(charNamesFromDeathTest, genderForDeathTest);
		
		Map<String, Integer> finalGenderPredMap1 = prepareFinalGenderMap(charNamesFromDeathTrain, genderForDeathTrain);

		// Utility.printList(new ArrayList<String>(genderForDeathTest));

		// Map for final prediction for Test Data

//		System.out.println("Map Size" + finalGenderPredMap.size());

		// Utility.printStringIntegerMap(genderForDeathTest);

//		TestPrediction tp = new TestPrediction();
//		Map<String, Integer> actualgenderdata = tp.getActualGenderFromCsvFile(Constants.DEATHS_TEST);

	//Utility.comparePredictionWithActualData(actualgenderdata, finalGenderPredMap);
		// return finalGenderMap for test set.
		return finalGenderPredMap1;
	}

	public Map<String, Integer> runGenderRelation1(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

	    List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);

		cf.santizieCharNames(charNamesFromDeathTrain);
	
		Map<String, Integer> genderForDeathTest = extractGenderFromBook(charNamesFromDeathTest);
		
		Map<String, Integer> finalGenderPredMap = prepareFinalGenderMap(charNamesFromDeathTest, genderForDeathTest);
		
		return finalGenderPredMap;
	}

	
	
	private Map<String, Integer> prepareFinalGenderMap(List<String> charName, Map<String, Integer> predictedMap) {

		Map<String, Integer> finalGenderMap = new LinkedHashMap<>();

		for (String chName : charName) {

			if (predictedMap.containsKey(chName)) {

				finalGenderMap.put(chName, predictedMap.get(chName));
			} else {

				finalGenderMap.put(chName, 1);
			}
		}

		return finalGenderMap;

	}

	private TreeMap<String, List<String>> pronounMatchTree(List<String> uniqueCharName) {

		TreeMap<String, List<String>> pronounMatchTreeMap = new TreeMap<String, List<String>>();

		for (String pr : pronounsForMale) {

			pronounMatchTreeMap.put(pr, uniqueCharName);

		}

		for (String pr : pronounsForFemale) {

			pronounMatchTreeMap.put(pr, uniqueCharName);

		}

		return pronounMatchTreeMap;

	}

	private Map<String, Integer> extractGenderFromBook(List<String> charNamesFromDeathTrain) {

		Map<String, Integer> gender = new LinkedHashMap<>();

		readForRelationExtraction(Constants.BOOK1, charNamesFromDeathTrain, gender);
		readForRelationExtraction(Constants.BOOK2, charNamesFromDeathTrain, gender);
		readForRelationExtraction(Constants.BOOK3, charNamesFromDeathTrain, gender);
		readForRelationExtraction(Constants.BOOK4, charNamesFromDeathTrain, gender);
		readForRelationExtraction(Constants.BOOK5, charNamesFromDeathTrain, gender);

		return gender;
	}

	/**
	 * Searching the relation phrase in the book in 2 steps: - 1. Using Syntactical
	 * extraction by searching the all possible relation for gender. 2. If result is
	 * not found then we apply the argument extraction to find the gender.
	 * 
	 * @param book
	 * @param uniqueCharName
	 * @param gender
	 * @return
	 */
	private Map<String, Integer> readForRelationExtraction(String book, List<String> uniqueCharName,
			Map<String, Integer> genderMap) {

		// pn : - Pronoun and noun combination.
		TreeMap<String, List<String>> pnMatchTree = pronounMatchTree(uniqueCharName);

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			/**
			 * We are building a sentence for current paragraph. Algo will search the
			 * coreference gender if searching noun and pronoun belongs to the current
			 * paragraph.
			 */

			StringBuilder currentSentence = new StringBuilder();

			/**
			 * We are building a sentence for previous paragraph. Algo will search the
			 * coreference gender if searching noun and pronoun belongs to the previous
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

				for (String fn : nounForFemale) {

					boolean paraFlag = false;

					if (line.toLowerCase().contains(fn.toLowerCase())) {

						if (!exactFemaleMatch(line, uniqueCharName, genderMap)) {
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

						if (!preciseMatchForFemale(uniqueCharName, currentSentence.toString(), genderMap)) {
							preciseMatchForFemale(uniqueCharName, previousSentence.toString(), genderMap);
						}

						currentSentence = new StringBuilder();
					}
				}

				for (String fn : nounForMale) {

					boolean paraFlag = false;

					if (line.contains(fn)) {

						if (!exactMaleMatch(line, uniqueCharName, genderMap)) {
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

						if (!preciseMatchForMale(uniqueCharName, currentSentence.toString(), genderMap)) {
							preciseMatchForMale(uniqueCharName, previousSentence.toString(), genderMap);
						}
						currentSentence = new StringBuilder();
					}
				}

				for (String fn : pronounsForFemale) {

					boolean paraFlag = false;

					if (line.toLowerCase().contains(fn.toLowerCase())) {

						if (!exactFemaleMatch(line, uniqueCharName, genderMap)) {
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

						if (!preciseMatchForFemale(uniqueCharName, currentSentence.toString(), genderMap)) {
							preciseMatchForFemale(uniqueCharName, previousSentence.toString(), genderMap);
						}

						currentSentence = new StringBuilder();
					}
				}

				for (String fn : pronounsForMale) {

					boolean paraFlag = false;

					if (line.toLowerCase().contains(fn.toLowerCase())) {

						if (!exactMaleMatch(line, uniqueCharName, genderMap)) {
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

						if (!preciseMatchForMale(uniqueCharName, currentSentence.toString(), genderMap)) {
							preciseMatchForMale(uniqueCharName, previousSentence.toString(), genderMap);
						}

						currentSentence = new StringBuilder();
					}
				}
				depthFirstSearch(line, pnMatchTree, genderMap);
			}

		} catch (FileNotFoundException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();
		}

		return genderMap;
	}

	/**
	 * Searching the gender in a paragraph using Tree which has the combination of
	 * noun as charName and pronoun as his/her/himself/herself etc. In dfs approach
	 * we are searching PN(pronoun,noun) node of tree into the paragraph and as per
	 * the pronoun identifier we are distinguishing whether the characters is male
	 * or female.
	 * 
	 * @param line
	 * @param pnTreeMap
	 * @param genderMap
	 * @return
	 */
	private boolean depthFirstSearch(String line, TreeMap<String, List<String>> pnTreeMap,
			Map<String, Integer> genderMap) {

		for (String genderIdentifier : pnTreeMap.keySet()) {

			if (line.toLowerCase().contains(genderIdentifier.toLowerCase())
					&& pronounsForMale.contains(genderIdentifier)) {

				String charNameForMale = Utility.fetchWordInString(line, pnTreeMap.get(genderIdentifier));

				if (charNameForMale != null) {

					genderMap.putIfAbsent(charNameForMale, 1);

				}
			} else if (line.toLowerCase().contains(genderIdentifier.toLowerCase())
					&& pronounsForFemale.contains(genderIdentifier)) {

				String charNameForFemale = Utility.fetchWordInString(line, pnTreeMap.get(genderIdentifier));

				if (charNameForFemale != null) {
					genderMap.putIfAbsent(charNameForFemale, 0);

				}
			}
		}

		return false;
	}

	/**
	 * 
	 * @param charName
	 * @param paragraph
	 * @param genderMap
	 */
	private boolean preciseMatchForFemale(List<String> charName, String paragraph, Map<String, Integer> genderMap) {

		for (String ch : charName) {

			if (paragraph.contains(ch) || Utility.checkWordInString(paragraph, nounForMale)) {

				genderMap.putIfAbsent(ch, 0);
				return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @param charName
	 * @param paragraph
	 * @param genderMap
	 */
	private boolean preciseMatchForMale(List<String> charName, String paragraph, Map<String, Integer> genderMap) {

		for (String ch : charName) {

			if (paragraph.contains(ch) || Utility.checkWordInString(paragraph, nounForMale)) {

				genderMap.putIfAbsent(ch, 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * In this methods we are just checking the character name with the possible
	 * relation. If the character name is present with the relation we directly add
	 * to the gender character list.
	 * 
	 * @param line
	 * @param uniqueCharName
	 * @param genderMap
	 * @return
	 */

	private boolean exactMaleMatch(String line, List<String> uniqueCharName, Map<String, Integer> genderMap) {

		String charPresence = Utility.fetchWordInString(line, uniqueCharName);

		if (charPresence != null) {

			genderMap.putIfAbsent(charPresence, 1);
			return true;
		}

		return false;
	}

	private boolean exactFemaleMatch(String line, List<String> uniqueCharName, Map<String, Integer> genderMap) {

		String charPresence = Utility.fetchWordInString(line, uniqueCharName);

		if (charPresence != null) {

			genderMap.putIfAbsent(charPresence, 0);
			return true;
		}

		return false;
	}
}

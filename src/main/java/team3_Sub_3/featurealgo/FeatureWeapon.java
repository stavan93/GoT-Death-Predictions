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
import java.util.Map.Entry;
import java.util.Set;

import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.Utility;

public class FeatureWeapon {

	public Map<String, Integer> runFeatureWeapon(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		// Step:- 1
		List<String> weaponList = getWeaponDictionary();

		// Reading Weapons from the Book.
		Map<String, Integer> mapOfWeapons = readBooksForWeaponFreq(weaponList);

		// Utility.printStringIntegerMap(mapOfWeapons);

		// Step:- 2
		int meanOfWeaponFreq = Utility.calculateMeanOfMapValue(mapOfWeapons);

		// System.out.println(" Mean Value "+ meanFreq);

		Set<String> charMostUsedWeapons = new HashSet<>();

		Set<String> charLeastUsedWeapons = new HashSet<>();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		// Step:- 3 This method will complete the charMostUsedWeapon and
		// charLeastUsedWeapon.
		searchCharacterNameWithWeapons(mapOfWeapons, charMostUsedWeapons, charLeastUsedWeapons, meanOfWeaponFreq,
				charNamesFromDeathTrain);

		// Step:-4
		// Here we are calculating the term frequency of the characters having most used
		// weapons.This is for Death_Train.

		Map<String, List<Double>> charHavingMostUsedWeaponTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(charMostUsedWeapons));

		// Here we are calculating the term frequency of the characters having least
		// used weapons.This is for Death_Train.

		Map<String, List<Double>> charHavingLeastUsedWeaponTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(charLeastUsedWeapons));

		// Step:-5
		double[] meanOfCharHavingMostUsedWeapon = cf.calculateMeanOfChar(charHavingMostUsedWeaponTFMap);

		double[] meanOfCharHavingLeastUsedWeapon = cf.calculateMeanOfChar(charHavingLeastUsedWeaponTFMap);

		// Step:-6
		// Here we are importing the Characters from Death Test.
		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		santizieCharNames(charNamesFromDeathTest);

		Map<String, List<Double>> charNamesTFMapforDeathTest = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);
		
		Map<String, List<Double>> charNamesTFMapforDeathTrain = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);

		// Step:-7
		Map<String, Integer> finalprediction = predictCharWithWeapons(charNamesTFMapforDeathTest,
				meanOfCharHavingMostUsedWeapon, meanOfCharHavingLeastUsedWeapon);
		
		Map<String, Integer> finalprediction1 = predictCharWithWeapons(charNamesTFMapforDeathTrain,
				meanOfCharHavingMostUsedWeapon, meanOfCharHavingLeastUsedWeapon);

//		Utility.printStringIntegerMap(finalprediction1);
//		Utility.printStringIntegerMap(finalprediction);

		return finalprediction1;

	}
	
	public Map<String, Integer> runFeatureWeapon1(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		// Step:- 1
		List<String> weaponList = getWeaponDictionary();

		// Reading Weapons from the Book.
		Map<String, Integer> mapOfWeapons = readBooksForWeaponFreq(weaponList);

		// Utility.printStringIntegerMap(mapOfWeapons);

		// Step:- 2
		int meanOfWeaponFreq = Utility.calculateMeanOfMapValue(mapOfWeapons);

		// System.out.println(" Mean Value "+ meanFreq);

		Set<String> charMostUsedWeapons = new HashSet<>();

		Set<String> charLeastUsedWeapons = new HashSet<>();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		// Step:- 3 This method will complete the charMostUsedWeapon and
		// charLeastUsedWeapon.
		searchCharacterNameWithWeapons(mapOfWeapons, charMostUsedWeapons, charLeastUsedWeapons, meanOfWeaponFreq,
				charNamesFromDeathTrain);

		// Step:-4
		// Here we are calculating the term frequency of the characters having most used
		// weapons.This is for Death_Train.

		Map<String, List<Double>> charHavingMostUsedWeaponTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(charMostUsedWeapons));

		// Here we are calculating the term frequency of the characters having least
		// used weapons.This is for Death_Train.

		Map<String, List<Double>> charHavingLeastUsedWeaponTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(charLeastUsedWeapons));

		// Step:-5
		double[] meanOfCharHavingMostUsedWeapon = cf.calculateMeanOfChar(charHavingMostUsedWeaponTFMap);

		double[] meanOfCharHavingLeastUsedWeapon = cf.calculateMeanOfChar(charHavingLeastUsedWeaponTFMap);

		// Step:-6
		// Here we are importing the Characters from Death Test.
		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		santizieCharNames(charNamesFromDeathTest);

		Map<String, List<Double>> charNamesTFMapforDeathTest = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);
		
		Map<String, List<Double>> charNamesTFMapforDeathTrain = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);

		// Step:-7
		Map<String, Integer> finalprediction = predictCharWithWeapons(charNamesTFMapforDeathTest,
				meanOfCharHavingMostUsedWeapon, meanOfCharHavingLeastUsedWeapon);
		
		Map<String, Integer> finalprediction1 = predictCharWithWeapons(charNamesTFMapforDeathTrain,
				meanOfCharHavingMostUsedWeapon, meanOfCharHavingLeastUsedWeapon);

		return finalprediction;

	}

	private Map<String, Integer> predictCharWithWeapons(Map<String, List<Double>> charNamesTFMapDeathTest,
			double[] meanOfMostUsedWeapon, double[] meanOfLeastUSedWeapon) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> predictionMap = new LinkedHashMap<String, Integer>();

		for (Entry<String, List<Double>> mapiterator : charNamesTFMapDeathTest.entrySet()) {

			List<Double> mapIterator = mapiterator.getValue();

			double[] charVectorForLeastUsedWeapon = new double[meanOfLeastUSedWeapon.length];
			for (int i = 0; i < meanOfLeastUSedWeapon.length; i++) {
				charVectorForLeastUsedWeapon[i] = 0.0;
			}

			int count = 0;
			for (Double itr : mapIterator) {
				charVectorForLeastUsedWeapon[count] = itr;
				count++;
			}

			double[] charVectorForMostUsedWeapon = new double[meanOfMostUsedWeapon.length];
			for (int i = 0; i < meanOfMostUsedWeapon.length; i++) {
				charVectorForMostUsedWeapon[i] = 0.0;
			}

			count = 0;

			for (Double itr : mapIterator) {
				charVectorForMostUsedWeapon[count] = itr;
				count++;
			}

			double mostUsedWeaponCosineValue = cf.cosineSimilarity(charVectorForMostUsedWeapon, meanOfMostUsedWeapon);

			double leastUsedWeaponCosineValue = cf.cosineSimilarity(charVectorForLeastUsedWeapon,
					meanOfLeastUSedWeapon);

			if (mostUsedWeaponCosineValue > leastUsedWeaponCosineValue) {
				predictionMap.put(mapiterator.getKey(), 1);
			} else {
				predictionMap.put(mapiterator.getKey(), 0);
			}
		}
		return predictionMap;
	}

	private void santizieCharNames(List<String> charNamesOfDeathTest) {
		List<String> sanitizedDeathtestCharList = new ArrayList<String>();
		for (String charName : charNamesOfDeathTest) {
			String sanitizedName = Utility.sanitizeName(charName);
			sanitizedDeathtestCharList.add(sanitizedName);
		}
		charNamesOfDeathTest.clear();
		charNamesOfDeathTest.addAll(sanitizedDeathtestCharList);
	}

	private void searchCharacterNameWithWeapons(Map<String, Integer> mapOfWeapons, Set<String> charMostUsedWeapons,
			Set<String> charLeastUsedWeapons, int meanFreq, List<String> charName) {

		List<String> mostUsedWeapons = new ArrayList<>();

		List<String> leastUsedWeapons = new ArrayList<>();

		Utility.splitMapFromGivenMeanValue(mapOfWeapons, mostUsedWeapons, leastUsedWeapons, meanFreq);

		searchWeaponUsedByCharacter(charName, charMostUsedWeapons, charLeastUsedWeapons, mostUsedWeapons,
				leastUsedWeapons);

	}

	private List<String> getWeaponDictionary() {

		List<String> listWeapons = new ArrayList<String>();

		listWeapons.add("hammer");
		listWeapons.add("sword");
		listWeapons.add("knife");
		listWeapons.add("stone");
		listWeapons.add("iron");
		listWeapons.add("helms");
		listWeapons.add("hauberks");
		listWeapons.add("pikes");
		listWeapons.add("spearheads");
		listWeapons.add("battleaxes");
		listWeapons.add("gauntlets");
		listWeapons.add("gorgets");
		listWeapons.add("greaves");
		listWeapons.add("breastplates");
		listWeapons.add("wagons");

		return listWeapons;

	}

	private Map<String, Integer> readBooksForWeaponFreq(List<String> wordList) {

		Map<String, Integer> storeWeaponFreqBook1 = new LinkedHashMap<String, Integer>();

		// Storing weapons frequency from Book 1
		storeWeaponFreqBook1 = Utility.searchWordFreqInBook(Constants.BOOK1, wordList);

		Map<String, Integer> storeWeaponFreqBook2 = new LinkedHashMap<String, Integer>();

		// Storing weapons frequency from Book 2
		storeWeaponFreqBook2 = Utility.searchWordFreqInBook(Constants.BOOK2, wordList);

		Map<String, Integer> storeWeaponFreqBook3 = new LinkedHashMap<String, Integer>();

		// Storing weapons frequency from Book 3
		storeWeaponFreqBook3 = Utility.searchWordFreqInBook(Constants.BOOK3, wordList);

		Map<String, Integer> storeWeaponFreqBook4 = new LinkedHashMap<String, Integer>();

		// Storing weapons frequency from Book 4
		storeWeaponFreqBook4 = Utility.searchWordFreqInBook(Constants.BOOK4, wordList);

		Map<String, Integer> storeWeaponFreqBook5 = new LinkedHashMap<String, Integer>();

		// Storing weapons frequency from Book 5
		storeWeaponFreqBook5 = Utility.searchWordFreqInBook(Constants.BOOK5, wordList);

		Map<String, Integer> sumOfFreqWeaponmap = new LinkedHashMap<String, Integer>();

		// For Book 1
		Utility.mergeDataMap(sumOfFreqWeaponmap, storeWeaponFreqBook1);

		// For Book 2
		Utility.mergeDataMap(sumOfFreqWeaponmap, storeWeaponFreqBook2);

		// For Book 3
		Utility.mergeDataMap(sumOfFreqWeaponmap, storeWeaponFreqBook3);

		// For Book 4
		Utility.mergeDataMap(sumOfFreqWeaponmap, storeWeaponFreqBook4);

		// For Book 5
		Utility.mergeDataMap(sumOfFreqWeaponmap, storeWeaponFreqBook5);

		return sumOfFreqWeaponmap;
	}

	/**
	 * Here we search the character having most weapons and the character having
	 * least weapons.
	 * 
	 * @param charNames
	 * @param charMostUsedWeapons
	 * @param charLeastUsedWeapons
	 * @param mostUsedWeapons
	 * @param leastUsedWeapons
	 */

	private void searchWeaponUsedByCharacter(List<String> charNames, Set<String> charMostUsedWeapons,
			Set<String> charLeastUsedWeapons, List<String> mostUsedWeapons, List<String> leastUsedWeapons) {

		searchCharacterWeaponCombination(Constants.BOOK1, charNames, charMostUsedWeapons, charLeastUsedWeapons,
				mostUsedWeapons);

		searchCharacterWeaponCombination(Constants.BOOK2, charNames, charMostUsedWeapons, charLeastUsedWeapons,
				mostUsedWeapons);

		searchCharacterWeaponCombination(Constants.BOOK3, charNames, charMostUsedWeapons, charLeastUsedWeapons,
				mostUsedWeapons);

		searchCharacterWeaponCombination(Constants.BOOK4, charNames, charMostUsedWeapons, charLeastUsedWeapons,
				mostUsedWeapons);

		searchCharacterWeaponCombination(Constants.BOOK5, charNames, charMostUsedWeapons, charLeastUsedWeapons,
				mostUsedWeapons);

	}

	public static Map<String, Integer> searchCharacterWeaponCombination(String book, List<String> charNames,
			Set<String> charMostUsedWeapons, Set<String> charLeastUsedWeapons, List<String> mostUsedWeapons) {

		Map<String, Integer> predictionMap = new LinkedHashMap<String, Integer>();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			while ((line = reader.readLine()) != null) {
				for (String charName : charNames)
					if (line.contains(charName)) {
						if (Utility.checkWordInString(line, mostUsedWeapons)) {
							charMostUsedWeapons.add(charName);
						} else if (!charMostUsedWeapons.contains(charName)) {
							charLeastUsedWeapons.add(charName);
						}
					}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error in reading Book " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in reading Book " + e.getMessage());
			e.printStackTrace();
		}
		return predictionMap;
	}

}

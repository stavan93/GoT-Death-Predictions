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

import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.Utility;

public class FeatureLongevity {

	public Map<String, Integer> runFeatureLongevity(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> shortLived = new ArrayList<>();

		List<String> longLived = new ArrayList<>();

		readBooksForCharFreq(charNamesFromDeathTrain, longLived, shortLived);

		Map<String, List<Double>> charLongLivedTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(longLived));

		Map<String, List<Double>> charShortLivedTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(shortLived));

		double[] meanOfCharLongLived = cf.calculateMeanOfChar(charLongLivedTFMap);

		double[] meanOfCharShortLived = cf.calculateMeanOfChar(charShortLivedTFMap);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		santizieCharNames(charNamesFromDeathTest);

		Map<String, List<Double>> charNamesTFMapforDeathTest = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);

		Map<String, List<Double>> charNamesTFMapforDeathTrain = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);

		Map<String, Integer> finalprediction = predictCharLongevity(charNamesTFMapforDeathTest, meanOfCharLongLived,
				meanOfCharShortLived);
		
		Map<String, Integer> finalprediction1 = predictCharLongevity(charNamesTFMapforDeathTrain, meanOfCharLongLived,
				meanOfCharShortLived);

//		Utility.printStringIntegerMap(finalprediction);

		return finalprediction1;

	}

	public Map<String, Integer> runFeatureLongevity1(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> shortLived = new ArrayList<>();

		List<String> longLived = new ArrayList<>();

		readBooksForCharFreq(charNamesFromDeathTrain, longLived, shortLived);

		Map<String, List<Double>> charLongLivedTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(longLived));

		Map<String, List<Double>> charShortLivedTFMap = cf.getCharWordsTermFrequency(charNamesFromDeathTrain,
				new ArrayList<String>(shortLived));

		double[] meanOfCharLongLived = cf.calculateMeanOfChar(charLongLivedTFMap);

		double[] meanOfCharShortLived = cf.calculateMeanOfChar(charShortLivedTFMap);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		santizieCharNames(charNamesFromDeathTest);

		Map<String, List<Double>> charNamesTFMapforDeathTest = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);

		Map<String, List<Double>> charNamesTFMapforDeathTrain = cf.getCharWordsTermFrequency(charNamesFromDeathTest,
				charNamesFromDeathTest);

		Map<String, Integer> finalprediction = predictCharLongevity(charNamesTFMapforDeathTest, meanOfCharLongLived,
				meanOfCharShortLived);
		
		Map<String, Integer> finalprediction1 = predictCharLongevity(charNamesTFMapforDeathTrain, meanOfCharLongLived,
				meanOfCharShortLived);

//		Utility.printStringIntegerMap(finalprediction);

		return finalprediction;

	}
	
	private Map<String, Integer> predictCharLongevity(Map<String, List<Double>> charNamesTFMapDeathTest,
			double[] meanOfLongLived, double[] meanOfShortLived) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> predictionMap = new LinkedHashMap<String, Integer>();

		for (Entry<String, List<Double>> mapiterator : charNamesTFMapDeathTest.entrySet()) {

			List<Double> mapIterator = mapiterator.getValue();

			double[] charVectorForShortLived = new double[meanOfShortLived.length];
			for (int i = 0; i < meanOfShortLived.length; i++) {
				charVectorForShortLived[i] = 0.0;
			}

			int count = 0;
			for (Double itr : mapIterator) {
				charVectorForShortLived[count] = itr;
				count++;
			}

			double[] charVectorForLongLived = new double[meanOfLongLived.length];
			for (int i = 0; i < meanOfLongLived.length; i++) {
				charVectorForLongLived[i] = 0.0;
			}

			count = 0;

			for (Double itr : mapIterator) {
				charVectorForLongLived[count] = itr;
				count++;
			}

			double longLivedCosineValue = cf.cosineSimilarity(charVectorForLongLived, meanOfLongLived);

			double shortLivedCosineValue = cf.cosineSimilarity(charVectorForShortLived, meanOfShortLived);
			// Long lived character will be marked as 0 (comparing death for 0 ), short
			// lived character as 1 (comparing alive for 1 values in other algos).

			if (longLivedCosineValue > shortLivedCosineValue) {
				predictionMap.put(mapiterator.getKey(), 0);
			} else {
				predictionMap.put(mapiterator.getKey(), 1);
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

	private void readBooksForCharFreq(List<String> uniqueCharWords, List<String> longLivedChar,
			List<String> shortLivedChar) {

		Map<String, Integer> charPresenceInBook = new LinkedHashMap<String, Integer>();
		Map<String, Integer> charPresenceInLastBook = new LinkedHashMap<String, Integer>();

		bookReader(Constants.BOOK1, uniqueCharWords, charPresenceInBook, null, 1);
		bookReader(Constants.BOOK2, uniqueCharWords, charPresenceInBook, null, 2);
		bookReader(Constants.BOOK3, uniqueCharWords, charPresenceInBook, null, 3);
		bookReader(Constants.BOOK4, uniqueCharWords, charPresenceInBook, null, 4);
		bookReader(Constants.BOOK5, uniqueCharWords, charPresenceInBook, charPresenceInLastBook, 5);

		fetchShortAndLongLivedCharacter(uniqueCharWords, charPresenceInBook, charPresenceInLastBook, longLivedChar,
				shortLivedChar);

	}

	public void bookReader(String book, List<String> uniqueCharWords, Map<String, Integer> freqCharmap,
			Map<String, Integer> charPresenceInLastBook, int bookCount) {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			while ((line = reader.readLine()) != null) {

				for (String charName : uniqueCharWords) {
					if (line.contains(charName)) {
						if (freqCharmap.containsKey(charName) && freqCharmap.get(charName) <= bookCount) {
							freqCharmap.put(charName, freqCharmap.get(charName) + 1);
						} else {
							freqCharmap.put(charName, 1);
						}

						if (charPresenceInLastBook != null && !charPresenceInLastBook.containsKey(charName)) {
							charPresenceInLastBook.put(charName, 1);
						}

					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error in reading Book" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in reading Book" + e.getMessage());
			e.printStackTrace();
		}

	}

	private void fetchShortAndLongLivedCharacter(List<String> charNamesFromDeathTest,
			Map<String, Integer> charPresenceInBook, Map<String, Integer> charPresenceInLastBook,
			List<String> longLivedChar, List<String> shortLivedChar) {

		for (String charName : charNamesFromDeathTest) {

			if ((charPresenceInBook.get(charName) != null && charPresenceInBook.get(charName) >= 3)
					|| (charPresenceInLastBook.containsKey(charName))) {

				longLivedChar.add(charName);

			} else {
				shortLivedChar.add(charName);

			}

		}

	}

}

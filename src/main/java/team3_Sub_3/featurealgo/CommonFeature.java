package team3_Sub_3.featurealgo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.Utility;

public class CommonFeature {

	public Map<String, List<Double>> calcuateCharWordsTFFreq(Map<String, Integer> finalFreqCharmap,
			List<String> charTrainList, int totalWordFreq) {
		Map<String, List<Double>> charFreqMap = new LinkedHashMap<>();

		for (String charName : charTrainList) {
			List<Double> charWordFreqs = new ArrayList<Double>();
			String[] chraNameWords = charName.split(" ");
			for (String charWords : chraNameWords) {
				double charWordFreq = finalFreqCharmap.getOrDefault(charWords, 0);
				Double charWordFreq1 = Math.floor((charWordFreq / totalWordFreq) * 100000);
				charWordFreqs.add(charWordFreq1);
			}

			charFreqMap.put(charName, charWordFreqs);
		}

		return charFreqMap;
	}

	public Map<String, List<Double>> getCharWordsTermFrequency(List<String> charNames, List<String> charList) {

		Set<String> uniqueCharNames = getCharWords(charNames);

		Map<String, Integer> charNamesFreqMap = readBooksForCharFreq(uniqueCharNames);

		int sumOfWordFreq = sumOfWordFreq(charNamesFreqMap);

		CommonFeature cf = new CommonFeature();

		return cf.calcuateCharWordsTFFreq(charNamesFreqMap, charList, sumOfWordFreq);

	}

	private Set<String> getCharWords(List<String> Charname) {

		Set<String> charWordSet = new HashSet<String>();

		for (String ch : Charname) {
			String[] charwords = ch.split(" ");

			charWordSet.addAll(Arrays.asList(charwords));

		}
		return charWordSet;
	}

	private int sumOfWordFreq(Map<String, Integer> charNameFreqMap) {

		int totaWordCount = 0;

		Collection<Integer> values = charNameFreqMap.values();

		for (int val : values) {
			totaWordCount += val;
		}

		return totaWordCount;

	}

	private Map<String, Integer> readBooksForCharFreq(Set<String> uniqueCharWords) {

		Map<String, Integer> finalFreqCharmap = new LinkedHashMap<String, Integer>();

		bookReader(Constants.BOOK1, uniqueCharWords, finalFreqCharmap);

		bookReader(Constants.BOOK2, uniqueCharWords, finalFreqCharmap);

		bookReader(Constants.BOOK3, uniqueCharWords, finalFreqCharmap);

		bookReader(Constants.BOOK4, uniqueCharWords, finalFreqCharmap);

		bookReader(Constants.BOOK5, uniqueCharWords, finalFreqCharmap);

		return finalFreqCharmap;
	}

	public Map<String, Integer> bookReader(String book, Set<String> uniqueCharWords, Map<String, Integer> freqCharmap) {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			while ((line = reader.readLine()) != null) {
				Iterator<String> itr = uniqueCharWords.iterator();
				while (itr.hasNext()) {
					// Here we get single character name.
					String charName = itr.next();
					// Here we will get the character frequency from the string.
					int charFreq = Utility.countFreqForSubStr(charName, line);
					if (freqCharmap.containsKey(charName)) {
						freqCharmap.put(charName, freqCharmap.get(charName) + charFreq);
					} else {
						freqCharmap.put(charName, charFreq);
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
		return freqCharmap;
	}

	/*
	 * Reference : -
	 * https://github.com/awangdev/LintCode/blob/master/Java/Cosine%20Similarity.
	 * java The reference is for Cosine Similarity only.
	 */
	public double cosineSimilarity(double[] deathCharTestVector, double[] charDeathTrainMean) {

		if (deathCharTestVector == null || charDeathTrainMean == null || deathCharTestVector.length == 0
				|| charDeathTrainMean.length == 0 || deathCharTestVector.length != charDeathTrainMean.length) {
			return 2;
		}

		double sumProduct = 0;
		double sumASq = 0;
		double sumBSq = 0;
		for (int i = 0; i < deathCharTestVector.length; i++) {
			sumProduct += deathCharTestVector[i] * charDeathTrainMean[i];
			sumASq += deathCharTestVector[i] * deathCharTestVector[i];
			sumBSq += charDeathTrainMean[i] * charDeathTrainMean[i];
		}
		if (sumASq == 0 && sumBSq == 0) {
			return 2.0;
		}
		return sumProduct / (Math.sqrt(sumASq) * Math.sqrt(sumBSq));

	}

	public double[] calculateMeanOfChar(Map<String, List<Double>> CharNamesFreqMap) {

		int size = CharNamesFreqMap.size();
		int maxLength = 0;

		for (Entry<String, List<Double>> entrySet : CharNamesFreqMap.entrySet()) {

			List<Double> charNames = entrySet.getValue();
			if (charNames.size() > maxLength) {
				maxLength = charNames.size();
			}
		}

		double[] mean = new double[maxLength];

		for (int i = 0; i < maxLength; i++) {
			mean[i] = 0.0d;
		}

		for (Entry<String, List<Double>> itearator : CharNamesFreqMap.entrySet()) {

			List<Double> frqVal = itearator.getValue();
			int indexCount = 0;
			for (Double freqVal : frqVal) {

				double meanVal = mean[indexCount];

				double sum = meanVal + freqVal;

				mean[indexCount] = sum;
				indexCount++;
			}
		}

		for (int i = 0; i < maxLength; i++) {
			double meanVal = mean[i] / size;
			mean[i] = meanVal;
		}

		return mean;

	}

	public void santizieCharNames(List<String> charNamesOfDeathTest) {
		List<String> sanitizedDeathtestCharList = new ArrayList<String>();
		for (String charName : charNamesOfDeathTest) {
			String sanitizedName = Utility.sanitizeName(charName);
			sanitizedDeathtestCharList.add(sanitizedName);
		}
		charNamesOfDeathTest.clear();
		charNamesOfDeathTest.addAll(sanitizedDeathtestCharList);
	}

}

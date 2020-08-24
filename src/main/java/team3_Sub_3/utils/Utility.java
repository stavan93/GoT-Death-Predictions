package team3_Sub_3.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Utility {

	public static List<String> getColumnSpecificDataFromCsvFile(String csvfilename, int columnName) {

		List<String> characternames = new ArrayList<String>();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(csvfilename));
			String line = null;
			// No to read the top row.
			reader.readLine();

			while ((line = reader.readLine()) != null) {
				String[] csvEnteries = line.split(",");
				characternames.add((csvEnteries[columnName]));

			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage() + "Error Reading from CSV File");
		} catch (IOException e) {
			System.out.println(e.getMessage() + "Error Reading from CSV File");
		}

		return characternames;

	}

	public static void comparePredictionWithActualData(Map<String, Integer> actualMap,
			Map<String, Integer> finalGenderMap) {

		int actualMatchCount = 0;
		Iterator<Entry<String, Integer>> genderIterator = actualMap.entrySet().iterator();
		while (genderIterator.hasNext()) {

			Entry<String, Integer> genderEntry = genderIterator.next();

			if (genderEntry.getValue() == (finalGenderMap.get(genderEntry.getKey()))) {
				actualMatchCount += 1;
			}

		}
		System.out.println(actualMatchCount);
		System.out.println(actualMap.size());

		float a = actualMatchCount;
		float b = actualMap.size();
		System.out.println("Percentage Match " + (a / b) * 100 + "%");

	}

	public static String sanitizeName(String names) {

		if (names.contains("(")) {

			int i = names.indexOf("(");

			names = names.substring(0, i - 1);

		}

		names = names.replaceAll("of ", "");
		names = names.replaceAll(" of", "");

		names = names.replaceAll("the ", "");
		names = names.replaceAll(" the", "");

		names = names.replaceAll("The ", "");

		return names;
	}

	public static void mergeDataMap(Map<String, Integer> mainDataMap, Map<String, Integer> dataMap) {

		Iterator<Entry<String, Integer>> iterator = dataMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Integer> entry = iterator.next();
			if (mainDataMap.containsKey(entry.getKey())) {
				Integer count = mainDataMap.get(entry.getKey());
				count = count + entry.getValue();
				mainDataMap.put(entry.getKey(), count);
			} else {
				mainDataMap.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/*
	 * Returns the frequency of subString in a given larger mainString Reference:
	 * https://www.geeksforgeeks.org/frequency-substring-string/
	 */
	public static int countFreqForSubStr(String word, String stringLine) {

		/**
		 * wordLength : - It is the character name. textLine:- TextLine is a line in the
		 * book freqCount: - This is the frequency of the substring.
		 */
		int wordLength = word.length();
		int stringLength = stringLine.length();
		int subStringFreqCount = 0;

		for (int i = 0; i <= stringLength - wordLength; i++) {

			int j;
			for (j = 0; j < wordLength; j++) {
				if (stringLine.charAt(i + j) != word.charAt(j)) {
					break;
				}
			}

			if (j == wordLength) {
				subStringFreqCount++;
				j = 0;
			}
		}
		return subStringFreqCount;
	}

	public static Map<String, Integer> searchWordFreqInBook(String book, List<String> wordList) {

		Map<String, Integer> predictionMap = new LinkedHashMap<String, Integer>();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			while ((line = reader.readLine()) != null) {
				Iterator<String> itr = wordList.iterator();
				while (itr.hasNext()) {
					// Here we get single character name.
					String charName = itr.next();
					// Here we will get the character frequency from the string.
					int charFreq = Utility.countFreqForSubStr(charName, line);
					if (predictionMap.containsKey(charName)) {
						predictionMap.put(charName, predictionMap.get(charName) + charFreq);
					} else {
						predictionMap.put(charName, charFreq);
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

	public static void printStringIntegerMap(Map<String, Integer> dataMap) {

		Iterator<Entry<String, Integer>> itr = dataMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Integer> iterator = itr.next();
			System.out.println(iterator.getKey() + " " + iterator.getValue());
		}

	}

	public static void printStringMap(Map<String, String> dataMap) {

		Iterator<Entry<String, String>> itr = dataMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, String> iterator = itr.next();
			System.out.println(iterator.getKey() + " " + iterator.getValue());
		}

	}

	public static void printMapOfMap(Map<String, Map<String, String>> dataMap) {

		Iterator<Entry<String, Map<String, String>>> itr = dataMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Map<String, String>> iterator = itr.next();
			Map<String, String> map = iterator.getValue();
			Iterator<Entry<String, String>> itr1 = map.entrySet().iterator();
			while (itr1.hasNext()) {
				Entry<String, String> iterator1 = itr1.next();

				System.out.println(iterator.getKey() + " " + iterator1.getKey() + " , " + iterator1.getValue());
			}

		}

	}

	public static Integer calculateMeanOfMapValue(Map<String, Integer> finalmap) {

		// Using Collection Interface to calculate the values from the map.

		Collection<Integer> freqCount = finalmap.values();
		int sum = 0;
		// Summing all the frequency values.
		for (int i : freqCount) {
			sum += i;
		}

		return sum / finalmap.size();

	}

	public static void splitMapFromGivenMeanValue(Map<String, Integer> dataMap, List<String> majorList,
			List<String> minorList, int meanValue) {

		Iterator<Entry<String, Integer>> itr = dataMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Integer> iterator = itr.next();
			if (iterator.getValue() > meanValue) {
				majorList.add(iterator.getKey());

			} else
				minorList.add(iterator.getKey());

		}

	}

	public static boolean checkWordInString(String line, List<String> words) {

		for (String word : words) {
			if (line.contains(word)) {
				return true;
			}
		}
		return false;

	}

	public static String fetchWordInString(String line, List<String> words) {

		for (String word : words) {
			if (line.contains(word)) {
				return word;
			}
		}
		return null;

	}

	public static void printList(List<String> list) {

		for (String s : list) {

			System.out.println(s);
		}
	}

	public static String removeSpecialChar(String name) {

		return name.replaceAll("[^a-zA-Z0-9_ ]", "");

	}

	// Reference : -
	// https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
	public static Map<String, Integer> sortHashMapByValue(Map<String, Integer> map) {
		// List of elements from the Map using entryset.
		List<Map.Entry<String, Integer>> entryList = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());

		// Using Collections to sort the Map
		Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// Putting the Data back into Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> sortedEntryList : entryList) {
			sortedMap.put(sortedEntryList.getKey(), sortedEntryList.getValue());
		}
		return sortedMap;
	}

}
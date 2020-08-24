package team3_Sub_3.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class TestPrediction {

	public Map<String, Integer> getActualGenderFromCsvFile(String csvFileName) {

		File file = new File(csvFileName);
		Map<String, Integer> ActualGenderMap = new HashMap<String, Integer>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] csvEntries = line.split(",");

				String name = csvEntries[1];
				int gender = Integer.parseInt(csvEntries[7]);
				int genderString = 0;
				if (gender == 1) {
					// Male: - 1
					genderString = 1;
				} else {
					// female value : - 0
					genderString = 0;
				}
				ActualGenderMap.put(name, genderString);

			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed to open csv file: " + csvFileName);
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("ERROR: Failed to read csv file: " + csvFileName);
			System.exit(-1);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}

		// Utility.printmap(ActualGenderMap);
		return ActualGenderMap;

	}

	public Map<String, Integer> getActualNobilityFromCsvFile(String csvFileName) {

		File file = new File(csvFileName);
		Map<String, Integer> actualNobilityMap = new HashMap<String, Integer>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] csvEntries = line.split(",");

				String name = csvEntries[1];
				int noblility = Integer.parseInt(csvEntries[8]);
				int nobilityString = 0;
				if (noblility == 1) {
					// noble for 1
					nobilityString = 1;
				} else {
					// not-noble =0
					nobilityString = 0;
				}
				actualNobilityMap.put(name, nobilityString);

			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed to open csv file: " + csvFileName);
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("ERROR: Failed to read csv file: " + csvFileName);
			System.exit(-1);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}

		return actualNobilityMap;
	}

	public Map<String, Integer> getActualDeathForChar(String csvFileName) {

		File file = new File(csvFileName);
		Map<String, Integer> actualDeathMap = new HashMap<String, Integer>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] csvEntries = line.split(",");

				String name = csvEntries[1];
				String deathYear = csvEntries[3];

				int isDeadString = 0;
				if (StringUtils.isNotBlank(deathYear)) {
					isDeadString = 1;
				} else {
					isDeadString = 0;
				}
				actualDeathMap.put(name, isDeadString);

			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed to open csv file: " + csvFileName);
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("ERROR: Failed to read csv file: " + csvFileName);
			System.exit(-1);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}

		return actualDeathMap;
	}
}

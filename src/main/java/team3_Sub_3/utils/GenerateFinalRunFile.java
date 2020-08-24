package team3_Sub_3.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GenerateFinalRunFile {

	/**
	 * 
	 * Run File Format
	 * 0 0 Aenys_Frey 1 1 team_X 
	 * 0 0 Alayaya 2 1 team_X 
	 * 
	 * @param csvFileName
	 * @return
	 */
	public Map<String, Integer> getMap(String csvFileName) {

		File file = new File(csvFileName);
		Map<String, Integer> ActualMap = new LinkedHashMap<String, Integer>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] csvEntries = line.split(",");

				String name = csvEntries[1];
				int deadAlive = Integer.parseInt(csvEntries[8]);

				ActualMap.put(name, deadAlive);

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

		return ActualMap;

	}

	public void writeCharacterPredictionsCsv(Map<String, Integer> charNameVsPrediction, String csvFileName) {

		File file = new File(csvFileName);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);

			Iterator<Entry<String, Integer>> iterator = charNameVsPrediction.entrySet().iterator();
			int count = 1;
			while (iterator.hasNext()) {
				Entry<String, Integer> prediction = iterator.next();

				writer.println("0 " + "0 " + prediction.getKey().trim().replaceAll(" ", "_") + " " + count + " "
						+ prediction.getValue() + " team_3");
				count++;
			}
		} catch (IOException e) {
			System.out.println("ERROR: while opening the file " + csvFileName);
			System.exit(-1);
		} finally {
			writer.close();
		}

	}

}

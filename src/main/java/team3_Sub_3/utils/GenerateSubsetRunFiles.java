package team3_Sub_3.utils;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class GenerateSubsetRunFiles {

	public List<String> readNames(String csvFileName) {
		List<String> namesList = new ArrayList<String>();
		File file = new File(csvFileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				namesList.add(line.trim());
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
		return namesList;

	}

	public void generateRunFile(int subsetCounter, String subSetFile, String runSubSetFile,String MLFileName) {
		List<String> charNamesFromSubset = new ArrayList<>();
		List<String> runFileDataLine = new ArrayList<String>();

		GenerateSubsetRunFiles subset = new GenerateSubsetRunFiles();
		charNamesFromSubset = subset.readNames(subSetFile);
		runFileDataLine = subset.readNames(MLFileName);
		File file = new File(runSubSetFile);
		PrintWriter writer = null;
		try {

			int charCounter = 1;
			writer = new PrintWriter(file);
			for (String name : charNamesFromSubset) {
				for (String tsvLine : runFileDataLine) {
					String[] names = tsvLine.split(" ");
					if (names[2].equals(name.replaceAll(" ", "_"))) {
						names[0] = String.valueOf(subsetCounter);
						names[3] = String.valueOf(charCounter);
						charCounter++;
						String line = "";
						for (String val : names) {
							line = line + val + " ";
						}
						writer.println(line.trim());
					}
				}
			}
		} catch (IOException e) {
			System.out.println("ERROR: while opening the file " + "subsetRunFile.run");
			System.exit(-1);
		} finally {
			writer.close();
		}
	}
}

package team3_Sub_3.featurealgo;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FinalSubsetGenerateFile {

	public Map<String, Integer> getMap(String csvFileName) {

		File file = new File(csvFileName);
		Map<String, Integer> ActualMap = new HashMap<String, Integer>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] csvEntries = line.split(",");
				String name = csvEntries[0];
				int deadAlive = Integer.parseInt(csvEntries[1]);

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

	public static void printmapStringInteger(Map<String, Integer> dataMap) {

		Iterator<Entry<String, Integer>> itr = dataMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Integer> iterator = itr.next();
			// System.out.println(iterator.getKey() + " " + iterator.getValue() );
		}

	}

	public static List<String> readNames(String csvFileName) {
		List<String> namesList = new ArrayList<String>();
		File file = new File(csvFileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = null;
			
			//reader.readLine();
			while ((line = reader.readLine()) != null) {
		
				namesList.add(line);
				//System.out.println(line);
		
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

	
	private static void writeCharacterPredictionsCsv(String queryId,String file1,Map<String, Integer> charNameVsPrediction, String csvFileName) {

		File file = new File(csvFileName);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			List<String> subsetList = new ArrayList<String>();
			subsetList = readNames(file1);
			//Iterator<Entry<String, Integer>> iterator = charNameVsPrediction.entrySet().iterator();
			int count = 1;
			for(String name : subsetList) {
				for(Map.Entry<String, Integer> charMap : charNameVsPrediction.entrySet())
				{
					if (charMap.getKey().equalsIgnoreCase(name)) {
						//System.out.println(charMap.getKey() + " " + name);
					//	System.out.println(queryId +" " + "0 " + charMap.getKey().trim().replaceAll(" ", "_") + " " + count + " "
							//	+ charMap.getValue() + " team_3");
						writer.println(queryId + " " + "0 " + charMap.getKey().trim().replaceAll(" ", "_") + " " + count + " "
								+ charMap.getValue() + " team_3");
				//writer.println("0 " + "0 " + prediction.getKey().trim().replaceAll(" ", "_") + " " + count + " "
						//+ prediction.getValue() + " team_3");
						count++;
					}		
				}
				
			}
//			while (iterator.hasNext()) {
//				for (String name : subsetList) {
//					Entry<String, Integer> prediction = iterator.next();
//					if (prediction.getKey().contains(name)) {
//						System.out.println("0 " + "0 " + prediction.getKey().trim().replaceAll(" ", "_") + " " + count + " "
//								+ prediction.getValue() + " team_3");
//				//writer.println("0 " + "0 " + prediction.getKey().trim().replaceAll(" ", "_") + " " + count + " "
//						//+ prediction.getValue() + " team_3");
//				
//					}		
//			} count++;
//		} 
		}catch (IOException e) {
			System.out.println("ERROR: while opening the file " + csvFileName);
			System.exit(-1);
		} finally {
			writer.close();
		}

	}

	/**
	 * Run File Format
	 * 
	 * 0 0 Aenys_Frey 1 1 team_X 
	 * 0 0 Alayaya 2 1 team_X 
	 * 
	 */

	public static void main(String args[]) {

		FinalSubsetGenerateFile m = new FinalSubsetGenerateFile();

		Map<String, Integer> map = m.getMap("data/svm_prediction1.csv");
	//	Map<String, Integer> map = m.getMap("C:\\Users\\Vivek\\PycharmProjects\\DataScience\\svmo.csv");
		String file ="../common-group-data/random-subsets/subset0.txt";
		String file1 ="../common-group-data/random-subsets/subset1.txt";
		String file2 ="../common-group-data/random-subsets/subset2.txt";
		String file3 ="../common-group-data/random-subsets/subset3.txt";
		String file4="../common-group-data/random-subsets/subset4.txt";
		String file5 ="../common-group-data/random-subsets/subset5.txt";
		String file6 ="../common-group-data/random-subsets/subset6.txt";
		String file7 ="../common-group-data/random-subsets/subset7.txt";
		String file8 ="../common-group-data/random-subsets/subset8.txt";
		String file9 ="../common-group-data/random-subsets/subset9.txt";
	
		// Map<String, Integer> map = m.getMap("BC_Character_Pred.csv");
		FinalSubsetGenerateFile.printmapStringInteger(map);
		
        writeCharacterPredictionsCsv("0",file,map, "data/subsetfile0.run");
		writeCharacterPredictionsCsv("1",file1,map, "data/subsetfile1.run");
		writeCharacterPredictionsCsv("2",file2,map, "data/subsetfile2.run");
		writeCharacterPredictionsCsv("3",file3,map, "data/subsetfile3.run");
		writeCharacterPredictionsCsv("4",file4,map, "data/subsetfile4.run");
		writeCharacterPredictionsCsv("5",file5,map, "data/subsetfile5.run");
		writeCharacterPredictionsCsv("6",file6,map, "data/subsetfile6.run");
		writeCharacterPredictionsCsv("7",file7,map, "data/subsetfile7.run");
		writeCharacterPredictionsCsv("8",file8,map, "data/subsetfile8.run");
		writeCharacterPredictionsCsv("9",file9,map, "data/subsetfile9.run");
		System.out.println("Run files created");

	}
}

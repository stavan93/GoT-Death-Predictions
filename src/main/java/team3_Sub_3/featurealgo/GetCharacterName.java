/**
 * 
 */
package team3_Sub_3.featurealgo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Gayathri
 *
 */
 
 /* A single class that will create the character names and can be used as and when required for character name
 comparison.
 */
 
public class GetCharacterName {

	List<String> characterNameList = new ArrayList<>();
/* This method is used to get the list of character names as an ArrayList
for ease of string manipulation tasks.*/

	public List<String> getActualNames() throws IOException {
		try {
			String filepath = "data/character-deaths.csv";
			String line = "";
			int count = 0;
			try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
				boolean flag = true;
				while ((line = reader.readLine()) != null) {
					if (flag) {
						flag = false;
						continue;
					}
					String[] name = line.split(",");

					if (!characterNameList.contains(name[0])) {
						characterNameList.add(name[0]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return characterNameList;
	}
	
	public HashMap<String, String> getDeaths() throws IOException {
		HashMap<String, String> characterNameMap = new HashMap<>();
		try {
			String filepath = "data/character-deaths.csv";
			String line = "";
			int count = 0;
			ArrayList<String> deathYears = new ArrayList<>();
			deathYears.add("299");
			deathYears.add("300");
			deathYears.add("298");
			deathYears.add("297");
			//BufferedWriter writer = new BufferedWriter(new FileWriter("Actual.txt"));
			try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
				boolean flag = true;
				while ((line = reader.readLine()) != null) {
					if (flag) {
						flag = false;
						continue;
					}
					String[] name = line.split(",");
					String pred = "0";
					if (!characterNameMap.containsKey(name[0])) {
						if(deathYears.contains(name[2]))
						{
						characterNameMap.put(name[0], "1");
						}
						else
						{
							characterNameMap.put(name[0], pred);
						}
					}

					// characterActualNames.add(name[5].replace(" ", "_"));
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return characterNameMap;
	}
	
	/* Method to return character names from character-deaths.csv */
	
	public HashMap<String, String> getActualNamesMap() throws IOException {
		HashMap<String, String> characterNameMap = new HashMap<>();
		try {
			String filepath = "data/character-deaths.csv";
			String line = "";
			int count = 0;
			
			//BufferedWriter writer = new BufferedWriter(new FileWriter("Actual.txt"));
			try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
				boolean flag = true;
				while ((line = reader.readLine()) != null) {
					if (flag) {
						flag = false;
						continue;
					}
					String[] name = line.split(",");

					if (!characterNameMap.containsKey(name[0])) {
						characterNameMap.put(name[0], name[6]);
					}

					// characterActualNames.add(name[5].replace(" ", "_"));
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return characterNameMap;
	}
	
	/*Method to return the character names from the deaths.csv file */
	
	public List<String> getActualNamesMap1() throws IOException {
		List<String> characterNameMap = new ArrayList<>();
		try {
			String filepath = "data/character-deaths.csv";
			String line = "";
			int count = 0;
			
			//BufferedWriter writer = new BufferedWriter(new FileWriter("Actual.txt"));
			try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
				boolean flag = true;
				while ((line = reader.readLine()) != null) {
					if (flag) {
						flag = false;
						continue;
					}
					String[] name = line.split(",");

					if (!characterNameMap.contains(name[0])) {
						characterNameMap.add(name[0]);
					}

					// characterActualNames.add(name[5].replace(" ", "_"));
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return characterNameMap;
	}
	
		/*Method to return the character names from the predictions.csv file.
		This is not currently being used here, but could be used later */
		
	public HashMap<String, String> getActualNamesPredictions() throws IOException {
		HashMap<String, String> characterNameMap = new HashMap<>();
		try {
			String filepath = "data/character-predictions.csv";
			String line = "";
			int count = 0;
			
			//BufferedWriter writer = new BufferedWriter(new FileWriter("Actual.txt"));
			try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
				boolean flag = true;
				while ((line = reader.readLine()) != null) {
					if (flag) {
						flag = false;
						continue;
					}
					String[] name = line.split(",");

					if (!characterNameMap.containsKey(name[5])) {
						characterNameMap.put(name[5], name[30]);
					}

					// characterActualNames.add(name[5].replace(" ", "_"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return characterNameMap;
	}
	
	public HashMap<String, String> getTrainNamesMap() throws IOException {
		HashMap<String, String> characterNameMap = new HashMap<>();
		try {
			String filepath = "data/deaths-train.csv";
			String line = "";
			int count = 0;
			
			//BufferedWriter writer = new BufferedWriter(new FileWriter("Actual.txt"));
			try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
				boolean flag = true;
				while ((line = reader.readLine()) != null) {
					if (flag) {
						flag = false;
						continue;
					}
					String[] name = line.split(",");

					if (!characterNameMap.containsKey(name[1])) {
						characterNameMap.put(name[1], name[7]);
					}

					// characterActualNames.add(name[5].replace(" ", "_"));
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return characterNameMap;
	}


}
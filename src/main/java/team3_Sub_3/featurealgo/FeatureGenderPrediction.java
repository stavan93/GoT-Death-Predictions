/**
 * 
 */

package team3_Sub_3.featurealgo;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gayathri
 *
 */
public class FeatureGenderPrediction {
	public static void mergeCSV() throws IOException {
		HashMap<String, String> characterNameMap = new HashMap<>();
		HashMap<String, String> characterNameMap1 = new HashMap<>();
		try {
			String filepath = "data/GenderFeatureFinal2.csv" ;
			String filepath1 = "data/LoGenderRe.csv";
			String line = "";
			int count = 0;

			// BufferedWriter writer = new BufferedWriter(new FileWriter("Actual.txt"));
			try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
				boolean flag = true;
				while ((line = reader.readLine()) != null) {
					if (flag) {
						flag = false;
						continue;
					}
					String[] name = line.split(",");

					if (!characterNameMap.containsKey(name[0])) {
						characterNameMap.put(name[0], name[1]);
					}

					// characterActualNames.add(name[5].replace(" ", "_"));
				}
				reader.close();
			}

			try (BufferedReader reader = new BufferedReader(new FileReader(filepath1))) {
				boolean flag = true;
				while ((line = reader.readLine()) != null) {
					if (flag) {
						flag = false;
						continue;
					}
					String[] name = line.split(",");

					if (!characterNameMap1.containsKey(name[0])) {
						characterNameMap1.put(name[0], name[1]);
					}

					// characterActualNames.add(name[5].replace(" ", "_"));
				}
				reader.close();
			}

			for (Map.Entry<String, String> character : characterNameMap.entrySet()) {
				for (Map.Entry<String, String> character1 : characterNameMap1.entrySet()) {
					if (character.getKey().equalsIgnoreCase(character1.getKey())) {
						characterNameMap.put(character.getKey(), character1.getValue());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	public static void Merge_CSV1() throws IOException {

		BufferedReader reader = new BufferedReader(
				new FileReader("data/character-deaths.csv"));
		BufferedReader reader1 = new BufferedReader(
				new FileReader("data/finalCSV.csv"));
		BufferedReader reader2 = new BufferedReader(new FileReader(
				"data/GenderFeatureFinal2.csv"));
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"data/EntityLinked_FeatureTrain.csv"));
		BufferedWriter writer1 = new BufferedWriter(new FileWriter(
				"data/EntityLinked_FeatureTest.csv"));
		HashMap<String,EntityCharacteristics> mainMap = new HashMap<String,EntityCharacteristics>();
		HashMap<String,GenderClass> genderMap = new HashMap<String,GenderClass>();
		String line = "";
		boolean flag = true;
		int count = 0;
		writer.write("No.,Character Name,Gender,Popularity,Death,Gender1,TFIDF\n");
		writer1.write("No.,Character Name,Gender,Popularity,Death,Gender1,TFIDF\n");


			while ((line = reader1.readLine()) != null) {
				if (flag) {
					// System.out.println(line);
					//System.out.println(reader1.readLine());
				//	System.out.println(reader2.readLine());
					flag = false;
					continue;
				}
				String[] linee = line.split(",");
				if (!mainMap.containsKey(linee[0])) {
					mainMap.put(linee[0], new EntityCharacteristics());
					mainMap.get(linee[0]).setGender(linee[1]);
					mainMap.get(linee[0]).setPopularity(linee[2]);
				}
			}
			
			while ((line = reader2.readLine()) != null) {
				if (flag) {
					// System.out.println(line);
					//System.out.println(reader1.readLine());
					//System.out.println(reader2.readLine());
					flag = false;
					continue;
				}
				String[] linee = line.split(",");
				if ((!genderMap.containsKey(linee[0]) && !linee[0].equals("Character Name"))) {
					genderMap.put(linee[0], new GenderClass());
					genderMap.get(linee[0]).setGender(linee[1]);
					genderMap.get(linee[0]).setTfidfscore(Double.valueOf(linee[2]));
				}
			}
			
			while ((line = reader.readLine()) != null) {
				if (flag) {
					// System.out.println(line);
				//	System.out.println(reader1.readLine());
					//System.out.println(reader2.readLine());
					flag = false;
					continue;
				}
				String[] linee = line.split(",");

			for(Map.Entry<String, EntityCharacteristics> character : mainMap.entrySet())
			{
				
				if(character.getKey().equalsIgnoreCase(linee[0]))
					if(count%2 == 0 )
					{
						if(linee[2]=="")
						{
						writer.append(count + "," + linee[0] + "," + character.getValue().getGender() + ","
								+ character.getValue().getPopularity() + ",0," + genderMap.get(linee[0]).getGender() + ","
								+ genderMap.get(linee[0]).getTfidfscore() + "\n");
						count++;
						}
						else
						{
						writer.append(count + "," + linee[0] + "," + character.getValue().getGender() + "," + character.getValue().getPopularity() + ",1," +  
								genderMap.get(linee[0]).getGender() + "," + genderMap.get(linee[0]).getTfidfscore() + "\n");
						count++;
						}
					}
					else
					{
						if(linee[2]=="")
						{
						writer1.append(count + "," + linee[0] + "," + character.getValue().getGender() + "," + character.getValue().getPopularity() + ",0," +  
								genderMap.get(linee[0]).getGender() + "," + genderMap.get(linee[0]).getTfidfscore() + "\n");
						count++;
						}
						else
						{
							writer1.append(count + "," + linee[0] + "," + character.getValue().getGender() + "," + character.getValue().getPopularity() + ",1," +  
									genderMap.get(linee[0]).getGender() + "," + genderMap.get(linee[0]).getTfidfscore() + "\n");
							count++;
						}
			
			}
			}
			}
			writer.close();
			writer1.close();
			
	}
	

	public static void main(String[] args) throws IOException {
		mergeCSV();
		Merge_CSV1();
	}
}

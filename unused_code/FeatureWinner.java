package team3_Sub_3.featurealgo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.Utility;

public class FeatureWinner {

	public Map<String, Integer> runFeatureWinner(String charTrainCSVFileName, String charTestCSVFileName) {

		List<String> houseNameFromCharPred = Utility.getColumnSpecificDataFromCsvFile(Constants.CHARACTER_PREDICTIONS_CSV, 14);
		
		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);
		
		Set<String> winerHouse = new HashSet<String>();

		fetchWinnerHouseName(houseNameFromCharPred,Constants.BOOK1,winerHouse);
		fetchWinnerHouseName(houseNameFromCharPred,Constants.BOOK2,winerHouse);
		fetchWinnerHouseName(houseNameFromCharPred,Constants.BOOK3,winerHouse);
		fetchWinnerHouseName(houseNameFromCharPred,Constants.BOOK4,winerHouse);
		fetchWinnerHouseName(houseNameFromCharPred,Constants.BOOK5,winerHouse);
		
		Set<String> winnerChars = new HashSet<String>();
		
		fetchWinningCharacter(charNamesFromDeathTest, Constants.BOOK1,winerHouse,winnerChars);
		fetchWinningCharacter(charNamesFromDeathTest, Constants.BOOK2,winerHouse,winnerChars);
		fetchWinningCharacter(charNamesFromDeathTest, Constants.BOOK3,winerHouse,winnerChars);
		fetchWinningCharacter(charNamesFromDeathTest, Constants.BOOK4,winerHouse,winnerChars);
		fetchWinningCharacter(charNamesFromDeathTest, Constants.BOOK5,winerHouse,winnerChars);

		Map<String, Integer> finalWinnerMap = prepareFinalWinnerMap(charNamesFromDeathTest, winnerChars);
		
		Utility.printStringIntegerMap(finalWinnerMap);
		
		return finalWinnerMap;

	}


	private Map<String, Integer> prepareFinalWinnerMap(List<String> charName, Set<String> winnerChars) {

		Map<String, Integer> finalWinnerMap = new LinkedHashMap<>();

		for (String chName : charName) {

			if (winnerChars.contains(chName)) {

				finalWinnerMap.put(chName, 1);
			} else {

				finalWinnerMap.put(chName, 0);
			}
		}

		return finalWinnerMap;

	}
	
	private void fetchWinningCharacter(List<String> uniqueCharName, String bookName, 
			Set<String> winerHouse, Set<String> winnerChars) {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(bookName)));
			String line = null;

			while ((line = reader.readLine()) != null) {
			for(String winnerHouse:winerHouse) {

				if(line.contains(winnerHouse)){
					
					String charName = Utility.fetchWordInString(line, uniqueCharName);
					
					if(StringUtils.isNotBlank(charName))
						winnerChars.add(charName);
				}
			}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error in Reading Book ");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in Reading Book ");
			e.printStackTrace();
		}
	}

	private void fetchWinnerHouseName(List<String> houseNames, String bookName, Set<String> houseWinner) {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(bookName)));
			String line = null;

			while ((line = reader.readLine()) != null) {

				Iterator<String> itr = houseNames.iterator();
				while (itr.hasNext()) {

					String houseName = itr.next();

					if (line.contains(houseName)) {

						if (line.contains("won") || line.contains("winner")) {
							houseWinner.add(houseName);
						} 
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error in Reading Book for Winner Prediction");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error in Reading Book for Winner Prediction");
			e.printStackTrace();
		}
	}
	
}

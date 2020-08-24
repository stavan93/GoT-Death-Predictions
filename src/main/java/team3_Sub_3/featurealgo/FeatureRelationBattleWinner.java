package team3_Sub_3.featurealgo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.CharArrayMap.EntrySet;

import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.Utility;

/**
 * Method: - Battle Winner on the Basis of House and Battles. Feature Winner
 * (REVERB Implementation)- combined with the battles and house names. 1. Search
 * the name of the battles in all the 5 books. 2. Calculate the frequency of the
 * battles. 3. Rank the battles on the basis of the frequency. 4. Pick the top 3
 * battles on the basis of Term frequency. 5. Calculate the list of winning
 * House for above 3 listed battles. (using winning phrases like win/won etc.)
 * 6. Calculate the list of winning character (using winning phrases like
 * win/won etc.) 7. To increase the accuracy we calculate the winning character
 * associated with the winning house. 8. Then in the final CSV we will write the
 * name of the characters and mark 1 if the character wins else mark as 0 if the
 * character losses.
 */

public class FeatureRelationBattleWinner {

	private static List<String> battleRelation = new ArrayList<String>();

	private static List<String> houseRelation = new ArrayList<String>();

	private static List<String> winKeywords = new ArrayList<String>();

	static {

		List<String> noun = new ArrayList<>();
		List<String> prepositions = new ArrayList<>();

		noun.add("battle");
		noun.add("Battle");
		prepositions.add("of");
		prepositions.add("of the");

		for (String n : noun) {

			for (String p : prepositions) {

				battleRelation.add(" " + n + " " + p);

			}

		}

		List<String> nounForHouse = new ArrayList<>();
		List<String> prepositionsForHouse = new ArrayList<>();

		nounForHouse.add("house");
		nounForHouse.add("houses");
		nounForHouse.add("Houses");
		nounForHouse.add("House");
		// prepositionsForHouse.add("of");
		prepositionsForHouse.add("the");

		for (String n : nounForHouse) {
			houseRelation.add(" " + n + " ");
			for (String p : prepositionsForHouse) {

				houseRelation.add(" " + p + " " + n + " ");

			}

		}

		winKeywords.add("win");
		winKeywords.add("winnner");
		winKeywords.add("won");
		winKeywords.add("conquer");

	}

	public Map<String, Integer> runBattleRelation(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> winnerPredictionMapTest = new LinkedHashMap<String, Integer>();
		
		Map<String, Integer> winnerPredictionMapTrain = new LinkedHashMap<String, Integer>();

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTrain);

		// Taking the name of the battles for checking the accuracy of battle names from
		// our Algo.
//		List<String> actualBattleNameFromFiles = Utility.getColumnSpecificDataFromCsvFile(Constants.BATTLES, 0);

		Set<String> battleName = extractBattleFromBook();

		Map<String, Integer> actualBattleMap = extractActualBattleFrequencyFromBook(new ArrayList<String>(battleName));

		Map<String, Integer> sortedBattleMap = Utility.sortHashMapByValue(actualBattleMap);

//		Utility.printStringIntegerMap(sortedBattleMap);

		List<String> popularBattle = getPopularBattle(sortedBattleMap, 3);

//		System.out.println("Popular Battle " + popularBattle);

		Set<String> houseNames = extractHouseFromBook();

		Set<String> winHouseName = extractWinningHouseFromBook(new ArrayList<String>(houseNames), popularBattle);

		Set<String> winCharNames = extractWinningCharFromBook(charNamesFromDeathTest);
		
		Set<String> winCharNames1 = extractWinningCharFromBook(charNamesFromDeathTrain);

		Set<String> winCharNameFinal = extractWinningCharLinkedToWinningHouse(new ArrayList<String>(winCharNames),
				new ArrayList<String>(winHouseName));
		
		Set<String> winCharNameFinal1 = extractWinningCharLinkedToWinningHouse(new ArrayList<String>(winCharNames1),
				new ArrayList<String>(winHouseName));


		// Utility.printList(new ArrayList<String>(killerForDeathTest));

		// Map for final prediction for Test Data
		winnerPredictionMapTest = winnerPrediction(charNamesFromDeathTest, winCharNameFinal);
		winnerPredictionMapTrain = winnerPrediction(charNamesFromDeathTrain, winCharNameFinal);
//		System.out.println("Map Size " + winnerPredictionMapTest.size());
//		Utility.printStringIntegerMap(winnerPredictionMapTest);

	//	return winnerPredictionMapTest;
		
		return winnerPredictionMapTrain;
	}
	
	public Map<String, Integer> runBattleRelation1(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> winnerPredictionMapTest = new LinkedHashMap<String, Integer>();
		
		Map<String, Integer> winnerPredictionMapTrain = new LinkedHashMap<String, Integer>();

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTrain);

			Set<String> battleName = extractBattleFromBook();

		Map<String, Integer> actualBattleMap = extractActualBattleFrequencyFromBook(new ArrayList<String>(battleName));

		Map<String, Integer> sortedBattleMap = Utility.sortHashMapByValue(actualBattleMap);

//		Utility.printStringIntegerMap(sortedBattleMap);

		List<String> popularBattle = getPopularBattle(sortedBattleMap, 3);

//		System.out.println("Popular Battle " + popularBattle);

		Set<String> houseNames = extractHouseFromBook();

		Set<String> winHouseName = extractWinningHouseFromBook(new ArrayList<String>(houseNames), popularBattle);

		Set<String> winCharNames = extractWinningCharFromBook(charNamesFromDeathTest);
		
		Set<String> winCharNames1 = extractWinningCharFromBook(charNamesFromDeathTrain);

		Set<String> winCharNameFinal = extractWinningCharLinkedToWinningHouse(new ArrayList<String>(winCharNames),
				new ArrayList<String>(winHouseName));
		
		Set<String> winCharNameFinal1 = extractWinningCharLinkedToWinningHouse(new ArrayList<String>(winCharNames1),
				new ArrayList<String>(winHouseName));

		winnerPredictionMapTest = winnerPrediction(charNamesFromDeathTest, winCharNameFinal);
		winnerPredictionMapTrain = winnerPrediction(charNamesFromDeathTrain, winCharNameFinal);

		return winnerPredictionMapTest;
		
		
	}

	private List<String> getPopularBattle(Map<String, Integer> battleMap, int topBattleCount) {

		List<String> topNBattleName = new ArrayList<String>();
		int count = 1;
		for (java.util.Map.Entry<String, Integer> entry : battleMap.entrySet()) {
			if (count <= topBattleCount) {
				topNBattleName.add(entry.getKey());
				count++;
			}

		}

		return topNBattleName;
	}

	/**
	 * This is the final method in which we make the map to give prediction whether
	 * the character is a killer or not. We mark the killer as 1 and 0 if not killed
	 * anyone.
	 * 
	 * @param charName
	 * @param winner
	 * @return
	 */
	private Map<String, Integer> winnerPrediction(List<String> charName, Set<String> winner) {

		Map<String, Integer> winnerPredictionMap = new LinkedHashMap<String, Integer>();

		for (String ch : charName) {

			if (winner.contains(ch)) {

				/**
				 * Logic here is that the character who wins will have value as 1 and the
				 * character who losses will be marked as 0.
				 */

				winnerPredictionMap.put(ch, 1);

			} else {

				winnerPredictionMap.put(ch, 0);
			}
		}
		return winnerPredictionMap;
	}

	private Set<String> extractWinningCharFromBook(List<String> charNamesFromDeathTest) {

		Set<String> winnerCharSet = new HashSet<>();

		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK1));
		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK2));
		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK3));
		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK4));
		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK5));

		return winnerCharSet;
	}

	private Set<String> extractWinningHouseFromBook(List<String> houseNames, List<String> popularBattle) {

		Set<String> winners = new HashSet<>();

		winners.addAll(extractBattleWinnerHouse(houseNames, Constants.BOOK1, popularBattle));
		winners.addAll(extractBattleWinnerHouse(houseNames, Constants.BOOK2, popularBattle));
		winners.addAll(extractBattleWinnerHouse(houseNames, Constants.BOOK3, popularBattle));
		winners.addAll(extractBattleWinnerHouse(houseNames, Constants.BOOK4, popularBattle));
		winners.addAll(extractBattleWinnerHouse(houseNames, Constants.BOOK5, popularBattle));

		return winners;
	}

	private Set<String> extractWinningCharLinkedToWinningHouse(List<String> charNamesFromDeathTest,
			List<String> winnerHouses) {

		Set<String> winnerCharSet = new HashSet<>();

		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK1, winnerHouses));
		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK2, winnerHouses));
		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK3, winnerHouses));
		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK4, winnerHouses));
		winnerCharSet.addAll(extractWinnerCharacterFromBook(charNamesFromDeathTest, Constants.BOOK5, winnerHouses));

		return winnerCharSet;
	}

	private Set<String> extractBattleFromBook() {

		Set<String> battles = new HashSet<>();

		battles.addAll(readForRelationExtraction(Constants.BOOK1));
		battles.addAll(readForRelationExtraction(Constants.BOOK2));
		battles.addAll(readForRelationExtraction(Constants.BOOK3));
		battles.addAll(readForRelationExtraction(Constants.BOOK4));
		battles.addAll(readForRelationExtraction(Constants.BOOK5));

		return battles;
	}

	private Set<String> extractHouseFromBook() {

		Set<String> house = new HashSet<>();

		house.addAll(readForRelationExtractionForHouse(Constants.BOOK1));
		house.addAll(readForRelationExtractionForHouse(Constants.BOOK2));
		house.addAll(readForRelationExtractionForHouse(Constants.BOOK3));
		house.addAll(readForRelationExtractionForHouse(Constants.BOOK4));
		house.addAll(readForRelationExtractionForHouse(Constants.BOOK5));

		return house;
	}

	private Map<String, Integer> extractActualBattleFrequencyFromBook(List<String> battleNames) {

		Map<String, Integer> battleNameMap = new LinkedHashMap<>();

		readForActualBattle(Constants.BOOK1, battleNames, battleNameMap);
		readForActualBattle(Constants.BOOK2, battleNames, battleNameMap);
		readForActualBattle(Constants.BOOK3, battleNames, battleNameMap);
		readForActualBattle(Constants.BOOK4, battleNames, battleNameMap);
		readForActualBattle(Constants.BOOK5, battleNames, battleNameMap);

		return battleNameMap;
	}

	private Map<String, Integer> readForActualBattle(String book, List<String> battleNames,
			Map<String, Integer> battleNameMap) {

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			while ((line = reader.readLine()) != null) {

				String battleName = Utility.fetchWordInString(line, battleNames);

				if (battleName != null) {
					if (battleNameMap.containsKey(battleName)) {
						battleNameMap.put(battleName, battleNameMap.get(battleName) + 1);
					} else {
						battleNameMap.put(battleName, 1);
					}

				}

			}
		} catch (FileNotFoundException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();
		}

		return battleNameMap;
	}

	/**
	 * Searching the relation phrase in the book in 2 steps: - 1. Using Syntactical
	 * extraction by searching the all possible relation for killer. 2. If result is
	 * not found then we apply the argument extraction to find the killer.
	 * 
	 * @param book
	 * @return
	 */
	private List<String> readForRelationExtraction(String book) {

		Set<String> battleNames = new HashSet<String>();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			while ((line = reader.readLine()) != null) {

				for (String br : battleRelation) {

					if (line.contains(br)) {
						int i = line.indexOf(br) + br.length() + 1;
						String temp = line.substring(i);
						String[] tempArray = temp.split(" ");
						String battleName = tempArray[0];
						if (battleName.length() > 4) {
							battleName = battleName.replace(".", "").replace(",", "");
							battleNames.add(br + " " + battleName);
						}
					}
				}

			}
		} catch (FileNotFoundException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();
		}

		return new ArrayList<String>(battleNames);
	}

	private List<String> readForRelationExtractionForHouse(String book) {

		Set<String> houseNames = new HashSet<String>();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			while ((line = reader.readLine()) != null) {

				for (String hr : houseRelation) {

					if (line.contains(hr)) {
						int i = line.indexOf(hr) + hr.length();
						String temp = line.substring(i);
						String[] tempArray = temp.split(" ");
						String houseName = tempArray[0];
						if (houseName.length() > 4) {
							houseName = houseName.replace(".", "").replace(",", "");
							houseNames.add(houseName);
						}
					}
				}

			}
		} catch (FileNotFoundException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();

		} catch (IOException e) {

			System.out.println("Error in readBook for Data Extraction" + e.getMessage());
			e.printStackTrace();
		}

		return new ArrayList<String>(houseNames);
	}

	private List<String> extractWinnerCharacterFromBook(List<String> uniqueCharName, String bookName) {

		List<String> winnerCharacter = new ArrayList<String>();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(bookName)));
			String line = null;

			while ((line = reader.readLine()) != null) {

				Iterator<String> itr = uniqueCharName.iterator();
				while (itr.hasNext()) {

					String charName = itr.next();

					if (line.contains(charName)) {
						for (String keyword : winKeywords)

						{
							if (line.contains(keyword)) {
								winnerCharacter.add(charName);
							}
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
		return winnerCharacter;
	}

	/**
	 * For Improving accuracy fetching the character name associated with the winner
	 * house.
	 * 
	 * @param uniqueCharName
	 * @param bookName
	 * @param winnerHouse
	 * @return
	 */
	private List<String> extractWinnerCharacterFromBook(List<String> uniqueCharName, String bookName,
			List<String> winnerHouse) {

		List<String> winnerCharacter = new ArrayList<String>();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(bookName)));
			String line = null;

			while ((line = reader.readLine()) != null) {

				Iterator<String> itr = uniqueCharName.iterator();
				while (itr.hasNext()) {

					String charName = itr.next();

					if (line.contains(charName)) {
						for (String keyword : winnerHouse)

						{
							if (line.contains(keyword)) {
								winnerCharacter.add(charName);
							}
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
		return winnerCharacter;
	}

	private List<String> extractBattleWinnerHouse(List<String> houseNames, String bookName,
			List<String> popularBattleList) {

		List<String> winnerHouses = new ArrayList<String>();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(bookName)));
			String line = null;

			while ((line = reader.readLine()) != null) {

				Iterator<String> itr = houseNames.iterator();
				while (itr.hasNext()) {

					String houseName = itr.next();

					if (line.contains(houseName)) {
						for (String keyword : winKeywords)

						{
							if (line.contains(keyword) || Utility.checkWordInString(line, popularBattleList)) {
								winnerHouses.add(houseName);
							}
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
		return winnerHouses;
	}

}

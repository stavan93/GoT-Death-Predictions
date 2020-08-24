package team3_Sub_3.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class FinalCSVPrediction {

	public void finalCsv(Map<String, Integer> weaponMap, Map<String, Integer> longevityMap,
			Map<String, Integer> relationKillerMap, Map<String, Integer> relationMarriedMap,
			Map<String, Integer> coreferenceGenderMap, Map<String, Integer> coreferenceNobilityMap,
			Map<String, Integer> relationBattleWinner, Map<String, Integer> deathMap) {

		List<String> uniqueCharName = Utility.getColumnSpecificDataFromCsvFile(Constants.DEATHS_TRAIN, 1);

		/*
		 * Even for Train Odd for Test
		 */

		File csvFinalFile = new File(Constants.FINAL_TRAIN_CSV);
//		File csvTrainFile = new File(Constants.FINAL_TRAIN_CSV);
//		File csvTestFile = new File(Constants.FINAL_TEST_CSV);

		PrintWriter csvwriterFinalFile = null;
//		PrintWriter csvwritercsvTrainFile = null;
//		PrintWriter csvwritercsvTestFile = null;

		try {
			csvwriterFinalFile = new PrintWriter(csvFinalFile);
			csvwriterFinalFile.println("S.NO:" + " , " + "name" + " , " + "weapon" + " , " + " longevity" + " , "
					+ "killer" + " , " + "married" + " , " + "gender" + " , " + "nobility" + " , " + "battleWinner");

//			csvwritercsvTrainFile = new PrintWriter(csvTrainFile);
//			csvwritercsvTrainFile.println("S.NO:" + " , " + "name" + " , " + "weapon" + " , " + " longevity" + " , "
//					+ "killer" + " , " + "married" + " , " + "gender" + " , " + "nobility" + " , " + "battleWinner"
//					+ " , " + "isAlive");
//
//			csvwritercsvTestFile = new PrintWriter(csvTestFile);
//			csvwritercsvTestFile.println("S.NO:" + " , " + "name" + " , " + "weapon" + " , " + " longevity" + " , "
//					+ "killer" + " , " + "married" + " , " + "gender" + " , " + "nobility" + " , " + "battleWinner");

			int weapon = 0;
			int longevity = 0;
			int killer = 0;
			int married = 0;
			int gender = 0;
			int nobility = 0;
			int battleWinner = 0;
			int death = 0;

			int counter = 1;
			for (String charName : uniqueCharName) {

				// 1
				if (weaponMap.containsKey(charName)) {
					weapon = weaponMap.get(charName);

				}
				// 2
				if (longevityMap.containsKey(charName)) {
					longevity = longevityMap.get(charName);

				}
				// 3
				if (relationKillerMap.containsKey(charName)) {
					married = relationKillerMap.get(charName);

				}
				// 4
				if (relationMarriedMap.containsKey(charName)) {
					killer = relationMarriedMap.get(charName);

				}
				// 5
				if (coreferenceGenderMap.containsKey(charName)) {
					gender = coreferenceGenderMap.get(charName);

				}
				// 6
				if (coreferenceNobilityMap.containsKey(charName)) {
					nobility = coreferenceNobilityMap.get(charName);

				}
				// 7
				if (relationBattleWinner.containsKey(charName)) {
					battleWinner = relationBattleWinner.get(charName);

				}
				// 8
				if (deathMap.containsKey(charName)) {
					death = deathMap.get(charName);

				}

				csvwriterFinalFile.println(
						counter + " , " + charName + " , " + weapon + " , " + longevity + " , " + killer + " , "
								+ married + " , " + gender + " , " + nobility + " , " + battleWinner);
//				if (counter % 2 == 0) {
//					csvwritercsvTrainFile.println(counter + " , " + charName + " , " + weapon + " , " + longevity
//							+ " , " + killer + " , " + married + " , " + gender + " , " + nobility + " , "
//							+ battleWinner + " , " + death);
//				} else {
//					csvwritercsvTestFile.println(counter + " , " + charName + " , " + weapon + " , " + longevity + " , "
//							+ killer + " , " + married + " , " + gender + " , " + nobility + " , " + battleWinner);
//				}
				counter++;

			}

		} catch (FileNotFoundException e) {
			System.out.println("Error while writing final csv File" + e.getMessage());
			e.printStackTrace();

		} finally {
			csvwriterFinalFile.close();
//			csvwritercsvTrainFile.close();
//			csvwritercsvTestFile.close();
		}
	}
}
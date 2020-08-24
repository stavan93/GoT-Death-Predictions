package team3_Sub_3.runner;

import java.util.Map;

import team3_Sub_3.featurealgo.FeatureCoreferenceGender;
import team3_Sub_3.featurealgo.FeatureCoreferenceNobility;
import team3_Sub_3.featurealgo.FeatureDeath;
import team3_Sub_3.featurealgo.FeatureLongevity;
import team3_Sub_3.featurealgo.FeatureRelationBattleWinner;
import team3_Sub_3.featurealgo.FeatureRelationKiller;
import team3_Sub_3.featurealgo.FeatureRelationMarried;
import team3_Sub_3.featurealgo.FeatureWeapon;
import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.FinalCSVPrediction;
import team3_Sub_3.utils.FinalCSVPrediction1;

public class FeatureRunner {

	public static void main(String[] args) {

		System.out.println("Algorithm Processing Started");

		long startTime = System.currentTimeMillis();

		// Predicting the Feature Weapon: - Characters using the most used Weapons.

		// Feature 1 : - Weapon
		FeatureWeapon fw = new FeatureWeapon();
		Map<String, Integer> weaponMap = fw.runFeatureWeapon(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);
		
		FeatureWeapon fw1 = new FeatureWeapon();
		Map<String, Integer> weaponMap1 = fw1.runFeatureWeapon1(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);

		// Feature 2: - Longevity
		FeatureLongevity fd = new FeatureLongevity();
		Map<String, Integer> longevityMap = fd.runFeatureLongevity(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);
		
		FeatureLongevity fd1 = new FeatureLongevity();
		Map<String, Integer> longevityMap1 = fd1.runFeatureLongevity1(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);

		// Feature 3: - Feature Killer Relation
		FeatureRelationKiller rk = new FeatureRelationKiller();
		Map<String, Integer> relationKillerMap = rk.runKillerRelation(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);
		
		FeatureRelationKiller rk1 = new FeatureRelationKiller();
		Map<String, Integer> relationKillerMap1 = rk1.runKillerRelation1(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);

		// Feature 4: - Feature relation Married
		FeatureRelationMarried rm = new FeatureRelationMarried();
		Map<String, Integer> relationMarriedMap = rm.runMarriedRelation(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);
		
		FeatureRelationMarried rm1 = new FeatureRelationMarried();
		Map<String, Integer> relationMarriedMap1 = rm1.runMarriedRelation1(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);

		// Feature 5: - Feature Coreference Gender
		FeatureCoreferenceGender crg = new FeatureCoreferenceGender();
		Map<String, Integer> coreferenceGenderMap = crg.runGenderRelation(Constants.DEATHS_TRAIN,
				Constants.DEATHS_TEST);
		
		FeatureCoreferenceGender crg1 = new FeatureCoreferenceGender();
		Map<String, Integer> coreferenceGenderMap1 = crg1.runGenderRelation1(Constants.DEATHS_TRAIN,
				Constants.DEATHS_TEST);

		// Feature 6: - Feature Coreference Nobility
		FeatureCoreferenceNobility crn = new FeatureCoreferenceNobility();
		Map<String, Integer> coreferenceNobilityMap = crn.runNobilityRelation(Constants.DEATHS_TRAIN,
				Constants.DEATHS_TEST);
		
		FeatureCoreferenceNobility crn1 = new FeatureCoreferenceNobility();
		Map<String, Integer> coreferenceNobilityMap1 = crn1.runNobilityRelation1(Constants.DEATHS_TRAIN,
				Constants.DEATHS_TEST);

		// Feature 7: - Feature Battle Winner
		FeatureRelationBattleWinner rb = new FeatureRelationBattleWinner();
		Map<String, Integer> relationBattleWinner = rb.runBattleRelation(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);
		
		FeatureRelationBattleWinner rb1 = new FeatureRelationBattleWinner();
		Map<String, Integer> relationBattleWinner1 = rb1.runBattleRelation1(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);

		// Feature 8 : - Feature Death
		FeatureDeath dm = new FeatureDeath();
		Map<String, Integer> deathMap = dm.rundeathRelation(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);
		
		FeatureDeath dm1 = new FeatureDeath();
		Map<String, Integer> deathMap1 = dm1.rundeathRelation1(Constants.DEATHS_TRAIN, Constants.DEATHS_TEST);

		// Final CSV Generation Code.
		FinalCSVPrediction csv = new FinalCSVPrediction();
		csv.finalCsv(weaponMap, longevityMap, relationKillerMap, relationMarriedMap, coreferenceGenderMap,
				coreferenceNobilityMap, relationBattleWinner, deathMap);

		FinalCSVPrediction1 csv1 = new FinalCSVPrediction1();
		csv1.finalCsv(weaponMap1, longevityMap1, relationKillerMap1, relationMarriedMap1, coreferenceGenderMap1,
				coreferenceNobilityMap1, relationBattleWinner1, deathMap1);
		
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		System.out.println(" Total Execution Time " + totalTime/1000 + " Seconds");
		System.out.println("Algorithm Processing Stopped");
	}

}

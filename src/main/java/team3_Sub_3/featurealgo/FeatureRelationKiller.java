package team3_Sub_3.featurealgo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import team3_Sub_3.utils.Constants;
import team3_Sub_3.utils.Utility;

public class FeatureRelationKiller {

	private static List<String> syntacticalConstraints = new ArrayList<>();

	private static List<String> lexicalConstraints = new ArrayList<>();

	/**
	 * Using Static block here For loading the relation verb and prepositions with
	 * the class in static scope as these are constant values.
	 */

	static {

		List<String> relationVerb = new ArrayList<>();

		relationVerb.add("killed");
		relationVerb.add("murdered");
		relationVerb.add("poisoned");
		relationVerb.add("stabbed"); // Stabbed in the throat
		relationVerb.add("slit");
		relationVerb.add("ripped"); // Ripped to shreds by Ghost
		relationVerb.add("burn"); // burn him alive
		relationVerb.add("hanging"); // body was seen hanging
		relationVerb.add("hung"); // hung her body
		relationVerb.add("pushed"); // pushed
		relationVerb.add("burned"); // burned her alive
		relationVerb.add("strangled"); // strangled her upon
		relationVerb.add("illness");
		relationVerb.add("beheaded"); // Beheaded for treason
		relationVerb.add("defeated"); // Hound defeated him
		relationVerb.add("mercy-killed"); // mercy-killed him with an arrow
		relationVerb.add("slashed");
		relationVerb.add("torn");

		List<String> prepositions = new ArrayList<>();

		prepositions.add("by");
		prepositions.add("to");
		prepositions.add("towards");
		prepositions.add("under");
		prepositions.add("on");
		prepositions.add("off");
		prepositions.add("underneath");
		prepositions.add("until");
		prepositions.add("upto");
		prepositions.add("into");
		prepositions.add("after");
		prepositions.add("with");
		prepositions.add("for");
		prepositions.add("in");

		for (String verb : relationVerb) {

			syntacticalConstraints.add(verb);

			for (String prep : prepositions) {

				StringBuilder sb = new StringBuilder();

				sb.append(verb).append(" ").append(prep);

				syntacticalConstraints.add(sb.toString());
			}
		}

		/**
		 * 
		 * In Lexical constraint these phrases will satisfy the syntactical constraint
		 * but are not relational. For Example: - Jon Snow mercy-killed him with an
		 * arrow Here the argument pair (Jon Snow, arrow) will represent a relation.
		 * 
		 */

		lexicalConstraints.add("ice blade");
		lexicalConstraints.add("dagger");
		lexicalConstraints.add("Goldcloaks");
		lexicalConstraints.add("dead");
		lexicalConstraints.add("Needle");
		lexicalConstraints.add("Arakh");
		lexicalConstraints.add("war");
		lexicalConstraints.add("battle");
		lexicalConstraints.add("executed");
		lexicalConstraints.add("arrow");

	}

	public Map<String, Integer> runKillerRelation(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> killerPredictionMapTrain = new LinkedHashMap<String, Integer>();

		Map<String, Integer> killerPredictionMapTest = new LinkedHashMap<String, Integer>();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);
		
		cf.santizieCharNames(charNamesFromDeathTrain);

		// Reading Character relation from All Books.

	 Set<String> killerForDeathTrain = extractKillerFromBook(charNamesFromDeathTrain);
//
//		 System.out.println(new ArrayList<String>(killerForDeathTrain).size());
//		 
//		 Utility.printList(new ArrayList<String>(killerForDeathTrain));
//		 
//		 // Map for final prediction for Train Data 
//		 killerPredictionMapTrain.putAll(killerPrediction(charNamesFromDeathTrain, killerForDeathTrain));

		Set<String> killerForDeathTest = extractKillerFromBook(charNamesFromDeathTest);

//		System.out.println(new ArrayList<String>(killerForDeathTest).size());

		// Utility.printList(new ArrayList<String>(killerForDeathTest));

		// Map for final prediction for Test Data
		killerPredictionMapTest = killerPrediction(charNamesFromDeathTest, killerForDeathTest);
		
		killerPredictionMapTrain = killerPrediction(charNamesFromDeathTrain, killerForDeathTrain);

//		Utility.printStringIntegerMap(killerPredictionMapTest);

//		return killerPredictionMapTest;
		return killerPredictionMapTrain;
	}

	public Map<String, Integer> runKillerRelation1(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> killerPredictionMapTrain = new LinkedHashMap<String, Integer>();

		Map<String, Integer> killerPredictionMapTest = new LinkedHashMap<String, Integer>();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);
		
		cf.santizieCharNames(charNamesFromDeathTrain);

	 Set<String> killerForDeathTrain = extractKillerFromBook(charNamesFromDeathTrain);

		Set<String> killerForDeathTest = extractKillerFromBook(charNamesFromDeathTest);

		// Map for final prediction for Test Data
		killerPredictionMapTest = killerPrediction(charNamesFromDeathTest, killerForDeathTest);
		
		killerPredictionMapTrain = killerPrediction(charNamesFromDeathTrain, killerForDeathTrain);


		return killerPredictionMapTest;
	}

	
	/**
	 * This is the final method in which we make the map to give prediction whether
	 * the character is a killer or not. We mark the killer as 1 and 0 if not killed
	 * anyone.
	 * 
	 * @param charName
	 * @param killer
	 * @return
	 */
	private Map<String, Integer> killerPrediction(List<String> charName, Set<String> killer) {

		Map<String, Integer> killerPredictionMap = new LinkedHashMap<String, Integer>();

		for (String ch : charName) {

			if (killer.contains(ch)) {

				/**
				 * Logic here is that the person who kills will have value as 1 and the person
				 * who did not kill any one will have value 0.
				 */

				killerPredictionMap.put(ch, 1);

			} else {

				killerPredictionMap.put(ch, 0);
			}
		}
		return killerPredictionMap;
	}

	private Set<String> extractKillerFromBook(List<String> charNamesFromDeathTrain) {

		Set<String> killer = new HashSet<>();

		killer.addAll(readForRelationExtraction(Constants.BOOK1, charNamesFromDeathTrain));

		killer.addAll(readForRelationExtraction(Constants.BOOK2, charNamesFromDeathTrain));

		killer.addAll(readForRelationExtraction(Constants.BOOK3, charNamesFromDeathTrain));

		killer.addAll(readForRelationExtraction(Constants.BOOK4, charNamesFromDeathTrain));

		killer.addAll(readForRelationExtraction(Constants.BOOK5, charNamesFromDeathTrain));

		return killer;
	}

	/**
	 * Searching the relation phrase in the book in 2 steps: - 1. Using Syntactical
	 * extraction by searching the all possible relation for killer. 2. If result is
	 * not found then we apply the argument extraction to find the killer.
	 * 
	 * @param book
	 * @param uniqueCharName
	 * @return
	 */
	private List<String> readForRelationExtraction(String book, List<String> uniqueCharName) {

		List<String> killerCharacter = new ArrayList<String>();

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(new File(book)));
			String line = null;

			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {

				for (String sc : syntacticalConstraints) {

					boolean paraFlag = false;

					if (line.contains(sc)) {

						/**
						 * Here we will search the character in the same line of the extraction. If
						 * character found then we are adding in the killer list. but there is no
						 * character in that line then we are making a paragraph and we are searching
						 * the killer character in the paragraph with our argument extraction. In
						 * argument extraction method which is below we search the character name in the
						 * whole paragraph with expression criteria. and expression criteria searching
						 * with the argument pair (Character-name, (weapon/dead or alive)).
						 */
						if (!relationExtraction(sc, uniqueCharName, killerCharacter)) {
							sb.append(line);
							paraFlag = true;
						}

					}
					if (paraFlag) {

						sb.append(line);

					}

					/**
					 * Here we are making the paragraph string based on the "."
					 */
					if (line.contains(".") && paraFlag) {

						sb.append(line);
						paraFlag = false;
						argumentExtraction(uniqueCharName, sb.toString(), killerCharacter);
						sb = new StringBuilder();
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

		return killerCharacter;
	}

	/**
	 * 
	 * @param charName
	 * @param paragraph
	 * @param killerCharacter
	 */
	private void argumentExtraction(List<String> charName, String paragraph, List<String> killerCharacter) {

		for (String ch : charName) {

			/**
			 * Here we are checking that the paragraph contains the character name or we
			 * check in the paragraph the lexical constraints are present and if character
			 * found with our predicted argument pair. Argument pair is the name of the
			 * character and the lexical constraint together. Then we add in the killer
			 * character list.
			 */

			if (paragraph.contains(ch) || Utility.checkWordInString(paragraph, lexicalConstraints)) {

				killerCharacter.add(ch);
				break;

			}
		}
	}

	/**
	 * 
	 * In this methods we are just checking the character name with the possible
	 * relation. If the character name is present with the relation we directly add
	 * to the killer character list.
	 * 
	 * @param line
	 * @param uniqueCharName
	 * @param killerCharacter
	 * @return
	 */
	private boolean relationExtraction(String line, List<String> uniqueCharName, List<String> killerCharacter) {

		String charPresence = Utility.fetchWordInString(line, uniqueCharName);

		if (charPresence != null) {

			killerCharacter.add(charPresence);
			return true;
		}

		return false;
	}
}

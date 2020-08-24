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

public class FeatureRelationMarried {

	private static List<String> syntacticalConstraints = new ArrayList<>();

	private static List<String> lexicalConstraints = new ArrayList<>();

	/**
	 * Using Static block here For loading the relation verb and prepositions with
	 * the class in static scope as these are constant values.
	 */

	static {

		List<String> relationVerb = new ArrayList<>();

		relationVerb.add(" married");
		relationVerb.add(" weds");
		relationVerb.add(" wed");
		relationVerb.add(" engaged");
		relationVerb.add(" wedlock");
		relationVerb.add(" wedded");
		relationVerb.add(" be married");

		List<String> prepositions = new ArrayList<>();

		prepositions.add("by");
		prepositions.add("to");
		prepositions.add("with");
		prepositions.add("for");
		prepositions.add("in");
		prepositions.add("of");

		List<String> pronouns = new ArrayList<>();

		pronouns.add("his");
		pronouns.add("her");

		for (String verb : relationVerb) {

			syntacticalConstraints.add(verb);

			for (String prep : prepositions) {

				StringBuilder sb = new StringBuilder();

				sb.append(verb).append(" ").append(prep);

				syntacticalConstraints.add(sb.toString());

				for (String pr : pronouns) {

					sb.append(" " + pr);
					syntacticalConstraints.add(sb.toString());
				}
			}
		}

		lexicalConstraints.add("husband");
		lexicalConstraints.add("wife");
		lexicalConstraints.add("spouse");
		lexicalConstraints.add("couple");
		lexicalConstraints.add("lover");
		lexicalConstraints.add("love");

	}

	public Map<String, Integer> runMarriedRelation(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> marriedPredictionMapTrain = new LinkedHashMap<String, Integer>();

		Map<String, Integer> marriedPredictionMapTest = new LinkedHashMap<String, Integer>();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);
		
		cf.santizieCharNames(charNamesFromDeathTrain);

		// Reading Character relation from All Books.

		 Set<String> marriedForDeathTrain = extractMarriedFromBook(charNamesFromDeathTrain);
//
//		 System.out.println(new ArrayList<String>(marriedForDeathTrain).size());
//		 
//		 Utility.printList(new ArrayList<String>(marriedForDeathTrain));
//		 
//		 // Map for final prediction for Train Data 
//		 marriedPredictionMapTrain.putAll(marriedPrediction(charNamesFromDeathTrain, marriedForDeathTrain));

		Set<String> marriedForDeathTest = extractMarriedFromBook(charNamesFromDeathTest);

//		System.out.println(new ArrayList<String>(marriedForDeathTest).size());

		// Utility.printList(new ArrayList<String>(marriedForDeathTest));

		// Map for final prediction for Test Data
		marriedPredictionMapTest = marriedPrediction(charNamesFromDeathTest, marriedForDeathTest);
		
		marriedPredictionMapTrain = marriedPrediction(charNamesFromDeathTrain, marriedForDeathTrain);

//		Utility.printStringIntegerMap(marriedPredictionMapTest);

		//return marriedPredictionMapTest;
		
		return marriedPredictionMapTrain;
	}
	
	public Map<String, Integer> runMarriedRelation1(String charTrainCSVFileName, String charTestCSVFileName) {

		CommonFeature cf = new CommonFeature();

		Map<String, Integer> marriedPredictionMapTrain = new LinkedHashMap<String, Integer>();

		Map<String, Integer> marriedPredictionMapTest = new LinkedHashMap<String, Integer>();

		List<String> charNamesFromDeathTrain = Utility.getColumnSpecificDataFromCsvFile(charTrainCSVFileName, 1);

		List<String> charNamesFromDeathTest = Utility.getColumnSpecificDataFromCsvFile(charTestCSVFileName, 1);

		cf.santizieCharNames(charNamesFromDeathTest);
		
		cf.santizieCharNames(charNamesFromDeathTrain);

		// Reading Character relation from All Books.

		 Set<String> marriedForDeathTrain = extractMarriedFromBook(charNamesFromDeathTrain);

		Set<String> marriedForDeathTest = extractMarriedFromBook(charNamesFromDeathTest);

		marriedPredictionMapTest = marriedPrediction(charNamesFromDeathTest, marriedForDeathTest);
		
		marriedPredictionMapTrain = marriedPrediction(charNamesFromDeathTrain, marriedForDeathTrain);


		return marriedPredictionMapTest;
		
	}

	/**
	 * This is the final method in which we make the map to give prediction whether
	 * the character is a married or not. We mark the married as 1 and 0 if not
	 * married.
	 * 
	 * @param charName
	 * @param married
	 * @return
	 */
	private Map<String, Integer> marriedPrediction(List<String> charName, Set<String> married) {

		Map<String, Integer> marriedPredictionMap = new LinkedHashMap<String, Integer>();

		for (String ch : charName) {

			if (married.contains(ch)) {

				marriedPredictionMap.put(ch, 1);

			} else {

				marriedPredictionMap.put(ch, 0);
			}
		}
		return marriedPredictionMap;
	}

	private Set<String> extractMarriedFromBook(List<String> charNamesFromDeathTrain) {

		Set<String> married = new HashSet<>();

		married.addAll(readForRelationExtraction(Constants.BOOK1, charNamesFromDeathTrain));
		married.addAll(readForRelationExtraction(Constants.BOOK2, charNamesFromDeathTrain));
		married.addAll(readForRelationExtraction(Constants.BOOK3, charNamesFromDeathTrain));
		married.addAll(readForRelationExtraction(Constants.BOOK4, charNamesFromDeathTrain));
		married.addAll(readForRelationExtraction(Constants.BOOK5, charNamesFromDeathTrain));

		return married;
	}

	/**
	 * Searching the relation phrase in the book in 2 steps: - 1. Using Syntactical
	 * extraction by searching the all possible relation for married. 2. If result
	 * is not found then we apply the argument extraction to find the married.
	 * 
	 * @param book
	 * @param uniqueCharName
	 * @return
	 */
	private List<String> readForRelationExtraction(String book, List<String> uniqueCharName) {

		List<String> marriedCharacter = new ArrayList<String>();

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
						 * character found then we are adding in the married list. but there is no
						 * character in that line then we are making a paragraph and we are searching
						 * the married character in the paragraph with our argument extraction. In
						 * argument extraction method which is below we search the character name in the
						 * whole paragraph with expression criteria. and expression criteria searching
						 * with the argument pair (Character-name, (spouse/engaged/couple)).
						 */
						if (!relationExtraction(sc, uniqueCharName, marriedCharacter)) {
							sb.append(line);
							paraFlag = true;
						}

					}
					if (paraFlag) {

						sb.append(line);

					}

					if (line.contains(".") && paraFlag) {

						sb.append(line);
						paraFlag = false;
						argumentExtraction(uniqueCharName, sb.toString(), marriedCharacter);
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

		return marriedCharacter;
	}

	/**
	 * Here we are checking that the paragraph contains the character name or we
	 * check in the paragraph the lexical constraints are present and if character
	 * found with our predicted argument pair. Then we add in the married character
	 * list.
	 */
	private void argumentExtraction(List<String> charName, String paragraph, List<String> marriedCharacter) {

		for (String ch : charName) {

			if (paragraph.contains(ch) || Utility.checkWordInString(paragraph, lexicalConstraints)) {

				marriedCharacter.add(ch);
				break;
			}
		}
	}

	/**
	 * 
	 * In this methods we are just checking the character name with the possible
	 * relation. If the character name is present with the relation we directly add
	 * to the married character list.
	 * 
	 * @param line
	 * @param uniqueCharName
	 * @param marriedCharacter
	 * @return
	 */
	private boolean relationExtraction(String line, List<String> uniqueCharName, List<String> marriedCharacter) {

		String charPresence = Utility.fetchWordInString(line, uniqueCharName);

		if (charPresence != null) {

			marriedCharacter.add(charPresence);
			return true;
		}

		return false;
	}
}

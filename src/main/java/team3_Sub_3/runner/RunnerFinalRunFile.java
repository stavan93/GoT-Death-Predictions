package team3_Sub_3.runner;

import java.util.Map;

import team3_Sub_3.utils.GenerateFinalRunFile;
import team3_Sub_3.utils.GenerateSubsetRunFiles;

public class RunnerFinalRunFile {

	public static void main(String args[]) {

		GenerateFinalRunFile m = new GenerateFinalRunFile();
		
		// Algo: - 1
		Map<String, Integer> map = m.getMap("data/Input_ML_CSV/BC_Pred.csv");
		m.writeCharacterPredictionsCsv(map, "data/Output_RunFile/BC_runfile.run");
		
		// Algo: - 2
		Map<String, Integer> map1 = m.getMap("data/Input_ML_CSV/LR_Pred.csv");
		m.writeCharacterPredictionsCsv(map1, "data/Output_RunFile/LR_runfile.run");
		
		// Algo: - 3
		Map<String, Integer> map2 = m.getMap("data/Input_ML_CSV/RC_Pred.csv");
		m.writeCharacterPredictionsCsv(map2, "data/Output_RunFile/RC_runfile.run");
		

	}
	
}

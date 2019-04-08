package cube.semantics.helpers;

import cube.semantics.blocks.MainBlock;

public class Assignment {
	public static boolean checkIfAssignment(String line) {
		if(line.contains("==")) {
			MainBlock.output_value += "::::::::::: Invalid assignment" + "\n";
			System.err.println("\t::::::::::: Invalid assignment");
		}
		else if(line.contains("=") && !line.contains("\"")){
			return true;
		}
		return false;
	}
}

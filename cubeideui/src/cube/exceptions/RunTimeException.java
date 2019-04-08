package cube.exceptions;

import cube.semantics.blocks.MainBlock;

public class RunTimeException{
	public static void showException(String message) {
		MainBlock.output_value += message + "\n";
		System.err.println("::::::::" + message);
	}
}

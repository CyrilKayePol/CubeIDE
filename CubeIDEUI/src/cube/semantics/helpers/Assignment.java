package cube.semantics.helpers;

public class Assignment {
	public static boolean checkIfAssignment(String line) {
		if(line.contains("==")) {
			System.err.println("\t::::::::::: Invalid assignment");
		}
		else if(line.contains("=")){
			return true;
		}
		return false;
	}
}

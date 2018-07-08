package cube.semantics.math_operations;

import cube.exceptions.RunTimeException;
import cube.semantics.Variable;
import cube.semantics.helpers.SeenVariableOperations;

public class EvaluateType {
	
	private static String to_evaluate;
	@SuppressWarnings("unused")
	private static Variable variable;

	public static void evaluate(Variable v, String evaluate) {
		variable = v;
		to_evaluate = evaluate;
	}

	public static boolean checkIfValidArithmeticOperands() {
		
		char[] operators = {'+', '-', '*', '/', '(', ')'};
		
		String eval = to_evaluate.replaceAll("\\s","");
		for(int i =0; i < operators.length; i++) {
			eval = eval.replace(operators[i], ',');
		}
		
		String[] operands = eval.split(",");
		
		boolean isValid = false;
		for(int i = 0; i < operands.length; i++) {
			operands[i] = operands[i].trim();
			if(operands[i].equals(""))
				continue;
			if(Variable.isInt(operands[i]) || Variable.isFloat(operands[i])) {
				isValid = true;
			}
			else if(operands[i].equals("true") || operands[i].equals("false")) {
				RunTimeException.showException("Invalid Expression. One of the operands is a boolean. " + operands[i]);
				isValid = false;
				break;
			}
			else if(operands[i].startsWith("\"") && operands[i].endsWith("\"")) {
				RunTimeException.showException("Invalid Expression. One of the operands is a String. " + operands[i]);
				isValid = false;
				break;
			}
			else {
				Variable v = SeenVariableOperations.findInSeenVariables(operands[i]);
				if(v == null) {
					RunTimeException.showException("Invalid Expression. One of the operands is undefined. " + operands[i]);
					isValid = false;
					//break;
				}
				else {
					if(v.getValue().getClass() == Integer.class || v.getValue().getClass() == Float.class) {
						isValid = true;
					}
					else {
						RunTimeException.showException("Invalid Expression. One of the operands value is not valid for arithmetic computation. " + operands[i]);
						isValid = false;
						break;
					}
				}
			}
		}
		return isValid;
	}
	
	public static void main(String[] args) {
		evaluate(null, "(10+191+true) * (1 + 90+6)");
		checkIfValidArithmeticOperands();
	}
}

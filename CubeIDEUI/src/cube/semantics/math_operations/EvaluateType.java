package cube.semantics.math_operations;

import java.util.ArrayList;

import cube.exceptions.RunTimeException;
import cube.semantics.Variable;
import cube.semantics.helpers.SeenVariableOperations;

public class EvaluateType {
	
	private static String to_evaluate;
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
					break;
				}
				else {
					if(v.getValue().getClass() == Integer.class || v.getValue().getClass() == Float.class) {
						isValid = true;
						to_evaluate = to_evaluate.replace(operands[i], v.getValue().toString());
					}
					else {
						System.out.println("Operand: "+  operands[i]);
						RunTimeException.showException("Invalid Expression. One of the operands value is not valid for arithmetic computation. " + operands[i]);
						isValid = false;
						break;
					}
				}
			}
		}
		
		return isValid;
	}
	
	public static double eval() {
	    return new Object() {
	    	String str = to_evaluate;
	        int pos = -1, ch;

	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }

	        boolean eat(int charToEat) {
	            while (ch == ' ') nextChar();
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        double parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
	            return x;
	        }

	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)`
	        //        | number | functionName factor | factor `^` factor

	        double parseExpression() {
	            double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }

	        double parseTerm() {
	            double x = parseFactor();
	            for (;;) {
	                if      (eat('*')) x *= parseFactor(); // multiplication
	                else if (eat('/')) x /= parseFactor(); // division
	                else return x;
	            }
	        }
	        double parseFactor() {
	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                eat(')');
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(str.substring(startPos, this.pos));
	            } else {
	                throw new RuntimeException("Unexpected: " + (char)ch);
	            }
	            variable.setValue(x);
	            return x;
	        }
	    }.parse();
	}
	
	public static ArrayList<String> createStack(String expression){
		ArrayList<String> stack  = new ArrayList<String>();
		
		
		return stack;
	}
	
	public static boolean evaluateLogicalOperation() {
		return false;
	}
}

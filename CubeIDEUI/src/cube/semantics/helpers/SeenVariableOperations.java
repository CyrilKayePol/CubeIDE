package cube.semantics.helpers;

import java.util.ArrayList;
import java.util.HashMap;

import cube.exceptions.RunTimeException;
import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.math_operations.EvaluateType;
import cube.semantics.math_operations.ExpressionParser;

public class SeenVariableOperations {
	
	private static ArrayList<Variable> seen_variables;
	private static ArrayList<String> arguments;
	public static void setSeenVariables(ArrayList<Variable> variables) {
		seen_variables = variables;
	}
	public static void removeVariableIfExists(String name) {
		for(int i = 0; i < seen_variables.size(); i++) {
			Variable var = seen_variables.get(i);
			if(var.getName().equals(name)) {
				seen_variables.remove(var);
				break;
			}
		}
	}
	
	public static void checkRightSide(String right, Variable v, int line_num) {
		if(Variable.isInt(right.trim())) {
			v.setValue(Integer.parseInt(right.trim()));
			v.setType(Type.INTEGER);
		}
		else if(Variable.isFloat(right.trim())) {
			v.setValue(Float.parseFloat(right.trim()));
			v.setType(Type.FLOAT);
		}
		else if(right.trim().equals("true")|| right.trim().equals("false")) {
			v.setValue(right.trim());
			v.setType(Type.BOOLEAN);
		}
		else if(right.trim().startsWith("\"") && right.trim().endsWith("\"")) {
			v.setType(Type.STRING);
			v.setValue(right.trim());
		}
		else {
			boolean hasEntered = false;
			for(int i = 0;  i < seen_variables.size(); i++) {
				if(right.trim().equals(seen_variables.get(i).getName().trim())){
					v.setValue(seen_variables.get(i).getValue());
					v.setType(seen_variables.get(i).getType());
					hasEntered = true;
					break;
				}
			}
			
			if(!hasEntered) {
				if(right.contains("+")|| right.contains("-") || right.contains("\\") ||
						right.contains("|") || right.contains("&&") ||right.contains("*") || 
						right.contains("!")||(right.contains("(" )&& right.contains(")"))){
							v.setValue(right);
							v.setType(Type.EVAL);
						}
				else {
					RunTimeException.showException("Value on the right is not valid "+ line_num);
				}
			}
		}
	}	
	
	public static void evaluateEvalType() {
		for(Variable vv : seen_variables) {
			if(vv.getType() != null) {
				if(vv.getType().equals(Type.EVAL)) {
					EvaluateType.evaluate(vv, vv.getValue().toString());
					
					if(EvaluateType.checkIfValidArithmeticOperands()) {
						vv.setType(Type.FLOAT);
						vv.setValue(EvaluateType.eval());
					}
					else {
						if(ExpressionParser.isValid) {
							vv.setType(Type.BOOLEAN);
							String[] args = new String[arguments.size()];
							String arg = putBraces(vv.getValue().toString());
							vv.setValue(ExpressionParser.evaluate(arg, arguments.toArray(args)));
						}
						else {
							RunTimeException.showException(" Specified value is invalid ");
						}
						ExpressionParser.isValid = true;
					}
				}
			}
			
		}
	}
	
	public static String putBraces(String stmt) {
		
		arguments = new ArrayList<String>();
		StringBuilder m = new StringBuilder(stmt);
		for(Variable vv: seen_variables) {
			if(stmt.contains(vv.getName())) {
				int offset = m.indexOf(vv.getName());
				m = m.insert(offset, "[");
				m = m.insert(offset+vv.getName().length(), "]");
				arguments.add(vv.getName());
				arguments.add(vv.getValue().toString());
				System.out.println(m);
			}
		}
		stmt = m.toString();
		return stmt;
	}
	public static void checkAssignments(HashMap<Integer, String> line_hash, int start_main, int end_main) {
		for(int i = start_main; i < end_main; i++) {
			String str = line_hash.get(i);
			if(str!= null) {
				if(str.startsWith("if") || str.startsWith("while") || str.startsWith("elsif") ||
						str.startsWith("var")) {}
				else if(str.contains("==")) {}
				else {
					if(str.contains("=")) {
						String name = str.substring(0, str.indexOf("="));
						
						for(int j = 0; j < seen_variables.size(); j++) {
							Variable v = seen_variables.get(j);
							
							if(v.getName().trim().equals(name.trim())) {
								SeenVariableOperations.checkRightSide(str.substring(str.indexOf("=") + 1, str.length()), v, i);
								break;
							}
							else {
								if(j == seen_variables.size() - 1) {
									RunTimeException.showException("Variable not found at line "+ i);
								}
									
							}
						}
					}
				}
			}
		}
	}
	
	public static Variable[] getArgsInSeenVariables(String[] args, String[] params) {
		Variable[] var = new Variable[args.length];
		int index = 0;
		for(int i = 0; i < params.length; i++) {
			String vv = params[i];
			
			Variable v = findInSeenVariables(vv);
			
			if(v!=null) {
				Variable vvv = new Variable(args[i], v.getValue());
				var[index] = vvv;
				++index;
			}
		}
		
		return var;
	}
	
	public static Variable findInSeenVariables(String name) {
		for(Variable vv : seen_variables) {
			if(vv.getName().equals(name)) {
				return vv;
			}
		}
		return null;
	}
}

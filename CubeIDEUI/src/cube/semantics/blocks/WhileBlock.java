package cube.semantics.blocks;

import java.util.ArrayList;

import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.helpers.Assignment;
import cube.semantics.helpers.SeenVariableOperations;
import cube.semantics.math_operations.EvaluateType;
import cube.semantics.math_operations.ExpressionParser;

public class WhileBlock extends Block{
	private String conditions;
	private boolean toEvaluate;
	public WhileBlock(Block block, int end, int start) {
		super(Type.WHILE, block, end, start);
		SeenVariableOperations.evaluateEvalType();
	}
	
	public void setCondition(String con) {
		conditions = con;
		evaluateCondition();
	}
	
	public String getConditions() {
		return conditions;
	}
	public boolean getToEvaluate() {
		return toEvaluate;
	}
	
	public void setToEvaluate(boolean bool) {
		toEvaluate = bool;
	}
	@Override
	public void whatToDo() {
		while(toEvaluate) {
			for(int i = startline;i < endline;i++) {
				Object b = lines_under_me.get(i);
				
				if(b!=null) {
					
					if(Assignment.checkIfAssignment(b.toString())) {
						int begin = b.toString().indexOf("=");
						
						String right = b.toString().substring(0, begin);
						String left = b.toString().substring(begin+1).trim();
						
						Variable v = SeenVariableOperations.findInSeenVariables(right.trim());
						
						if(v!=null) {
							Variable v1 = SeenVariableOperations.findInSeenVariables(left);
							if(v1!=null) {
								v.setValue(v1.getValue());
							}
							else {
								String type = Variable.identifyTypes(left);
								
								if(type.equals(Type.EVAL)) {
									EvaluateType.evaluate(v, left);
									if(EvaluateType.checkIfValidArithmeticOperands()) {
										v.setType(Type.FLOAT);
										v.setValue(EvaluateType.eval());
									}
									else {
										/**
										 * TO DO: check if value is logical: true or false
										 */
									}
								}
							}
						}
						
					}
					else {
						if(b.toString().startsWith("print")) {
							String line = b.toString().replace("print", "").trim();
							Variable v = SeenVariableOperations.findInSeenVariables(line);
							if(v!=null) {
								MainBlock.output_value += v.getValue()+ "\n";
							}
						}
					}
				}
				else {
					for(int j = i; j < sub_blocks.size(); j++) {
						Block block = sub_blocks.get(j);
						
						if(block.getStartline() == i) {
							block.whatToDo();
							
							mother_block.setVariables(block.variables);
							break;
						}
					}
				}
			}
			evaluateCondition();
		}
	}
	public void evaluateCondition() {
		conditions = conditions.replaceAll("\\s","");
		String[] operands = conditions.split("!=|==|>=|<=|>|<|\\|\\||&&|\\*|/|\\+|-|^");
		
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder inputParser = new StringBuilder(conditions);
		for(String str:operands) {
			Variable v = SeenVariableOperations.findInSeenVariables(str.trim());
			if(v != null) {
				int beginIndex = inputParser.indexOf(str.trim());
				inputParser.insert(beginIndex, '[');
				inputParser.insert(beginIndex+str.length()+1, ']');
				params.add(str);
				if(v.getValue() == null) params.add("");
				else params.add(v.getValue().toString().trim());
			}
		}
		try {
			toEvaluate = (ExpressionParser.evaluate(inputParser.toString(),params.toArray()));
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("::::::::::::::::: Condition is not valid");
		}
	}
}

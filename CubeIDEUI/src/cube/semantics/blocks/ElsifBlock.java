package cube.semantics.blocks;

import java.util.ArrayList;

import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.helpers.Assignment;
import cube.semantics.helpers.SeenVariableOperations;
import cube.semantics.math_operations.EvaluateType;
import cube.semantics.math_operations.ExpressionParser;

public class ElsifBlock extends Block{

	private IfBlock owner;
	public String condition;
	private boolean toEvaluate;
	public ElsifBlock(Block block, int end, int start) {
		super(Type.ELSIF, block, end, start);
		
		owner = (IfBlock) block;
		SeenVariableOperations.evaluateEvalType();
	}

	public void setCondition(String con) {
		condition = con;
	}
	
	public String getCondition() {
		return condition;
	}
	public IfBlock getIfBlockOwner() {
		return owner;
	}
	public boolean getToEvaluate() {
		return toEvaluate;
	}
	
	public void setToEvaluate(boolean bool) {
		toEvaluate = bool;
	}
	public void setIfOwner(IfBlock fblock) {
		owner = fblock;
	}
	@Override
	public void whatToDo() {
		if(toEvaluate) {
			
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
										if(v.getType() == Type.INTEGER) v.setValue( (int) EvaluateType.eval());
										else v.setValue(EvaluateType.eval());
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
							String[] to_be_printed = line.split("\\+");
							
							for(int k = 0; k < to_be_printed.length; k ++) {
								Variable v = SeenVariableOperations.findInSeenVariables(to_be_printed[k].trim());
								if(v!=null) {
									MainBlock.output_value += v.getValue().toString().replace("\"", "");
								}
								else {
									MainBlock.output_value +=  to_be_printed[k].replace("\"", "");
								}
								
								if(k == to_be_printed.length - 1)
									MainBlock.output_value +=   "\n";
							}
						}
					}
				}
				else {
					for(int j = 0; j < sub_blocks.size(); j++) {
						Block block = sub_blocks.get(j);
						
						if(block.getStartline() == i) {
							block.whatToDo();
							
							owner.setVariables(block.variables);
							break;
						}
					}
				}
			}
		}
	}
	public void evaluateCondition() {
		for(int m = 0; m < SeenVariableOperations.getSeenVariables().size(); m++)
			System.out.println("xx: "+ SeenVariableOperations.getSeenVariables().get(m).getValue());
		condition = condition.replaceAll("\\s","");
		String[] operands = condition.split("!=|==|>=|<=|>|<|\\|\\||&&|\\*|/|\\+|-|^");
		
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder inputParser = new StringBuilder(condition);
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
			System.err.println(":::::::::::::::: Condition is not valid");
		}
	}
}

package cube.semantics.blocks;

import java.util.HashMap;

import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.helpers.Assignment;
import cube.semantics.helpers.SeenVariableOperations;
import cube.semantics.math_operations.EvaluateType;

public class MethodBlock extends MainBlock{

	private int functionCall = 0;
	public MethodBlock(HashMap<Integer, String> line_hash, int start_line, int end_line) {
		super(line_hash, Type.METHOD, end_line, start_line);
	}
	
	public void whatToDo() {
		super.defineBlocks(startline, endline);
		super.defineMotherBlocks();
		
		directBlocks();
	//	SeenVariableOperations.checkAssignments(line_hash, startline, endline);
	
		super.defineSubBlocks();
		super.findElsifsEnd();
		
		removeNotSubBlocks();
		for(int i = startline;i < endline; i++) {
			Object b = lines_under_me.get(i);
			
			if(b!=null) {
				System.out.println("methodblock: "+b.toString());
				if(Assignment.checkIfAssignment(b.toString())) {
					System.out.println("line 35 methodblock");
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
									v.setType(Type.BOOLEAN);
									v.setValue(EvaluateType.evaluateLogicalOperation(left));
								}
							}
							else v.setValue(left);
						}
					}
				}
				else {
					if(b.toString().startsWith("print")) {
						System.out.println("line 68 methodblock");
						String line = b.toString().replace("print", "").replace("(", "").replace(")", "").trim();
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
					
					if(block.getType() == 2) {
						MethodBlock mBlock = (MethodBlock) block;
						if(mBlock.getFunctionCall() == i) {
							mBlock.whatToDo();
							break;
						}
					}
					
					if(block.getStartline() == i) {
						block.whatToDo();
						break;
					}
					
				}
			}
		}
		
	}
	
	public void directBlocks() {
		for(int i = 0; i < sub_blocks.size(); i++) {
			Block b = sub_blocks.get(i);
			if(b.getMotherBlock() == null)
				b.setMotherBlock(this);
		}
	}
	
	public void removeNotSubBlocks() {
		for(int i = 0; i < sub_blocks.size(); i++) {
			Block b = sub_blocks.get(i);
			if(b.getMotherBlock() != this) {
				sub_blocks.remove(i);
				i = 0;
			}
		}
	}
	
	public void addToSeenVariables(Variable[] variables) {
		for(Variable vv : variables) {
			SeenVariableOperations.removeVariableIfExists(vv.getName());
			seen_variables.add(vv);
		}
		SeenVariableOperations.setSeenVariables(seen_variables);
	}
	
	public void setFunctionCall(int f) {
		functionCall = f;
	}
	
	public int getFunctionCall() {
		return functionCall;
	}
}
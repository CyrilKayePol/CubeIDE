package cube.semantics.blocks;

import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.helpers.Assignment;
import cube.semantics.helpers.SeenVariableOperations;
import cube.semantics.math_operations.EvaluateType;

public class ElseBlock extends Block{
	
	private IfBlock owner;
	
	public ElseBlock(Block block, int end, int start) {
		super(Type.ELSE, block, end, start);
	}
	
	public IfBlock getIfBlockOwner() {
		return owner;
	}
	public void setIfOwner(IfBlock own) {
		owner = own;
	}
	@Override
	public void whatToDo() {
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

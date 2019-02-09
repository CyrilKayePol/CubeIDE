package cube.semantics.blocks;

import java.util.ArrayList;
import java.util.HashMap;

import cube.exceptions.RunTimeException;
import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.helpers.Assignment;
import cube.semantics.helpers.MethodHelper;
import cube.semantics.helpers.SeenVariableOperations;
import cube.semantics.math_operations.EvaluateType;


public class MainBlock extends Block{
	protected ArrayList<Variable> seen_variables;
	
	protected HashMap<Integer, String> main_block_lines;
	public static HashMap<Integer, String> line_hash;
	
	protected int end_main, start_main;
	public static String output_value;
	
	public MainBlock(HashMap<Integer, String> line_hash, int type, int main_line, int end_main_line) {
		
		super(type, null, main_line, end_main_line);
		MainBlock.line_hash = line_hash;
		
		end_main = end_main_line;
		start_main = main_line;
		
		initMainBlockLines(main_line, end_main);
		
		output_value = "";
	}
	
	public ArrayList<Variable> getSeenVariables(){
		return seen_variables;
	}
	

	protected void initMainBlockLines(int main_line, int end_main_line) {
		main_block_lines = new HashMap<Integer, String>();
		
		for(int i = main_line+1; i < end_main_line; i++) {
			main_block_lines.put(new Integer(i), line_hash.get(new Integer(i)));
		}
	}
	
	
	protected void initSeenVariables(int start_index, int end_line) {
		seen_variables = new ArrayList<Variable>();
		SeenVariableOperations.setSeenVariables(seen_variables);
		
		for(int i = start_index; i < end_line; i++) {
			String value_at_i = line_hash.get(new Integer(i));
			if(value_at_i.startsWith("var")) {
				value_at_i = value_at_i.replaceAll("var ","");
				
				String[] line_var = value_at_i.split(",");
				
				for(int j = 0; j < line_var.length; j++) {
					String var = line_var[j];
					Variable new_var = null;
					if(var.indexOf('=') > 0) {
						new_var = new Variable((var.substring(0, var.indexOf("="))).trim(),
								var.substring(var.indexOf("=") +1, var.length()));
					}
					else {
						new_var = new Variable(var.trim(), null);
					}
					SeenVariableOperations.removeVariableIfExists(new_var.getName());
					seen_variables.add(new_var);
				}
			}
		}
	}
	
	
	protected void defineBlocks(int start, int end) {
		MethodHelper.setLineHash(line_hash);
		ArrayList<Block> unmatched = new ArrayList<Block>();
		Block above = this;
		for(int i = start + 1; i < end; i++) {
			
			if (line_hash.get(i).startsWith("if")) {
				IfBlock if_block = new IfBlock(null, -1, i);
				
				String if_line = line_hash.get(i);
				String con = if_line.substring(if_line.indexOf("(") + 1, if_line.indexOf(")"));
				
				if_block.setCondition(con);
				sub_blocks.add(if_block);
				unmatched.add(if_block);
				if_block.setVariables(seen_variables);
				above = if_block;
			}
			else if (line_hash.get(i).startsWith("elsif")) {
				ElsifBlock elsif_block = new ElsifBlock(null, -1, i);
				
				String elsif_line = line_hash.get(i);
				String con = elsif_line.substring(elsif_line.indexOf("(") + 1, elsif_line.indexOf(")"));
				
				elsif_block.setCondition(con);
				above = elsif_block;
				for(int j = unmatched.size() -1; j >= 0; j--) {
					if(unmatched.get(j).getType() == Type.IF) {
						((IfBlock)unmatched.get(j)).getElsifs().add(elsif_block);
						elsif_block.setMotherBlock((IfBlock)unmatched.get(j));
						elsif_block.setIfOwner((IfBlock)unmatched.get(j));
						((IfBlock) unmatched.get(j)).getElsifs().add(elsif_block);
						break;
					}
				}
				
				elsif_block.setVariables(seen_variables);
			}
			else if(line_hash.get(i).startsWith("else")) {
				ElseBlock else_block = new ElseBlock(null, -1, i);
				
				above = else_block;
				for(int j = unmatched.size() -1; j >= 0; j--) {
					if(unmatched.get(j).getType() == Type.IF) {
						((IfBlock)unmatched.get(j)).setElse(else_block);
						else_block.setMotherBlock((IfBlock)unmatched.get(j));
						else_block.setIfOwner((IfBlock)unmatched.get(j));
						
						if(((IfBlock)unmatched.get(j)).getElse() == null) {
							((IfBlock)unmatched.get(j)).setElse(else_block);
						}
						break;
					}
				}
				else_block.setVariables(seen_variables);
			}
			else if(line_hash.get(i).startsWith("while")) {
				WhileBlock while_block = new WhileBlock( null, -1, i);
				String while_line = line_hash.get(i);
				String con = while_line.substring(while_line.indexOf("(") + 1, while_line.indexOf(")"));
				
				while_block.setCondition(con);
				sub_blocks.add(while_block);
				unmatched.add(while_block);
				while_block.setVariables(seen_variables);
				
				above = while_block;
			}
			else if(MethodHelper.functionCallMatcher(line_hash.get(i))){
				int start_method = MethodHelper.getFunctionStartLine();
				
				if(MethodHelper.checkValidNumOfArguments()) {
					MethodBlock method_block = new MethodBlock(line_hash, start_method, MethodHelper.getFunctionEndLine(start_method));
					method_block.setFunctionCall(i);
					method_block.seen_variables = seen_variables;
					
					String[] args = MethodHelper.getArguments();
					String[] params = MethodHelper.getParams();
				
					if(args.length > 0)
						method_block.addToSeenVariables(SeenVariableOperations.getArgsInSeenVariables(args, params));
					
					seen_variables = SeenVariableOperations.getSeenVariables();
					
					if(unmatched.size() > 0) {
						method_block.setMotherBlock(unmatched.get(unmatched.size() -1));
						unmatched.get(unmatched.size() -1).getSubBlocks().add(method_block);
					}
					else {
						sub_blocks.add(method_block);
					}
				}
				else {
					RunTimeException.showException("Function is undefined. Number of arguments does not match. ");
				}
			}
			else if(line_hash.get(i).startsWith("#")) {
				line_hash.replace(i, "");
			}
			else {
				/**
				 * TO-DO
				 */
				if(line_hash.get(i).startsWith("var")) {
					Variable new_var = null;
					String var = line_hash.get(i).replaceAll("var ","");
					if(var.indexOf('=') > 0) {
						new_var = new Variable((var.substring(0, var.indexOf("="))).trim(),
								var.substring(var.indexOf("=") +1, var.length()));
						
					}
					else {
						new_var = new Variable(var.trim(), null);
					}
					SeenVariableOperations.removeVariableIfExists(new_var.getName());
					seen_variables.add(new_var);
				}
				else {
					if(!line_hash.get(i).startsWith("end")) {
						above.getLinesUnderMe().put(i, line_hash.get(i));
					}
				}
			}
			int size = unmatched.size();
			if(line_hash.get(i).startsWith("end")) {
				if(unmatched.size()> 0) {
					Block b = unmatched.remove(size -1);
					
					b.setEndline(i);
					
					if(unmatched.size() == 0)
						above = this;
					else if(unmatched.size() > 0)
						above = unmatched.get(unmatched.size()-1);
				}
			}
		}
	}

	protected void defineMotherBlocks() {
		for(int i = 0; i < sub_blocks.size(); i++) {
			Block outer = sub_blocks.get(i);
			
			for(int j = 0; j < sub_blocks.size(); j++) {
				Block inner = sub_blocks.get(j);
				
				if(inner != outer && inner.getEndline() < outer.getEndline() &&
						inner.getStartline() > outer.getStartline()) {
					inner.setMotherBlock(outer);
				}
				
					
			}
		}
	}
	protected void defineSubBlocks() {
		for(int i = 0; i < sub_blocks.size(); i++) {
			Block b = sub_blocks.get(i);
			
			for(int j = 0; j < sub_blocks.size(); j++) {
				Block mother = sub_blocks.get(j).getMotherBlock();
				
				if(mother != null) {
					if(mother.equals(b)) {
						b.getSubBlocks().add(sub_blocks.get(j));
					}
				}
			}
		}
	}
	
	public void findElsifsEnd() {
		for(int i = 0; i < sub_blocks.size(); i++) {
			Block b = sub_blocks.get(i);
			
			if(b.getType() == Type.IF) {
				IfBlock fblock = ((IfBlock) b);
				
				ElseBlock elseB = fblock.getElse();
				ArrayList<ElsifBlock> blocks = fblock.getElsifs();
				
				if(blocks.size() > 0) {
					for(int j = 0; j < blocks.size(); j++) {
						if(j + 1 <= blocks.size() - 1) {
							blocks.get(j).setEndline(blocks.get(j+1).getStartline());
						}
						else {
							if(elseB != null) {
								blocks.get(j).setEndline(elseB.getStartline());
								elseB.setEndline(elseB.getIfBlockOwner().getEndline());
							}
							else {
								blocks.get(j).setEndline(blocks.get(j).getIfBlockOwner().getEndline());
							}
						}
					}
					fblock.setEndline(blocks.get(0).getStartline());
				}
				else if(elseB != null) {
					elseB.setEndline(elseB.getIfBlockOwner().getEndline());
					elseB.getIfBlockOwner().setEndline(elseB.getStartline());
				}
			}
		}
	}

	protected void removeNotMainSubBlock() {
		for(int i = 0; i < sub_blocks.size(); i++) {
			if(sub_blocks.get(i).getMotherBlock() != null) {
				sub_blocks.remove(i);
				i =0;
			}
		}
	}
	
	@Override
	public void whatToDo() {
		for(int i = start_main;i < end_main;i++) {
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
	
	public void initAll() {
		initSeenVariables(1, end_main);
		
		defineBlocks(start_main, end_main);
		defineMotherBlocks();
		SeenVariableOperations.setSeenVariables(seen_variables);
		//SeenVariableOperations.checkAssignments(line_hash, start_main, end_main);
		SeenVariableOperations.evaluateEvalType();
		defineSubBlocks();
		findElsifsEnd();
		removeNotMainSubBlock();
	}
}

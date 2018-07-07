package cube.semantics.blocks;

import java.util.ArrayList;
import java.util.HashMap;

import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.helpers.MethodHelper;
import cube.semantics.helpers.Print;
import cube.semantics.helpers.SeenVariableOperations;

public class MainBlock extends Block{
	protected ArrayList<Variable> seen_variables;
	
	protected HashMap<Integer, String> main_block_lines;
	protected HashMap<Integer, String> line_hash;
	
	protected int end_main, start_main;
	public static String output_value;
	
	public MainBlock(HashMap<Integer, String> line_hash, int type, int main_line, int end_main_line) {
		
		super(type, null, main_line, end_main_line);
		this.line_hash = line_hash;
		
		end_main = end_main_line;
		start_main = main_line;
		
		initMainBlockLines(main_line, end_main);
		initSeenVariables(1, end_main_line);
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
	
	
	protected void defineBlocks() {
		MethodHelper.setLineHash(line_hash);
		ArrayList<Block> unmatched = new ArrayList<Block>();
		for(int i = start_main + 1; i < end_main; i++) {
			if (line_hash.get(i).startsWith("if")) {
				IfBlock if_block = new IfBlock(null, -1, i);
				
				String if_line = line_hash.get(i);
				String con = if_line.substring(if_line.indexOf("(") + 1, if_line.indexOf(")"));
				
				if_block.setCondition(con);
				sub_blocks.add(if_block);
				unmatched.add(if_block);
				if_block.setVariables(seen_variables);
			}
			else if (line_hash.get(i).startsWith("elsif")) {
				ElsifBlock elsif_block = new ElsifBlock(null, -1, i);
				
				String elsif_line = line_hash.get(i);
				String con = elsif_line.substring(elsif_line.indexOf("(") + 1, elsif_line.indexOf(")"));
				
				elsif_block.setCondition(con);
				for(int j = unmatched.size() -1; j >= 0; j--) {
					if(unmatched.get(j).getType() == Type.IF) {
						((IfBlock)unmatched.get(j)).getElsifs().add(elsif_block);
						elsif_block.setMotherBlock((IfBlock)unmatched.get(j));
						elsif_block.setIfOwner((IfBlock)unmatched.get(j));
						break;
					}
				}
				
				elsif_block.setVariables(seen_variables);
			}
			else if(line_hash.get(i).startsWith("else")) {
				ElseBlock else_block = new ElseBlock(null, -1, i);
				
				for(int j = unmatched.size() -1; j >= 0; j--) {
					if(unmatched.get(j).getType() == Type.IF) {
						((IfBlock)unmatched.get(j)).setElse(else_block);
						else_block.setMotherBlock((IfBlock)unmatched.get(j));
						else_block.setIfOwner((IfBlock)unmatched.get(j));
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
			}
			else if(MethodHelper.functionCallMatcher(line_hash.get(i))){
				int start = MethodHelper.getFunctionStartLine();
				MethodBlock method_block = new MethodBlock(line_hash, start, MethodHelper.getFunctionEndLine(start));
				
				if(unmatched.size() > 0) {
					method_block.setMotherBlock(unmatched.get(unmatched.size() -1));
					unmatched.get(unmatched.size() -1).getSubBlocks().add(method_block);
				}
				else {
					sub_blocks.add(method_block);
				}
			}
			
			int size = unmatched.size();
			if(line_hash.get(i).startsWith("end")) {
				if(unmatched.size()> 0) {
					Block b = unmatched.remove(size -1);
					
					b.setEndline(i);
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
		System.out.println(" ==== Sub-blocks ====");
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
			Print.printBlockInformation(b);
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
		defineBlocks();
		defineMotherBlocks();
		SeenVariableOperations.checkAssignments(line_hash, start_main, end_main);
		
		Print.printMainBlock(sub_blocks);
		defineSubBlocks();
		findElsifsEnd();
		Print.printElsifs(sub_blocks);
		removeNotMainSubBlock();
		Print.printMainBlock(sub_blocks);
	}
}

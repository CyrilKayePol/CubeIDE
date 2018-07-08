package cube.semantics.blocks;

import java.util.HashMap;

import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.helpers.SeenVariableOperations;

public class MethodBlock extends MainBlock{

	public MethodBlock(HashMap<Integer, String> line_hash, int start_line, int end_line) {
		
		super(line_hash, Type.METHOD, end_line, start_line);
	}
	
	public void whatToDo() {
		super.defineBlocks(startline, endline);
		super.defineMotherBlocks();
		
		directBlocks();
		SeenVariableOperations.checkAssignments(line_hash, start_main, end_main);
	
		super.defineSubBlocks();
		super.findElsifsEnd();
		
		removeNotSubBlocks();
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
}

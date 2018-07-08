package cube.semantics.blocks;

import java.util.ArrayList;

import cube.semantics.Variable;

public abstract class Block {
	protected ArrayList<Block> sub_blocks;
	
	protected ArrayList<Variable> variables;
	protected int type;
	protected Block mother_block;
	protected int endline, startline;
	
	public Block(int type, Block block, int end, int start) {
		this.type = type;
		mother_block = block;
		endline = end;
		startline = start;
		sub_blocks = new ArrayList<Block>();
	}
	
	public int getType() {
		return type;
	}
	
	public int getEndline() {
		return endline;
	}
	
	public void setEndline(int end) {
		endline = end;
	}
	
	public int getStartline() {
		return startline;
	}
	
	public void setStartine(int start) {
		startline = start;
	}
	
	public ArrayList<Variable> getVariables(){
		return variables;
	}
	
	public void setVariables(ArrayList<Variable> vars) {
		variables = vars;
	}

	public void setMotherBlock(Block b) {
		mother_block = b;
	}
	
	public Block getMotherBlock() {
		return mother_block;
	}
	
	public ArrayList<Block> getSubBlocks(){
		return sub_blocks;
	}
	public abstract void whatToDo();
}

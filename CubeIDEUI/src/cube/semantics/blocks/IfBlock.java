package cube.semantics.blocks;

import java.util.ArrayList;

import cube.semantics.Type;

public class IfBlock extends Block{

	private String conditions;
	private ArrayList<ElsifBlock> elsifs;
	private ElseBlock else_block;
	public IfBlock(Block block, int end, int start) {
		super(Type.IF, block, end,start);
		elsifs = new ArrayList<ElsifBlock>();
	}
	
	public void setCondition(String con) {
		conditions = con;
	}
	
	public ArrayList<ElsifBlock> getElsifs() {
		return elsifs;
	}
	
	public ElseBlock getElse() {
		return else_block;
	}
	
	public void setElse(ElseBlock e) {
		else_block = e;
	}
	
	public String getConditions() {
		return conditions;
	}
	@Override
	public void whatToDo() {
		
		
	}

}

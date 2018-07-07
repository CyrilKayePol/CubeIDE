package cube.semantics.blocks;

import cube.semantics.Type;

public class WhileBlock extends Block{
	private String conditions;
	public WhileBlock(Block block, int end, int start) {
		super(Type.WHILE, block, end, start);
	}
	
	public void setCondition(String con) {
		conditions = con;
	}
	
	public String getConditions() {
		return conditions;
	}
	@Override
	public void whatToDo() {
		
	}
}

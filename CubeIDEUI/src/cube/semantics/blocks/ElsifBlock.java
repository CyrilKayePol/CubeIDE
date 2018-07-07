package cube.semantics.blocks;

import cube.semantics.Type;

public class ElsifBlock extends Block{

	private IfBlock owner;
	public String condition;
	public ElsifBlock(Block block, int end, int start) {
		super(Type.ELSIF, block, end, start);
		
		owner = (IfBlock) block;
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
	
	public void setIfOwner(IfBlock fblock) {
		owner = fblock;
	}
	@Override
	public void whatToDo() {
		
	}

}

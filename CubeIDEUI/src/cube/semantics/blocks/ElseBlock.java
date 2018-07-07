package cube.semantics.blocks;

import cube.semantics.Type;

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
		
	}

}

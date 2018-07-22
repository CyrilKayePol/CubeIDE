package cube.semantics.blocks;

import java.util.ArrayList;

import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.helpers.SeenVariableOperations;

public class IfBlock extends Block{

	private String conditions;
	private ArrayList<ElsifBlock> elsifs;
	private ElseBlock else_block;
	private boolean toEvaluate;
	
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
	
	public boolean getToEvaluate() {
		return toEvaluate;
	}
	
	public void setToEvaluate(boolean bool) {
		toEvaluate = bool;
	}
	@Override
	public void whatToDo() {
		if(toEvaluate) {
			for(int i = 0; i < lines_under_me.size(); i++) {
				if(lines_under_me.get(i).startsWith("print")) {
					String line = lines_under_me.get(i).replace("print", "").trim();
					Variable v = SeenVariableOperations.findInSeenVariables(line);
					if(v!=null) {
						MainBlock.output_value += v.getValue();
					}
				}
			}
		}
	}
}

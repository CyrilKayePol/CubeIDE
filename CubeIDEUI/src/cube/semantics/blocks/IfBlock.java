package cube.semantics.blocks;

import java.util.ArrayList;
import java.util.Arrays;

import cube.semantics.Type;
import cube.semantics.Variable;
import cube.semantics.helpers.SeenVariableOperations;
import cube.semantics.math_operations.ExpressionParser;

public class IfBlock extends Block{

	private String conditions;
	private ArrayList<ElsifBlock> elsifs;
	private ElseBlock else_block;
	private boolean toEvaluate;
	
	public IfBlock(Block block, int end, int start) {
		super(Type.IF, block, end,start);
		elsifs = new ArrayList<ElsifBlock>();
		SeenVariableOperations.evaluateEvalType();
	}
	
	public void setCondition(String con) {
		conditions = con;
		evaluateCondition();
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
			
		}
		else {
			
		}
	}
	public void evaluateCondition() {
		conditions = conditions.replaceAll("\\s","");
		String[] operands = conditions.split("!=|==|>=|<=|>|<|\\|\\||&&|\\*|/|\\+|-|^");
		
		ArrayList<Object> params = new ArrayList<Object>();
		StringBuilder inputParser = new StringBuilder(conditions);
		for(String str:operands) {
			Variable v = SeenVariableOperations.findInSeenVariables(str.trim());
			if(v != null) {
				int beginIndex = inputParser.indexOf(str.trim());
				inputParser.insert(beginIndex, '[');
				inputParser.insert(beginIndex+str.length()+1, ']');
				params.add(str);
				if(v.getValue() == null) params.add("");
				else params.add(v.getValue().toString().trim());
			}
		}
		try {
			System.out.println("Input Parser: "+ inputParser + " params: " + Arrays.toString(params.toArray()));
			toEvaluate = (ExpressionParser.evaluate(inputParser.toString(),params.toArray()));
			System.out.println("To Evaluate: "+toEvaluate);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("Condition is not valid");
		}
	}
}

package cube.semantics;

import java.util.ArrayList;
import java.util.HashMap;

import cube.exceptions.RunTimeException;
import cube.semantics.blocks.MainBlock;
import cube.semantics.helpers.Print;

public class SemanticsChecker{
	
	private HashMap<Integer, String> line_hash;
	private int main_line_num, end_main_line_num;
	
	private MainBlock main_block;
	
	public SemanticsChecker() {
		line_hash = new HashMap<Integer, String>();
		
		Print.readFile(line_hash);
		findMain();
		
		main_block = new MainBlock(line_hash, Type.MAIN, main_line_num, end_main_line_num);
		
		main_block.initAll();
		//Print.printMainBlockVariables(main_block);
		
		main_block.whatToDo();
		System.out.println("***************************************\n");
		System.out.println("OUTPUT VALUE\n"+ MainBlock.output_value);
		System.out.println("***************************************");
	}
	
	public SemanticsChecker(String code) {
		line_hash = new HashMap<Integer, String>();
		
		Print.readCode(line_hash, code);
		findMain();
		
		main_block = new MainBlock(line_hash, Type.MAIN, main_line_num, end_main_line_num);
		
		main_block.initAll();
		//Print.printMainBlockVariables(main_block);
		
		main_block.whatToDo();
		System.out.println("***************************************\n");
		System.out.println("OUTPUT VALUE\n"+ MainBlock.output_value);
		System.out.println("***************************************");
	}
	
	private void findMain() {
		Object[] line_hash_values = line_hash.values().toArray();
		
		int main_count = 0;
		for(int i = 0; i < line_hash_values.length; i++) {
			String value_at_i = line_hash_values[i].toString();
		
			if(value_at_i.startsWith("fn main")) {
				++main_count;
			}
		}
		
		if(main_count == 0) {
			RunTimeException.showException("Function main not found.");
		}
		else if(main_count == 1) {
			for(int i  = 0; i <= line_hash.size(); i++) {
				String value_at_key = line_hash.get(new Integer(i+1));
				
				if(value_at_key.startsWith("fn main")) {
					main_line_num = i+1;
					findEndOfMain();
					break;
				}
			}
		}
		else {
			RunTimeException.showException("There are too many main functions.");
		}
	}
	
	private void findEndOfMain() {
		int block_count = 0;
		int end_count = 0;
		
		ArrayList<Integer> ends = new ArrayList<Integer>();
		for (int i = main_line_num + 1; i <= line_hash.size(); i++) {
			String value_at_i = line_hash.get(new Integer(i));
			
			if(value_at_i.startsWith("fn"))
				break;
			
			if(value_at_i.startsWith("if")  || value_at_i.startsWith("while"))
				++block_count;
			else if (value_at_i.startsWith("end")) {
				++end_count;
				ends.add(new Integer(i));
			}
		}
		
		try {
			if(block_count ==  end_count -1)
				end_main_line_num = ends.get(end_count-1);
			else {}
		}
		catch(Exception e) {
			RunTimeException.showException(e.getMessage());

		}
	}
	
	public static void main(String[] args) {
		new SemanticsChecker();
	}	
}
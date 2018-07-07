package cube.semantics.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cube.exceptions.RunTimeException;
import cube.semantics.blocks.Block;
import cube.semantics.blocks.IfBlock;

public class MethodHelper {
	
	/**
	 * 
	 * @param toMatch
	 * @return boolean value specifying whether the String toMatch matches the regex expression
	 */
	
	private static String functionName;
	private static HashMap<Integer, String> line_hash;
	
	public static void setLineHash(HashMap<Integer, String> hash) {
		line_hash = hash;
	}
	
	public static boolean functionCallMatcher(String toMatch) {
		toMatch = toMatch.replace(" ", "");
		String regex="([a-zA-Z0-9]+)\\((([a-zA-Z0-9]+)((,)([a-zA-Z0-9])+)*?)*?\\)";
		
		Pattern funcPattern = Pattern.compile(regex);
		Matcher m = funcPattern.matcher(toMatch);
		 
		if (m.matches()){
		     for (int index= 0 ; index <= m.groupCount(); index++){
		          if(index == 1)
		        	  functionName = m.group(index);
		     }
		     return true;
		}
		return false;
	}
	
	public static String getFunctionName() {
		return functionName;
	}
	
	public static int getFunctionStartLine() {
		for(int i = 1; i < line_hash.size(); i++) {
			if(line_hash.get(i).startsWith("fn")) {
				String line = line_hash.get(i);
				
				line = line.replace("fn", "").trim();
				
				if(line.startsWith(functionName)) {
					return i;
				}
				
				if(i == line_hash.size()) {
					RunTimeException.showException("Function is undefined. ");
					return -1;
				}
			}
		}
		return -1;
	}
	
	public static int getFunctionEndLine(int start) {
		ArrayList<Block> unmatched = new ArrayList<Block>();
		for(int i = start; i < line_hash.size(); i++) {
			String line = line_hash.get(i);
			
			if(line != null) {
				if(line.startsWith("if") || line.startsWith("while")) {
					unmatched.add(new IfBlock(null, 0, 0));
				}
				
				if(line.startsWith("end")) {
					if(unmatched.size() > 0) {
						unmatched.remove(0);
					}
					else {
						return i;
					}
				}
			}
			
		}
		return -1;
	}
}

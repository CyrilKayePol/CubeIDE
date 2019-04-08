package cube.semantics.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cube.exceptions.RunTimeException;
import cube.semantics.blocks.Block;
import cube.semantics.blocks.IfBlock;

public class MethodHelper {
	
	private static String functionName, functionCall;
	private static HashMap<Integer, String> line_hash;
	private static String[] params, arguments;
	
	public static void setLineHash(HashMap<Integer, String> hash) {
		line_hash = hash;
	}
	
	public static boolean functionCallMatcher(String toMatch) {
		
		String regex="([a-zA-Z0-9]+)\\(((([a-zA-Z0-9]+)||(\"[a-zA-Z0-9!#&\\s]+\"))((,)(([a-zA-Z0-9!#&\\s]+)||(\\\"[a-zA-Z0-9!#&\\s]+\\\")))*?)*?\\)";
	
		functionCall = toMatch;
		Pattern funcPattern = Pattern.compile(regex);
		Matcher m = funcPattern.matcher(toMatch);
		 
		if (m.matches()){
		     for (int index= 0 ; index <= m.groupCount(); index++){
		          if(index == 1)
		        	  functionName = m.group(index);
		     }
		     if(functionName.equals("print"))
		    	 return false;
		     
		     System.out.println("\t[INFO] @MethodHelper a function call matched" );
		     return true;
		}
		
		return false;
	}
	
	public static String getFunctionName() {
		return functionName;
	}
	
	public static int getFunctionStartLine() {
		
		String args = functionCall.replace(functionName + "(", "");
		args = args.replace(")", "");
		
		params = args.split(",");
		
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
		for(int i = start; i <= line_hash.size(); i++) {
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
	
	public static boolean checkValidNumOfArguments() {
		String method_dec = line_hash.get(getFunctionStartLine());
		method_dec = method_dec.replaceAll("fn", "").replace(" ", "");
		method_dec = method_dec.replaceAll(functionName + "\\(", "");
		method_dec = method_dec.replaceAll("\\)", "").trim();
		
		String[] args = method_dec.split(",");
		
		if(args != null && params !=null && args.length == params.length) {
			arguments = args;
			
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String[] getParams() {
		return params;
	}
	
	public static String[] getArguments() {
		for(int i = 0; i < arguments.length; i++) {
			if(arguments[i].startsWith("var")) {
				arguments[i] = arguments[i].replaceAll("var", "").trim();
			}
		}
		
		return arguments;
	}
}

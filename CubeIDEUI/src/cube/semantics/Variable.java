package cube.semantics;

public class Variable {
	
	private boolean isInitialized = false;
	private Object value;
	private String variable_type;
	private String variable_name;
	
	public Variable(String name, Object value) {
		variable_name = name;
		this.value = value;
		if(value != null) {
			isInitialized = true;
			
			String val = value.toString().trim();
			identifyTypes(val);
		}
	}
	
	public String getType() {
		return variable_type;
	}
	public void setType(String t) {
		variable_type =t;
	}
	public boolean getIsInitialized() {
		return isInitialized;
	}
	
	public void setIsInitialized() {
		isInitialized = true;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object v) {
		value =v;
	}
	
	public String getName() {
		return variable_name;
	}
	
	public static boolean isInt(String val) {
		try {
			Integer.parseInt(val);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isFloat(String val) {
		try {
			Float.parseFloat(val);
			return true;
		}
		catch(NumberFormatException e) {
			return false;
		}
	}
	
	public void identifyTypes(String val) {
		if(val.equals("true") || value.toString().equals("false")) {
			variable_type = Type.BOOLEAN;
		}
		else if(val.startsWith("\"") && val.endsWith("\"")){
			variable_type =  Type.STRING;
		}	
		else {
			if(isInt(val)) {
				variable_type = Type.INTEGER;
			}
			else {
				if(isFloat(val))
					variable_type = Type.FLOAT;
				else
					variable_type =  Type.EVAL;
			}
		}
	}
}

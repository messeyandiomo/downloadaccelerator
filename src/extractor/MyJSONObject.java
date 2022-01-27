package extractor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MyJSONObject extends LinkedHashMap<String, MyType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5022135480259542271L;

	public MyJSONObject() {
		// TODO Auto-generated constructor stub
		super();
	}

	public MyJSONObject(int initialCapacity) {
		// TODO Auto-generated constructor stub
		super(initialCapacity);
	}
	
	
	private static void parseJsonString(MyJSONObject jsonToReturn, String exprtoparse) {
		
		String key = null;
		MyType value = null;
		String[] splitjsonstring = exprtoparse.split("");
		int i = 0;
		int keyfirstindex = i;
		
		while(!splitjsonstring[i].equals(":"))
			i++;
		key = exprtoparse.substring(keyfirstindex, i).strip();
		
		String subexprtoparseforvalue = exprtoparse.substring(i + 1).stripLeading();
		int lensubexprtoparseforvalue = subexprtoparseforvalue.length();
		String[] splitsubexprtoparseforvalue = subexprtoparseforvalue.split("");
		int valuefirstindex = 0;
		if(subexprtoparseforvalue.startsWith("{")) {
			int countaccol = 1;
			i = valuefirstindex + 1;
			while(countaccol > 0) {
				if(splitsubexprtoparseforvalue[i].equals("{"))
					countaccol++;
				else if(splitsubexprtoparseforvalue[i].equals("}"))
					countaccol--;
				i++;
			}
			try {
				value = new MyType(loads(subexprtoparseforvalue.substring(valuefirstindex, i)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonToReturn.put(key, value);
		}
		else {
			i = valuefirstindex;
			while((!splitsubexprtoparseforvalue[i].equals(",")) && (i < lensubexprtoparseforvalue))
				i++;
			value = new MyType(subexprtoparseforvalue.substring(valuefirstindex, i));
			jsonToReturn.put(key, value);
		}
		if(i < lensubexprtoparseforvalue)
			if(splitsubexprtoparseforvalue[i].equals(","))
				parseJsonString(jsonToReturn, subexprtoparseforvalue.substring(i+1).stripLeading());
	}
	
	
	public static MyJSONObject loads(String jsonstring) throws Exception {
		
		MyJSONObject toReturn = new MyJSONObject();
		
		if(jsonstring.startsWith("{")) {
			String exprtoparse = jsonstring.substring(1, jsonstring.length() - 1);
			parseJsonString(toReturn, exprtoparse.strip());
		}
		else
			throw new Exception("This is not a json string");
		
		return toReturn;
	}
	
	
	public static String dumps(MyType vartype) {
		
		String toReturn = "{";
		
		if(vartype.getStr() != null)
			toReturn = vartype.getStr();
		else if(vartype.getJson() != null) {
			MyJSONObject varjson = vartype.getJson();
			ArrayList<String> keysjson = new ArrayList<>(varjson.keySet());
			for (String key : keysjson) {
				toReturn += (key + ":");
				toReturn += dumps(varjson.get(key)) + ",";
			}
			toReturn = toReturn.substring(0, toReturn.length() - 1);
		}
		toReturn += "}";
		
		return toReturn;
	}

}

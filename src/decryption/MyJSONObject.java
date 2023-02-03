package decryption;

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
	
	
	public static Tuple getKeyFromExpr(String expr) {
		
		String key = null;
		String[] splitexpr = expr.split("");
		int i = 0;
		
		while(!splitexpr[i].equals(":"))
			i++;
		key = Common.removeQuotes(expr.substring(0, i));
		
		return new Tuple(key, i + 1);
	}
	
	
	public static Tuple getValueFromExpr(String expr) {
		
		MyType value = null;
		Integer nextindex = null;
		
		int valuefirstindex = 0;
		int valuelastindex = valuefirstindex;
		String[]splitexpr = expr.split("");
		if(expr.startsWith("{")) {
			int accoladescount = 1;
			while(accoladescount > 0) {
				if(splitexpr[valuelastindex].equals("{"))
					accoladescount++;
				else if(splitexpr[valuelastindex].equals("}"))
					accoladescount--;
				valuelastindex++;
			}
			value = new MyType(parse(expr.substring(valuefirstindex, valuelastindex)));
			nextindex = valuelastindex + 1;
		}
		else if(expr.startsWith("[")) {
			int bracketscount = 1;
			while(bracketscount > 0) {
				if(splitexpr[valuelastindex].equals("["))
					bracketscount++;
				else if(splitexpr[valuelastindex].equals("]"))
					bracketscount--;
				valuelastindex++;
			}
			int firstindex = 0;
			int lastindex = 0;
			if(splitexpr[valuefirstindex + 1].equals("{")) {
				ArrayList<MyType> jsonlistforvalue = new ArrayList<MyType>();//list that contains MyJSONObject object
				for (int j = valuefirstindex + 1; j < valuelastindex - 1; j++) {
					if (splitexpr[j].equals("{")) {
						firstindex = j;
					}
					else if(splitexpr[j].equals("}")) {
						lastindex = j;
						jsonlistforvalue.add(new MyType(new MyType(parse(Common.removeQuotes(expr.substring(firstindex, lastindex + 1))))));
					}
				}
				value = new MyType(jsonlistforvalue);
				nextindex = valuelastindex + 1;
			}
			else {
				ArrayList<MyType> stringlistforvalue = new ArrayList<MyType>();
				firstindex = valuefirstindex + 1;
				for (int j = valuefirstindex + 1; j < valuelastindex - 1; j++) {
					if(splitexpr[j].equals(",")) {
						lastindex = j;
						stringlistforvalue.add(new MyType(expr.substring(firstindex, lastindex)));
						firstindex = lastindex + 1;
					}
				}
				value = new MyType(stringlistforvalue);
				nextindex = valuelastindex + 1;
			}
		}
		else {
			boolean exprcontinue = false;
			for (int k = valuefirstindex; k < splitexpr.length; k++) {
				if (splitexpr[k].equals(",")) {
					valuelastindex = k;
					exprcontinue = true;
					break;
				}
			}
			if(!exprcontinue)
				value = new MyType(Common.removeQuotes(expr.substring(valuefirstindex)));
			else {
				value = new MyType(Common.removeQuotes(expr.substring(valuefirstindex, valuelastindex)));
				nextindex = valuelastindex + 1;
			}	
		}
		
		return new Tuple(value, nextindex);
	}
	
	public static MyJSONObject parse(String jsonstring) {
		
		MyJSONObject result = null;
		
		if(jsonstring.startsWith("{")) {
			
			String exprtoparse = jsonstring.substring(1, jsonstring.length() - 1);
			Tuple keytuple = getKeyFromExpr(exprtoparse);
			exprtoparse = exprtoparse.substring(keytuple.getNextIndex());
			Tuple valuetuple = getValueFromExpr(exprtoparse);
			MyType value = valuetuple.getMyType();
			Integer nextindex = valuetuple.getNextIndex();
			
			result = new MyJSONObject();
			result.put(keytuple.getStr(), value);
			
			while(nextindex != null) {
				exprtoparse = exprtoparse.substring(nextindex);
				keytuple = getKeyFromExpr(exprtoparse);
				exprtoparse = exprtoparse.substring(keytuple.getNextIndex());
				valuetuple = getValueFromExpr(exprtoparse);
				value = valuetuple.getMyType();
				nextindex = valuetuple.getNextIndex();
				result.put(keytuple.getStr(), value);
			}
		}
		
		return result;
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

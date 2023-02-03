package decryption;

import java.util.LinkedHashMap;
import java.util.function.Function;

public class OperatorsDictionary {
	
	private static OperatorsDictionary instance = null;
	private LinkedHashMap<String, Function<MyType[], MyType>> operators = new LinkedHashMap<>();
	private LinkedHashMap<String, Function<MyType[], MyType>> assignOperators = new LinkedHashMap<>();
	private String nameRe = "[a-zA-Z_$][a-zA-Z_$0-9]*";

	private OperatorsDictionary() {
		// TODO Auto-generated constructor stub
		this.operators.put("|", Operator.or);
		this.operators.put("^", Operator.xor);
		this.operators.put("&", Operator.and);
		this.operators.put(">>", Operator.rshift);
		this.operators.put("<<", Operator.lshift);
		this.operators.put("-", Operator.sub);
		this.operators.put("+", Operator.add);
		this.operators.put("%", Operator.mod);
		this.operators.put("/", Operator.truediv);
		this.operators.put("*", Operator.mul);
		
		for (String op : this.operators.keySet())
			this.assignOperators.put(op + "=", this.operators.get(op));
		this.assignOperators.put("=", new Function<MyType[], MyType>() {

			@Override
			public MyType apply(MyType[] arg0) {
				// TODO Auto-generated method stub
				
				return arg0[1];
			}
		});
	}
	
	
	public static OperatorsDictionary getInstance() {
		
		if(instance == null)
			instance = new OperatorsDictionary();
		return instance;
	}
	
	public LinkedHashMap<String, Function<MyType[], MyType>> getOperators() {
		return operators;
	}

	public LinkedHashMap<String, Function<MyType[], MyType>> getAssignOperators() {
		return assignOperators;
	}

	public String getNameRe() {
		return nameRe;
	}

}

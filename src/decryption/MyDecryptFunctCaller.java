package decryption;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class MyDecryptFunctCaller implements MyInterface {
	/**
	 * this class is an implementation of MyInterface. it
	 * implements the method resf which decipher the cipher signature
	 */
	
	private static final long serialVersionUID = 6994101560849517281L;
	private String[] argnames = null;
	private String functionCode = null;
	private String playerCode = null;
	private transient OperatorsDictionary operatorDictionary = null;
	private MyDict objects = null;
	private MyObject functions = null;
	
	
	public MyDecryptFunctCaller(String functionCode, String playerCode) {
		// TODO Auto-generated constructor stub
		this.functionCode = functionCode;
		this.playerCode = playerCode;
		
		this.operatorDictionary = OperatorsDictionary.getInstance();
		this.objects = new MyDict();
		this.functions = new MyObject();
	}
	
	
	public MyDecryptFunctCaller(String[] argnames, String functionCode, String playerCode) {
		// TODO Auto-generated constructor stub
		this(functionCode, playerCode);
		if(argnames != null) {
			this.argnames = new String[argnames.length];
			for (int i = 0; i < argnames.length; i++)
				this.argnames[i] = argnames[i];
		}
	}
	
	public OperatorsDictionary resetOperatorsDictionary() {
		this.operatorDictionary = OperatorsDictionary.getInstance();
		return operatorDictionary;
	}
	
	@Override
	public String resf(MyType[] args) {
		// TODO Auto-generated method stub
		PairMyType result = null;
		Hashtable<String, MyType> localVars = null;
		
		if(this.argnames != null) {
			localVars = new Hashtable<String, MyType>(argnames.length);
			for (int i = 0; i < argnames.length; i++)
				localVars.put(argnames[i], args[i]);
		}
		
		if(this.functionCode != null) {
			for (String stmt : functionCode.split(";")) {
				result = interpretStatement(stmt, localVars, playerCode);
				if(result.getAbort())
					break;
			}
		}
		
		return result.getRes().getStr();
	}
	
	
	private PairMyType interpretStatement(String stmt, Hashtable<String,MyType> localVars, String playerCode) {
		
		Boolean shouldAbort = false;
		String expr = null;
		String statement = stmt.stripLeading();
		Boolean stmtIsVar = statement.startsWith("var ");
		
		if(stmtIsVar)
			expr = statement.substring(4).stripLeading();
		else {
			System.out.println("statement : " + statement);
			
			Boolean stmtIsReturn = statement.startsWith("return ");
			
			if(stmtIsReturn) {
				
				System.out.println("test of return statement is done ");
				
				expr = statement.substring(7).stripLeading();
				shouldAbort = true;
			}
			else
				expr = statement;
		}
		MyType v = this.interpretExpression(expr, localVars, playerCode);
		PairMyType result = new PairMyType(v, shouldAbort);
		
		return result;
	}
	
	
	private MyType interpretExpression(String expression, Hashtable<String,MyType> localVars, String playerCode) {
		
		MyType toReturn = null;
		
		System.out.println("expression au debut : " + expression);
		
		String expr = expression.strip();
		if(expr.length() == 0)
			toReturn = null;
		
		/** this part return a string */
		if(expr.startsWith("(")) {
			int parensCount = 0;
			Pattern patternParens = Pattern.compile("[()]");
			Matcher matcherExpr = patternParens.matcher(expr);
			while (matcherExpr.find()) {
				if(matcherExpr.toString() == "(")
					parensCount += 1;
				else {
					parensCount -= 1;
					if(parensCount == 0) {
						String subExpr = expr.substring(1, matcherExpr.start());
						MyType subExprMyType =  this.interpretExpression(subExpr, localVars, playerCode);
						String remainingExpr = expr.substring(matcherExpr.end()).strip();
						if(remainingExpr.length() == 0)
							return subExprMyType;
						else
							expr = MyJSONObject.dumps(subExprMyType) + remainingExpr;
						break;
					}
				}
			}
		}
		
		for (String op : operatorDictionary.getAssignOperators().keySet()) {
			Pattern operationPattern = Pattern.compile(String.format("^(?<out>%s)(?:\\[(?<index>[^\\]]+?)\\])?\\s*%s(?<remain>.*)$", operatorDictionary.getNameRe(), Common.escape(op)));
			Matcher operationMatcher = operationPattern.matcher(expression);
			if(!operationMatcher.find())
				continue;
			String out = operationMatcher.group("out");
			String index = operationMatcher.group("index");
			String remain = operationMatcher.group("remain");
			
			System.out.println("out : " + out);
			System.out.println("op : " + op);
			System.out.println("remain : " + remain);
			
			MyType rightVal = this.interpretExpression(remain, localVars, playerCode);
			
			System.out.println("rightValue : " + rightVal.val().toString());
			
			MyType lvar = localVars.get(out);
			
			if(index != null) {
				
				System.out.println("index : " + index);
				
				MyType idx = this.interpretExpression(index, localVars, playerCode);
				assert ((idx.getNbr() != null) || (Common.isNumeric(idx.getStr())));
				MyType cur = lvar.get(idx);
				MyType[] params = {cur, rightVal};
				MyType val = operatorDictionary.getAssignOperators().get(op).apply(params);
				lvar.set(idx, val);
				toReturn = val;
			}
			else {
				MyType[] params = {lvar, rightVal};
				MyType val = operatorDictionary.getAssignOperators().get(op).apply(params);
				localVars.put(out, val);
				toReturn = val;
			}
			
			if(toReturn != null)
				return toReturn;
		}
		
		
		if(expr.matches("^[0-9]+$"))
			return new MyType(Integer.parseInt(expr));
		
		
		Pattern returnPattern = Pattern.compile(String.format("^(?!if|return|true|false)(?<name>%s)$", operatorDictionary.getNameRe()));
		Matcher returnMatcher = returnPattern.matcher(expr);
		if(returnMatcher.find()) {
			String name = returnMatcher.group("name");
			if(localVars.get(name) != null)
				return localVars.get(name);
		}
		
		try {
			return new MyType(MyJSONObject.loads(expr));
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
		Pattern patternIn = Pattern.compile(String.format("^(?<in>%s)\\[(?<idx>.+)\\]$", operatorDictionary.getNameRe()));
		Matcher matcherIn = patternIn.matcher(expr);
		if(matcherIn.find()) {
			String in = matcherIn.group("in");
			String idx = matcherIn.group("idx");
			MyType lin = localVars.get(in);
			MyType lidx = this.interpretExpression(idx, localVars, playerCode);
			
			System.out.println("in : " + in);
			
			if(lin != null && lidx != null) {
				toReturn = lin.get(lidx);
				if(toReturn != null)
					return toReturn; 
			}
		}
		
		Pattern patternDictMember = Pattern.compile(String.format("^(?<var>%s)(?:\\.(?<member1>[^(]+)|\\[(?<member2>[^]]+)\\])\\s*(?:\\(+(?<args>[^()]*)\\))?$", operatorDictionary.getNameRe()));
		Matcher matcherDictMember = patternDictMember.matcher(expr);
		if(matcherDictMember.find()) {
			String variable = matcherDictMember.group("var");
			String member1 = matcherDictMember.group("member1");
			String member2 = matcherDictMember.group("member2");
			String member = null;
			if((member1 != null) && (!member1.isEmpty()))
				member = Common.removeQuotes(member1);
			else if((member2 != null) && (!member2.isEmpty()))
				member = Common.removeQuotes(member2);
			String args = matcherDictMember.group("args");
			
			System.out.println("variable : " + variable);
			System.out.println("member : " + member);
			System.out.println("args : " + args);
			
			String arg = Common.removeQuotes(args);
			MyType obj = null;
			
			if(localVars.containsKey(variable)) {
				obj = localVars.get(variable);
				
				System.out.println("value of " + variable + " is: " + obj.val());
				if(obj.getArray() != null)
					for (int i = 0; i < obj.getArray().length; i++) {
						System.out.print(obj.getArray()[i] + ", ");
					}
				System.out.println();
				
			}
			else {
				if(!this.objects.containsKey(variable)) {
					System.out.println(variable + " does not meet in localVars");
					this.objects.put(variable, this.extractObject(variable, playerCode));
				}
				obj = new MyType(this.objects.get(variable));
			}
			
			if(arg == null) {
				/** member access */
				if(member.equals("length"))
					return obj.length();
				return obj.get(new MyType(member));
			}
			
			assert expr.endsWith(")");
			/** function call */
			MyType[] argvals = null;
			if(arg.equals("")) {
				argvals = new MyType[1];
				argvals[0] = new MyType("");
			}
			else {
				String[] argSplit = arg.split(",");
				argvals = new MyType[argSplit.length];
				
				for (int i = 0; i < argSplit.length; i++)
					argvals[i] = this.interpretExpression(argSplit[i], localVars, playerCode);
			}
			
			if(member.equals("split")) {
				assert arg.length() == 0;
				return obj.split();
			}
			if(member.equals("join")) {
				assert argvals.length == 1;
				return argvals[0].join(obj);
			}
			if(member.equals("reverse")) {
				assert argvals.length == 0;
				obj.reverse();
				return obj;
			}
			if(member.equals("slice")) {
				assert argvals.length == 1;
				return obj.slice(argvals[0]);
			}
			if(member.equals("splice")) {
				assert (obj.getList() != null) || (obj.getArray() != null);
				return obj.splice(argvals[0], argvals[1]);
			}
			if(obj.getObject() != null)
				return new MyType(obj.getObject().get(member).resf(argvals));
		}
		
		for (String op : operatorDictionary.getOperators().keySet()) {
			Pattern patternOP = Pattern.compile(String.format("^(?<x>.+?)%s(?<y>.+)", Common.escape(op)));
			Matcher matcherOp = patternOP.matcher(expr);
			if(!matcherOp.find())
				continue;
			PairMyType pairX = this.interpretStatement(matcherOp.group("x"), localVars, playerCode);
			if(pairX.getAbort())
				System.exit(-1);
			PairMyType pairY = this.interpretStatement(matcherOp.group("y"), localVars, playerCode);
			if(pairY.getAbort())
				System.exit(-2);
			MyType[] operands = {pairX.getRes(), pairY.getRes()};
			
			return operatorDictionary.getOperators().get(op).apply(operands);
		}
		
		Pattern patternFunc = Pattern.compile(String.format("^(?<func>%s)\\((?<args>[a-zA-Z0-9_$,]*)\\)$", operatorDictionary.getNameRe()));
		Matcher matcherFunc = patternFunc.matcher(expr);
		if(matcherFunc.find()) {
			
			String func = matcherFunc.group("func");
			String args = matcherFunc.group("args");
			MyType[] argvals = null;
			
			if(args.length() > 0) {
				
				String[] argSplit = args.split(",");
				argvals = new MyType[argSplit.length];
				
				for (int i = 0; i < argSplit.length; i++) {
					if(argSplit[i].matches("^[0-9]$"))
						argvals[i] = new MyType(Integer.parseInt(argSplit[i]));
					else
						argvals[i] = localVars.get(argSplit[i]);
				}
			}
			else
				argvals = new MyType[0];
			
			if(!this.functions.containsKey(func))
				this.functions.put(func, this.extractFunction(func, playerCode));
			
			return new MyType(this.functions.get(func).resf(argvals));
		}
		
		return toReturn;
	}
	
	
	private MyObject extractObject(String objname, String playerCode) {
		
		String funcNameRe = "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')";
		MyObject obj = null;
		Pattern patternObjRe = Pattern.compile(String.format("(?<!this\\.)%s\\s*=\\s*\\{\\s*(?<fields>(?:%s\\s*:\\s*function\\s*\\(.*?\\)\\s*\\{.*?\\}(?:,\\s*)?)*)\\}\\s*;", Common.escape(objname), funcNameRe), Pattern.MULTILINE);
		Matcher matcherObj = patternObjRe.matcher(playerCode);
		
		if(matcherObj.find()) {
			
			System.out.println("extraction de lobject : " + objname);
			
			String fields = matcherObj.group("fields");
			/** Currently, it only supports function definitions */
			Pattern patternFields = Pattern.compile(String.format("(?<key>%s)\\s*:\\s*function\\s*\\((?<args>[a-z,]+)\\)\\{(?<code>[^}]+)\\}", funcNameRe));
			Matcher matcherFields = patternFields.matcher(fields);
			obj = new MyObject();
			String[] argnames = null;
			
			while (matcherFields.find()) {
				argnames = matcherFields.group("args").split(",");
				obj.put(Common.removeQuotes(matcherFields.group("key")), buildFunction(argnames, matcherFields.group("code"), playerCode));
				
				System.out.println("lobject " + objname + " est en cours dextraction");
			}
		}
		else
			System.out.println("lobject " + objname + " est introuvable");
		
		return obj;
	}
	
	
	private MyDecryptFunctCaller extractFunction(String funcname, String playerCode){
		
		Pattern patternFunc = Pattern.compile(String.format("(?:function\\s+%s|[{;,]\\s*%s\\s*=\\s*function|var\\s+%s\\s*=\\s*function)\\s*\\((?<args>[^)]*)\\)\\s*\\{(?<code>[^}]+)\\}", funcname, funcname, funcname));
		Matcher matcherFunc = patternFunc.matcher(playerCode);
		String[] argnames = matcherFunc.group("args").split(",");
		
		return buildFunction(argnames, matcherFunc.group("code"), playerCode);
	}
	
	
	public MyDecryptFunctCaller buildFunction(String[] argnames, String functionCode, String playerCode) {
		
		return new MyDecryptFunctCaller(argnames, functionCode, playerCode);
	}
	
}





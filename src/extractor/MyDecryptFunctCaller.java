package extractor;

import java.util.ArrayList;
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
	private Common common = null;
	private MyDict objects = null;
	private MyObject functions = null;
	
	public MyDecryptFunctCaller(String[] argnames, String functionCode, String playerCode) {
		// TODO Auto-generated constructor stub
		this.argnames = new String[argnames.length];
		for (int i = 0; i < argnames.length; i++)
			this.argnames[i] = argnames[i];
		this.functionCode = functionCode;
		this.playerCode = playerCode;
		
		this.common = Common.getInstance();
		this.objects = new MyDict();
		this.functions = new MyObject();
	}
	
	@Override
	public String resf(MyType[] args) {
		// TODO Auto-generated method stub
		PairMyType result = null;
		Hashtable<String, MyType> localVars = new Hashtable<String, MyType>(argnames.length);
		
		for (int i = 0; i < argnames.length; i++)
			localVars.put(argnames[i], args[i]);
		
		for (String stmt : functionCode.split(";")) {
			
			System.out.println("localVars before statement : " + stmt);
			if(localVars.get(argnames[0]).getStr() != null)
				System.out.println(localVars.get(argnames[0]).getStr());
			else if(localVars.get(argnames[0]).getArray() != null)
				for (int i = 0; i < localVars.get(argnames[0]).getArray().length; i++) {
					System.out.print(localVars.get(argnames[0]).getArray()[i] + ",");
				}
			System.out.println();
			
			
			result = interpretStatement(stmt, localVars, playerCode);
			
			System.out.println("localVars after statement : " + stmt);
			if(localVars.get(argnames[0]).getStr() != null)
				System.out.println("localVars does not change !!");
			else if(localVars.get(argnames[0]).getArray() != null)
				for (int i = 0; i < localVars.get(argnames[0]).getArray().length; i++) {
					System.out.print(localVars.get(argnames[0]).getArray()[i] + ",");
				}
			System.out.println();
			
			
			if(result.getAbort())
				break;
		}
		
		return result.getRes().getStr();
	}
	
	
	private PairMyType interpretStatement(String stmt, Hashtable<String,MyType> localVars, String playerCode) {
		
		Boolean shouldAbort = false;
		String expr = null;
		String statement = stmt.stripLeading();
		Boolean stmtIsVar = statement.startsWith("var ");
		
		if(stmtIsVar)
			expr = statement.substring(4);
		else {
			System.out.println("statement : " + statement);
			
			Boolean stmtIsReturn = statement.startsWith("return ");
			
			System.out.println("test of return statement : " + stmtIsReturn);
			if(stmtIsReturn) {
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
						String subExprMyType =  this.interpretExpression(subExpr, localVars, playerCode).getStr();
						String remainingExpr = expr.substring(matcherExpr.end()).strip();
						if(remainingExpr.length() == 0)
							return new MyType(subExprMyType);
						else
							expr = subExprMyType + remainingExpr;
						break;
					}
				}
			}
		}
		
		for (String op : common.getAssignOperators()) {
			Pattern operationPattern = Pattern.compile(String.format("^(?<out>%s)(?:\\[(?<index>[^\\]]+?)\\])?\\s*%s(?<remain>.*)$", common.getNameRe(), common.escape(op)));
			Matcher operationMatcher = operationPattern.matcher(expression);
			if(!operationMatcher.find())
				continue;
			String out = operationMatcher.group("out");
			String index = operationMatcher.group("index");
			String remain = operationMatcher.group("remain");
			
			System.out.println("out : " + out);
			System.out.println("op : " + op);
			System.out.println("remain : " + remain);
			
			MyType rightResult = (common.isNumeric(remain)) ? (new MyType(remain)) : (this.interpretExpression(remain, localVars, playerCode));
			
			System.out.println("rightValue : " + rightResult.val().toString());
			
			MyType lvar = localVars.get(out);
			
			
			if(!(index == null)) {
				
				System.out.println("index : " + index);
				
				String idx = (common.isNumeric(index)) ? (new String(index)) : (this.interpretExpression(index, localVars, playerCode).getStr());				
				int key = Integer.parseInt(idx);
				
				if(lvar.getArray() != null) {
					if(rightResult.getStr() != null)
						lvar.getArray()[key] = common.operatorFunction(op, lvar.getArray()[key], rightResult.getStr());
					else if(rightResult.getNbr() != null)
						lvar.getArray()[key] = common.operatorFunction(op, lvar.getArray()[key], rightResult.getNbr());
					toReturn = new MyType(lvar.getArray());
				}
				else if(lvar.getList() != null) {
					if(rightResult.getStr() != null)
						lvar.getList().set(key, common.operatorFunction(op, lvar.getList().get(key), rightResult.getStr()));
					else if(rightResult.getNbr() != null)
						lvar.getList().set(key, common.operatorFunction(op, lvar.getList().get(key), rightResult.getNbr()));
					toReturn = new MyType(lvar.getList());
				}
				else if(lvar.getStr() != null) {
					String[] tempStr = new String[lvar.getStr().length()];
					String[] splitStr = lvar.getStr().split("");
					for (int i = 0; i < tempStr.length; i++)
						tempStr[i] = splitStr[i];
					if(rightResult.getStr() != null)
						tempStr[key] = common.operatorFunction(op, tempStr[key], rightResult.getStr());
					else if(rightResult.getNbr() != null)
						tempStr[key] = common.operatorFunction(op, tempStr[key], rightResult.getNbr());
					toReturn = new MyType(String.join("", tempStr));
				}
			}
			else {
				if(op.equals("=")) {
					toReturn = rightResult;
					
					if(rightResult.getArray() != null) {
						for (int i = 0; i < rightResult.getArray().length; i++) {
							System.out.print(rightResult.getArray()[i] + ",");
						}
						System.out.println();
					}
					
				}
				else if((rightResult.getStr() != null) && (lvar.getStr() != null))
					toReturn = new MyType(Integer.parseInt(common.operatorFunction(op, lvar.getStr(), rightResult.getStr())));
				else if((rightResult.getStr() != null) && (lvar.getNbr() != null))
					toReturn = new MyType(Integer.parseInt(common.operatorFunction(op, lvar.getNbr(), rightResult.getStr())));
				else if((rightResult.getNbr() != null) && (lvar.getStr() != null))
					toReturn = new MyType(Integer.parseInt(common.operatorFunction(op, lvar.getStr(), rightResult.getNbr())));
				else if((rightResult.getNbr() != null) && (lvar.getNbr() != null))
					toReturn = new MyType(Integer.parseInt(common.operatorFunction(op, lvar.getNbr(), rightResult.getNbr())));
			}
			
			localVars.put(out, toReturn);
			return toReturn;
		}
		
		
		if(expr.matches("^[0-9]+$"))
			return new MyType(expr);
		
		if(expr.matches("^[a-zA-Z]$")) {
			System.out.println("expression de la classe [a-zA-Z] : " + expr);
			if(localVars.get(expr) != null) {
				
				if(localVars.get(expr).getArray() != null) {
					for (int i = 0; i < localVars.get(expr).getArray().length; i++) {
						System.out.print(localVars.get(expr).getArray()[i] + ",");
					}
					System.out.println();
				}
				else
					System.out.println(localVars.get(expr).getStr());
				
				return localVars.get(expr);
			}
			else
				return new MyType(expr);
		}
		
		Pattern returnPattern = Pattern.compile(String.format("^(?!if|return|true|false)(?<name>%s)$", common.getNameRe()));
		Matcher returnMatcher = returnPattern.matcher(expr);
		if(returnMatcher.find()) {
			String name = returnMatcher.group("name");
			if(localVars.get(name) != null)
				return localVars.get(name);
		}
		
		/*****/
		
		/** JSONObjet result */
		/*
		try {
			return new MyType((JSONObject) new JSONParser().parse(expr));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		/*****/
		
		Pattern patternIn = Pattern.compile(String.format("^(?<in>%s)\\[(?<idx>.+)\\]$", common.getNameRe()));
		Matcher matcherIn = patternIn.matcher(expr);
		if(matcherIn.find()) {
			String in = matcherIn.group("in");
			String idx = matcherIn.group("idx");
			MyType lin = localVars.get(in);
			MyType lidx = (common.isNumeric(idx)) ? new MyType(idx) : this.interpretExpression(idx, localVars, playerCode);
			
			System.out.println("in : " + in);
			
			if(lin != null && lidx != null) {
				if(lin.getArray() != null) {
					if(lidx.getNbr() != null)
						toReturn = new MyType(lin.getArray()[lidx.getNbr()]);
					else if(lidx.getStr() != null)
						toReturn = new MyType(lin.getArray()[Integer.parseInt(lidx.getStr())]);
				}
				else if(lin.getList() != null) {
					if(lidx.getNbr() != null)
						toReturn = new MyType(lin.getList().get(lidx.getNbr()));
					else if(lidx.getStr() != null)
						toReturn = new MyType(lin.getList().get(Integer.parseInt(lidx.getStr())));
				}
				else if(lin.getDict() != null) {
					if(lidx.getStr() != null)
						toReturn = new MyType(lin.getDict().get(lidx.getStr()));
				}
				else if(lin.getObject() != null) {
					if(lidx.getStr() != null)
						toReturn = new MyType(lin.getObject().get(lidx.getStr()));
				}
				else if(lin.getStr() != null) {
					String[] splitLin = lin.getStr().split("");
					if(lidx.getNbr() != null) {
						System.out.println("lidx : " + lidx.getNbr());
						System.out.println("valeur in[lidx] : " + splitLin[lidx.getNbr()]);
						
						toReturn = new MyType(splitLin[lidx.getNbr()]);
					}
					else if(lidx.getStr() != null) {
						System.out.println("lidx : " + lidx.getStr());
						System.out.println("valeur in[lidx] : " + splitLin[Integer.parseInt(lidx.getStr())]);
						
						toReturn = new MyType(splitLin[Integer.parseInt(lidx.getStr())]);
					}
				}
				
				return toReturn;
			}
		}
		
		Pattern patternDictMember = Pattern.compile(String.format("^(?<var>%s)(?:\\.(?<member1>[^(]+)|\\[(?<member2>[^]]+)\\])\\s*(?:\\(+(?<args>[^()]*)\\))?$", common.getNameRe()));
		Matcher matcherDictMember = patternDictMember.matcher(expr);
		if(matcherDictMember.find()) {
			String variable = matcherDictMember.group("var");
			String member1 = matcherDictMember.group("member1");
			String member2 = matcherDictMember.group("member2");
			String member = null;
			if((member1 != null) && (!member1.isEmpty()))
				member = new String(common.removeQuotes(member1));
			else if((member2 != null) && (!member2.isEmpty())) {
				MyType lmembre2 = this.interpretExpression(member2, localVars, playerCode);
				member = new String(common.removeQuotes(lmembre2.getStr()));
			}
			String args = matcherDictMember.group("args");
			
			System.out.println("variable : " + variable);
			System.out.println("member : " + member);
			System.out.println("args : " + args);
			
			String arg = common.removeQuotes(args);
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
				if(member.equals("length")) {
					int len = 0;
					if(obj.getObject() != null)
						len = obj.getObject().size();
					else if(obj.getList() != null)
						len = obj.getList().size();
					else if(obj.getArray() != null)
						len = obj.getArray().length;
					else if(obj.getStr() != null)
						len = obj.getStr().length();
					else if(obj.getDict() != null)
						len = obj.getDict().size();
					System.out.println("len : " + len);
					toReturn = new MyType(len);
				}
				else if(obj.getDict() != null)
					toReturn = new MyType(obj.getDict().get(member));
				else if(obj.getObject() != null)
					toReturn = new MyType(obj.getObject().get(member));
				
				return toReturn;
			}
			
			assert expr.endsWith(")");
			/** function call */
			MyType[] argvals = null;
			if(!arg.equals("")) {
				String[] argSplit = arg.split(",");
				argvals = new MyType[argSplit.length];
				
				System.out.println("longueur argvals : " + argSplit.length);
				for (int i = 0; i < argSplit.length; i++) {
					
					System.out.println("arg " + i + " : " + argSplit[i]);
					
					if(common.isNumeric(argSplit[i]))
						argvals[i] = new MyType(argSplit[i]);
					else
						argvals[i] = this.interpretExpression(argSplit[i], localVars, playerCode);
					
					System.out.println("argvals " + i + " : " + argvals[i].getStr());
				}
			}
			else {
				argvals = new MyType[1];
				argvals[0] = new MyType("");
			}
			
			if(member.equals("split")) {
				
				System.out.println("ouais on y est. arg est : " + arg);
				System.out.println("la longueur de arg est : " + arg.length());
				
				if(arg.length() == 0) {
					System.out.println("on procede a la separation de " + variable);
					
					obj.setArray(obj.getStr().split(""));
					obj.setStr(null);
					
					return obj;
				}
			}
			if(member.equals("join")) {
				assert argvals.length == 1;
				if(obj.getObject() != null)
					toReturn = new MyType(String.join(argvals[0].getStr(), obj.getObject().keySet()));
				else if(obj.getList() != null)
					toReturn = new MyType(String.join(argvals[0].getStr(), obj.getList()));
				else if(obj.getArray() != null) {
					System.out.println("argvals[0] : " + argvals[0].getStr());
					
					toReturn = new MyType(String.join(argvals[0].getStr(), obj.getArray()));
				}
				return toReturn;
			}
			if(member.equals("reverse")) {
				assert argvals.length == 0;
				if(obj.getObject() != null) {
					MyObject newObject = new MyObject();
					ArrayList<String> keys = new ArrayList<String>(obj.getObject().keySet());
					ArrayList<MyDecryptFunctCaller> values = new ArrayList<MyDecryptFunctCaller>(obj.getObject().values());
					for (int i = obj.getObject().size() - 1; i >= 0 ; i--)
						newObject.put(keys.get(i), values.get(i));
					obj.setObject(newObject);
					return obj;
				}
				else if(obj.getList() != null) {
					ArrayList<String> newList = new ArrayList<>();
					for (int i = obj.getList().size() - 1; i >= 0; i--)
						newList.add(obj.getList().size() - 1 - i, obj.getList().get(i));
					obj.setList(newList);
					return obj;
				}
				else if(obj.getArray() != null) {
					String[] newArray = new String[obj.getArray().length];
					for (int i = newArray.length - 1; i >= 0; i--)
						newArray[newArray.length - 1 - i] = obj.getArray()[i];
					obj.setArray(newArray);
					return obj;
				}
				else if(obj.getStr() != null) {
					String[] splitStr = obj.getStr().split("");
					String[] newSplitStr = new String[splitStr.length];
					for (int i = newSplitStr.length - 1; i >= 0; i--)
						newSplitStr[newSplitStr.length - 1 - i] = splitStr[i];
					obj.setStr(String.join("", newSplitStr));
					return obj;
				}
			}
			if(member.equals("slice")) {
				assert argvals.length == 1;
				if(obj.getObject() != null) {
					ArrayList<String> keys = new ArrayList<String>(obj.getObject().keySet());
					ArrayList<MyDecryptFunctCaller> values = new ArrayList<MyDecryptFunctCaller>(obj.getObject().values());
					int j = 0;
					for (int i = 0; i < obj.getObject().size(); i++)
						if(keys.get(i) == argvals[0].getStr()) {
							j = i;
							break;
						}
					MyObject newObject = new MyObject(obj.getObject().size() - j);
					for (int i = j; i < obj.getDict().size(); i++)
						newObject.put(keys.get(i), values.get(i));
					obj.setObject(newObject);
					return obj;
				}
				else if(obj.getList() != null) {
					int j = Integer.parseInt(argvals[0].getStr());
					ArrayList<String> newList = new ArrayList<>(obj.getList().size() - j);
					for (int i = j; i < obj.getList().size(); i++)
						newList.add(i - j, obj.getList().get(i));
					obj.setList(newList);
					return obj;
				}
				else if(obj.getArray() != null) {
					int j = Integer.parseInt(argvals[0].getStr());
					String[] newArray = new String[obj.getArray().length - j];
					for (int i = j; i < obj.getArray().length; i++)
						newArray[i - j] = obj.getArray()[i];
					obj.setArray(newArray);
					return obj;
				}
				else if(obj.getStr() != null) {
					obj.setStr(obj.getStr().substring(Integer.parseInt(argvals[0].getStr())));
					return obj;
				}
			}
			if(member.equals("splice")) {
				
				Integer index = (common.isNumeric(argvals[0].getStr())) ? (Integer.parseInt(argvals[0].getStr())) : (Integer.parseInt(this.interpretExpression(argvals[0].getStr(), localVars, playerCode).getStr()));
				Integer howMany = null;
				String itemToAdd = null;
				
				if(common.isNumeric(argvals[1].getStr()))
					howMany = Integer.parseInt(argvals[1].getStr());
				else
					itemToAdd = this.interpretExpression(argvals[1].getStr(), localVars, playerCode).getStr();
				
				if (obj.getList() != null) {
					if(howMany != null) {
						int sizeSplice = Math.min(index + howMany, obj.getList().size());
						ArrayList<String> returnList = new ArrayList<>(sizeSplice);
						
						for (int i = index; i < sizeSplice; i++)
							returnList.add(i - index, obj.getList().remove(index.intValue()));
						toReturn = new MyType(returnList);
					}
					else if(itemToAdd != null) {
						int sizeSplice = obj.getList().size() + 1;
						ArrayList<String> newList = new ArrayList<>(sizeSplice);
						ArrayList<String> returnList = null;
						
						for (int i = 0; i < sizeSplice; i++) {
							if(i < index)
								newList.add(i, obj.getList().get(i));
							else if(i == index)
								newList.add(index, itemToAdd);
							else if(i > index)
								newList.add(i, obj.getList().get(i - 1));
						}
						obj.setList(newList);
						toReturn = new MyType(returnList);
					}
					System.out.println("splice array entry");
					
					return toReturn;
				}
				if(obj.getArray() != null) {
					if(howMany != null) {
						int sizeSplice = Math.min(index + howMany, obj.getArray().length);
						int sizeNewArray = obj.getArray().length - howMany;
						String[] newArray = new String[sizeNewArray];
						String[] returnArray = new String[sizeSplice];
						
						for (int i = index; i < sizeSplice; i++)
							returnArray[i - index] = obj.getArray()[i];
						
						for (int i = 0; i < obj.getArray().length; i++) {
							if(i < index)
								newArray[i] = obj.getArray()[i];
							if(i >= sizeSplice)
								newArray[i - howMany] = obj.getArray()[i];
						}
						
						obj.setArray(newArray);
						toReturn = new MyType(returnArray);
					}
					else if(itemToAdd != null) {
						int sizeSplice = obj.getArray().length + 1;
						String[] newArray = new String[sizeSplice];
						String[] returnArray = null;
						
						for (int i = 0; i < sizeSplice; i++) {
							if(i < index)
								newArray[i] = obj.getArray()[i];
							else if(i == index)
								newArray[index] = itemToAdd;
							else if(i > index)
								newArray[i] = obj.getArray()[i - 1];
						}
						obj.setArray(newArray);
						toReturn = new MyType(returnArray);
					}
					
					return toReturn;
				}
				if(obj.getStr() != null) {
					if(howMany != null) {
						int sizeSplice = Math.min(index + howMany, obj.getStr().length());
						String newStr = new String(obj.getStr().substring(0, index.intValue()).concat(obj.getStr().substring(sizeSplice)));
						
						obj.setStr(newStr);
						toReturn = new MyType(obj.getStr().substring(index.intValue(), sizeSplice));
					}
					else if(itemToAdd != null) {
						String newStr = new String(obj.getStr().substring(0, index.intValue()).concat(itemToAdd).concat(obj.getStr().substring(index.intValue())));
						String returnString = null;
						
						obj.setStr(newStr);
						toReturn = new MyType(returnString);
					}
					
					return toReturn;
				}
				
				System.out.println("splice out");
			}
			if(obj.getObject() != null)
				return new MyType(obj.getObject().get(member).resf(argvals));
		}
		
		for (String op : common.getOperators()) {
			Pattern patternOP = Pattern.compile(String.format("^(?<x>.+?)%s(?<y>.+)", common.escape(op)));
			Matcher matcherOp = patternOP.matcher(expr);
			if(!matcherOp.find())
				continue;
			MyType x = this.interpretExpression(matcherOp.group("x"), localVars, playerCode);
			MyType y = this.interpretExpression(matcherOp.group("y"), localVars, playerCode);
			
			System.out.println("x : " + x.val().toString());
			System.out.println("y : " + y.val().toString());
			
			if((x.getStr() != null) && (common.isNumeric(x.getStr())) && (y.getStr() != null) && (common.isNumeric(y.getStr())))
				toReturn = new MyType(common.operatorFunction(op, x.getStr(), y.getStr()));
			if((x.getStr() != null) && (common.isNumeric(x.getStr())) && (y.getNbr() != null))
				toReturn = new MyType(common.operatorFunction(op, x.getStr(), y.getNbr()));
			if((x.getNbr() != null) && (y.getStr() != null) && (common.isNumeric(y.getStr())))
				toReturn = new MyType(common.operatorFunction(op, x.getNbr(), y.getStr()));
			if((x.getNbr() != null) && (y.getNbr() != null))
				toReturn = new MyType(common.operatorFunction(op, x.getNbr(), y.getNbr()));
			
			return toReturn;
		}
		
		Pattern patternFunc = Pattern.compile(String.format("^(?<func>%s)\\((?<args>[a-zA-Z0-9_$,]*)\\)$", common.getNameRe()));
		Matcher matcherFunc = patternFunc.matcher(expr);
		if(matcherFunc.find()) {
			
			String func = matcherFunc.group("func");
			String args = matcherFunc.group("args");
			MyType[] argvals = null;
			int argsLength = args.length();
			
			if(argsLength > 0) {
				
				String[] argSplit = args.split(",");
				argvals = new MyType[argsLength];
				
				for (int i = 0; i < argsLength; i++) {
					if(common.isNumeric(argSplit[i]))
						argvals[i] = new MyType(argSplit[i]);
					else
						argvals[i] = this.interpretExpression(argSplit[i], localVars, playerCode);
				}
			}
			else {
				argvals = new MyType[1];
				argvals[0] = new MyType("");
			}
			if(!this.functions.containsKey(func))
				this.functions.put(func, this.extractFunction(func, playerCode));
			return new MyType(this.functions.get(func).resf(argvals));
		}
		return toReturn;
	}
	
	
	private MyObject extractObject(String objname, String playerCode) {
		
		String funcNameRe = "(?:[a-zA-Z$0-9]+|\"[a-zA-Z$0-9]+\"|'[a-zA-Z$0-9]+')";
		MyObject obj = null;
		Pattern patternObjRe = Pattern.compile(String.format("(?<!this\\.)%s\\s*=\\s*(?:\\{\\s*(?<fields>(?:%s\\s*:\\s*(?:function\\s*\\(.*?\\)\\s*\\{.*?\\}|%s)(?:,\\s*)?)*)\\}|%s)\\s*;", objname, funcNameRe, funcNameRe, funcNameRe), Pattern.MULTILINE);
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
				obj.put(common.removeQuotes(matcherFields.group("key")), buildFunction(argnames, matcherFields.group("code"), playerCode));
				
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





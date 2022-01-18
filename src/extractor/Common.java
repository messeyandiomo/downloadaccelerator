package extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Common implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7266154229644168193L;
	private static Common instance = null;
	private ArrayList<String> operators = new ArrayList<String>();
	private ArrayList<String> assignOperators = new ArrayList<String>();
	private String nameRe = "[a-zA-Z_$][a-zA-Z_$0-9]*";
	

	private Common() {
		// TODO Auto-generated constructor stub
		this.operators.add("|");
		this.operators.add("^");
		this.operators.add("&");
		this.operators.add(">>");
		this.operators.add("<<");
		this.operators.add("-");
		this.operators.add("+");
		this.operators.add("%");
		this.operators.add("/");
		this.operators.add("*");
		
		for (String op : operators)
			this.assignOperators.add(op + "=");
		this.assignOperators.add("=");
		
	}

	public static Common getInstance() {
		
		if(instance == null)
			instance = new Common();
		return instance;
	}
	
	public ArrayList<String> getOperators() {
		return operators;
	}

	public ArrayList<String> getAssignOperators() {
		return assignOperators;
	}

	public String getNameRe() {
		return nameRe;
	}
	
	public String escape(String operation) {
		
		String escapeOp = "";
		String escape = "\\";
		if(operation.length() == 1)
			escapeOp = escape + operation;
		else {
			String[] splitOperation = operation.split("");
			for (int i = 0; i < operation.length(); i++)
				escapeOp = escapeOp + escape + splitOperation[i];
		}
		
		return escapeOp;
	}
	
	
	public String removeQuotes(String string) {
		if (string == null)
			return null;
		return string.replaceAll("^\\\"+|\\\"+$", "");
	}
	
	
	public String getPlayerInfo(ArrayList<Pattern> infoRe, String data, String group) {
		
		String info = null;
		
		for(Pattern re:infoRe) {
			Matcher matcher = re.matcher(data);
			if(matcher.find()) {
				info = matcher.group(group);
				break;
			}
		}
		return info;
	}
	
	
	public String getWebPage(String webPageUrl) {
		
		String codeWebPage = "";
		try {
			URL url = new URL(webPageUrl);
			try {
				BufferedReader codeBuffer = new BufferedReader(new InputStreamReader(url.openStream()));
				String codeLine;
				while ((codeLine = codeBuffer.readLine()) != null)
					codeWebPage += codeLine;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return codeWebPage;
	}
	
	
	public String operatorFunction(String operator, Object operd1, Object operd2) {
		
		Long val = null;
		
		if(this.isNumeric(operd1) && this.isNumeric(operd2)) {
			
			String operand1 = operd1.toString();
			String operand2 = operd2.toString();
			
			if(operator.contains("|"))
				val = ((Long.parseLong(operand1)) | (Long.parseLong(operand2)));
			else if(operator.contains("^"))
				val = ((Long.parseLong( operand1)) ^ (Long.parseLong(operand2)));
			else if(operator.contains("&"))
				val = ((Long.parseLong( operand1)) & (Long.parseLong(operand2)));
			else if(operator.contains(">>"))
				val = ((Long.parseLong( operand1)) >> (Long.parseLong(operand2)));
			else if(operator.contains("<<"))
				val = ((Long.parseLong( operand1)) << (Long.parseLong(operand2)));
			else if(operator.contains("-"))
				val = ((Long.parseLong( operand1)) - (Long.parseLong(operand2)));
			else if(operator.contains("+"))
				val = ((Long.parseLong( operand1)) + (Long.parseLong(operand2)));
			else if(operator.contains("%"))
				val = ((Long.parseLong( operand1)) % (Long.parseLong(operand2)));
			else if(operator.contains("/"))
				val = ((Long.parseLong( operand1)) / (Long.parseLong(operand2)));
			else if(operator.contains("*"))
				val = ((Long.parseLong( operand1)) * (Long.parseLong(operand2)));
		}
		else if (operator == "=")
			return operd2.toString();
		
		return val.toString();
	}
	
	
	public boolean isNumeric(Object str) {
		
		Boolean isinteger = false;
		
		if(str instanceof String) {
			try {
			    Long.parseLong((String) str);
			    isinteger = true;
			    
			} catch (NumberFormatException e) {
			}
		}
		else if(str instanceof Integer)
			isinteger = true;
		
		return isinteger;
	}
	

}

package extractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Common {
	

	private Common() {
		// TODO Auto-generated constructor stub
	}

	
	
	public static String escape(String str) {
		
		String escapeStr = "";
		String[] splitStr = str.split("");
		
		for (int i = 0; i < str.length(); i++) {
			if(Pattern.matches("\\w", splitStr[i]))
				escapeStr = escapeStr + splitStr[i];
			else
				escapeStr = escapeStr + Pattern.quote(splitStr[i]);
		}
		
		return escapeStr;
	}
	
	
	public static String removeQuotes(String string) {
		if (string == null)
			return null;
		return string.replaceAll("^\\\"+|\\\"+$", "");
	}
	
	
	public static String getPlayerInfo(ArrayList<Pattern> infoRe, String data, String group) {
		
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
	
	
	public static String getWebPage(String webPageUrl) {
		
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
	
	
	public static boolean isNumeric(Object str) {
		
		Boolean isinteger = false;
		
		if(str != null) {
			if(str instanceof String) {
				try {
				    Long.parseLong((String) str);
				    isinteger = true;
				    
				} catch (NumberFormatException e) {
				}
			}
			else if(str instanceof Integer)
				isinteger = true;
		}
		
		return isinteger;
	}
	

}

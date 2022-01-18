package extractor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;


public class Youtube {
	
	private String signatureCipher = null;
	private String playerUrl = null;
	private YoutubeRE youtubeRe = null;
	private Common common = null;
	private String playerCode = null;
	private String sectionPathName = null;
	
	
	public Youtube(String signatureCipher, String playerUrl, String cachedirectory) {
		// TODO Auto-generated constructor stub
		this.signatureCipher = signatureCipher;
		this.playerUrl = playerUrl;
		this.youtubeRe = YoutubeRE.getInstance();
		this.common = Common.getInstance();
		this.sectionPathName = cachedirectory + "youtube.txt";
		
	}
	
	
	public String getPlayerId() {
		return common.getPlayerInfo(youtubeRe.getPlayerInfoRe(), playerUrl, "id");
	}
	
	
	public String getPlayerCode() {
		if(this.playerCode == null)
			this.playerCode = common.getWebPage(playerUrl);
		
		return this.playerCode;
	}
	
	
	public String getSignFuncName() {
		
		return common.getPlayerInfo(youtubeRe.getSignatureFuncNameRe(), this.getPlayerCode(), "sig");
	}
	
	
	public MyDecryptFunctCaller extractSignatureFunction() {
		
		MyDecryptFunctCaller toReturn = null;
		CacheSignFunc cachesignfunc = new CacheSignFunc(sectionPathName);
		String playerid = this.getPlayerId();
		toReturn = cachesignfunc.load(playerid);
		if(toReturn == null) {
			System.out.println("le fichier nest pas charge dans le cache");
			/** real extraction */
			String[] argnames = null;
			String functionCode = null;
			String funcName = this.getSignFuncName();
			Pattern signatureFunctionRe = Pattern.compile(String.format("(?:function\\s+%s|[{;,]\\s*%s\\s*=\\s*function|var\\s+%s\\s*=\\s*function)\\s*\\((?<args>[^)]*)\\)\\s*\\{(?<code>[^}]+)\\}", funcName, funcName, funcName));
			Matcher matcher = signatureFunctionRe.matcher(this.getPlayerCode());
			if(matcher.find()) {
				argnames = matcher.group("args").split(",");
				functionCode = matcher.group("code");
				System.out.println(functionCode);
			}
			System.out.print("Arguments names : ");
			for (int i = 0; i < argnames.length; i++) {
				System.out.println(argnames[i]);
			}
			
			toReturn = new MyDecryptFunctCaller(argnames, functionCode, this.playerCode);
			/** end of real extraction */
			cachesignfunc.store(playerid, toReturn);
		}
		
		return toReturn;
	}
	
	
	@SuppressWarnings("unchecked")
	private JSONObject parseCipherUrl() {
		
		JSONObject result = new JSONObject();
		String[] splitCipherAnd = this.signatureCipher.split("&");
		ArrayList<String> splitCipher = new ArrayList<>();
		for (String eltSplit : splitCipherAnd) {
			if(eltSplit.contains(";")) {
				String[] subSplit = eltSplit.split(";");
				for (String sub : subSplit)
					splitCipher.add(sub);
			}
			else
				splitCipher.add(eltSplit);
		}
		
		String[] splitCipherPart = null;
		
		for (String cipherPart : splitCipher) {
			splitCipherPart = cipherPart.split("=", 2);
			splitCipherPart[0].replace("+", " ");
			splitCipherPart[1].replace("+", " ");
			result.put(splitCipherPart[0], splitCipherPart[1]);
		}
		
		return result;
	}
	
	
	public String decryptUrl() {
		
		JSONObject urlParsed = this.parseCipherUrl();
		String url = urlParsed.get("url").toString();
		/** Decipher signature */
		MyType[] s = new MyType[1];
		s[0] = new MyType(urlParsed.get("s").toString());
		String signature = extractSignatureFunction().resf(s);
		/** generate URL */
		@SuppressWarnings("unchecked")
		ArrayList<String> keys = new ArrayList<String>(urlParsed.keySet());
		for (String key : keys) {
			if((!key.equals("url")) && (!key.equals("sp")) && (!key.equals("s")))
				url += "&" + key + "=" + urlParsed.get(key).toString();
		}
		String sp = urlParsed.get("sp").toString();
		if(sp.isEmpty())
			sp = "signature";
		url += "&" + sp + "=" + signature;
		
		return url;
	}
	

}
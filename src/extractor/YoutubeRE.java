package extractor;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class YoutubeRE {
	
	private static YoutubeRE instance = null;
	
	private ArrayList<Pattern> playerInfoRe = new ArrayList<Pattern>();
	private ArrayList<Pattern> signatureFuncNameRe = new ArrayList<Pattern>();
	
	
	private YoutubeRE() {
		// TODO Auto-generated constructor stub
		this.playerInfoRe.add(Pattern.compile("/s/player/(?<id>[a-zA-Z0-9_-]{8,})/player"));
		this.playerInfoRe.add(Pattern.compile("/(?<id>[a-zA-Z0-9_-]{8,})/player(?:_ias\\.vflset(?:/[a-zA-Z]{2,3}_[a-zA-Z]{2,3})?|-plasma-ias-(?:phone|tablet)-[a-z]{2}_[A-Z]{2}\\.vflset)/base\\.js$"));
		this.playerInfoRe.add(Pattern.compile("\\b(?<id>vfl[a-zA-Z0-9_-]+)\\b.*?\\.js$"));
		
		this.signatureFuncNameRe.add(Pattern.compile("\\b[cs]\\s*&&\\s*[adf]\\.set\\([^,]+\\s*,\\s*encodeURIComponent\\s*\\(\\s*(?<sig>[a-zA-Z0-9$]+)\\("));
		this.signatureFuncNameRe.add(Pattern.compile("\\b[a-zA-Z0-9]+\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*encodeURIComponent\\s*\\(\\s*(?<sig>[a-zA-Z0-9$]+)\\("));
		this.signatureFuncNameRe.add(Pattern.compile("\\bm=(?<sig>[a-zA-Z0-9$]{2})\\(decodeURIComponent\\(h\\.s\\)\\)"));
		this.signatureFuncNameRe.add(Pattern.compile("\\bc&&\\(c=(?<sig>[a-zA-Z0-9$]{2})\\(decodeURIComponent\\(c\\)\\)"));
		this.signatureFuncNameRe.add(Pattern.compile("(?:\\b|[^a-zA-Z0-9$])(?<sig>[a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\);[a-zA-Z0-9$]{2}\\.[a-zA-Z0-9$]{2}\\(a,\\d+\\)"));
		this.signatureFuncNameRe.add(Pattern.compile("(?:\\b|[^a-zA-Z0-9$])(?<sig>[a-zA-Z0-9$]{2})\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)"));
		this.signatureFuncNameRe.add(Pattern.compile("(?<sig>[a-zA-Z0-9$]+)\\s*=\\s*function\\(\\s*a\\s*\\)\\s*\\{\\s*a\\s*=\\s*a\\.split\\(\\s*\"\"\\s*\\)"));
		/* Obsolete patterns */
		this.signatureFuncNameRe.add(Pattern.compile("([\"\\'])signature\\1\\s*,\\s*(?<sig>[a-zA-Z0-9$]+)\\("));
		this.signatureFuncNameRe.add(Pattern.compile("\\.sig\\|\\|(?<sig>[a-zA-Z0-9$]+)\\("));
		this.signatureFuncNameRe.add(Pattern.compile("yt\\.akamaized\\.net/\\)\\s*\\|\\|\\s*.*?\\s*[cs]\\s*&&\\s*[adf]\\.set\\([^,]+\\s*,\\s*(?:encodeURIComponent\\s*\\()?\\s*(?<sig>[a-zA-Z0-9$]+)\\("));
		this.signatureFuncNameRe.add(Pattern.compile("\\b[cs]\\s*&&\\s*[adf]\\.set\\([^,]+\\s*,\\s*(?<sig>[a-zA-Z0-9$]+)\\("));
		this.signatureFuncNameRe.add(Pattern.compile("\\b[a-zA-Z0-9]+\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*(?<sig>[a-zA-Z0-9$]+)\\("));
		this.signatureFuncNameRe.add(Pattern.compile("\\bc\\s*&&\\s*a\\.set\\([^,]+\\s*,\\s*\\([^)]*\\)\\s*\\(\\s*(?<sig>[a-zA-Z0-9$]+)\\("));
		this.signatureFuncNameRe.add(Pattern.compile("\\bc\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*\\([^)]*\\)\\s*\\(\\s*(?<sig>[a-zA-Z0-9$]+)\\("));
		this.signatureFuncNameRe.add(Pattern.compile("\\bc\\s*&&\\s*[a-zA-Z0-9]+\\.set\\([^,]+\\s*,\\s*\\([^)]*\\)\\s*\\(\\s*(?<sig>[a-zA-Z0-9$]+)\\("));
		
	}
	
	
	public static YoutubeRE getInstance() {
		
		if(instance == null) {
			instance = new YoutubeRE();
		}
				
		return instance;
	}
	
	public ArrayList<Pattern> getPlayerInfoRe(){
		return this.playerInfoRe;
	}
	
	public ArrayList<Pattern> getSignatureFuncNameRe(){
		return this.signatureFuncNameRe;
	}
	
	
}

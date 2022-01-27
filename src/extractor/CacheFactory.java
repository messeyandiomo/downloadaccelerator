package extractor;

import java.util.LinkedHashMap;

public class CacheFactory {
	
	private static CacheFactory instance = null;
	private static LinkedHashMap<String, Cache> cacheDictionary = new LinkedHashMap<String, Cache>();
	
	private CacheFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static CacheFactory getInstance() {
		
		if(instance == null)
			instance = new CacheFactory();
		return instance;
	}

	public synchronized Cache getCache(String sectionfilepath) {
		
		Cache toReturn = cacheDictionary.get(sectionfilepath);
		
		if(toReturn != null)
			return toReturn;
		else {
			toReturn = new Cache(sectionfilepath);
			cacheDictionary.put(sectionfilepath, toReturn);
		}
		
		return toReturn;
	}

}

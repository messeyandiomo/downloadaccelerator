package extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CacheSignFunc {
	/**
	 * this class contains the cache which contains in
	 * his turn a player id and an object MyDecryptCaller 
	 */
	private String sectionFilePath = null;
	private MyObject cache = null;
	private File file = null;
	
	
	public CacheSignFunc(String sectionfilepath) {
		// TODO Auto-generated constructor stub
		this.sectionFilePath = sectionfilepath;
		this.cache = new MyObject();
		this.file = new File(sectionfilepath);
		
		if(!this.isEmpty())
			fillCache();
		else {
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private synchronized void fillCache() {
		System.out.println("chargement du fichier dans le cache");
		FileInputStream cachefile;
		try {
			cachefile = new FileInputStream(sectionFilePath);
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(cachefile);
				CacheEntry entry = null;
				try {
					while(in.available() > 0) {
						entry = (CacheEntry) in.readObject();
						this.cache.put(entry.getKey(), entry.getFunctCaller());
					}	
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	
	public void store(String key, MyDecryptFunctCaller data) {
		
		cache.put(key, data);
		updateCacheFile(key, data);
	}
	
	
	private synchronized void updateCacheFile(String key, MyDecryptFunctCaller data) {
		
		boolean keyisfind = false;
		if(!this.isEmpty()) {
			FileInputStream cachefile;
			try {
				cachefile = new FileInputStream(sectionFilePath);
				ObjectInputStream in;
				try {
					in = new ObjectInputStream(cachefile);
					CacheEntry entry = null;
					try {
						while((entry = (CacheEntry) in.readObject()) != null)
							if(entry.getKey().equals(key)) {
								keyisfind = true;
								break;
							}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(!keyisfind) {
			ObjectOutputStream out;
			try {
				out = new ObjectOutputStream(new FileOutputStream(sectionFilePath));
				out.writeObject(new CacheEntry(key, data));
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public synchronized MyDecryptFunctCaller load(String key) {
		
		MyDecryptFunctCaller toReturn = null;
		
		if(!this.isEmpty()) {
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(new FileInputStream(sectionFilePath));
				CacheEntry entry = null;
				try {
					while((entry = (CacheEntry) in.readObject()) != null)
						if(entry.getKey().equals(key)) {
							toReturn = entry.getFunctCaller();
							break;
						}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				in.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return toReturn;
	}
	
	
	public boolean isEmpty() {
		boolean toReturn = true;
		
		if(this.file.length() != 0)
			toReturn = false;
		
		return toReturn;
	}
}



class CacheEntry implements Serializable {
	
	private static final long serialVersionUID = 3741144144461153143L;
	private String key = null;
	private MyDecryptFunctCaller functCaller = null;
	
	public CacheEntry(String key, MyDecryptFunctCaller functcaller) {
		this.setKey(key);
		this.setFunctCaller(functcaller);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public MyDecryptFunctCaller getFunctCaller() {
		return functCaller;
	}

	public void setFunctCaller(MyDecryptFunctCaller functCaller) {
		this.functCaller = functCaller;
	}
}

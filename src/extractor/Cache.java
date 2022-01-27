package extractor;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Cache {
	/**
	 * this class contains the cache which contains in
	 * his turn a player id and an object MyDecryptCaller 
	 */
	private String sectionFilePath = null;
	private MyObject cacheMemory = null;
	private File file = null;
	
	
	public Cache(String sectionfilepath) {
		// TODO Auto-generated constructor stub
		this.sectionFilePath = sectionfilepath;
		this.cacheMemory = new MyObject();
		this.file = new File(sectionfilepath);
		
		if(!this.cacheFileIsEmpty())
			if(this.cacheIsEmpty())
				fillCache();
		else
			this.createFile(file);
	}
	
	
	private void createFile(File file) {
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void fillCache() {
		System.out.println("chargement du fichier dans le cache");
		FileInputStream cachefile;
		try {
			cachefile = new FileInputStream(sectionFilePath);
			ObjectInputStream in;
			try {
				in = new ObjectInputStream(cachefile);
				CacheEntry entry = null;
				try {
					while(true) {
						try {
							entry = (CacheEntry) in.readObject();
							if(entry != null) {
								entry.getFunctCaller().resetOperatorsDictionary();
								this.cacheMemory.put(entry.getKey(), entry.getFunctCaller());
							}
						} catch (EOFException e) {
							System.out.println("End of file reached for fillCache");
							break;
						}
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
	
	
	public synchronized void store(String key, MyDecryptFunctCaller data) {
		
		cacheMemory.put(key, data);
		updateCacheFile(key, data);
	}
	
	
	private void updateCacheFile(String key, MyDecryptFunctCaller data) {
		
		boolean keyisfind = false;
		if(!this.cacheFileIsEmpty()) {
			FileInputStream cachefile;
			try {
				cachefile = new FileInputStream(sectionFilePath);
				ObjectInputStream in;
				try {
					in = new ObjectInputStream(cachefile);
					CacheEntry entry = null;
					try {
						while(true) {
							try {
								entry = (CacheEntry) in.readObject();
								if(entry != null) {
									if(entry.getKey().equals(key)) {
										keyisfind = true;
										System.out.println("le lecteur a ete trouve dans le fichier");
										break;
									}
								}
							} catch (EOFException e) {
								System.out.println("End of file reached for store");
								break;
							}
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
				System.out.println("enregistrement dun nouveau lecteur dans le fichier");
				
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
		
		if(!this.cacheIsEmpty()) {
			toReturn = cacheMemory.get(key);
			if(toReturn == null) {
				System.out.println("recherche dans le fichier du cache");
				if(!this.cacheFileIsEmpty()) {
					System.out.println("le fichier du cache nest pas vide donc ...");
					ObjectInputStream in;
					try {
						in = new ObjectInputStream(new FileInputStream(sectionFilePath));
						
						System.out.println("recherche dans : " + sectionFilePath);
						
						CacheEntry entry = null;
						try {
							while(true) {
								try {
									entry = (CacheEntry) in.readObject();
									if(entry != null) {
										System.out.println("key : " + entry.getKey());
										
										if(entry.getKey().equals(key)) {
											entry.getFunctCaller().resetOperatorsDictionary();
											toReturn = entry.getFunctCaller();
											
											System.out.println("le lecteur a ete trouve by load");
											
											break;
										}
									}
								} catch (EOFException e) {
									System.out.println("le lecteur na pas ete trouve by load");
									break;
								}
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
			}
		}
		
		return toReturn;
	}
	
	
	public boolean cacheFileIsEmpty() {
		boolean toReturn = true;
		
		if(this.file.length() != 0)
			toReturn = false;
		
		return toReturn;
	}
	
	public boolean cacheIsEmpty() {
		boolean toReturn = true;
		
		if(this.cacheMemory.size() != 0)
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

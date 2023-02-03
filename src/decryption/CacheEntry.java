package decryption;

import java.io.Serializable;

public class CacheEntry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6629277625726691298L;
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

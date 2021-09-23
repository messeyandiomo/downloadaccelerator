import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadProperties {
	
	private int subDownloadCount = 10;
	private SubDownloadProperties[] subDownloadProperties;
	
	
	
	public DownloadProperties() {
		
		this.subDownloadProperties = new SubDownloadProperties[subDownloadCount];
	}
	
	
	public DownloadProperties(URL url) {
		
		HttpURLConnection connexion = null;
		try {
			connexion = (HttpURLConnection) (url.openConnection());
			String maxConnections = connexion.getRequestProperty("http.maxConnections");
			if(maxConnections != null)
				this.setSubDownloadCount(Integer.parseInt(maxConnections));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.subDownloadProperties = new SubDownloadProperties[subDownloadCount];
	}
	
	
	public DownloadProperties(int subdownloadcount) {
		
		this.setSubDownloadCount(subdownloadcount);
		this.subDownloadProperties = new SubDownloadProperties[subDownloadCount];
	}
	
	
	/******************
	 * 
	 * @return
	 */
	public int getSubDownloadCount() {
		return subDownloadCount;
	}


	public void setSubDownloadCount(int subdownloadcount) {
		this.subDownloadCount = subdownloadcount;
	}


	public SubDownloadProperties getSubDownloadProperties(int subdownloadpropertiesnumber) {
		return subDownloadProperties[subdownloadpropertiesnumber];
	}


	public void setSubDownloadProperties(int subdownloadpropertiesnumber, SubDownloadProperties subDownloadProperties) {
		this.subDownloadProperties[subdownloadpropertiesnumber] = subDownloadProperties;
	}


	
	
	
}

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class SubDownloadPropertiesFactory extends Thread {
	
	private int subDownloadNumber;
	private long firstOctet;
	private long size;
	private long downloaded = 0;
	private String fileType = null;
	private String filePathName;
	private SubDownloadPropertiesFactoriesManager subDownloadPropertiesFactoriesManager;
	private RequestProperties requestProperties;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private URL url;
	
	
	public SubDownloadPropertiesFactory(SubDownloadPropertiesFactoriesManager subdownloadpropertiesfactoriesmanager, int subdownloadnumber, long firstoctet, long size, String filetype, String filepathname, URL url, RequestProperties requestproperties) {
		// TODO Auto-generated constructor stub
		super();
		this.setSubDownloadPropertiesFactoriesManager(subdownloadpropertiesfactoriesmanager);
		this.setRequestProperties(requestproperties);
		this.setSubDownloadNumber(subdownloadnumber);
		this.setFirstOctet(firstoctet);
		this.setSize(size);
		this.setFileType(filetype);
		this.filePathName = filepathname + subdownloadnumber;
		this.setUrl(url);
		this.start();
	}
	
	
	public void run() {
		
		File file = new File(filePathName);
		long downloaded = checkFile(file);
		if(downloaded >= 0) {
			if(downloaded > 0) {
				this.setDownloaded(downloaded);
				this.setFirstOctet(firstOctet + downloaded);
			}
		}
		this.subDownloadPropertiesFactoriesManager.recordSubDownloadProperties(subDownloadNumber, new SubDownloadProperties(firstOctet, this.downloaded, size, this.getFileType(), file, url, requestProperties));
	}
	
	
	long checkFile(File file) {
		
		long retour = 0;//size already downloaded
		
		if(file.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
			byte[] buf = new byte[1024];
			int b;
			try {
				while((b = in.read(buf)) != -1) {
					retour += b;
				}
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return -2;
			}
		}
		return retour;
		
	}


	public int getSubDownloadNumber() {
		return subDownloadNumber;
	}


	public void setSubDownloadNumber(int subDownloadNumber) {
		this.subDownloadNumber = subDownloadNumber;
	}


	public long getFirstOctet() {
		return firstOctet;
	}


	public void setFirstOctet(long firstOctet) {
		this.firstOctet = firstOctet;
	}


	public long getSize() {
		return size;
	}


	public void setSize(long size) {
		this.size = size;
	}


	public long getDownloaded() {
		return downloaded;
	}


	public void setDownloaded(long downloaded) {
		this.downloaded = downloaded;
	}


	public String getFilePathName() {
		return filePathName;
	}


	public void setFilePathName(String filePathName) {
		this.filePathName = filePathName;
	}


	public SubDownloadPropertiesFactoriesManager getSubDownloadPropertiesFactoriesManager() {
		return subDownloadPropertiesFactoriesManager;
	}


	public void setSubDownloadPropertiesFactoriesManager(SubDownloadPropertiesFactoriesManager subDownloadPropertiesFactoriesManager) {
		this.subDownloadPropertiesFactoriesManager = subDownloadPropertiesFactoriesManager;
	}


	public InputStream getInputStream() {
		return inputStream;
	}


	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}


	public OutputStream getOutputStream() {
		return outputStream;
	}


	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}


	public URL getUrl() {
		return url;
	}


	public void setUrl(URL url) {
		this.url = url;
	}


	private void setRequestProperties(RequestProperties requestProperties) {
		this.requestProperties = requestProperties;
	}


	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	

}

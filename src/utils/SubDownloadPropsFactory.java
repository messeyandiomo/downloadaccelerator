package utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class SubDownloadPropsFactory extends Thread {
	
	private int subDownloadNumber;
	private long firstOctet;
	private long size;
	private long downloaded = 0;
	private String fileType = null;
	private String filePathName;
	private SubDownloadPropsFactoriesManager subDownloadPropsFactoriesManager;
	private InputStream inputStream = null;
	private FileOutputStream outputStream = null;
	private URL url;
	
	
	public SubDownloadPropsFactory(SubDownloadPropsFactoriesManager subdownloadpropsfactoriesmanager, int subdownloadnumber, long firstoctet, long size, String filetype, String filepathname, URL url) {
		// TODO Auto-generated constructor stub
		super();
		this.setSubDownloadPropsFactoriesManager(subdownloadpropsfactoriesmanager);
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
		this.subDownloadPropsFactoriesManager.recordSubDownloadProps(subDownloadNumber, new SubDownloadProps(firstOctet, size, this.getFileType(), file, url));
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


	public SubDownloadPropsFactoriesManager getSubDownloadPropsFactoriesManager() {
		return subDownloadPropsFactoriesManager;
	}


	public void setSubDownloadPropsFactoriesManager(SubDownloadPropsFactoriesManager subdownloadpropsfactoriesmanager) {
		this.subDownloadPropsFactoriesManager = subdownloadpropsfactoriesmanager;
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


	public void setOutputStream(FileOutputStream outputStream) {
		this.outputStream = outputStream;
	}


	public URL getUrl() {
		return url;
	}


	public void setUrl(URL url) {
		this.url = url;
	}


	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	

}
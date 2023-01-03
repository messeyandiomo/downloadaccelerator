import java.io.File;

public class DownloadDirectories {
	
	private String destinationDirectory = "";
	private String tempDirectory = "";
	private String cacheDirectory = "downloadaccelerator";
	private String ffmpeg = "ffmpeg";
	private static String imagesDirectory = "images" + System.getProperty("file.separator");
	private static String dacLogo = imagesDirectory + "daclogo.png";

	public DownloadDirectories() {
		// TODO Auto-generated constructor stub
		if(System.getProperty("os.name").matches("Linux")) {
        	this.setDestinationDirectory(System.getenv("HOME"));
        	this.setCacheDirectory(this.getDestinationDirectory() + "." + cacheDirectory);
        	this.setTempDirectory(this.getCacheDirectory() + "tmp");
        }
        else if(System.getProperty("os.name").matches("Windows")){
        	this.setDestinationDirectory(System.getenv("%HOMEPATH%"));
        	this.setCacheDirectory(this.getDestinationDirectory() + cacheDirectory);
        	this.setTempDirectory(System.getenv("%TEMP%"));
        }
		this.createCacheDirectory();
		this.createDirectory(this.getTempDirectory());
	}
	
	
	private synchronized void createCacheDirectory() {
		
		File theDir = new File(this.getCacheDirectory());
		if (!theDir.exists())
		    theDir.mkdirs();
	}
	
	private void createDirectory(String filepath) {
		
		File theDir = new File(filepath);
		if (!theDir.exists())
		    theDir.mkdirs();
	}
	
	public static String getImagesDirectory() {
		return imagesDirectory;
	}
	
	
	public String getDestinationDirectory() {
		return destinationDirectory;
	}


	public void setDestinationDirectory(String destinationdirectory) {
		this.destinationDirectory = destinationdirectory + System.getProperty("file.separator");
	}


	public String getTempDirectory() {
		return tempDirectory;
	}


	public void setTempDirectory(String tempdirectory) {
		this.tempDirectory = tempdirectory + System.getProperty("file.separator");
	}


	public static String getPathLogo() {
		return dacLogo;
	}


	public String getCacheDirectory() {
		return cacheDirectory;
	}
	
	
	private void setCacheDirectory(String cachedir) {
		this.cacheDirectory = cachedir + System.getProperty("file.separator");
	}


	public String getFfmpeg() {
		return ffmpeg;
	}
	

}

import java.io.File;

public class DownloadDirs {
	
	private static DownloadDirs instance = null;
	
	private String rootDir = "";
	private String destinationDir = "";
	private String tempDir = "";
	private String appName = "dwaccelerator";
	private String ffmpeg = "ffmpeg";
	private static String imagesDir = "images" + System.getProperty("file.separator");
	private static String dacLogo = imagesDir + "daclogo.png";

	private DownloadDirs() {
		// TODO Auto-generated constructor stub
		if(System.getProperty("os.name").matches("Linux")) {
			this.setRootDir(System.getenv("HOME"));
        	this.setDestinationDir(this.getRootDir() + appName);
        	this.setCacheDir(this.getRootDir() + "." + appName);
        	this.setTempDir(this.getCacheDir() + "tmp");
        }
        else if(System.getProperty("os.name").matches("Windows")){
        	this.setRootDir(System.getenv("%HOMEPATH%"));
        	this.setDestinationDir(this.getRootDir() + appName);
        	this.setCacheDir(this.getRootDir() + appName);
        	this.setTempDir(System.getenv("%TEMP%"));
        }
		this.createDir(this.getDestinationDir());
		this.createDir(this.getCacheDir());
		this.createDir(this.getTempDir());
	}
	
	
	public static DownloadDirs getInstance() {
		
		if(instance == null) {
			instance = new DownloadDirs();
		}
				
		return instance;
	}
	
	
	
	private void createDir(String filepath) {
		
		File theDir = new File(filepath);
		if (!theDir.exists())
		    theDir.mkdirs();
	}
	
	public static String getImagesDir() {
		return imagesDir;
	}
	
	private String getRootDir() {
		return rootDir;
	}

	private void setRootDir(String rootDir) {
		this.rootDir = rootDir + System.getProperty("file.separator");
	}
	
	
	public String getDestinationDir() {
		return destinationDir;
	}


	public void setDestinationDir(String destinationDir) {
		this.destinationDir = destinationDir + System.getProperty("file.separator");
	}


	public String getTempDir() {
		return tempDir;
	}


	private void setTempDir(String tempDir) {
		this.tempDir = tempDir + System.getProperty("file.separator");
	}


	public static String getPathLogo() {
		return dacLogo;
	}


	public String getCacheDir() {
		return appName;
	}
	
	
	private void setCacheDir(String dirpath) {
		this.appName = dirpath + System.getProperty("file.separator");
	}


	public String getFfmpeg() {
		return ffmpeg;
	}
	

}

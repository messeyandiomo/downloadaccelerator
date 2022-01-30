import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadDirectories {
	
	private String destinationDirectory = "";
	private String tempDirectory = "";
	private String cacheDirectory = "cacheofdac";
	private String ffmpeg = "ffmpeg";
	private boolean iamSnap = false;
	private static String imagesDirectory = "images" + System.getProperty("file.separator");
	private static String dacLogo = imagesDirectory + "daclogo.png";

	public DownloadDirectories() {
		// TODO Auto-generated constructor stub
		if(System.getProperty("os.name").matches("Linux")) {
        	this.setDestinationDirectory(System.getenv("HOME"));
        	this.setCacheDirectory(this.getDestinationDirectory() + "." + cacheDirectory);
        	this.setTempDirectory("/tmp");
        	
        	String nameRe = "[a-zA-Z_$][a-zA-Z_$0-9]*";
        	Pattern snapPattern = Pattern.compile(String.format("^\\/home\\/(?:%s)\\/snap\\/(?<snapname>%s)\\/(?<snaprevision>[a-zA-Z_$0-9.]+)(?:\\/)?$", nameRe, nameRe));
        	Matcher snapMatcher = snapPattern.matcher(this.getDestinationDirectory());
        	if(snapMatcher.find())
        		this.iamSnap = true;
        }
        else if(System.getProperty("os.name").matches("Windows")){
        	this.setDestinationDirectory(System.getenv("%HOMEPATH%"));
        	this.setCacheDirectory(this.getDestinationDirectory() + cacheDirectory);
        	this.setTempDirectory(System.getenv("%TEMP%"));
        }
		this.createCacheDirectory();
	}
	
	
	private synchronized void createCacheDirectory() {
		
		File theDir = new File(this.getCacheDirectory());
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
	
	
	public boolean isSnap() {
		return this.iamSnap;
	}


	public String getFfmpeg() {
		return ffmpeg;
	}
	
	/*
	private void setFfmpeg(String ffmpeg) {
		this.ffmpeg = ffmpeg;
	}
	*/


	/*public void setPathLogo(String pathlogo) {
		dacLogo = pathlogo;
	}*/

}

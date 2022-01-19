import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadDirectories {
	
	private String destinationDirectory = "";
	private String tempDirectory = "";
	private String cacheDirectory = "cacheofdac";
	private static String imagesDirectory = "images" + System.getProperty("file.separator");
	private static String dacLogo = imagesDirectory + "daclogo.png";

	public DownloadDirectories() {
		// TODO Auto-generated constructor stub
		if(System.getProperty("os.name").matches("Linux")) {
        	this.setDestinationDirectory(System.getenv("HOME"));
        	this.setCacheDirectory(this.getDestinationDirectory() + "." + cacheDirectory);
        	this.setTempDirectory("/tmp");
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


	public String getFfmpegPathName() {
		return ffmpegPathName;
	}


	private void setFfmpegPathName(String ffmpegPathName) {
		this.ffmpegPathName = ffmpegPathName;
	}


	/*public void setPathLogo(String pathlogo) {
		dacLogo = pathlogo;
	}*/

}

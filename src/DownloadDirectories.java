
public class DownloadDirectories {
	
	private String destinationDirectory = "";
	private String tempDirectory = "";
	private static String imagesDirectory = "images" + System.getProperty("file.separator");
	private static String dacLogo = imagesDirectory + "daclogo.png";

	public DownloadDirectories() {
		// TODO Auto-generated constructor stub
		if(System.getProperty("os.name").matches("Linux")) {
        	this.setDestinationDirectory(System.getenv("HOME"));;
        	this.setTempDirectory("/tmp");
        }
        else if(System.getProperty("os.name").matches("Windows")){
        	this.setDestinationDirectory(System.getenv("%HOMEPATH%"));;
        	this.setTempDirectory(System.getenv("%TEMP%"));
        }
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


	/*public void setPathLogo(String pathlogo) {
		dacLogo = pathlogo;
	}*/

}

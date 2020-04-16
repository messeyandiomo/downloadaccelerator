import java.io.IOException;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class DownloadControl {
	
	private static DownloadControl instance = null;
	private String imagesDirectory = "images" + System.getProperty("file.separator");
	private String homeDirectory = "";
	private String tempDirectory = "";
	private String pathLogo = imagesDirectory + "daclogo.png";
	

	private DownloadControl() {
		// TODO Auto-generated constructor stub
		if(System.getProperty("os.name").matches("Linux")) {
        	this.homeDirectory = System.getenv("HOME");
        	this.tempDirectory = "/tmp";
        }
        else if(System.getProperty("os.name").matches("Windows")){
        	this.homeDirectory = System.getenv("%HOMEPATH%");
        	this.tempDirectory = System.getenv("%TEMP%");
        }
	}
	
	
	public static DownloadControl getInstance() {
		
		if(instance == null) {
			instance = new DownloadControl();
		}
		
		return instance;
	}
	
	public int requestFileProperties(URL url, FileProperties properties) {
		
		int retour = -1;
		HttpURLConnection connexion = null;
		
		try {
			connexion = (HttpURLConnection) url.openConnection();
			connexion.setRequestMethod("HEAD");
			try {
				int response = connexion.getResponseCode();
				if(response == HttpURLConnection.HTTP_OK) {
					connexion.getInputStream();
					properties.setSize(connexion.getContentLengthLong());
					if(connexion.getContentType() != null) {
						properties.setType(connexion.getContentType().split(";")[0].split("/")[0]);
						properties.setSubType(connexion.getContentType().split(";")[0].split("/")[1]);
					}
				}
				retour = response;
			}catch(IOException e) {
				retour = -2;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			connexion.disconnect();
		}
		
		return retour;
	}
	
	
	public Download launchDownload(DownloadWindow downloadwindow, URL url, String destination, String temp, String filename, int subDownloadCount) {
		
		Download retour = null;
		Download telechargement = new Download(downloadwindow, url, destination, temp, filename, subDownloadCount);
		if(telechargement != null) {
			retour = telechargement;
		}
		return retour;
	}
	
	
	//conversion des bytes en kilo, mega ou gigabytes
	public String convertSize(long fileSize) {
		
		String retour = "0";
		double size = (double)fileSize;
		DecimalFormatSymbols dcmfs = new DecimalFormatSymbols();
		dcmfs.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("0.0");
		df.setRoundingMode(RoundingMode.HALF_UP);
		df.setDecimalFormatSymbols(dcmfs);
		
		if(fileSize < 0) {
			retour = "?";
		}
		else if(fileSize >= 0 && fileSize < 1024) {
			retour = fileSize + "o";
		}
		else if(fileSize >= 1024 && fileSize < 1048576) {
			retour = df.format(size/1024) + "ko";
		}
		else if(fileSize >= 1048576 && fileSize < 1073741824) {
			retour = df.format(size/1048576) + "Mo";
		}
		else if(fileSize >= 1073741824) {
			retour = df.format(size/1073741824) + "Go";
		}
		
		return retour;
	}
	
	//repertoire des images
	public String getImagesDirectory() {
		
		return this.imagesDirectory;
	}
	
	//repertoire de destination(HOME)
	public String getHomeDirectory() {
		
		return this.homeDirectory;
	}
	
	//repertoire de fichiers temporaires
	public String getTempDirectory() {
		
		return this.tempDirectory; 
		
	}
	//chemin du logo
	public String getPathLogo() {
		
		return this.pathLogo;
	}
	
}

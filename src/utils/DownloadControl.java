package utils;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class DownloadControl {
	

	public DownloadControl() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	/*** conversion des bytes en kilo, mega ou gigabytes ***/
	public static String convertSize(long fileSize) {
		
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
	
	
	/*** generate final output filename ***/
	public static String generateFilename(String dirpath, String filename) {
		
		String finalfilename = filename;
		String basename = "";
		String extension = "";
		int i = 1;
		int lastindex = filename.lastIndexOf('.');
		if(lastindex > 0 && lastindex < filename.length()) {
			basename = filename.substring(0, lastindex);
			extension = filename.substring(lastindex);
		}
		
		while (new File(dirpath + finalfilename).exists()) {
			finalfilename = basename + i + extension;
			i++;
		}
		
		return finalfilename;
	}
}

package utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SubDownloadProps {
	
	private long firstOctet;
	private long downloaded;
	private long size;
	private File file;
	private String filetype = null;
	private URL url;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	
	

	public SubDownloadProps(long firstoctet, long downloaded, long size, String filetype, File file, URL url) {
		// TODO Auto-generated constructor stub
		this.setFirstOctet(firstoctet);
		this.setDownloaded(downloaded);
		this.setSize(size);
		this.setFile(file);
		this.setUrl(url);
		this.setFiletype(filetype);
		if(this.downloaded != this.size) {
			this.createInputStream();
			this.createOutputStream();
		}
	}
	
	
	
	
	int tryToCreateInputStream() {
		
		HttpURLConnection connexion = null;
		
		try {
			connexion = (HttpURLConnection) (url.openConnection());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return -1;
		}
		connexion.setRequestProperty("Range", "bytes=" + firstOctet + "-" + (firstOctet + size - downloaded - 1));
		connexion.setConnectTimeout(10000);
		connexion.setReadTimeout(10000);
		int response = 0;
		try {
			response = connexion.getResponseCode();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return -2;
		}
		if(response == HttpURLConnection.HTTP_PARTIAL) {
			InputStream in = null;
			try {
				in = connexion.getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				return -3;
			}
			this.setInputStream(in);
		}
		
		return response;
	}
	
	
	public boolean createInputStream() {
		
		return (HttpURLConnection.HTTP_PARTIAL == tryToCreateInputStream());
	}
	
	
	public boolean createOutputStream() {
		
		boolean response = false;
		
		try {
			setOutputStream(new FileOutputStream(file, true));
			response = true;
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
		}
		
		return response;
	}
	

	public long getFirstOctet() {
		return firstOctet;
	}

	public void setFirstOctet(long firstoctet) {
		this.firstOctet = firstoctet;
	}

	public long getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(long downloaded) {
		this.downloaded = downloaded;
	}
	
	public void update(long downloaded) {
		this.downloaded += downloaded;
		this.firstOctet += downloaded;
	}
	

	public long getSize() {
		return size;
	}
	

	public void setSize(long size) {
		this.size = size;
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

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	
	
	public boolean isAudio() {
		return this.getFiletype().contains("audio");
	}
	
	
	public boolean isVideo() {
		return this.getFiletype().contains("video");
	}


	public String getFiletype() {
		return filetype;
	}
	
	
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

}
package utils;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class DownloadProps {
	
	private URL url;
	private long size = 0;
	private String type = "";
	private String subtype = "";
	private String filename = "";
	private String fileLogo = "";
	private String error = "";
	
	private int subDownloadCount = 10;
	private SubDownloadProps[] subDownloadProps = null;
	
	
	public DownloadProps(String urlstring) {
		// TODO Auto-generated constructor stub
		try {
			this.url = new URL(urlstring);
			HttpURLConnection connexion = null;
			int response = 0;
			try {
				connexion = (HttpURLConnection) this.url.openConnection();
				String maxConnections = connexion.getRequestProperty("http.maxConnections");
				if(maxConnections != null)
					this.setSubDownloadCount(Integer.parseInt(maxConnections));
				
				connexion.setRequestMethod("HEAD");
				try {
					response = connexion.getResponseCode();
					if(response == HttpURLConnection.HTTP_OK) {
						connexion.getInputStream();
						long contentLength = connexion.getContentLengthLong();
						if(contentLength == -1) {
							//the server refuse to give us file size
							this.setSubDownloadCount(1);
						}
						this.setSize(contentLength);
						if(connexion.getContentType() != null) {
							this.setType(connexion.getContentType().split(";")[0].split("/")[0]);
							this.setSubType(connexion.getContentType().split(";")[0].split("/")[1]);
							this.setFilename(decodeString(Paths.get(this.url.getPath()).getFileName().toString()));
							this.setFileLogo(type, subtype);
						}
						this.subDownloadProps = new SubDownloadProps[subDownloadCount];
					}
					else if(response == HttpURLConnection.HTTP_UNAVAILABLE) {
						this.setError("Service indisponible !!!");
					}
					else if(response == HttpURLConnection.HTTP_NOT_FOUND) {
						this.setError("Fichier non trouvé !!!");
					}
					else if(response == HttpURLConnection.HTTP_UNAUTHORIZED) {
						this.setError("Accès au fichier non autorizé !!!");
					}
				}catch(IOException e) {
					this.setError("Réseau indisponible !!!");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				this.setError("Pas de connexion !!!");
			}
			finally {
				connexion.disconnect();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			this.setError("Url mal formée !!!");
		}
	}
	
	public DownloadProps(String urlstring, String filename, String filetype, String filesubtype, long filesize) {
		// TODO Auto-generated constructor stub
		try {
			this.url = new URL(urlstring);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			this.setError("Url mal formée !!!");
		}
		this.setFilename(filename);
		this.setType(filetype);
		this.setSubType(filesubtype);
		this.setSize(filesize);
		this.setFileLogo(filetype, filesubtype);
		
		this.subDownloadProps = new SubDownloadProps[subDownloadCount];
		
	}
	
	public DownloadProps(String urlaudiostring, String filename, long filesizeaudio) {
		// TODO Auto-generated constructor stub
		this(urlaudiostring, filename, null, null, filesizeaudio);
	}
	
	
	
	// Decodes a URL encoded string using UTF-8
	String decodeString(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			return value;//e1.printStackTrace();
		}
	}
	
	public void setSize(long filesize) {
		this.size = filesize;
	}
	
	public void setType(String filetype) {
		this.type = filetype;
	}
	
	public void setSubType(String filesubtype) {
		this.subtype = filesubtype;
	}
	
	public long getSize() {
		return this.size;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getSubType() {
		return this.subtype;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}


	public String getFileLogo() {
		return fileLogo;
	}


	private void setFileLogo(String type, String subtype) {
		String filelogo = "inconnu";
		String imagesDirectory = DownloadDirs.getImagesDir();
		if(!type.isEmpty()) {
			File dir = new File(imagesDirectory);
			File[] fileslist = dir.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return (name.contains(type) || (!subtype.isEmpty() && name.contains(subtype)));
				}
			});
			if(fileslist.length >= 2) {
				filelogo = subtype;
			}
			else if(fileslist.length == 1 && fileslist[0].getName().contains(type)) {
				filelogo = type;
			}
		}
		filelogo += ".jpg";
		this.fileLogo = imagesDirectory + filelogo;
	}


	public String getError() {
		return error;
	}


	public void setError(String error) {
		this.error = error;
	}
	
	
	public boolean hasError() {
		boolean retour = true;
		if(this.error.isEmpty())
			retour = false;
		return retour;
	}
	
	
	/******************
	 * 
	 * @return
	 */
	public int getSubDownloadCount() {
		return subDownloadCount;
	}


	public void setSubDownloadCount(int subdownloadcount) {
		this.subDownloadCount = subdownloadcount;
	}


	public SubDownloadProps getSubDownloadProps(int subdownloadpropsnumber) {
		return subDownloadProps[subdownloadpropsnumber];
	}


	public void setSubDownloadProps(int subdownloadpropsnumber, SubDownloadProps subdownloadprops) {
		this.subDownloadProps[subdownloadpropsnumber] = subdownloadprops;
	}
	
	/****
	 * creation of sub download properties
	 * @param 
	 * @return
	 */
	public void createSubDownloadProps(SubDownloadPropsFactoriesManager subdownloadpropsfactoriesmanager) {
		
		String temporaryfilename = DownloadDirs.getInstance().getTempDir() + this.getFilename();
		long restOfSize = this.getSize()%this.getSubDownloadCount();//surplus of bytes
		long subdownloadsize = (this.getSize() - restOfSize)/this.getSubDownloadCount();//size of sub-download
		long firstoctet = 0;
		
		for (int i = 0; i < this.getSubDownloadCount(); i++) {
			if(i == 0) {
				new SubDownloadPropsFactory(subdownloadpropsfactoriesmanager, i, firstoctet, subdownloadsize + restOfSize, this.getType(), temporaryfilename, this.getUrl());
				firstoctet = (subdownloadsize + restOfSize);
			}
			else {
				new SubDownloadPropsFactory(subdownloadpropsfactoriesmanager, i, firstoctet, subdownloadsize, this.getType(), temporaryfilename, this.getUrl());
				firstoctet += subdownloadsize;
			}
		}
	}
	
	/***
	 * check if download file exist
	 */
	public boolean fileDownloadedExist() {
		
		return new File(DownloadDirs.getInstance().getTempDir() + this.getFilename() + "0").exists();
	}
	
	/***
	 * generate another filename
	 */
	public String generateAnotherFileName() {
		
		this.setFilename(this.getFilename() + "bis");
		return DownloadDirs.getInstance().getTempDir() + this.getFilename();
	}
}
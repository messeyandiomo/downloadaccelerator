import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SubDownload extends Thread {
	
	private String filename = "";
	private String temp = "";
	private URL url;
	private int subdownloadnumber;
	private long first;
	private long size;
	private Download download;
	private InfosManager infosManager;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private boolean continuer = true;
	private boolean annuler = false;
	
	
	public SubDownload(Download telechargement, InfosManager infosmanager, int subdownloadnumber, long first, long size) {
		// TODO Auto-generated constructor stub
		super();
		this.setDownload(telechargement);
		setInfoManager(infosmanager);
		this.setFilename(telechargement.getFilename());
		this.setTemp(telechargement.getTemp());
		this.setUrl(telechargement.getUrl());
		this.setSubdownloadnumber(subdownloadnumber);
		this.setFirst(first);
		this.setSize(size);
		this.start();
		
	}
	
	
	public SubDownload(Download telechargement, InfosManager infosmanager, int subdownloadnumber) {
		super();
		this.setDownload(telechargement);
		setInfoManager(infosmanager);
		this.setFilename(telechargement.getFilename());
		this.setFirst(0);
		this.setSize(0);
		this.setTemp(telechargement.getTemp());
		this.setUrl(telechargement.getUrl());
		this.setSubdownloadnumber(subdownloadnumber);
		this.start();
	}
	
	
	public void run() {
		
		File file = new File(temp + filename + subdownloadnumber);
		boolean testAnnuler;
		long downloadedByteCount;
		
		do {
			downloadedByteCount = checkFile(file);
		}while(downloadedByteCount < 0);
		if(downloadedByteCount >= 0) {
			if(downloadedByteCount > 0) {
				download.getDownloadWindow().getSubDownloadProgressBar(subdownloadnumber).update(downloadedByteCount);
				download.wakeUp(downloadedByteCount);
			}
			setFirst(downloadedByteCount + first);
			if(size <= 0)
				requestEntireFile(file);
			else {
				if(downloadedByteCount < size) {
					setSize(size - downloadedByteCount);
					requestSplitedFile(file);
				}
			}
			testAnnuler = checkCancel();
			if(!testAnnuler)
				download.endOfSubDownload(subdownloadnumber, file);
		}
	}
	
	//vérification de l'existence du file à télécharger et récupération de sa taille
	long checkFile(File file) {
		
		long retour = 0;//size already downloaded
		
		if(file.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return -1;
			}
			byte[] buf = new byte[1024];
			int b;
			try {
				while((b = in.read(buf)) != -1) {
					retour += b;
				}
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
				return -2;
			}
		}
		return retour;
		
	}
	
	
	private void requestEntireFile(File file) {
		
		createInputStream();
		createOutputStream(file);
		byte[] buf;
		do {
			buf = new byte[1024];
		}while(buf == null);
		int b = 0;
		boolean testAnnuler;
		long downloadedByteCount = 0;
		boolean finish = false;
		while(!finish) {
			try {
				while((b = inputStream.read(buf)) > 0) {
					synchronized (this) {
						outputStream.write(buf, 0, b);
					}
					downloadedByteCount += b;
					download.getDownloadWindow().getSubDownloadProgressBar(subdownloadnumber).update(b);
					download.wakeUp(b);
					infosManager.update(b);
					synchronized (this) {
						while(!continuer && !annuler) {
							try {
								wait();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
							}
						}
					}
					testAnnuler = checkCancel();
					if(testAnnuler) {
						finish = true;
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				setFirst(downloadedByteCount);
				createInputStream();
			}
			if(b == 0)
				finish = true;
		}
		synchronized (this) {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	private void requestSplitedFile(File file) {
		
		createInputStream();
		createOutputStream(file);
		byte[] buf;
		do {
			buf = new byte[1024];
		}while(buf == null);
		int b = 0;
		long remainByteCount = size;
		boolean testAnnuler;
		while(remainByteCount > 0) {
			try {
				b = inputStream.read(buf);
				if(b > 0) {
					if(remainByteCount < b) {
						synchronized (this) {
							outputStream.write(buf, 0, (int) remainByteCount);
						}
						download.getDownloadWindow().getSubDownloadProgressBar(subdownloadnumber).update(remainByteCount);
						download.wakeUp(remainByteCount);
						infosManager.update(remainByteCount);
						remainByteCount = 0;
					}
					else {
						synchronized (this) {
							outputStream.write(buf, 0, b);
						}
						download.getDownloadWindow().getSubDownloadProgressBar(subdownloadnumber).update(b);
						download.wakeUp(b);
						infosManager.update(b);
						remainByteCount -= b;
					}
				}
			}catch (IOException e) {
				// TODO: handle exception
				this.setFirst(first + size - remainByteCount);
				this.setSize(remainByteCount);
				createInputStream();
			}
			synchronized (this) {
				while(!continuer && !annuler) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					}
				}
			}
			testAnnuler = checkCancel();
			if(testAnnuler) {
				break;
			}
		}
		synchronized (this) {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}


	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}


	/**
	 * @return the temp
	 */
	public String getTemp() {
		return temp;
	}


	/**
	 * @param temp the temp to set
	 */
	public void setTemp(String temp) {
		this.temp = temp;
	}


	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}


	/**
	 * @param url the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}


	/**
	 * @return the subdownloadnumber
	 */
	public int getSubdownloadnumber() {
		return subdownloadnumber;
	}


	/**
	 * @param subdownloadnumber the subdownloadnumber to set
	 */
	public void setSubdownloadnumber(int subdownloadnumber) {
		this.subdownloadnumber = subdownloadnumber;
	}


	/**
	 * @return the first
	 */
	public long getFirst() {
		return first;
	}


	/**
	 * @param first the first to set
	 */
	public void setFirst(long first) {
		this.first = first;
	}


	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}


	/**
	 * @param size the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}


	/**
	 * @return the download
	 */
	public Download getDownload() {
		return download;
	}


	/**
	 * @param download the download to set
	 */
	public void setDownload(Download download) {
		this.download = download;
	}
	
	
	/**
	 * @return the infoManager
	 */
	public InfosManager getInfoManager() {
		return infosManager;
	}


	/**
	 * @param infoManager the infoManager to set
	 */
	public void setInfoManager(InfosManager infosManager) {
		this.infosManager = infosManager;
	}
	
	
	//obtention de l'état annuler
	private synchronized boolean checkCancel() {
		
		return annuler;
	}


	//suspension du sous téléchargement
	public synchronized void suspend2() {
		continuer = false;
	}
	
	
	//reprise du sous téléchargement précédemment suspendu
	public synchronized void resume2() {
		continuer = true;
		notify();
	}
	
	
	//annulation du sous téléchargement
	public synchronized void stop2() {
		annuler = true;
		this.outputStream = null;
		notify();
	}



	/**
	 * tentative de création de l'inputstream
	 */
	private int tryToCreateInputStream() {
		
		HttpURLConnection connexion = null;
		
		try {
			connexion = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return -1;
		}
		if(size > 0)
			connexion.setRequestProperty("Range", "bytes=" + first + "-" + (first + size - 1));
		else
			connexion.setRequestProperty("Range", "bytes=" + first + "-");
		int response;
		try {
			response = connexion.getResponseCode();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return -2;
		}
		if(response == HttpURLConnection.HTTP_PARTIAL) {
			InputStream in;
			try {
				in = connexion.getInputStream();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				return -3;
			}
			this.inputStream = in;
		}
		return response;
	}
	
	
	/**
	 * remplacement de l'inputstream
	 */
	private void createInputStream() {
		int request;
		do {
			request = tryToCreateInputStream();
		}while(request != HttpURLConnection.HTTP_PARTIAL);
	}



	/**
	 * @param file the file from outputStream is generated
	 */
	private void createOutputStream(File file) {
		boolean response;
		do {
			try {
				synchronized (this) {
					outputStream = new FileOutputStream(file, true);
				}
				response = true;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
				response = false;
			}
		}while(!response);
	}
}

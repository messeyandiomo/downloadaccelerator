import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class Download extends Thread {
	
	private int subDownloadEndedCount = 0;
	private int subDownloadCount = 0;
	private String filename = "";
	private String filesize = "";
	private String temp = "";
	private String destination = "";
	private URL url;
	private DownloadWindow downloadwindow;
	private SubDownload[] subDownloadSet;
	private InfosManager infosManager;
	private StateManager stateManager;
	private File[] subDownloadFile;
	private File fileDownloaded;
	
	private boolean update = true;
	private long currentSize = 0;
	
	public Download(DownloadWindow downloadwindow, URL url, String destination, String temp, String filename, int subDownloadCount) {
		// TODO Auto-generated constructor stub
		super();
		this.downloadwindow = downloadwindow;
		this.setUrl(url);
		this.setDestination(destination);
		this.setTemp(temp);
		this.setFilename(filename);
		this.subDownloadCount = subDownloadCount;
		subDownloadFile = new File[subDownloadCount];
		this.start();
	}
	
	public void run() {
		
		subDownloadSet = new SubDownload[subDownloadCount];
		infosManager = new InfosManager(this);
		if(this.getDownloadWindow().getFilesize() <= 0) {
			subDownloadSet[0] = new SubDownload(this, infosManager, 0);
		}
		else {
			long firstoctet = 0, subdownloadsize;
			for(int subdownloadnumber = 0; subdownloadnumber < subDownloadCount; subdownloadnumber++) {
				subdownloadsize = downloadwindow.getSubDownloadProgressBar(subdownloadnumber).getFilesize();
				subDownloadSet[subdownloadnumber] = new SubDownload(this, infosManager, subdownloadnumber, firstoctet, subdownloadsize);
				firstoctet += subdownloadsize;
			}
		}
		stateManager = new StateManager(this);
		//attente de la fin des sous téléchargements
		waitForSubDownloads();
		int sousTelTermine = getSubDownloadEndedCount();
		if(subDownloadCount == sousTelTermine) {
			infosManager.stop2();//interruption du manager d'informations(vitesse et durée)
			while(infosManager.isAlive()) yield();
			downloadwindow.prepareForConcat();
			fileDownloaded = new File(destination + filename);
			try {
				OutputStream out = new FileOutputStream(fileDownloaded);
				InputStream in;
				byte[] buf = new byte[1024];
				int b = 0;
				for(int i = 0; i < subDownloadCount; i++) {
					//concatenation des fichiers téléchargés
					in = new FileInputStream(subDownloadFile[i]);
					try {
						while((b = in.read(buf)) > 0) {
							out.write(buf, 0, b);
							downloadwindow.getFileConcatProgressBar().update(b);//mise à jour de la barre de concaténation
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						in.close();
						subDownloadFile[i].delete();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					out.close();
					downloadwindow.setEndOf(filesize);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stateManager.stop2();
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
	 * @return the destination
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(String destination) {
		this.destination = destination;
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
	 * @return the subDownloadCount
	 */
	public int getSubDownloadCount() {
		return subDownloadCount;
	}

	/**
	 * @param subDownloadCount the subDownloadCount to set
	 */
	public void setSubDownloadCount(int subDownloadCount) {
		this.subDownloadCount = subDownloadCount;
	}
	
	/**
	 * @return update
	 */
	public synchronized boolean getUpdate() {
		return update;
	}
	
	
	
	//obtention du sous téléchargement i
	public SubDownload getSubDownload(int i) {
		return this.subDownloadSet[i];
	}
	
	
	//obtenir infoManager
	public InfosManager getInfosManager() {
		return infosManager;
	}

	/**
	 * @return the currentSize
	 */
	public synchronized long getCurrentSize() {
		return currentSize;
	}

	
	public DownloadWindow getDownloadWindow() {
		return downloadwindow;
	}
	
	
	public synchronized int getSubDownloadEndedCount() {
		return subDownloadEndedCount;
	}
	
	
	private void waitForSubDownloads() {
		filesize = DownloadControl.getInstance().convertSize(downloadwindow.getFilesize());
		boolean testupdate = getUpdate();
		while(testupdate) {
			try {
				synchronized (this) {
					wait();
				}
				this.getDownloadWindow().getDownloadWindowCurrentSizeInfos().setText(getInfos());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			testupdate = getUpdate();
		}
	}
	
	//incrémentation du nombre de sous téléchargement terminés
	private synchronized int incrementSubDownloadEndedCount() {
		int retour = ++subDownloadEndedCount;
		if(retour == subDownloadCount) {
			update = false;
			notify();
		}
		return retour;
	}
	
	
	public synchronized void freeDownload() {
		update = false;
		notify();
	}
	
	
	public void endOfSubDownload(int subdownloadnumber, File file) {
		
		subDownloadFile[subdownloadnumber] = file;
		/*int currentsubDownloadEndedCount = */incrementSubDownloadEndedCount();
		/*if(currentsubDownloadEndedCount == subDownloadCount) {
			freeDownload();
		}*/
	}
	
	
	//suspension du téléchargement
	public void suspend2() {
		stateManager.suspend2();
	}
	
	
	//reprise du téléchargement précédemment suspendu
	public void resume2() {
		stateManager.resume2();
	}
	
	
	//annulation du téléchargement
	public void stop2() {
		stateManager.stop2();
	}
	
	
	//mise à jour de la taille du téléchargement
	public synchronized void wakeUp(long sizeAdded) {
		currentSize += sizeAdded;
		notify();
	}
	
	
	//mise en place de l'information sur le téléchargement
	private synchronized String getInfos() {
		String retour = DownloadControl.getInstance().convertSize(currentSize) + "/" + filesize;
		return retour;
	}
	
}

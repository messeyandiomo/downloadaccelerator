import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Download extends Thread implements Observable {
	
	/**********
	 * 
	 */
	private DownloadDirectories downloadDirectories;
	private DownloadProperties downloadProperties;
	private String fileName;
	private StatisticsManager staticsticsManager;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	private File[] filesOfSubDownloads;
	private File downloadedFile;
	private long downloaded = 0;
	private int numberOfSubDownloadsCompleted;
	private int numberOfSubDownloadsNotCompleted;
	private int numberOfSubDownloads;
	private boolean complete = false;
	
	
	
	/*************
	 * 
	 * @param statisticsmanager
	 * @param fileproperties
	 * @param downloadproperties
	 */
	public Download(StatisticsManager statisticsmanager, DownloadProperties downloadproperties, DownloadDirectories downloaddirectories, String filename) {
		super();
		this.setDownloadDirectories(downloaddirectories);
		this.setDownloadProperties(downloadproperties);
		this.setStaticsticsManager(statisticsmanager);
		this.setFileName(filename);
		this.numberOfSubDownloads = downloadProperties.getSubDownloadCount();
		filesOfSubDownloads = new File[numberOfSubDownloads];
		this.start();
	}
	
	public void run() {
		boolean iscompleted = false;
		while(true) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			iscompleted = isCompleted();
			if(iscompleted)
				break;
			else {
				this.staticsticsManager.pause();
				while(!staticsticsManager.isSuspended()) yield();
				this.updateObserver();
			}
		}
		int numberofcomplete = this.getNumberOfSubDownloadsCompleted();
		if(numberofcomplete == numberOfSubDownloads) {
			staticsticsManager.complete();
			while(staticsticsManager.isAlive()) yield();
			downloadedFile = new File(downloadDirectories.getDestinationDirectory() + fileName);
			try {
				OutputStream out = new FileOutputStream(downloadedFile);
				InputStream in = null;
				byte[] buf = new byte[1024];
				int b = 0;
				for (int i = 0; i < filesOfSubDownloads.length; i++) {
					in = new FileInputStream(filesOfSubDownloads[i]);
					try {
						while((b = in.read(buf)) > 0) {
							out.write(buf, 0, b);
							this.setDownloaded(b);
							this.updateObserver();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						in.close();
						filesOfSubDownloads[i].delete();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*********
	 * 
	 */
	
	
	/***************
	 * 
	 */

	@Override
	public void addObserver(Observer obs) {
		// TODO Auto-generated method stub
		this.listObserver.add(obs);
	}

	@Override
	public void updateObserver() {
		// TODO Auto-generated method stub
		boolean iscompleted = this.isCompleted();
		long downloaded = this.getDownloaded();
		for(Observer obs : this.listObserver)
			obs.update(iscompleted, downloaded);
	}

	@Override
	public void delObserver() {
		// TODO Auto-generated method stub
		this.listObserver = new ArrayList<Observer>();
	}


	public DownloadDirectories getDownloadDirectories() {
		return downloadDirectories;
	}

	public void setDownloadDirectories(DownloadDirectories downloaddirectories) {
		this.downloadDirectories = downloaddirectories;
	}

	public DownloadProperties getDownloadProperties() {
		return downloadProperties;
	}

	public void setDownloadProperties(DownloadProperties downloadproperties) {
		this.downloadProperties = downloadproperties;
	}

	public StatisticsManager getStaticsticsManager() {
		return staticsticsManager;
	}

	public void setStaticsticsManager(StatisticsManager staticsticsManager) {
		this.staticsticsManager = staticsticsManager;
	}

	private synchronized int getNumberOfSubDownloadsCompleted() {
		return numberOfSubDownloadsCompleted;
	}
	
		
	public synchronized void reset() {
		numberOfSubDownloadsCompleted = 0;
		numberOfSubDownloadsNotCompleted = 0;
	}
	

	public synchronized void notifyComplete(int subdownloadnumber, File file) {
		filesOfSubDownloads[subdownloadnumber] = file;
		this.numberOfSubDownloadsCompleted++;
		if(numberOfSubDownloadsCompleted == numberOfSubDownloads)
			this.complete = true;
		if((numberOfSubDownloadsCompleted + numberOfSubDownloadsNotCompleted) == numberOfSubDownloads)
			notify();
	}


	public synchronized void notifyNotComplete() {
		this.numberOfSubDownloadsNotCompleted++;
		if((numberOfSubDownloadsCompleted + numberOfSubDownloadsNotCompleted) == numberOfSubDownloads)
			notify();
	}
	
	private synchronized boolean isCompleted() {
		return complete;
	}
	

	private long getDownloaded() {
		return downloaded;
	}
	

	private void setDownloaded(long downloaded) {
		this.downloaded += downloaded;
	}
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}

package utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import gui.Observable;
import gui.Observer;

public class Download extends Thread implements Observable {
	
	/**********
	 * 
	 */
	private DownloadProps downloadProps = null;
	private StatisticsManager staticsticsManager;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	private ArrayList<Integer> subdownloadsNumbersNotComplete = null;
	private File[] filesOfSubDownloads;
	private File downloadedFile;
	private long downloaded = 0;
	private int numberOfSubDownloadsCompleted;
	private int numberOfSubDownloadsNotCompleted;
	private int numberOfSubDownloads;
	private boolean complete = false;
	private boolean suspend = false;
	private int attempt = 0;
	
	
	/*************
	 * 
	 * @param statisticsmanager
	 * @param downloadprops
	 */
	public Download(StatisticsManager statisticsmanager, DownloadProps downloadprops) {
		super();
		this.setDownloadProps(downloadprops);
		this.setStaticsticsManager(statisticsmanager);
		this.setAttempt(this.getDownloadProps().getSubDownloadCount());
		this.numberOfSubDownloads = downloadProps.getSubDownloadCount();
		filesOfSubDownloads = new File[numberOfSubDownloads];
		for (int i = 0; i < numberOfSubDownloads; i++)
			filesOfSubDownloads[i] = null;
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
				if(this.getAttempt() == 0) {
					this.subdownloadsNumbersNotComplete = null;
					this.setSuspend(true);
					this.staticsticsManager.pause();
					while(!staticsticsManager.isSuspended()) Thread.yield();
				}
				else {
					this.setAttempt(this.getAttempt() - 1);
					this.setSuspend(false);
					this.resetNumberOfSubDownloadNotComplete();
					this.subdownloadsNumbersNotComplete = new ArrayList<>();
					for (int i = 0; i < filesOfSubDownloads.length; i++) {
						if(filesOfSubDownloads[i] == null) {
							this.subdownloadsNumbersNotComplete.add(i);
						}
					}
				}
				this.updateObserver();
			}
		}
		int numberofcomplete = this.getNumberOfSubDownloadsCompleted();
		if(numberofcomplete == numberOfSubDownloads) {
			staticsticsManager.complete();
			while(staticsticsManager.isAlive()) Thread.yield();
			downloadProps.setFilename(DownloadControl.generateFilename(DownloadDirs.getInstance().getDestinationDir(), downloadProps.getFilename()));
			downloadedFile = new File(DownloadDirs.getInstance().getDestinationDir() + downloadProps.getFilename());
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
		ArrayList<Integer> cloneofsubdownloadsnumbersnotcomplete = null;
		if(subdownloadsNumbersNotComplete != null) {
			cloneofsubdownloadsnumbersnotcomplete = new ArrayList<>();
			for (int i = 0; i < subdownloadsNumbersNotComplete.size(); i++) {
				cloneofsubdownloadsnumbersnotcomplete.add(subdownloadsNumbersNotComplete.get(i));
			}
		}
		boolean iscompleted = this.isCompleted();
		boolean issuspended = this.isSuspend();
		long downloaded = this.getDownloaded();
		
		for(Observer obs : this.listObserver)
			obs.update(iscompleted, issuspended, cloneofsubdownloadsnumbersnotcomplete, downloaded);
	}
	

	@Override
	public void delObserver() {
		// TODO Auto-generated method stub
		this.listObserver = new ArrayList<Observer>();
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
		complete = false;
		suspend = false;
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
	
	
	private synchronized void resetNumberOfSubDownloadNotComplete() {
		this.numberOfSubDownloadsNotCompleted = 0;
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


	public DownloadProps getDownloadProps() {
		return downloadProps;
	}


	public void setDownloadProps(DownloadProps downloadProps) {
		this.downloadProps = downloadProps;
	}



	public int getAttempt() {
		return attempt;
	}



	public void setAttempt(int attempt) {
		this.attempt = attempt;
	}



	public boolean isSuspend() {
		return suspend;
	}



	public void setSuspend(boolean suspend) {
		this.suspend = suspend;
	}
	
}

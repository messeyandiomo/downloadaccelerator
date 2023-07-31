package utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import gui.Observable;
import gui.Observer;

public class Download extends Thread implements Observable {
	
	/**********
	 * 
	 */
	private DownloadProps downloadProps = null;
	private StatisticsManager statisticsManager;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	private ArrayList<Integer> notCompleteIds = new ArrayList<Integer>();
	private File[] filesOfSubDownloads;
	private File downloadedFile;
	private long downloaded = 0;
	private int numberOfSubDownloadsCompleted = 0;
	private int numberOfSubDownloadsNotCompleted = 0;
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
			System.out.println("i am download and i wake up for the " + (this.numberOfSubDownloads - this.getAttempt()) + "th time");
			iscompleted = isCompleted();
			if(iscompleted)
				break;
			else {
				synchronized (this) {
					if(this.getAttempt() == 0) {
						suspend = true;
						this.statisticsManager.pause();
						while(!statisticsManager.isSuspended()) Thread.yield();
						attempt = this.getDownloadProps().getSubDownloadCount();
					}
					else {
						this.setAttempt(this.getAttempt() - 1);
						suspend = false;
						this.statisticsManager.reset();
						System.out.println("Attempt " + this.getAttempt());
					}
					this.reset();
					this.updateObserver();
				}
			}
		}
		int numberofcomplete = this.getNumberOfSubDownloadsCompleted();
		if(numberofcomplete == numberOfSubDownloads) {
			System.out.println("Begining of sub download files concatenation");
			statisticsManager.complete();
			while(statisticsManager.isAlive()) Thread.yield();
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
		boolean iscompleted = this.isCompleted();
		long downloaded = this.getDownloaded();
		
		for(Observer obs : this.listObserver)
			obs.update(iscompleted, suspend, downloaded);
	}
	
	private void reset() {
		numberOfSubDownloadsCompleted = 0;
		numberOfSubDownloadsNotCompleted = 0;
		notCompleteIds = new ArrayList<>();
		for (int i = 0; i < numberOfSubDownloads; i++)
			filesOfSubDownloads[i] = null;
	}

	@Override
	public void delObserver() {
		// TODO Auto-generated method stub
		this.listObserver = new ArrayList<Observer>();
	}

	public StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	public void setStaticsticsManager(StatisticsManager statisticsmanager) {
		this.statisticsManager = statisticsmanager;
	}

	private synchronized int getNumberOfSubDownloadsCompleted() {
		return numberOfSubDownloadsCompleted;
	}

	public synchronized void notifyComplete(int subdownloadnumber, File file) {
		filesOfSubDownloads[subdownloadnumber] = file;
		this.numberOfSubDownloadsCompleted++;
		if(numberOfSubDownloadsCompleted == numberOfSubDownloads)
			this.complete = true;
		if((numberOfSubDownloadsCompleted + numberOfSubDownloadsNotCompleted) == numberOfSubDownloads)
			notify();
	}
	
	
	boolean hasNotifyNotComplete(int subdownloadnumber) {
		
		boolean result = false;
		
		if(notCompleteIds != null) {
			for (Iterator<Integer> iterator = notCompleteIds.iterator(); iterator.hasNext();) {
				Integer integer = (Integer) iterator.next();
				if(integer.intValue() == subdownloadnumber) {
					result = true;
					break;
				}
			}
		}
		
		return result;
	}


	public synchronized void notifyNotComplete(int subdownloadnumber) {
		if(!hasNotifyNotComplete(subdownloadnumber)) {
			notCompleteIds.add(subdownloadnumber);
			this.numberOfSubDownloadsNotCompleted++;
			if((numberOfSubDownloadsCompleted + numberOfSubDownloadsNotCompleted) == numberOfSubDownloads) {
				System.out.println("wake download up for restart");
				notify();
			}
		}
	}
	
	
	private synchronized boolean isCompleted() {
		return complete;
	}
	
	public synchronized boolean hasSuspended() {
		return suspend;
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
	
}

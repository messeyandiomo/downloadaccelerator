package utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import gui.Observable;
import gui.Observer;

public class SubDownload extends Thread implements Observable {
	
	private long size;
	private long first;
	private Download download;
	private int subdownloadnumber;
	private File file;
	private InputStream inputStream = null;
	private FileOutputStream outputStream = null;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	private long downloaded = 0;
	private boolean suspend = false;
	private boolean cancel = false;
	private boolean shutdown = false;
	private SubDownloadProps subDownloadProps;
	private boolean complete = false;
	private SubDownload myself = null;
	private Timer subDownloadTimer = null;
	
	
	public SubDownload(Download download, int subdownloadnumber, boolean shutdown) {
		super();
		this.myself = this;
		this.setDownload(download);
		this.setShutdown(shutdown);
		this.setSubdownloadnumber(subdownloadnumber);
		subDownloadProps = this.download.getDownloadProps().getSubDownloadProps(this.subdownloadnumber);
		this.file = subDownloadProps.getFile();
		this.setSize(subDownloadProps.getSize());
		this.setFirst(subDownloadProps.getFirstOctet());
		this.setDownloaded(subDownloadProps.getDownloaded());
		this.start();
	}
	
	
	public void run() {
		
		if(downloaded > 0) {
			if(shutdown) {
				this.updateObserver();
				this.download.getStatisticsManager().update(downloaded, shutdown);
			}
			if(downloaded < size)
				downloadFile();
			else
				complete = true;
		}
		else
			downloadFile();
		
		if(complete) {
			if(outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			subDownloadProps.setOutputStream(null);
			this.download.notifyComplete(subdownloadnumber, file);
		}
		else
			if(!this.isCancel())
				this.download.notifyNotComplete(subdownloadnumber);
	}
	
	
	
	
	void downloadFile() {
		
		boolean isCancel = false;
		boolean isSuspend = false;
		if(subDownloadProps.getOutputStream() == null)
			subDownloadProps.createOutputStream();
		if(subDownloadProps.getInputStream() == null)
			subDownloadProps.createInputStream();
		if((subDownloadProps.getOutputStream() != null) && (subDownloadProps.getInputStream() != null)) {
			this.inputStream = subDownloadProps.getInputStream();
			this.outputStream = subDownloadProps.getOutputStream();
			byte[] buf = new byte[1024];
			int b = 0;
			long remainByteCount = size - downloaded;
			
			while(remainByteCount > 0) {
				subDownloadTimer = new Timer();
				subDownloadTimer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(myself.isAlive()) {
							myself.interrupt();
							myself.closeStreams();
							download.notifyNotComplete(subdownloadnumber);
							System.out.println(subdownloadnumber + " not complete for having make more than 10 seconds");
						}
					}
				}, SubDownloadProps.getReadtimeout() + 5000);
				try {
					b = inputStream.read(buf);
					subDownloadTimer.cancel();
					subDownloadTimer.purge();
					subDownloadTimer = null;
					if(b > 0) {
						if(remainByteCount < b) {
							outputStream.write(buf, 0, (int) remainByteCount);
							outputStream.flush();
							outputStream.getFD().sync();
							this.setDownloaded(remainByteCount);
							this.subDownloadProps.update(remainByteCount);
							this.download.getStatisticsManager().update(remainByteCount, false);
							this.updateObserver();
							remainByteCount = 0;
							break;
						}
						else {
							outputStream.write(buf, 0, b);
							outputStream.flush();
							outputStream.getFD().sync();
							this.setDownloaded(b);
							this.subDownloadProps.update(b);
							this.download.getStatisticsManager().update(b, false);
							this.updateObserver();
							remainByteCount -= b;
						}
					}
					else {
						break;
					}
				} catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
					if(subDownloadTimer != null) {
						subDownloadTimer.cancel();
						subDownloadTimer.purge();
						subDownloadTimer = null;
					}
					this.subDownloadProps.setInputStream(null);
					break;
				}
				isSuspend = this.isSuspended();
				if(isSuspend) {
					this.updateObserver();
					try {
						synchronized (this) {
							wait();
						}
						this.updateObserver();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				isCancel = this.isCancel();
				if(isCancel) {
					break;
				}
			}
			if(remainByteCount == 0)
				complete = true;
		}
		
		this.closeStreams();
	}
	
	/** closure of the streams **/
	private void closeStreams() {
		/*
		if(outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		subDownloadProps.setOutputStream(null);
		*/
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		subDownloadProps.setInputStream(null);
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


	@Override
	public void addObserver(Observer obs) {
		// TODO Auto-generated method stub
		this.listObserver.add(obs);
	}


	@Override
	public void updateObserver() {
		// TODO Auto-generated method stub
		long downloaded = this.getDownloaded();
		boolean issuspended = this.isSuspended();
		for(Observer obs : this.listObserver)
			obs.update(false, issuspended, downloaded, false);
	}


	@Override
	public void delObserver() {
		// TODO Auto-generated method stub
		this.listObserver = new ArrayList<Observer>();
	}


	public long getDownloaded() {
		return downloaded;
	}


	public void setDownloaded(long downloaded) {
		this.downloaded += downloaded;
	}


	public boolean isShutdown() {
		return shutdown;
	}


	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}


	public synchronized boolean isSuspended() {
		return suspend;
	}


	public synchronized void pause() {
		this.suspend = true;
	}
	
	
	public synchronized void revive() {
		suspend = false;
		notify();
	}


	public synchronized boolean isCancel() {
		return cancel;
	}


	public synchronized void cancel() {
		this.cancel = true;
		notify();
	}
	
	
	@Override
	public void interrupt() {
		super.interrupt();
	}
	
}

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SubDownload extends Thread implements Observable {
	
	private long size;
	private long first;
	private Download download;
	private int subdownloadnumber;
	private File file;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	private long downloaded = 0;
	private boolean suspend = false;
	private boolean cancel = false;
	private boolean shutdown = false;
	private SubDownloadProperties subDownloadProperties;
	private boolean complete = false;
	private SubDownload myself = null;
	
	
	public SubDownload(Download download, int subdownloadnumber, boolean shutdown) {
		super();
		this.myself = this;
		this.setDownload(download);
		this.setSubdownloadnumber(subdownloadnumber);
		this.setShutdown(shutdown);
		subDownloadProperties = this.download.getDownloadProperties().getSubDownloadProperties(this.subdownloadnumber);
		this.file = subDownloadProperties.getFile();
		this.setSize(subDownloadProperties.getSize());
		this.setFirst(subDownloadProperties.getFirstOctet());
		this.setDownloaded(subDownloadProperties.getDownloaded());
		this.download.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(!complete) {
					myself.interrupt();
				}
			}
		});
		this.start();
	}
	
	
	public void run() {
		
		if(downloaded > 0) {
			if(shutdown) {
				this.updateObserver();
				this.download.getStaticsticsManager().update(downloaded);
			}
			if(downloaded < size)
				downloadFile();
			else
				complete = true;
		}
		else
			downloadFile();
		
		if(complete)
			this.download.notifyComplete(subdownloadnumber, file);
		else
			if(!this.isCancel())
				this.download.notifyNotComplete();
	}
	
	
	
	
	void downloadFile() {
		
		boolean isCancel = false;
		boolean isSuspend = false;
		
		if((subDownloadProperties.createOutputStream()) && (subDownloadProperties.createInputStream())) {
			this.inputStream = subDownloadProperties.getInputStream();
			this.outputStream = subDownloadProperties.getOutputStream();
			byte[] buf = new byte[1024];
			int b = 0;
			long remainByteCount = size - downloaded;
			
			while(remainByteCount > 0) {
				try {
					b = inputStream.read(buf);
					download.resetWaiting();
					if(b > 0) {
						if(remainByteCount < b) {
							outputStream.write(buf, 0, (int) remainByteCount);
							this.setDownloaded(remainByteCount);
							this.subDownloadProperties.update(remainByteCount);
							this.download.getStaticsticsManager().update(remainByteCount);
							this.updateObserver();
							remainByteCount = 0;
							break;
						}
						else {
							outputStream.write(buf, 0, b);
							this.setDownloaded(b);
							this.subDownloadProperties.update(b);
							this.download.getStaticsticsManager().update(b);
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
					this.subDownloadProperties.setInputStream(null);
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
		
		if(outputStream != null) {
			try {
				outputStream.close();
				subDownloadProperties.setOutputStream(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(inputStream != null) {
			try {
				inputStream.close();
				subDownloadProperties.setInputStream(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
		for(Observer obs : this.listObserver)
			obs.update(false, downloaded);
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
		if(inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			finally {
				super.interrupt();
			}
		}
	}
	
	
}

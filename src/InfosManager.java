
public class InfosManager extends Thread {
	
	private Download download;
	private long sizePerSecond = 0;
	private boolean suspend = false;
	private boolean stop = false;

	public InfosManager(Download download) {
		// TODO Auto-generated constructor stub
		super();
		this.setDownload(download);
		this.start();
	}
	
	
	public void run() {
		
		long filesize = download.getDownloadWindow().getFilesize();
		long downloadedcurrentsize;
		long velocity;
		boolean testsuspend, teststop;
		
		while(true) {
			try {
				sleep(1000);
				downloadedcurrentsize = download.getCurrentSize();
				velocity = getAndResizeSizePerSecond();
				//resetSizePerSecond();
				download.getDownloadWindow().getDownloadWindowSpeedInfos().setText(DownloadControl.getInstance().convertSize(velocity) + "/s");
				download.getDownloadWindow().getDownloadWindowDurationInfos().setText(duration(downloadedcurrentsize, velocity, filesize));
				testsuspend = getSuspend();
				if(testsuspend) {
					download.getDownloadWindow().suspend2();
					try {
						synchronized (this) {
							wait();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					download.getDownloadWindow().resume2();
				}
				teststop = getStop();
				if(teststop) {
					break;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

	/**
	 * @return the downloadwindow
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
	 * @return the sizePerSecond
	 */
	private synchronized long getAndResizeSizePerSecond() {
		long sizepersecond = sizePerSecond;
		sizePerSecond = 0;
		return sizepersecond;
	}
	
	
	public synchronized void update(long sizeAdded) {
		
		sizePerSecond += sizeAdded;
		
	}
	
	
	public synchronized boolean getSuspend() {
		return this.suspend;
	}
	
	
	public synchronized boolean getStop() {
		return this.stop;
	}
	
	
	public synchronized void suspend2() {
		suspend = true;
	}
	
	
	public synchronized void resume2() {
		suspend = false;
		notify();
	}
	
	
	public synchronized void stop2() {
		stop = true;
		suspend = false;
		notify();
	}
	
	
	String duration(long downloadedcurrentsize, long velocity, long filesize) {
		
		String retour = "";
		
		if(velocity == 0) {
			retour = "...";
		}
		else {
			long secondes = (filesize - downloadedcurrentsize)/velocity;
			long minutes = 0;
			long heures = 0;
			long jours = 0;
			long tampon;
			
			if(secondes >= 60) {
				tampon = secondes%60;
				minutes = (secondes - tampon)/60;
				secondes = tampon;
			}
			if(secondes > 0) {
				retour = secondes + "s";
			}
			if(minutes >= 60) {
				tampon = minutes%60;
				heures = (minutes - tampon)/60;
				minutes = minutes%60;
			}
			if(minutes > 0) {
				retour = minutes + "min" + retour;
			}
			if(heures >= 24) {
				tampon = heures%24;
				jours = (heures - tampon)/24;
				heures = tampon;
			}
			if(heures > 0) {
				retour = heures + "h" + retour;
			}
			if(jours > 0) {
				retour = jours + "j" + retour;
			}
		}
				
		return retour;
		
	}

	
}



public class StateManager extends Thread {
	
	private Download download;
	private int subDownloadCount;
	private SubDownload[] subDownloadSet;
	private InfosManager infosManager;
	private boolean suspend = false;
	private boolean resume = true;
	private boolean stop = false;

	public StateManager(Download download) {
		// TODO Auto-generated constructor stub
		super();
		this.setDownload(download);
		subDownloadCount = download.getSubDownloadCount();
		subDownloadSet = new SubDownload[subDownloadCount];
		for(int i = 0; i< subDownloadCount; i++) {
			subDownloadSet[i] = download.getSubDownload(i);
		}
		infosManager = download.getInfosManager();
		this.start();
	}

	public void run() {
		
		boolean testsuspend, testresume, teststop;
		
		while(true) {
			
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			testsuspend = getSuspend();
			testresume = getResume();
			teststop = getStop();
			if(testsuspend) {
				int[] subDownloadAlive = new int[subDownloadCount];
				int j = 0;
				for(int i = 0; i < subDownloadCount; i++) {
					if(subDownloadSet[i].isAlive())	{
						subDownloadSet[i].suspend2();
						subDownloadAlive[j] = i;
						j++;
					}
				}
				yield();
				int tryOut = 5;
				int m;
				for(int l = 0; l < j; l++) {
					m = subDownloadAlive[l];
					while(subDownloadSet[m].getState() != Thread.State.WAITING) {
						tryOut--;
						if(tryOut == 0) {
							subDownloadSet[m].interrupt();
							break;
						}
						yield();
					}
				}
				infosManager.suspend2();
			}
			else if(testresume) {
				infosManager.resume2();
				for(int i = 0; i < subDownloadCount; i++) {
					if(subDownloadSet[i].isAlive()) subDownloadSet[i].resume2();
				}
			}
			else if(teststop) {
				int[] subDownloadAlive = new int[subDownloadCount];
				int j = 0;
				for(int i = 0; i < subDownloadCount; i++) {
					if(subDownloadSet[i].isAlive()) {
						subDownloadSet[i].stop2();
						subDownloadAlive[j] = i;
						j++;
					}
				}
				yield();
				int tryOut = 5;
				int m;
				for(int l = 0; l < j; l++) {
					m = subDownloadAlive[l];
					while(subDownloadSet[m].isAlive()) {
						tryOut--;
						if(tryOut == 0) {
							subDownloadSet[m].interrupt();
							break;
						}
						yield();
					}
				}
				if(infosManager.isAlive()) {
					infosManager.stop2();
					try {
						infosManager.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(download.isAlive()) {
					download.freeDownload();
					try {
						download.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
		}
	}
	
	
	//obtention de la variable suspend
	private synchronized boolean getSuspend() {
		return suspend;
	}
	
	
	//obtention de la variable resume
	private synchronized boolean getResume() {
		return resume;
	}
	
	
	//obtention de la variable stop
	private synchronized boolean getStop() {
		return stop;
	}
	
	
	//suspension du téléchargement
	public synchronized void suspend2() {
		suspend = true;
		resume = false;
		notify();
	}
	
	
	public synchronized void resume2() {
		resume = true;
		suspend = false;
		notify();
	}
	
	
	public synchronized void stop2() {
		resume = false;
		suspend = false;
		stop = true;
		notify();
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
}

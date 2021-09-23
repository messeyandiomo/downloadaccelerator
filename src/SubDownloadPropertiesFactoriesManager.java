import java.util.ArrayList;

public class SubDownloadPropertiesFactoriesManager extends Thread implements Observable {
	
	private int subDownloadPropertiesGenerated = 0;
	private DownloadProperties downloadProperties = null;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	
	
	public SubDownloadPropertiesFactoriesManager(DownloadProperties downloadproperties) {
		// TODO Auto-generated constructor stub
		super();
		this.setDownloadProperties(downloadproperties);
		this.start();
	}
	
	
	public void run() {
		
		int numberofsubdownloadpropertiesgenerated = 0;
		while(true) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			numberofsubdownloadpropertiesgenerated = this.getSubDownloadPropertiesGenerated();
			if(numberofsubdownloadpropertiesgenerated == downloadProperties.getSubDownloadCount()) {
				this.updateObserver();
				break;
			}
		}
	}

	public DownloadProperties getDownloadProperties() {
		return downloadProperties;
	}

	public void setDownloadProperties(DownloadProperties downloadProperties) {
		this.downloadProperties = downloadProperties;
	}

	public synchronized int getSubDownloadPropertiesGenerated() {
		return subDownloadPropertiesGenerated;
	}

	public synchronized void setSubDownloadPropertiesGenerated() {
		this.subDownloadPropertiesGenerated++;
		notify();
	}
	
	public void recordSubDownloadProperties(int subdownloadpropertiesnumber, SubDownloadProperties subdownloadproperties) {
		this.downloadProperties.setSubDownloadProperties(subdownloadpropertiesnumber, subdownloadproperties);
		this.setSubDownloadPropertiesGenerated();
	}


	@Override
	public void addObserver(Observer obs) {
		// TODO Auto-generated method stub
		this.listObserver.add(obs);
	}


	@Override
	public void updateObserver() {
		// TODO Auto-generated method stub
		for(Observer obs : this.listObserver)
			obs.update(true, 0);
	}


	@Override
	public void delObserver() {
		// TODO Auto-generated method stub
		this.listObserver = new ArrayList<Observer>();
	}

}

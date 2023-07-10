package utils;
import java.util.ArrayList;

import gui.Observable;
import gui.Observer;

public class SubDownloadPropsFactoriesManager extends Thread implements Observable {
	
	private int subDownloadPropsGenerated = 0;
	private DownloadProps downloadProps = null;
	private DownloadProps audioDownloadProps = null;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	private int subDownloadCount = 0;
	
	
	/*** newer gererators ***/
	public SubDownloadPropsFactoriesManager(DownloadProps downloadprops) {
		// TODO Auto-generated constructor stub
		super();
		this.setDownloadProps(downloadprops);
		this.subDownloadCount = downloadProps.getSubDownloadCount();
		this.start();
	}
	
	public SubDownloadPropsFactoriesManager(DownloadProps downloadprops, DownloadProps audiodownloadprops) {
		// TODO Auto-generated constructor stub
		super();
		this.setDownloadProps(downloadprops);
		this.setAudioDownloadProps(audiodownloadprops);
		this.subDownloadCount = downloadProps.getSubDownloadCount() + audioDownloadProps.getSubDownloadCount();
		this.start();
	}
	
	
	public void run() {
		
		int numberofsubdownloadpropsgenerated = 0;
		while(true) {
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			numberofsubdownloadpropsgenerated = this.getSubDownloadPropsGenerated();
			if(numberofsubdownloadpropsgenerated == (this.subDownloadCount)) {
				this.updateObserver();
				break;
			}
		}
	}

	public DownloadProps getDownloadProps() {
		return downloadProps;
	}

	public void setDownloadProps(DownloadProps downloadprops) {
		this.downloadProps = downloadprops;
	}
	
	public DownloadProps getAudioDownloadProps() {
		return audioDownloadProps;
	}

	public void setAudioDownloadProps(DownloadProps audiodownloadprops) {
		this.audioDownloadProps = audiodownloadprops;
	}

	public synchronized int getSubDownloadPropsGenerated() {
		return subDownloadPropsGenerated;
	}

	public synchronized void setSubDownloadPropsGenerated() {
		this.subDownloadPropsGenerated++;
		notify();
	}
	
	
	public void recordSubDownloadProps(int subdownloadpropsnumber, SubDownloadProps subdownloadprops) {
		if(subdownloadprops.isAudio())
			this.audioDownloadProps.setSubDownloadProps(subdownloadpropsnumber, subdownloadprops);
		else
			this.downloadProps.setSubDownloadProps(subdownloadpropsnumber, subdownloadprops);
		this.setSubDownloadPropsGenerated();
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
			obs.update(true, false, 0);
	}


	@Override
	public void delObserver() {
		// TODO Auto-generated method stub
		this.listObserver = new ArrayList<Observer>();
	}

	@Override
	public void initObserver() {
		// TODO Auto-generated method stub
		
	}

}
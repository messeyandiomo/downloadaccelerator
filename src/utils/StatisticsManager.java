package utils;
import java.util.ArrayList;

import gui.Observable;
import gui.Observer;

public class StatisticsManager extends Thread implements Observable {
	
	private boolean suspend = false;
	private boolean complete = false;
	private long downloaded = 0;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();

	public StatisticsManager() {
		// TODO Auto-generated constructor stub
		super();
		this.start();
	}

	
	public void run() {
		
		boolean issuspended, iscompleted;
		
		while(true) {
			try {
				sleep(1000);
				this.updateObserver();
				issuspended = isSuspended();
				if(issuspended) {
					this.updateObserver();
					try {
						synchronized (this) {
							wait();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				iscompleted = isCompleted();
				if(iscompleted) {
					break;
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.updateObserver();
	}
	
	
	private synchronized long getDownloaded() {
		long downloadedpersecond = downloaded;
		downloaded = 0;
		return downloadedpersecond;
	}
	
	
	public synchronized void update(long sizeAdded) {
		downloaded += sizeAdded;
	}
	
	
	public synchronized boolean isSuspended() {
		return this.suspend;
	}
	
	
	public synchronized void pause() {
		suspend = true;
	}
	
	
	public synchronized void revive() {
		suspend = false;
		notify();
	}
	
	
	public synchronized boolean isCompleted() {
		return this.complete;
	}
	
	
	public synchronized void complete() {
		complete = true;
		suspend = false;
		notify();
	}
	
	
	

	@Override
	public void addObserver(Observer obs) {
		// TODO Auto-generated method stub
		this.listObserver.add(obs);
	}

	@Override
	public void updateObserver() {
		// TODO Auto-generated method stub
		boolean iscompleted = this.isCompleted();
		boolean issuspended = this.isSuspended();
		long downloadedpersecond = this.getDownloaded();
		for(Observer obs : this.listObserver)
			obs.update(iscompleted, issuspended, null, downloadedpersecond);
	}

	@Override
	public void delObserver() {
		// TODO Auto-generated method stub
		this.listObserver = new ArrayList<Observer>();
	}

}

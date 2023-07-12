package gui;

import utils.SubDownload;

public interface Observable {
	public void addObserver(Observer obs);
	public void updateObserver();
	public void updateObserver(SubDownload subdownload, int progressbarnumber);
	public void delObserver();
}

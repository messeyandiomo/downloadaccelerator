package gui;

import utils.SubDownload;

public interface Observer {
	public void update(boolean complete, boolean trytodownloadagain, long infos);
	public void update(SubDownload subdownload, int progressbarnumber);
}

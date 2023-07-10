package gui;

import utils.SubDownload;

public interface Observer {
	public void update(boolean complete, boolean suspend, long infos, SubDownload subdownload);
}

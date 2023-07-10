package gui;

import utils.SubDownload;

public interface Observer {
	public void init(Thread thread);
	public void update(boolean complete, boolean suspend, long infos);
}

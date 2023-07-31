package gui;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import utils.Download;
import utils.DownloadProps;

@SuppressWarnings("serial")
public class SubDownloadsContainer extends JPanel {
	
	private SubDownloadProgressBar[] subDownloadProgressBarArray;
	private int subDownloadsCount;
	private Download download;
	
	
	/*** newer constructor ***/
	public SubDownloadsContainer(Download download, DownloadProps downloadprops, int width, int height) {
		// TODO Auto-generated constructor stub
		super();
		this.download = download;
		this.setSubDownloadsCount(downloadprops.getSubDownloadCount());
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		subDownloadProgressBarArray = new SubDownloadProgressBar[subDownloadsCount];
		
		int subDownloadwidth = (int)(width/subDownloadsCount);
		for (int i = 0; i < subDownloadsCount; i++) {
			subDownloadProgressBarArray[i] = new SubDownloadProgressBar(this.download, i, 0, subDownloadwidth);
			subDownloadProgressBarArray[i].setPreferredSize(new Dimension(subDownloadwidth, height));
			this.add(subDownloadProgressBarArray[i]);
		}	
	}
	

	public int getSubDownloadsCount() {
		return subDownloadsCount;
	}

	public void setSubDownloadsCount(int subDownloadsCount) {
		this.subDownloadsCount = subDownloadsCount;
	}

	public Download getDownload() {
		return download;
	}

	public void setDownload(Download download) {
		this.download = download;
	}
	
	public void pause() {
		for (int i = 0; i < subDownloadsCount; i++)
			subDownloadProgressBarArray[i].pause();
	}
	
	
	public void revive() {
		for (int i = 0; i < subDownloadsCount; i++)
			subDownloadProgressBarArray[i].revive();
	}
	
	public void restart() {
		for (int i = 0; i < subDownloadsCount; i++)
			this.download.getDownloadProps().getSubDownloadProps(i).resetDownloaded();
		/** don't try to put these instructions into same brackets **/
		for (int i = 0; i < subDownloadsCount; i++)
			subDownloadProgressBarArray[i].restart();
	}

}

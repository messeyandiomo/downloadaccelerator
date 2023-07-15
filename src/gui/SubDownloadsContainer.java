package gui;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import utils.Download;
import utils.DownloadProps;

@SuppressWarnings("serial")
public class SubDownloadsContainer extends JPanel {
	
	private SubDownloadProgressBar[] subDownloadProgressBarArray;
	private int subDownloadsCount;
	private Download download;
	private JPanel subDownloadsContainer;
	
	
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
		
		this.subDownloadsContainer = this;
		
		this.download.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, boolean suspend, ArrayList<Integer> subdownloadnumbersnotcomplete, long infos) {
				// TODO Auto-generated method stub
				if(complete) {
					if(subDownloadsContainer.isVisible())
						subDownloadsContainer.setVisible(false);
				}
				else if(subdownloadnumbersnotcomplete != null) {
					for (int i = 0; i < subdownloadnumbersnotcomplete.size(); i++) {
						subDownloadProgressBarArray[subdownloadnumbersnotcomplete.get(i)].reset();
					}
				}
			}
		});		
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
		for (int i = 0; i < subDownloadsCount; i++) {
			subDownloadProgressBarArray[i].pause();
		}
		download.getStaticsticsManager().pause();
	}
	
	public void revive() {
		download.getStaticsticsManager().revive();
		download.reset();
		for (int i = 0; i < subDownloadsCount; i++) {
			subDownloadProgressBarArray[i].revive();
		}
	}

}

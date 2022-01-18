import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SubDownloadsContainer extends JPanel {
	
	private SubDownloadProgressBar[] subDownloads;
	private int subDownloadsCount;
	private Download download;
	private JPanel subDownloadsContainer;

	public SubDownloadsContainer(Download download, DownloadProperties downloadproperties, int width, int height) {
		// TODO Auto-generated constructor stub
		super();
		this.download = download;
		this.setSubDownloadsCount(downloadproperties.getSubDownloadCount());
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		subDownloads = new SubDownloadProgressBar[subDownloadsCount];
		
		int subDownloadwidth = (int)(width/subDownloadsCount);
		for (int i = 0; i < subDownloadsCount; i++) {
			subDownloads[i] = new SubDownloadProgressBar(this.download, i, 0, subDownloadwidth);
			subDownloads[i].setPreferredSize(new Dimension(subDownloadwidth, height));
			this.add(subDownloads[i]);
		}
		
		this.subDownloadsContainer = this;
		
		this.download.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(complete) {
					if(subDownloadsContainer.isVisible())
						subDownloadsContainer.setVisible(false);
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
			subDownloads[i].pause();
		}
		download.getStaticsticsManager().pause();
	}
	
	public void revive() {
		download.getStaticsticsManager().revive();
		download.reset();
		for (int i = 0; i < subDownloadsCount; i++) {
			subDownloads[i].revive();
		}
	}

}

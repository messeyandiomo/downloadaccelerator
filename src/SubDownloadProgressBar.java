import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class SubDownloadProgressBar extends JProgressBar {

	private long filesize = 0;
	private int barLength;
	private Download download;
	private SubDownload subDownload;
	private int subDownloadNumber;
	private JProgressBar subDownloadProgressBar;
	private Color originalColor;
	private Border originalBorder;
	private long previousInfos = 0;
	private Observer subDownloadObserver;
	private Observer downloadObserver;
	
	public SubDownloadProgressBar(int arg0, int arg1, long filesize) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		this.setFilesize(filesize);
		barLength = arg1;
	}
	
	public SubDownloadProgressBar(Download download, int subdownloadnumber, int arg0, int arg1) {

		this(arg0, arg1, download.getDownloadProperties().getSubDownloadProperties(subdownloadnumber).getSize());
		this.download = download;
		this.subDownloadNumber = subdownloadnumber;
		this.subDownload = new SubDownload(this.download, this.subDownloadNumber, true);
		this.subDownloadProgressBar = this;
		
		this.originalColor = subDownloadProgressBar.getForeground();
		this.originalBorder = subDownloadProgressBar.getBorder();
		
		this.subDownloadObserver = new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(infos != previousInfos) {
					previousInfos = infos;
					subDownloadProgressBar.setValue((int) ((infos*barLength)/filesize));
				}
				else {
					if(subDownload.isSuspended()) {
						subDownloadProgressBar.setForeground(Color.LIGHT_GRAY);
						subDownloadProgressBar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
					}
					else
						if(subDownloadProgressBar.getForeground().equals(Color.LIGHT_GRAY)) {
							subDownloadProgressBar.setForeground(originalColor);
							subDownloadProgressBar.setBorder(originalBorder);
						}
				}
			}
		};
		
		this.downloadObserver = new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(!complete) {
					subDownloadProgressBar.setForeground(Color.LIGHT_GRAY);
					subDownloadProgressBar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				}
			}
		};
		
		this.subDownload.addObserver(subDownloadObserver);
		this.download.addObserver(downloadObserver);
	}

	/**
	 * @return the filesize
	 */
	public long getFilesize() {
		return filesize;
	}

	/**
	 * @param filesize the filesize to set
	 */
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	
	
	
	public void pause() {
		if(this.subDownload.isAlive())
			this.subDownload.pause();
	}
	
	
	public void revive() {
		if(this.subDownload.isAlive())
			this.subDownload.revive();
		else {
			subDownloadProgressBar.setForeground(originalColor);
			subDownloadProgressBar.setBorder(originalBorder);
			this.subDownload = new SubDownload(download, subDownloadNumber, false);
			this.subDownload.addObserver(subDownloadObserver);
			this.download.addObserver(downloadObserver);
		}
	}


}

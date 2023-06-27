package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import utils.Download;
import utils.DownloadControl;
import utils.MergeFile;

@SuppressWarnings("serial")
public class FileConcatContainer extends JPanel {
	
	private SubDownloadProgressBar fileConcatProgressBar;
	private JPanel fileConcatContainer;
	private Font font = new Font("Serif", Font.BOLD, 10);
	private Color VERY_DARK_GREEN = new Color(0, 102, 0);
	
	private Download download = null;
	private MergeFile mergeFile = null;
	
	
	public FileConcatContainer(int width, int height) {
		super();
		this.setFileConcatProgressBar(new SubDownloadProgressBar(0, width));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		fileConcatProgressBar.setBorder(BorderFactory.createLineBorder(VERY_DARK_GREEN));
		fileConcatProgressBar.setForeground(VERY_DARK_GREEN);
		fileConcatProgressBar.setPreferredSize(new Dimension(width, height));
		
		fileConcatProgressBar.setStringPainted(true);
		fileConcatProgressBar.setFont(font);
		fileConcatProgressBar.setString("BUILDING OF FILE");
		
		this.add(fileConcatProgressBar);
		fileConcatContainer = this;
	}
	
	
	public FileConcatContainer(Download download, int width, int height, long filesize) {
		// TODO Auto-generated constructor stub
		this(width, height);
		this.setDownload(download);
		
		this.download.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(complete) {
					if(!fileConcatContainer.isVisible())
						fileConcatContainer.setVisible(true);
					/** here infos represents current size **/
					fileConcatProgressBar.setValue((int) ((infos*width)/filesize));
					if(infos >= filesize)
						fileConcatProgressBar.setString(DownloadControl.convertSize(filesize));
				}
			}
		});
	}
	
	
	public FileConcatContainer(MergeFile mergefile, int width, int height, long filesize) {
		// TODO Auto-generated constructor stub
		this(width, height);
		this.setMergeFile(mergefile);
		
		this.mergeFile.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(!complete) {
					if(!fileConcatContainer.isVisible())
						fileConcatContainer.setVisible(true);
					fileConcatProgressBar.setValue((int) infos);
				}
				else
					fileConcatProgressBar.setString(DownloadControl.convertSize(filesize));
			}
		});
	}
	
	


	private void setFileConcatProgressBar(SubDownloadProgressBar fileConcatProgressBar) {
		this.fileConcatProgressBar = fileConcatProgressBar;
	}


	private void setDownload(Download download) {
		this.download = download;
	}


	public void setMergeFile(MergeFile mergeFile) {
		this.mergeFile = mergeFile;
	}

}

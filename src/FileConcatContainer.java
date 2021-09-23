import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FileConcatContainer extends JPanel {
	
	private SubDownloadProgressBar fileConcatProgressBar;
	private Download download;
	private JPanel fileConcatContainer;
	private Font font = new Font("Serif", Font.BOLD, 10);
	private Color VERY_DARK_GREEN = new Color(0, 102, 0);

	public FileConcatContainer(Download download, int width, int height, long filesize) {
		// TODO Auto-generated constructor stub
		super();
		this.setDownload(download);
		this.setFileConcatProgressBar(new SubDownloadProgressBar(0, width, filesize));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		fileConcatProgressBar.setBorder(BorderFactory.createLineBorder(VERY_DARK_GREEN));
		fileConcatProgressBar.setForeground(VERY_DARK_GREEN);
		fileConcatProgressBar.setPreferredSize(new Dimension(width, height));
		
		fileConcatProgressBar.setStringPainted(true);
		fileConcatProgressBar.setFont(font);
		fileConcatProgressBar.setString("BUILDING OF FILE");
		
		this.add(fileConcatProgressBar);
		fileConcatContainer = this;
		
		this.download.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(complete) {
					if(!fileConcatContainer.isVisible())
						fileConcatContainer.setVisible(true);
					//here infos represents current size
					fileConcatProgressBar.setValue((int) ((infos*width)/filesize));
					if(infos >= filesize)
						fileConcatProgressBar.setString(DownloadControl.convertSize(filesize));
				}
			}
		});
	}
	
	


	private void setFileConcatProgressBar(SubDownloadProgressBar fileConcatProgressBar) {
		this.fileConcatProgressBar = fileConcatProgressBar;
	}


	private void setDownload(Download download) {
		this.download = download;
	}

}

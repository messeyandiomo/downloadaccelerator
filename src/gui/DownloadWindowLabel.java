package gui;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Download;
import utils.DownloadControl;
import utils.StatisticsManager;
import utils.SubDownload;

@SuppressWarnings("serial")
public class DownloadWindowLabel extends JPanel {
	
	private JLabel labelValue = new JLabel("");
	private JLabel labelTitle = new JLabel();
	private JPanel labelContainer;
	private Download download = null;
	private StatisticsManager statisticsManager;
	private long currentSize;
	private String fileSizeInString;
	private Font fontSize = new Font("Serif", Font.BOLD, 10);
	
	
	//filename label constructor
	public DownloadWindowLabel(String value, int max) {
		// TODO Auto-generated constructor stub
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		if(value.length() > max)
			this.setValue(value.substring(value.length() - max));
		else
			this.setValue(value);
		this.add(labelValue);
		
		labelContainer = this;
	}
	
	
	//suspended state label constructor
	public DownloadWindowLabel(Download download, Dimension dim, long size) {
		// TODO Auto-generated constructor stub
		super();
		this.download = download;
		this.statisticsManager = download.getStaticsticsManager();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(labelValue);
		this.setMaximumSize(dim);
		labelContainer = this;
		
		this.statisticsManager.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, boolean suspend, long infos, SubDownload subdownload) {
				// TODO Auto-generated method stub
				if(!complete) {
					if(DownloadWindowLabel.this.statisticsManager.isSuspended()) {
						setValue("SUSPEND");
						labelContainer.setVisible(true);
					}
					else
						labelContainer.setVisible(false);
				}
			}
		});
		
		this.download.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, boolean suspend, long infos, SubDownload subdownload) {
				// TODO Auto-generated method stub
				if(infos == size)
					setValue("");
			}
		});
	}
	
	
	//size, speed and duration labels constructor
	public DownloadWindowLabel(StatisticsManager statisticsmanager, String title, Dimension dim, long filesize) {
		// TODO Auto-generated constructor stub
		super();
		this.statisticsManager = statisticsmanager;
		this.fileSizeInString = DownloadControl.convertSize(filesize);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setTitle(title);
		this.add(labelTitle);
		this.add(labelValue);
		this.setMaximumSize(dim);
		labelContainer = this;
		
		this.statisticsManager.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, boolean suspend, long infos, SubDownload subdownload) {
				// TODO Auto-generated method stub
				if(getTitle().contentEquals("Size: ")) {
					if(complete)
						labelContainer.setVisible(false);
					else {
						currentSize += infos;
						setValue(DownloadControl.convertSize(currentSize) + "/" + fileSizeInString);
					}
				}
				else if(getTitle().contentEquals("Speed: ")) {
					if(complete||DownloadWindowLabel.this.statisticsManager.isSuspended())
						labelContainer.setVisible(false);
					else {
						labelContainer.setVisible(true);
						setValue(DownloadControl.convertSize(infos) + "/s");
					}
				}
				else if(getTitle().contentEquals("Duration: ")) {
					if(complete||DownloadWindowLabel.this.statisticsManager.isSuspended())
						labelContainer.setVisible(false);
					else {
						currentSize += infos;
						labelContainer.setVisible(true);
						setValue(duration(currentSize, infos, filesize));
					}
				}
			}
		});
	}
	
	
	

	public String getValue() {
		return this.labelValue.getText();
	}

	public void setValue(String value) {
		this.labelValue.setText(value);
		this.labelValue.setFont(fontSize);
	}
	
	
	public String getTitle() {
		return this.labelTitle.getText();
	}
	
	public void setTitle(String title) {
		this.labelTitle.setText(title + ": ");
		this.labelTitle.setFont(fontSize);
	}
	
	/** modify font from outside. To not confuse with method setFont of JComponent **/
	public void changeFont(Font newFont) {
		this.labelValue.setFont(newFont);
		this.labelTitle.setFont(newFont);
	}

	
	
	String duration(long downloadedcurrentsize, long velocity, long filesize) {
				
		String retour = "";
		
		if(velocity == 0) {
			retour = "...";
		}
		else {
			long secondes = (filesize - downloadedcurrentsize)/velocity;
			long minutes = 0;
			long heures = 0;
			long jours = 0;
			long tampon;
			
			if(secondes >= 60) {
				tampon = secondes%60;
				minutes = (secondes - tampon)/60;
				secondes = tampon;
			}
			if(secondes > 0) {
				retour = secondes + "s";
			}
			if(minutes >= 60) {
				tampon = minutes%60;
				heures = (minutes - tampon)/60;
				minutes = minutes%60;
			}
			if(minutes > 0) {
				retour = minutes + "min" + retour;
			}
			if(heures >= 24) {
				tampon = heures%24;
				jours = (heures - tampon)/24;
				heures = tampon;
			}
			if(heures > 0) {
				retour = heures + "h" + retour;
			}
			if(jours > 0) {
				retour = jours + "j" + retour;
			}
		}
		
		return retour;
		
	}
	
	
}

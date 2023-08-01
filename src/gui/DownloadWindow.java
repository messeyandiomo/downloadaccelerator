package gui;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import utils.Download;
import utils.DownloadControl;
import utils.DownloadDirs;
import utils.DownloadProps;
import utils.StatisticsManager;

@SuppressWarnings("serial")
public class DownloadWindow extends JInternalFrame{
	
	private DownloadWindowLabel fileName;
	private SubDownloadProgressBar[] subDownloadWindows;
	private SubDownloadsContainer containerSubDownloadWindows;
	private FileConcatContainer containerFileConcatProgressBar;
	private JPanel containerProgressBars = new JPanel();
	private JPanel containerProgressBarsDownloadWindowButtons = new JPanel();
	private DownloadWindowLabel size;
	private DownloadWindowLabel speed;
	private DownloadWindowLabel duration;
	private DownloadWindowLabel state;
	private JPanel containerStatistics = new JPanel();
	
	private DownloadWindowButton buttonSuspend;
	private DownloadWindowButton buttonResume;
	private DownloadWindowButton buttonStop;
	private JPanel containerRight = new JPanel();
	private JPanel container = new JPanel();
	private JInternalFrame downloadwindow;
	
	private Download download = null;
	private StatisticsManager statisticsManager = null;
	private Font fontSize = new Font("Serif", Font.BOLD, 10);
	
	
	public DownloadWindow(DownloadProps downloadprops) {
		
		Rectangle graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int coef = 50;
		int largeurDownloadWindowButton = (int)(graphicEnvironment.getHeight()/coef);
		int longueurDownloadWindowButton = 2*largeurDownloadWindowButton;
		Dimension dimDownloadWindowButton = new Dimension(longueurDownloadWindowButton, largeurDownloadWindowButton);
		Dimension dimFileName = new Dimension(16*longueurDownloadWindowButton, largeurDownloadWindowButton);
		Dimension dimStats = new Dimension((int)(dimFileName.width/3), dimFileName.height);
		Dimension dimLogo = new Dimension(3*dimFileName.height, 3*dimFileName.height);
		int subDownloadsContainerWidth = dimFileName.width - 2*(dimDownloadWindowButton.width);
		int subDownloadsContainerHeight = dimDownloadWindowButton.height;
		int filenameMaxLength = 60;/**maximum number of filename printable character **/
				
		statisticsManager = new StatisticsManager();
		this.download = new Download(statisticsManager, downloadprops);
			
		/** create filename **/
		fileName = new DownloadWindowLabel(downloadprops.getFilename(), filenameMaxLength);
		
		/** progress bars container **/
		containerProgressBars.setLayout(new BoxLayout(containerProgressBars, BoxLayout.PAGE_AXIS));
		containerSubDownloadWindows = new SubDownloadsContainer(download, downloadprops, subDownloadsContainerWidth, subDownloadsContainerHeight);
		containerProgressBars.add(containerSubDownloadWindows);
		containerFileConcatProgressBar = new FileConcatContainer(download, dimFileName.width - dimDownloadWindowButton.width, dimDownloadWindowButton.height, downloadprops.getSize());
		containerProgressBars.add(containerFileConcatProgressBar);
		
		/** make containerFileConcatProgressBar invisible **/
		containerFileConcatProgressBar.setVisible(false);
		
		/** create buttons **/
		buttonSuspend = new DownloadWindowButton(download, DownloadDirs.getImagesDir() + "suspend.png", longueurDownloadWindowButton, largeurDownloadWindowButton, false);
		buttonResume = new DownloadWindowButton(download, DownloadDirs.getImagesDir() + "resume.png", longueurDownloadWindowButton, largeurDownloadWindowButton, true);
		buttonStop = new DownloadWindowButton(DownloadDirs.getImagesDir() + "stop.png", longueurDownloadWindowButton, largeurDownloadWindowButton);
		
		/** progress bars and buttons container **/
		containerProgressBarsDownloadWindowButtons.setLayout(new BoxLayout(containerProgressBarsDownloadWindowButtons, BoxLayout.LINE_AXIS));
		containerProgressBarsDownloadWindowButtons.add(containerProgressBars);
		containerProgressBarsDownloadWindowButtons.add(buttonSuspend);
		containerProgressBarsDownloadWindowButtons.add(buttonResume);
		containerProgressBarsDownloadWindowButtons.add(buttonStop);
		containerProgressBarsDownloadWindowButtons.setMaximumSize(dimFileName);
		
		/** make resume button invisible **/
		buttonResume.setVisible(false);
		
		/** creation of statistics DownloadWindowLabels **/
		size = new DownloadWindowLabel(statisticsManager, "Size", dimStats, downloadprops.getSize());
		speed = new DownloadWindowLabel(statisticsManager, "Speed", dimStats, downloadprops.getSize());
		duration = new DownloadWindowLabel(statisticsManager, "Duration", dimStats, downloadprops.getSize());
		state = new DownloadWindowLabel(download, dimStats, downloadprops.getSize());
		
		/** setting up of statistics container **/
		containerStatistics.setLayout(new BoxLayout(containerStatistics, BoxLayout.LINE_AXIS));
		containerStatistics.add(size);
		containerStatistics.add(speed);
		containerStatistics.add(duration);
		containerStatistics.add(state);
		state.setVisible(false);
				
		/** assembly of filename, progress bars container and statistics container **/
		containerRight.setLayout(new BoxLayout(containerRight, BoxLayout.PAGE_AXIS));
		containerRight.add(fileName);
		containerRight.add(containerProgressBarsDownloadWindowButtons);
		containerRight.add(containerStatistics);
		containerRight.setPreferredSize(new Dimension(dimFileName.width, 3*dimFileName.height));
		
		/** creation of logo **/
		Logo logo = new Logo(downloadprops.getFileLogo(), dimLogo);
		
		/** assembly of image and containerRight into container **/
		container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
		container.add(logo);
		container.add(containerRight);
		
		/** buttons listeners **/
		buttonSuspend.addActionListener(new SuspendDownloadWindowButtonListener());
		buttonResume.addActionListener(new ResumeDownloadWindowButtonListener());
		buttonStop.addActionListener(new StopDownloadWindowButtonListener());
		
		downloadwindow = this;
		this.setContentPane(container);
		((BasicInternalFrameUI)getUI()).setNorthPane(null);
		this.pack();
		this.setVisible(true);
		this.setFont(fontSize);
		
		this.download.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, boolean suspend, long infos, boolean shutdown) {
				// TODO Auto-generated method stub
				if(complete) {
					if(containerSubDownloadWindows.isVisible())
						containerSubDownloadWindows.setVisible(false);
				}
				else {
					if(suspend) {
						if(!statisticsManager.isSuspended())
							statisticsManager.pause();
						containerSubDownloadWindows.pause();
					}
					else {
						/** the position of these instructions is very important. there is before **/
						size.reset();
						duration.reset();
						if(statisticsManager.isSuspended())
							statisticsManager.restart();
						containerSubDownloadWindows.restart();
					}
				}
			}
		});
	}
	
	
	public SubDownloadProgressBar getSubDownloadProgressBar(int i) {
		
		return subDownloadWindows[i];
		
	}
	
	
	/**
	 * @return the download
	 */
	public Download getDownload() {
		return download;
	}


	/**
	 * @param download the download to set
	 */
	public void setDownload(Download download) {
		this.download = download;
	}
	
	/**
	 * set the title of download window
	 */
	public void setDownloadName(String title) {
		this.fileName.setTitle(title);
	}
	
	/**
	 * set the size of download window
	 */
	public void setDownloadSize(long size) {
		this.containerFileConcatProgressBar.setString(DownloadControl.convertSize(size));
	}


	/** écouteur du bouton suspend **/
	class SuspendDownloadWindowButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(!statisticsManager.isSuspended())
				statisticsManager.pause();
			containerSubDownloadWindows.pause();
			buttonSuspend.setVisible(false);
			buttonResume.setVisible(true);
		}
		
	}
	
	/** écouteur du bouton resume **/
	class ResumeDownloadWindowButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(download.hasSuspended()) {
				size.reset();
				duration.reset();
				statisticsManager.reset();
				if(statisticsManager.isSuspended())
					statisticsManager.restart();
				containerSubDownloadWindows.restart();
			}
			else {
				if(statisticsManager.isSuspended())
					statisticsManager.revive();
				containerSubDownloadWindows.revive();
			}
			buttonResume.setVisible(false);
			buttonSuspend.setVisible(true);
		}
		
	}
	
	/** écouteur du bouton stop **/
	class StopDownloadWindowButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			DownloadWindowsContainer downloadwindowscontainer = DownloadWindowsContainer.getInstance();
			downloadwindowscontainer.removeDownloadWindow(downloadwindow);
		}
		
	}
	
}

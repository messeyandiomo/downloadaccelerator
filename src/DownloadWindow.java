import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;

@SuppressWarnings("serial")
public class DownloadWindow extends JInternalFrame{
	
	private Label fileName;
	private SubDownloadProgressBar[] subDownloadWindows;
	private SubDownloadProgressBar fileConcatProgressBar;
	private SubDownloadsContainer containerSubDownloadWindows;
	private FileConcatContainer containerFileConcatProgressBar;
	private JPanel containerProgressBars = new JPanel();
	private JPanel containerProgressBarsButtons = new JPanel();
	private Label size;
	private Label speed;
	private Label duration;
	private Label state;
	private JPanel containerStatistics = new JPanel();
	
	private Button buttonSuspend;
	private Button buttonResume;
	private Button buttonStop;
	private JPanel containerRight = new JPanel();
	private JPanel container = new JPanel();
	private JInternalFrame downloadwindow;
	
	//private int subDownloadProgressBarCount;
	private Download download = null;
	private StatisticsManager statisticsManager = null;
	private Font fontSize = new Font("Serif", Font.BOLD, 10);//police de caractere utilise
	
	private long filesize = 0;
	
	
	public DownloadWindow(DownloadProperties downloadproperties, DownloadDirectories downloaddirectories, FileProperties fileproperties) {
				
		Rectangle graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int coef = 50;
		int largeurButton = (int)(graphicEnvironment.getHeight()/coef);
		int longueurButton = 2*largeurButton;
		Dimension dimButton = new Dimension(longueurButton, largeurButton);
		Dimension dimFileName = new Dimension(16*longueurButton, largeurButton);
		Dimension dimStats = new Dimension((int)(dimFileName.width/3), dimFileName.height);
		Dimension dimLogo = new Dimension(3*dimFileName.height, 3*dimFileName.height);
		int subDownloadsContainerWidth = dimFileName.width - 2*(dimButton.width);
		int subDownloadsContainerHeight = dimButton.height;
		int filenameMaxLength = 60;//maximum number of filename printable character
		
		//FileProperties fileproperties = downloadproperties.getFileProperties();
		String imagesDirectory = DownloadDirectories.getImagesDirectory();//images directory
		String filename = fileproperties.getFilename();
		
		filesize = fileproperties.getSize();
				
		statisticsManager = new StatisticsManager();
		this.download = new Download(statisticsManager, downloadproperties, downloaddirectories, filename);
			
		//create filename
		fileName = new Label(filename, filenameMaxLength);
		
		//progress bars container
		containerProgressBars.setLayout(new BoxLayout(containerProgressBars, BoxLayout.PAGE_AXIS));
		containerSubDownloadWindows = new SubDownloadsContainer(download, downloadproperties, subDownloadsContainerWidth, subDownloadsContainerHeight);
		containerProgressBars.add(containerSubDownloadWindows);
		containerFileConcatProgressBar = new FileConcatContainer(download, dimFileName.width - dimButton.width, dimButton.height, filesize);
		containerProgressBars.add(containerFileConcatProgressBar);
		
		//make containerFileConcatProgressBar invisible
		containerFileConcatProgressBar.setVisible(false);
		
		//create buttons
		buttonSuspend = new Button(download, imagesDirectory + "suspend.png", longueurButton, largeurButton, false);
		buttonResume = new Button(download, imagesDirectory + "resume.png", longueurButton, largeurButton, true);
		buttonStop = new Button(imagesDirectory + "stop.png", longueurButton, largeurButton);
		
		//progress bars and buttons container
		containerProgressBarsButtons.setLayout(new BoxLayout(containerProgressBarsButtons, BoxLayout.LINE_AXIS));
		containerProgressBarsButtons.add(containerProgressBars);
		containerProgressBarsButtons.add(buttonSuspend);
		containerProgressBarsButtons.add(buttonResume);
		containerProgressBarsButtons.add(buttonStop);
		containerProgressBarsButtons.setMaximumSize(dimFileName);
		
		//make resume button invisible
		buttonResume.setVisible(false);
		
		//creation of statistics labels
		size = new Label(statisticsManager, "Size", dimStats, filesize);
		speed = new Label(statisticsManager, "Speed", dimStats, filesize);
		duration = new Label(statisticsManager, "Duration", dimStats, filesize);
		state = new Label(download, dimStats, filesize);
		
		//setting up of statistics container
		containerStatistics.setLayout(new BoxLayout(containerStatistics, BoxLayout.LINE_AXIS));
		containerStatistics.add(size);
		containerStatistics.add(speed);
		containerStatistics.add(duration);
		containerStatistics.add(state);
		state.setVisible(false);
				
		//assembly of filename, progress bars container and statistics container
		containerRight.setLayout(new BoxLayout(containerRight, BoxLayout.PAGE_AXIS));
		containerRight.add(fileName);
		containerRight.add(containerProgressBarsButtons);
		containerRight.add(containerStatistics);
		containerRight.setPreferredSize(new Dimension(dimFileName.width, 3*dimFileName.height));
		
		//creation of logo
		Logo logo = new Logo(fileproperties.getFileLogo(), dimLogo);
		
		//assembly of image and containerRight into container
		container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
		container.add(logo);
		container.add(containerRight);
		
		//buttons listeners
		buttonSuspend.addActionListener(new SuspendButtonListener());
		buttonResume.addActionListener(new ResumeButtonListener());
		buttonStop.addActionListener(new StopButtonListener());
		
		downloadwindow = this;
		this.setContentPane(container);
		((BasicInternalFrameUI)getUI()).setNorthPane(null);
		this.pack();
		this.setVisible(true);
		this.setFont(fontSize);
	}
	
	
	public SubDownloadProgressBar getSubDownloadProgressBar(int i) {
		
		return subDownloadWindows[i];
		
	}
	
	public SubDownloadProgressBar getFileConcatProgressBar() {
		
		return fileConcatProgressBar;
		
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
	
	

	//écouteur du bouton suspend
	class SuspendButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			containerSubDownloadWindows.pause();
			buttonSuspend.setVisible(false);
			buttonResume.setVisible(true);
		}
		
	}
	
	//écouteur du bouton resume
	class ResumeButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			containerSubDownloadWindows.revive();
			buttonResume.setVisible(false);
			buttonSuspend.setVisible(true);
		}
		
	}
	
	//écouteur du bouton stop
	class StopButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
//			download.stop2();
			DownloadWindowsContainer downloadwindowscontainer = DownloadWindowsContainer.getInstance();
			downloadwindowscontainer.removeDownloadWindow(downloadwindow);
		}
		
	}
	
}

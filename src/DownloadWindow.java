import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;

@SuppressWarnings("serial")
public class DownloadWindow extends JInternalFrame{
	
	private JLabel labelFileName = new JLabel();
	private JPanel containerFileName = new JPanel();
	private SubDownloadProgressBar[] subDownloadWindows;
	private SubDownloadProgressBar fileConcatProgressBar;
	private JPanel containerSubDownloadWindows = new JPanel();
	private JLabel labelDownloadWindowCurrentSize = new JLabel("Size: ");
	private JLabel labelDownloadWindowCurrentSizeInfos = new JLabel();
	private JPanel containerDownloadWindowCurrentSize = new JPanel();
	private JLabel labelDownloadWindowSpeed = new JLabel("Speed: ");
	private JLabel labelDownloadWindowSpeedInfos = new JLabel();
	private JPanel containerDownloadWindowSpeed = new JPanel();
	private JLabel labelDownloadWindowDuration = new JLabel("Duration: ");
	private JLabel labelDownloadWindowDurationInfos = new JLabel();
	private JPanel containerDownloadWindowDuration = new JPanel();
	private JLabel labelDownloadWindowState = new JLabel();
	private JPanel containerDownloadWindowInfos = new JPanel();
	private JButton buttonSuspend = new JButton();
	private JButton buttonResume = new JButton();
	private JButton buttonStop = new JButton();
	private JPanel containerRight = new JPanel();
	private JPanel container = new JPanel();
	private JInternalFrame downloadwindow;
	
	private int subDownloadProgressBarCount;
	private Download download = null;
	private long filesize = 0;
	private Font fontSize = new Font("Serif", Font.BOLD, 10);//police de caractere utilise
	
	
	
	public DownloadWindow(String logo, URL url, String filename, String dir, String temp, long size, int subDownloadWindowsCount){
		
		//mise en place du nombre de sous téléchargement
		if(size <= 0) {
			subDownloadProgressBarCount = 1;
		}
		else {
			subDownloadProgressBarCount = subDownloadWindowsCount;
		}
		
		Rectangle graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int coef = 50;
		int largeurButton = (int)(graphicEnvironment.getHeight()/coef);
		int longueurButton = 2*largeurButton;
		Dimension dimButton = new Dimension(longueurButton, largeurButton);
		Dimension dimFileName = new Dimension(16*longueurButton, largeurButton);
		Dimension dimInfos = new Dimension((int)(dimFileName.width/3), dimFileName.height);
		int largeurLogo = 3*dimFileName.height;
		int largeurIconButton = (int)(3*largeurButton/4);
		int subDownloadWindowLength = (int)((dimFileName.width - 2*(dimButton.width))/subDownloadProgressBarCount);
		int filenameMaxLength = 60;//nombre  maximum de caractères affichable du filename
		String imagesDirectory = DownloadControl.getInstance().getImagesDirectory();//repertoire des images
		
		//mise en place du filename
		if(filename.length() > filenameMaxLength) {
			labelFileName.setText(filename.substring(filename.length() - filenameMaxLength));
		}
		else {
			labelFileName.setText(filename);
		}
		labelFileName.setFont(fontSize);//modification de la police
		containerFileName.setLayout(new BoxLayout(containerFileName, BoxLayout.LINE_AXIS));
		containerFileName.add(labelFileName);
		
		//mise en place des barres de défilement
		containerSubDownloadWindows.setLayout(new BoxLayout(containerSubDownloadWindows, BoxLayout.LINE_AXIS));
		subDownloadWindows = new SubDownloadProgressBar[subDownloadProgressBarCount];
		long restOfSize = size%subDownloadProgressBarCount;//excédent d'octets
		long subdownloadsize = (size -restOfSize)/subDownloadProgressBarCount;//taille du sous téléchargement
		for (int i = 0; i < subDownloadProgressBarCount; i++) {
			if(i == 0) {
				subDownloadWindows[i] = new SubDownloadProgressBar(0, subDownloadWindowLength, subdownloadsize + restOfSize);//on ajoute l'exédent d'octets sur le premier sous téléchargement
			}
			else {
				subDownloadWindows[i] = new SubDownloadProgressBar(0, subDownloadWindowLength, subdownloadsize);
			}
			subDownloadWindows[i].setPreferredSize(new Dimension(subDownloadWindowLength, dimButton.height));
			containerSubDownloadWindows.add(subDownloadWindows[i]);
		}
		//barre de concatenation final
		fileConcatProgressBar = new SubDownloadProgressBar(0, dimFileName.width - dimButton.width, size);
		fileConcatProgressBar.setPreferredSize(new Dimension(dimFileName.width - dimButton.width, dimButton.height));
		containerSubDownloadWindows.add(fileConcatProgressBar);
		//rendre la barre de concatenation invisible et le label de fin du téléchargement
		fileConcatProgressBar.setVisible(false);
		
		//dimensionnement des boutons
		buttonSuspend.setPreferredSize(dimButton);
		buttonResume.setPreferredSize(dimButton);
		buttonStop.setPreferredSize(dimButton);
		
		//mise en place des logos sur les buttons
		try {
			Image imgSuspend = ImageIO.read(new File(imagesDirectory + "suspend.png")).getScaledInstance(largeurIconButton, largeurIconButton, Image.SCALE_DEFAULT);
			buttonSuspend.setIcon(new ImageIcon(imgSuspend));
			Image imgResume = ImageIO.read(new File(imagesDirectory + "resume.png")).getScaledInstance(largeurIconButton, largeurIconButton, Image.SCALE_DEFAULT);
			buttonResume.setIcon(new ImageIcon(imgResume));
			Image imgStop = ImageIO.read(new File(imagesDirectory + "stop.png")).getScaledInstance(largeurIconButton, largeurIconButton, Image.SCALE_DEFAULT);
			buttonStop.setIcon(new ImageIcon(imgStop));
		}catch (IOException ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}
				
		//ajout des boutons dans le conteneur de subDownloadWindows
		containerSubDownloadWindows.add(buttonSuspend);
		containerSubDownloadWindows.add(buttonResume);
		containerSubDownloadWindows.add(buttonStop);
		containerSubDownloadWindows.setMaximumSize(dimFileName);
		
		//rendre le bouton resume invisible
		buttonResume.setVisible(false);
		
		//mise en place des infos du telechargement
		labelDownloadWindowCurrentSize.setFont(fontSize);
		labelDownloadWindowCurrentSizeInfos.setFont(fontSize);
		containerDownloadWindowCurrentSize.setLayout(new BoxLayout(containerDownloadWindowCurrentSize, BoxLayout.LINE_AXIS));
		containerDownloadWindowCurrentSize.add(labelDownloadWindowCurrentSize);
		containerDownloadWindowCurrentSize.add(labelDownloadWindowCurrentSizeInfos);
		labelDownloadWindowSpeed.setFont(fontSize);
		labelDownloadWindowSpeedInfos.setFont(fontSize);
		containerDownloadWindowSpeed.setLayout(new BoxLayout(containerDownloadWindowSpeed, BoxLayout.LINE_AXIS));
		containerDownloadWindowSpeed.add(labelDownloadWindowSpeed);
		containerDownloadWindowSpeed.add(labelDownloadWindowSpeedInfos);
		labelDownloadWindowDuration.setFont(fontSize);
		labelDownloadWindowDurationInfos.setFont(fontSize);
		containerDownloadWindowDuration.setLayout(new BoxLayout(containerDownloadWindowDuration, BoxLayout.LINE_AXIS));
		containerDownloadWindowDuration.add(labelDownloadWindowDuration);
		containerDownloadWindowDuration.add(labelDownloadWindowDurationInfos);
		containerDownloadWindowCurrentSize.setMaximumSize(dimInfos);
		containerDownloadWindowSpeed.setMaximumSize(dimInfos);
		containerDownloadWindowDuration.setMaximumSize(dimInfos);
		containerDownloadWindowInfos.setLayout(new BoxLayout(containerDownloadWindowInfos, BoxLayout.LINE_AXIS));
		containerDownloadWindowInfos.add(containerDownloadWindowCurrentSize);
		containerDownloadWindowInfos.add(containerDownloadWindowSpeed);
		containerDownloadWindowInfos.add(containerDownloadWindowDuration);
		containerDownloadWindowInfos.add(labelDownloadWindowState);
		labelDownloadWindowState.setFont(fontSize);
		labelDownloadWindowState.setVisible(false);
		
				
		//assemblage des conteneurs de filename, des barres de défilements et des infos du téléchargement
		containerRight.setLayout(new BoxLayout(containerRight, BoxLayout.PAGE_AXIS));
		containerRight.add(containerFileName);
		containerRight.add(containerSubDownloadWindows);
		containerRight.add(containerDownloadWindowInfos);
				
		//assemblage des conteneurs ImageIcon et Right
		container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
		
		//mise en place du logo
		try {
			Image imgLogo = ImageIO.read(new File(imagesDirectory + logo));
			Logo logopane = new Logo(imgLogo);
			logopane.setPreferredSize(new Dimension(largeurLogo, largeurLogo));
			container.add(logopane);
		}catch(IOException e){
			// TODO: handle exception
			e.printStackTrace();
		}
		container.add(containerRight);
		
		//ajout des écouteurs de boutons suspend, resume et stop
		buttonSuspend.addActionListener(new SuspendButtonListener());
		buttonResume.addActionListener(new ResumeButtonListener());
		buttonStop.addActionListener(new StopButtonListener());
		
		downloadwindow = this;
		this.setContentPane(container);
		((BasicInternalFrameUI)getUI()).setNorthPane(null);
		this.pack();
		this.setVisible(true);
		
		//lancement du téléchargement
		setDownload(DownloadControl.getInstance().launchDownload(this, url, dir, temp, filename, subDownloadProgressBarCount));
		setFilesize(size);
		this.setFont(fontSize);
	}
	
	
	public SubDownloadProgressBar getSubDownloadProgressBar(int i) {
		
		return subDownloadWindows[i];
		
	}
	
	public SubDownloadProgressBar getFileConcatProgressBar() {
		
		return fileConcatProgressBar;
		
	}
	
	
	public JLabel getDownloadWindowCurrentSizeInfos() {
		
		return labelDownloadWindowCurrentSizeInfos;
		
	}
	
	
	public JLabel getDownloadWindowSpeedInfos() {
		
		return labelDownloadWindowSpeedInfos;
		
	}
	
	
	public JLabel getDownloadWindowDurationInfos() {
		
		return labelDownloadWindowDurationInfos;
		
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
	
	
	
	//préparation à la suspension du télécharegemnt
	public void suspend2() {
		containerDownloadWindowSpeed.setVisible(false);
		containerDownloadWindowDuration.setVisible(false);
		labelDownloadWindowState.setText("SUSPENDED");
		labelDownloadWindowState.setVisible(true);
	}
	
	
	//préparation à la relance du téléchargement
	public void resume2() {
		labelDownloadWindowState.setText("");
		labelDownloadWindowState.setVisible(false);
		containerDownloadWindowSpeed.setVisible(true);
		containerDownloadWindowDuration.setVisible(true);
	}
	
	
	//préparation à la concaténation des sous fichiers téléchargés
	public void prepareForConcat() {
		for(int i = 0; i < subDownloadProgressBarCount; i++) {
			this.getSubDownloadProgressBar(i).setVisible(false);
		}
		this.getFileConcatProgressBar().setVisible(true);
		buttonSuspend.setVisible(false);
		containerDownloadWindowCurrentSize.setVisible(false);
		containerDownloadWindowSpeed.setVisible(false);
		containerDownloadWindowDuration.setVisible(false);
		labelDownloadWindowState.setText("Building the file");
		labelDownloadWindowState.setVisible(true);
	}
	
	
	//annonce de la fin du téléchargement
	public void setEndOf(String filesize) {
		this.getFileConcatProgressBar().setStringPainted(true);
		this.getFileConcatProgressBar().setFont(fontSize);
		this.getFileConcatProgressBar().setString("COMPLETED");
		labelDownloadWindowState.setText(filesize);
	}
	
	
	//indication de l'erreur du sous téléchargement
	public void displayError(int subdownloadnumber, int error) {
		this.getSubDownloadProgressBar(subdownloadnumber).setStringPainted(true);
		this.getSubDownloadProgressBar(subdownloadnumber).setFont(fontSize);
		this.getSubDownloadProgressBar(subdownloadnumber).setString("" + error);
	}
	

	//écouteur du bouton suspend
	class SuspendButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			download.suspend2();
			buttonSuspend.setVisible(false);
			buttonResume.setVisible(true);
		}
		
	}
	
	//écouteur du bouton resume
	class ResumeButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			download.resume2();
			buttonResume.setVisible(false);
			buttonSuspend.setVisible(true);
		}
		
	}
	
	//écouteur du bouton stop
	class StopButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			download.stop2();
			DownloadWindowsContainer downloadwindowscontainer = DownloadWindowsContainer.getInstance();
			downloadwindowscontainer.removeDownloadWindow(downloadwindow);
		}
		
	}
	
}

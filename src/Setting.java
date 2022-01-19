import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import extractor.Youtube;


@SuppressWarnings("serial")
public class Setting extends JFrame{
	
	private JLabel labelUrl = new JLabel("Url");
	private JTextField parameterUrl = new JTextField();
	private JButton buttonUrl = new JButton("OK");
	private JLabel labelIconUrl = new JLabel(null, null, JLabel.CENTER);
	private JPanel containerUrl = new JPanel();
	private JLabel labelErreur = new JLabel();
	private JPanel containerErreur = new JPanel();
	private JLabel labelDestination = new JLabel("Destination");
	private JTextField parameterDestination = new JTextField();
	private JButton buttonDestination = new JButton("...");
	private JPanel containerDestination = new JPanel();
	private JLabel labelFileName = new JLabel("Filename");
	private JTextField parameterFileName = new JTextField();
	private JLabel labelSizeFileName = new JLabel("", JLabel.CENTER);
	private JPanel containerFileName = new JPanel();
	private JButton buttonCancel = new JButton("Cancel");
	private JButton buttonStart = new JButton("Launch");
	private JPanel containerStart = new JPanel();
	private JPanel containerBottom = new JPanel();
	private JPanel container = new JPanel();
	private JFrame window;
	
	private int hauteurWidget = 25;
	private int largeurLabel = 120;
    private int largeurParameter = 340;
    private int largeurAdded = 60;
    private int largeurRigidArea = 3;
    private int largeurWidget = largeurLabel+largeurParameter+largeurAdded+3*largeurRigidArea;
    private int largeurButton = (largeurParameter - largeurRigidArea)/4;
    
    private Dimension dimLabel = new Dimension(largeurLabel, hauteurWidget);
    private Dimension dimParameter = new Dimension(largeurParameter, hauteurWidget);
    private Dimension dimAdded = new Dimension(largeurAdded, hauteurWidget);
    private Dimension dimHRigidArea = new Dimension(largeurRigidArea, 0);
    private Dimension dimVRigidArea = new Dimension(0, largeurRigidArea);
    
    private DownloadProperties downloadProperties = null;
    private DownloadProperties audioDownloadProperties = null;
    private DownloadDirectories downloadDirectories = new DownloadDirectories();
    private FileProperties fileProperties = null;
    private FileProperties audioFileProperties = null;
    private RequestProperties requestProperties = null;
    private SubDownloadPropertiesFactoriesManager subDownloadPropertiesFactoriesManager = null;
	
    
    /**
     * Download file without knowing any parameter
     * 
    */
	public Setting() {
        
		buildGuiSetting();        
        labelIconUrl.setVisible(false);
        
        this.setContentPane(container);
        SwingUtilities.updateComponentTreeUI(container);
        this.pack();
        this.setVisible(true);
	}
	
	/**
	 * Download file from just parameter url
	 * 
	 * @param url
	 */
	public Setting(String url) {
		
		buildGuiSetting();
		this.parameterUrl.setText(url);
		labelIconUrl.setVisible(false);
		
		this.setContentPane(container);
        SwingUtilities.updateComponentTreeUI(container);
        this.pack();
        this.setVisible(true);
	}
	
	/**
	 * The video just must be download 
	 * 
	 * @param filename
	 * @param fileType
	 * @param contentLength
	 * @param url
	 */
	public Setting(String filename, String fileType, long contentLength, String url) {
		
		buildGuiSetting();
		this.parameterUrl.setText(url);
		String nameOfFile = filename + "." + fileType.split("/")[1];
		this.fileProperties = new FileProperties(url, nameOfFile, fileType.split("/")[0], fileType.split("/")[1], contentLength);
		
		prepareSetting(this.fileProperties);
		
		this.setContentPane(container);
        SwingUtilities.updateComponentTreeUI(container);
        this.pack();
        this.setVisible(true);
	}
	
	/**
	 * The video and audio must be download and then be merge
	 * 
	 * @param filename
	 * @param fileType
	 * @param contentLength
	 * @param urlvideo
	 * @param audioFileType
	 * @param contentLengthAudio
	 * @param urlAudio
	 */
	public Setting(String filename, String fileType, long contentLength, String urlvideo, String audioFileType, long contentLengthAudio, String urlaudio) {
		
		String videofilename = filename + "." + fileType.split("/")[1];
		String audiofilename = filename + ".audio." + audioFileType.split("/")[1];
		
		buildGuiSetting();
		this.parameterUrl.setText(urlvideo);
		this.fileProperties = new FileProperties(urlvideo, videofilename, fileType.split("/")[0], fileType.split("/")[1], contentLength);
		this.audioFileProperties = new FileProperties(urlaudio, audiofilename, audioFileType.split("/")[0], audioFileType.split("/")[1], contentLengthAudio);
		
		prepareSetting(this.fileProperties, this.audioFileProperties);
		
		this.setContentPane(container);
        SwingUtilities.updateComponentTreeUI(container);
        this.pack();
        this.setVisible(true);
	}
	
	/**
	 * The video URL is ciphered and have to be decipher then the video will be download
	 * 
	 * @param filename
	 * @param fileType
	 * @param contentLength
	 * @param signatureCipher
	 * @param playerUrl
	 * @param useragent
	 * @param cookies
	 */
	public Setting(String filename, String fileType, long contentLength, String signatureCipher, String playerUrl, String useragent, String cookies) {
		
		String url = new Youtube(signatureCipher, playerUrl, downloadDirectories.getCacheDirectory()).decryptUrl();
		
		buildGuiSetting();
		this.parameterUrl.setText(url);
		String nameOfFile = filename + "." + fileType.split("/")[1];
		this.fileProperties = new FileProperties(url, nameOfFile, fileType.split("/")[0], fileType.split("/")[1], contentLength);
		
		prepareSetting(this.fileProperties);
		
		this.setContentPane(container);
        SwingUtilities.updateComponentTreeUI(container);
        this.pack();
        this.setVisible(true);
	}
	
	/**
	 * The video and audio URLS are ciphered and have to be decipher then the video and audio will be download and finally merge
	 * 
	 * @param filename
	 * @param fileType
	 * @param contentLength
	 * @param signatureCipher
	 * @param playerUrl
	 * @param fileTypeAudio
	 * @param contentLengthAudio
	 * @param signatureCipherAudio
	 * @param useragent
	 * @param cookies
	 */
	public Setting(String filename, String fileType, long contentLength, String signatureCipher, String playerUrl, String fileTypeAudio, long contentLengthAudio, String signatureCipherAudio, String useragent, String cookies) {
		
		String urlvideo = new Youtube(signatureCipher, playerUrl, downloadDirectories.getCacheDirectory()).decryptUrl();
		String urlaudio = new Youtube(signatureCipherAudio, playerUrl, downloadDirectories.getCacheDirectory()).decryptUrl();
		String filenamevideo = filename + "." + fileType.split("/")[1];
		String filenameaudio = filename + ".audio." + fileTypeAudio.split("/")[1];
		
		buildGuiSetting();
		this.parameterUrl.setText(urlvideo);
		this.fileProperties = new FileProperties(urlvideo, filenamevideo, fileType.split("/")[0], fileType.split("/")[1], contentLength);
		this.audioFileProperties = new FileProperties(urlaudio, filenameaudio, fileTypeAudio.split("/")[0], fileTypeAudio.split("/")[1], contentLengthAudio);
		
		prepareSetting(this.fileProperties, this.audioFileProperties);
		
		this.setContentPane(container);
        SwingUtilities.updateComponentTreeUI(container);
        this.pack();
        this.setVisible(true);
		
	}
	
	
	void buildGuiSetting() {
		
		Font font = new Font("Serif", Font.BOLD, 12);
		
		UIManager.put("Label.font", font);//modification de la police des labels
		UIManager.put("Button.font", font);//modification de la police des boutons
		
		this.setTitle("Create Download");
		try {
			this.setIconImage(ImageIO.read(new File(DownloadDirectories.getPathLogo())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        
        labelUrl.setPreferredSize(dimLabel);
        parameterUrl.setPreferredSize(dimParameter);
        buttonUrl.setPreferredSize(dimAdded);
        buttonUrl.setContentAreaFilled(false);
        labelIconUrl.setPreferredSize(dimAdded);
        containerUrl.setLayout(new BoxLayout(containerUrl, BoxLayout.LINE_AXIS));
        containerUrl.add(Box.createRigidArea(dimHRigidArea));
        containerUrl.add(labelUrl);
        containerUrl.add(parameterUrl);
        containerUrl.add(Box.createRigidArea(dimHRigidArea));
        containerUrl.add(buttonUrl);
        containerUrl.add(labelIconUrl);
        containerUrl.add(Box.createRigidArea(dimHRigidArea));
        
        labelErreur.setFont(new Font("Serif", Font.ITALIC, 12));
        labelErreur.setForeground(Color.RED);
        labelErreur.setPreferredSize(new Dimension(dimParameter));
        containerErreur.setLayout(new BoxLayout(containerErreur, BoxLayout.LINE_AXIS));
        containerErreur.add(Box.createRigidArea(dimLabel));
        containerErreur.add(labelErreur);
        containerErreur.add(Box.createRigidArea(dimAdded));
        
        labelDestination.setPreferredSize(dimLabel);
        parameterDestination.setEditable(false);
        parameterDestination.setPreferredSize(dimParameter);
        buttonDestination.setPreferredSize(dimAdded);
        buttonDestination.setContentAreaFilled(false);
        containerDestination.setLayout(new BoxLayout(containerDestination, BoxLayout.LINE_AXIS));
        containerDestination.add(Box.createRigidArea(dimHRigidArea));
        containerDestination.add(labelDestination);
        containerDestination.add(parameterDestination);
        containerDestination.add(Box.createRigidArea(dimHRigidArea));
        containerDestination.add(buttonDestination);
        containerDestination.add(Box.createRigidArea(dimHRigidArea));
        
        parameterDestination.setText(downloadDirectories.getDestinationDirectory());
                
        labelFileName.setPreferredSize(dimLabel);
        parameterFileName.setPreferredSize(dimParameter);
        labelSizeFileName.setPreferredSize(dimAdded);
        containerFileName.setLayout(new BoxLayout(containerFileName, BoxLayout.LINE_AXIS));
        containerFileName.add(Box.createRigidArea(dimHRigidArea));
        containerFileName.add(labelFileName);
        containerFileName.add(parameterFileName);
        containerFileName.add(Box.createRigidArea(dimHRigidArea));
        containerFileName.add(labelSizeFileName);
        containerFileName.add(Box.createRigidArea(dimHRigidArea));
        
        buttonCancel.setPreferredSize(new Dimension(largeurButton, hauteurWidget));
        buttonCancel.setContentAreaFilled(false);
        buttonStart.setPreferredSize(new Dimension(largeurButton, hauteurWidget));
        buttonStart.setContentAreaFilled(false);
        buttonStart.setEnabled(false);
        containerStart.setLayout(new BoxLayout(containerStart, BoxLayout.LINE_AXIS));
        containerStart.add(Box.createRigidArea(new Dimension((largeurWidget-largeurParameter)/2, 0)));
        containerStart.add(buttonCancel);
        containerStart.add(Box.createRigidArea(dimHRigidArea));
        containerStart.add(buttonStart);
        containerStart.add(Box.createRigidArea(new Dimension((largeurWidget-largeurParameter)/2, 0)));
        
        containerBottom.setLayout(new BoxLayout(containerBottom, BoxLayout.PAGE_AXIS));
        containerBottom.add(Box.createRigidArea(dimVRigidArea));
		containerBottom.add(containerDestination);
		containerBottom.add(Box.createRigidArea(dimVRigidArea));
        
        containerBottom.add(Box.createRigidArea(dimVRigidArea));
        containerBottom.add(containerFileName);
        containerBottom.add(Box.createRigidArea(new Dimension(largeurWidget, hauteurWidget)));
        containerBottom.add(containerStart);
        containerBottom.add(Box.createRigidArea(dimVRigidArea));
        
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.add(containerUrl);
        
        //creation des ecouteurs de boutons
        buttonUrl.addActionListener(new ButtonListenerUrl());
        buttonDestination.addActionListener(new ButtonListener());
        buttonCancel.addActionListener(new ButtonListenerCancel());
        parameterFileName.addKeyListener(new FileNameListener());
        buttonStart.addActionListener(new startButtonListener());
        
        window = this;

	}
	
	
	void prepareSetting(FileProperties fileprperties) {
		this.prepareSetting(fileprperties, null);
	}
	
	
	void prepareSetting(FileProperties fileproperties, FileProperties audiofileproperties) {
		
		downloadProperties = new DownloadProperties(fileproperties.getUrl());
		if(audiofileproperties != null) {
			audioDownloadProperties = new DownloadProperties(audiofileproperties.getUrl());
			subDownloadPropertiesFactoriesManager = new SubDownloadPropertiesFactoriesManager(downloadProperties, audioDownloadProperties);
		}
		else
			subDownloadPropertiesFactoriesManager = new SubDownloadPropertiesFactoriesManager(downloadProperties);
		
		subDownloadPropertiesFactoriesManager.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(complete)
					buttonStart.setEnabled(true);
			}
		});
		/** verify if a file with the same name already exist in temporarily folder **/
		int res = 0;
		String[] choices = {"Yes", "No"};
		String defaultChoice = choices[0];
		
		while(fileDownloadExist(fileproperties) || fileDownloadExist(audiofileproperties)) {
			res = JOptionPane.showOptionDialog(window, "Do you want to continue(Yes) or make another download(No) ?", "This file is already begining to be download", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, defaultChoice);
			if(res == JOptionPane.NO_OPTION) {
				if(fileDownloadExist(fileproperties))
					do {
						generateAnotherFileName(fileproperties);
					}while(fileDownloadExist(fileproperties));
				if(fileDownloadExist(audiofileproperties))
					do {
						generateAnotherFileName(audiofileproperties);
					}while(fileDownloadExist(audiofileproperties));
				break;
			}
			if(res == JOptionPane.YES_OPTION)
				break;
		}
		
		if(fileproperties != null)
			createSubDownloadProperties(downloadProperties, fileproperties, subDownloadPropertiesFactoriesManager);
		if(audiofileproperties != null)
			createSubDownloadProperties(audioDownloadProperties, audiofileproperties, subDownloadPropertiesFactoriesManager);
		
		buttonUrl.setVisible(false);
		parameterFileName.setText(fileproperties.getFilename());
		drawLogo(labelIconUrl);
		labelSizeFileName.setText(DownloadControl.convertSize(fileproperties.getSize()));
		container.add(containerBottom);
	}
	
	
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub        
        new Setting();
	}
	*/
	
	public static void changeLookAndFell() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void setUIFont(FontUIResource font) {
		
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if(value instanceof FontUIResource) {
				UIManager.put(key, font);
			}
		}
	}
	
	void drawLogo(JLabel labelicon) {
		
		try {
			Image img = ImageIO.read(new File(fileProperties.getFileLogo())).getScaledInstance(hauteurWidget, hauteurWidget, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(img);
			labelicon.setIcon(icon);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		labelicon.setVisible(true);
		
	}
	
	/****
	 * verify if download of file has already begin by another task **
	 * ***/
	boolean fileDownloadExist(FileProperties fileproperties) {
		boolean res = false;
		
		if(fileproperties != null)
			res = new File(downloadDirectories.getTempDirectory() + fileproperties.getFilename() + "0").exists();
		
		return res;
	}
	
	/****
	 * generate a new temporarily file name *
	 * ***/
	String generateAnotherFileName(FileProperties fileproperties) {
		
		fileproperties.setFilename(fileproperties.getFilename() + "bis");
		return downloadDirectories.getTempDirectory() + fileproperties.getFilename();
	}
	
	/****
	 * creation of sub download properties
	 * @param 
	 * @return
	 */
	void createSubDownloadProperties(DownloadProperties downloadproperties, FileProperties fileproperties, SubDownloadPropertiesFactoriesManager subdownloadpropertiesfactoriesmanager) {
		
		long filesize = fileproperties.getSize();
		int subdownloadcount = downloadproperties.getSubDownloadCount();
		URL url = fileproperties.getUrl();
		String temporarilyfilename = downloadDirectories.getTempDirectory() + fileproperties.getFilename();
		String filetype = fileproperties.getType();
		long restOfSize = filesize%subdownloadcount;//surplus of bytes
		long subdownloadsize = (filesize - restOfSize)/subdownloadcount;//size of sub-download
		long firstoctet = 0;
		
		for (int i = 0; i < subdownloadcount; i++) {
			if(i == 0) {
				new SubDownloadPropertiesFactory(subdownloadpropertiesfactoriesmanager, i, firstoctet, subdownloadsize + restOfSize, filetype, temporarilyfilename, url, requestProperties);
				firstoctet = (subdownloadsize + restOfSize);
			}
			else {
				new SubDownloadPropertiesFactory(subdownloadpropertiesfactoriesmanager, i, firstoctet, subdownloadsize, filetype, temporarilyfilename, url, requestProperties);
				firstoctet += subdownloadsize;
			}
		}
	}
	

	//listener class of button OK
	class ButtonListenerUrl implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if(parameterUrl.getText().isEmpty()) {
				container.add(containerErreur);
				labelErreur.setText("Ce champs est vide !!!");
				parameterUrl.requestFocus();
			}
			else {
				fileProperties = new FileProperties(parameterUrl.getText());
				if(!fileProperties.hasError()) {
					container.remove(containerErreur);
					prepareSetting(fileProperties, audioFileProperties);
				}
				else {
					container.add(containerErreur);
					labelErreur.setText(fileProperties.getError());
					parameterUrl.requestFocus();
				}
			}
			SwingUtilities.updateComponentTreeUI(container);
			window.pack();
			window.setCursor(Cursor.getDefaultCursor());
		}
	}
	
	//classe d'écoute de boutons
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JFileChooser choix = new JFileChooser();
			choix.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(choix.showOpenDialog(choix.getParent()) == JFileChooser.APPROVE_OPTION){
				String dirPath = choix.getSelectedFile().getAbsolutePath();
				if(e.getSource() == buttonDestination) {
					parameterDestination.setText(dirPath);
					downloadDirectories.setDestinationDirectory(dirPath);
				}
			}
		}
	}
	
	//classe d'écoute du champs filename
	class FileNameListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			
			// TODO Auto-generated method stub
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			Pattern p = Pattern.compile("[a-zéèàA-Z0-9]");
			Pattern p2 = Pattern.compile("[a-zéèàA-Z_0-9-]|\\.");
			boolean filenameIsEmpty = parameterFileName.getText().isEmpty();
			Character keyChar = arg0.getKeyChar();
			if((filenameIsEmpty && !(p.matcher(keyChar.toString()).matches())) || (!filenameIsEmpty && !((p2.matcher(keyChar.toString()).matches())))) {
				arg0.consume();
			}
		}
	}
	
	//classe d'écoute du bouton Cancel
	class ButtonListenerCancel implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			labelIconUrl.setVisible(false);
			buttonUrl.setVisible(true);
			parameterUrl.setText(null);
			container.remove(containerBottom);
			window.pack();
		}
	}
	
	File merge(File videofile, File audioFile) {
		
		return null;
	}
	
	void linkDownloads(DownloadWindow lastwindowtofinish, DownloadWindow firstwindowtofinish, DownloadWindowsContainer containerWindow) {
		Download lastdownloadtofinish = lastwindowtofinish.getDownload();
		Download firstdownloadtofinish = firstwindowtofinish.getDownload();
		firstdownloadtofinish.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(complete && (infos >= firstwindowtofinish.getFilesize())) {
					if(!lastdownloadtofinish.isAlive()) {
						containerWindow.removeDownloadWindow(lastwindowtofinish);
						String audiopathfilename = downloadDirectories.getDestinationDirectory() + audioFileProperties.getFilename();
						String videopathfilename = downloadDirectories.getDestinationDirectory() + fileProperties.getFilename();
						String outputpathfilename = downloadDirectories.getDestinationDirectory() + fileProperties.getFilename() + ".merging." + fileProperties.getSubType();
						String[] cmd = {downloadDirectories.getFfmpegPathName(), "-i", audiopathfilename, "-i", videopathfilename, "-acodec", "copy", "-vcodec", "copy", outputpathfilename};
						ProcessBuilder pb = new ProcessBuilder(cmd);
						try {
							Process commandProcess = pb.start();
							try {
								commandProcess.waitFor();
								new File(audiopathfilename).delete();
								new File(videopathfilename).delete();
								new File(outputpathfilename).renameTo(new File(videopathfilename));
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
	}
	
	//classe d'écoute du bouton Lauch
	class startButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent ev) {
			fileProperties.setFilename(parameterFileName.getText());
			DownloadWindow newDownloadVideo = new DownloadWindow(downloadProperties, downloadDirectories, fileProperties);
			DownloadWindowsContainer containerWindow = DownloadWindowsContainer.getInstance();
			containerWindow.addDownloadWindow(newDownloadVideo);
			if(audioFileProperties != null) {
				DownloadWindow newDownloadAudio = new DownloadWindow(audioDownloadProperties, downloadDirectories, audioFileProperties);
				containerWindow.addDownloadWindow(newDownloadAudio);
				linkDownloads(newDownloadVideo, newDownloadAudio, containerWindow);
				linkDownloads(newDownloadAudio, newDownloadVideo, containerWindow);
			}
			containerWindow.toFront();
			window.dispose();
		}
	}
	
}


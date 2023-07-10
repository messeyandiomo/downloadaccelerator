package gui;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.fasterxml.jackson.databind.JsonNode;

import decryption.Common;
import decryption.Youtube;
import net.gcardone.junidecode.Junidecode;
import utils.Download;
import utils.DownloadControl;
import utils.DownloadDirs;
import utils.DownloadProps;
import utils.SubDownloadPropsFactoriesManager;



@SuppressWarnings("serial")
public class MainWindow extends JFrame{
	
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
    private boolean showbottom = false;
    
    private DownloadProps downloadProps = null;
    private DownloadProps audioDownloadProps = null;
    private static DownloadDirs downloadDirs = DownloadDirs.getInstance();
    private SubDownloadPropsFactoriesManager subDownloadPropsFactoriesManager = null;
    
    
    /**
     * Download file without knowing any parameter
     * 
    */
	public MainWindow(boolean showbottom) {
        
		Font font = new Font("Serif", Font.BOLD, 12);
		
		UIManager.put("Label.font", font);//modification de la police des labels
		UIManager.put("Button.font", font);//modification de la police des boutons
		
		this.setTitle("Create Download");
		try {
			this.setIconImage(ImageIO.read(new File(DownloadDirs.getPathLogo())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.showbottom = showbottom;
        
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
        
        parameterDestination.setText(downloadDirs.getDestinationDir());
                
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
        
        window = this;
        
        if(!this.showbottom) {
        	labelIconUrl.setVisible(false);
        	this.setContentPane(container);
            SwingUtilities.updateComponentTreeUI(container);
            this.pack();
            this.setVisible(true);
        }
        
        /*** creation des ecouteurs de boutons ***/
        buttonUrl.addActionListener(new ButtonListenerUrl());
        buttonDestination.addActionListener(new ButtonListener());
        buttonCancel.addActionListener(new ButtonListenerCancel());
        parameterFileName.addKeyListener(new FileNameListener());
        buttonStart.addActionListener(new startButtonListener());
	}
	
	/**
	 * Download file from just parameter url
	 * 
	 * @param url
	 */
	public MainWindow(String url, boolean showbottom) {
		
		this(showbottom);
		this.parameterUrl.setText(url);
	}
	
	/**
	 * The video just must be download 
	 * 
	 * @param filename
	 * @param fileType
	 * @param contentLength
	 * @param url
	 */
	public MainWindow(String filename, String fileType, long contentLength, String url) {
		
		this(url, true);
		this.downloadProps = new DownloadProps(url, (filename + "." + fileType.split("/")[1]), fileType.split("/")[0], fileType.split("/")[1], contentLength);
		completeMainWindow();
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
	public MainWindow(String filename, String fileType, long contentLength, String urlvideo, String audioFileType, long contentLengthAudio, String urlaudio) {
		
		this(urlvideo, true);
		String videofilename = filename + "." + fileType.split("/")[1];
		String audiofilename = filename + ".audio." + audioFileType.split("/")[1];
		
		this.downloadProps = new DownloadProps(urlvideo, videofilename, fileType.split("/")[0], fileType.split("/")[1], contentLength);
		this.audioDownloadProps = new DownloadProps(urlaudio, audiofilename, audioFileType.split("/")[0], audioFileType.split("/")[1], contentLengthAudio);
		completeMainWindow();
	}
	
	/**
	 * The video URL is ciphered and have to be decipher then the video will be download
	 * 
	 * @param filename
	 * @param fileType
	 * @param contentLength
	 * @param signatureCipher
	 * @param playerUrl
	 */
	public MainWindow(String filename, String fileType, long contentLength, String signatureCipher, String playerUrl) {
		
		this(filename, fileType, contentLength, new Youtube(signatureCipher, playerUrl, downloadDirs.getCacheDir()).decryptUrl());
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
	 */
	public MainWindow(String filename, String fileType, long contentLength, String signatureCipher, String playerUrl, String fileTypeAudio, long contentLengthAudio, String signatureCipherAudio) {
		
		this(filename, fileType, contentLength, new Youtube(signatureCipher, playerUrl, downloadDirs.getCacheDir()).decryptUrl(), fileTypeAudio, contentLengthAudio, new Youtube(signatureCipherAudio, playerUrl, downloadDirs.getCacheDir()).decryptUrl());
	}
	
	
	/*** complete MainWindow with bottom widget qnd other stuff ***/
	void completeMainWindow() {
		
		if(this.audioDownloadProps != null)
			this.subDownloadPropsFactoriesManager = new SubDownloadPropsFactoriesManager(this.downloadProps, this.audioDownloadProps);
		else
			this.subDownloadPropsFactoriesManager = new SubDownloadPropsFactoriesManager(this.downloadProps);
		
		subDownloadPropsFactoriesManager.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, boolean suspend, long infos) {
				// TODO Auto-generated method stub
				if(complete)
					buttonStart.setEnabled(true);
			}

			@Override
			public void init(Thread thread) {
				// TODO Auto-generated method stub
				
			}
		});
		/** verify if a file with the same name already exist in temporarily folder **/
		int res = 0;
		String[] choices = {"Yes", "No"};
		String defaultChoice = choices[0];
		
		while((this.downloadProps != null && this.downloadProps.fileDownloadedExist()) || (this.audioDownloadProps != null && this.audioDownloadProps.fileDownloadedExist())) {
			res = JOptionPane.showOptionDialog(window, "Do you want to continue(Yes) or make another download(No) ?", "This file is already begining to be download", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, defaultChoice);
			if(res == JOptionPane.NO_OPTION) {
				if(this.downloadProps != null && this.downloadProps.fileDownloadedExist())
					do {
						this.downloadProps.generateAnotherFileName();
					}while(this.downloadProps.fileDownloadedExist());
				if(this.audioDownloadProps != null && this.audioDownloadProps.fileDownloadedExist())
					do {
						this.audioDownloadProps.generateAnotherFileName();
					}while(this.audioDownloadProps.fileDownloadedExist());
				break;
			}
			if(res == JOptionPane.YES_OPTION)
				break;
		}
		
		if(this.downloadProps != null)
			this.downloadProps.createSubDownloadProps(this.subDownloadPropsFactoriesManager);
		if(this.audioDownloadProps != null)
			this.audioDownloadProps.createSubDownloadProps(this.subDownloadPropsFactoriesManager);
		
		buttonUrl.setVisible(false);
		parameterFileName.setText(this.downloadProps.getFilename());
		drawLogo(labelIconUrl);
		labelSizeFileName.setText(DownloadControl.convertSize(this.downloadProps.getSize()));
		container.add(containerBottom);
		
		if(this.showbottom) {
			labelIconUrl.setVisible(true);
			this.setContentPane(container);
	        SwingUtilities.updateComponentTreeUI(container);
	        this.pack();
	        this.setVisible(true);
		}
	}
	
	/*** draw the image type of download ***/ 
	void drawLogo(JLabel labelicon) {
		
		try {
			Image img = ImageIO.read(new File(this.downloadProps.getFileLogo())).getScaledInstance(hauteurWidget, hauteurWidget, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(img);
			labelicon.setIcon(icon);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		labelicon.setVisible(true);
	}
	
	
	/*** listener class of button OK ***/
	class ButtonListenerUrl implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if(parameterUrl.getText().isEmpty()) {
				container.add(containerErreur);
				labelErreur.setText("This field is empty !!!");
				parameterUrl.requestFocus();
				SwingUtilities.updateComponentTreeUI(container);
				window.pack();
				window.setCursor(Cursor.getDefaultCursor());
			}
			else {
				String pageurl = parameterUrl.getText();
				if(Common.getDomainName(pageurl).contains("youtube.com")){
                    if (pageurl.contains("embed")) {
                    	String[] urlcomponents = pageurl.split("/");
                    	pageurl = "https://www.youtube.com/watch?v=" + urlcomponents[urlcomponents.length - 1];
                    }
                    if(pageurl.contains("watch")) {
                    	extractor.Youtube yt = new extractor.Youtube(pageurl, downloadDirs.getTempDir(), DownloadDirs.getImagesDir());
                    	ArrayList<JsonNode> listOfMedias = yt.getMedias();
                    	System.out.println("number of medias : " + listOfMedias.size());
                    	window.dispose();
                    	MediaContainer mediacontainer = new MediaContainer();
                    	for (JsonNode jsonNode : listOfMedias) {
                    		mediacontainer.addMedia(new Media(jsonNode));
						}
                    }
				}
				else if(pageurl.contains("facebook")) {
					Pattern facebookUrlPattern = Pattern.compile("(?:https?://(?:[\\w-]+\\.)?(?:facebook\\.com|facebookcorewwwi\\.onion)/(?:[^#]*?\\#!/)?(?:(?:video/video\\.php|photo\\.php|video\\.php|video/embed|story\\.php|watch(?:/live)?/?)\\?(?:.*?)(?:v|video_id|story_fbid)=|[^/]+/videos/(?:[^/]+/)?|[^/]+/posts/|groups/[^/]+/permalink/|watchparty/)|facebook:)(?<id>[0-9]+)");
					Matcher pageurlMatcher = facebookUrlPattern.matcher(pageurl);
					if(pageurlMatcher.find()) {
						String videoId = pageurlMatcher.group("id");
						String realpageurl = pageurl;
						if(pageurl.startsWith("facebook"))
							realpageurl = "https://www.facebook.com/video/video.php?v=" + videoId;
						String webpage = Common.getWebPage(realpageurl.replaceAll("://m.facebook.com/", "://www.facebook.com/"));
						Pattern resultpattern = Pattern.compile(",\\\"result\\\":(\\{.+?\\}),\\\"sequence_number\\\":");
						Matcher resultmatcher = resultpattern.matcher(webpage);
						ArrayList<String> resultArray = new ArrayList<>();
						
						while(resultmatcher.find()) {
							String resultstr = resultmatcher.group(1);
							resultstr = resultstr.replaceAll("\\\\/", "/");
							resultstr = resultstr.replaceAll("\\\\\\\"", "\"");
							resultstr = Junidecode.unidecode(resultstr);
							resultArray.add(resultstr);
						}
						int i = 0;
						for (String string : resultArray) {
							System.out.println(i + ": " + string);
							i++;
						}
						System.out.println("number of result: " + resultArray.size());
						
					}
				}
				else {
					downloadProps = new DownloadProps(parameterUrl.getText());
					if(!downloadProps.hasError()) {
						container.remove(containerErreur);
						completeMainWindow();
					}
					else {
						container.add(containerErreur);
						labelErreur.setText(downloadProps.getError());
						parameterUrl.requestFocus();
					}
					SwingUtilities.updateComponentTreeUI(container);
					window.pack();
					window.setCursor(Cursor.getDefaultCursor());
				}
			}
		}
	}
	
	/*** listener class of button buttonDestination ***/
	class ButtonListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JFileChooser choix = new JFileChooser();
			choix.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if(choix.showOpenDialog(choix.getParent()) == JFileChooser.APPROVE_OPTION){
				String dirPath = choix.getSelectedFile().getAbsolutePath();
				if(e.getSource() == buttonDestination) {
					parameterDestination.setText(dirPath);
					downloadDirs.setDestinationDir(dirPath);
				}
			}
		}
	}
	
	/*** listener class of field filename ***/
	class FileNameListener implements KeyListener{

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getKeyCode() == KeyEvent.VK_CONTROL) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				String str = null;
				try {
					str = clipboard.getData(DataFlavor.stringFlavor).toString().replaceAll("[\\/\\\\]", "-");
				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				StringSelection strsel = new StringSelection(str);
				clipboard.setContents(strsel, strsel);
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			Pattern p = Pattern.compile("[a-zéèàA-Z0-9]");
			Pattern p2 = Pattern.compile("[\\/\\\\]");
			boolean filenameIsEmpty = parameterFileName.getText().isEmpty();
			Character keyChar = arg0.getKeyChar();
			if((filenameIsEmpty && !(p.matcher(keyChar.toString()).matches())) || (!filenameIsEmpty && (p2.matcher(keyChar.toString()).matches()))) {
				arg0.consume();
			}
		}
	}	
	
	/*** listener class of button Cancel ***/
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
	
	/*** listener class of button Launch ***/
	class startButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent ev) {
			downloadProps.setFilename(parameterFileName.getText());
			DownloadWindow newDownloadVideo = new DownloadWindow(downloadProps);
			DownloadWindowsContainer containerWindow = DownloadWindowsContainer.getInstance();
			containerWindow.addDownloadWindow(newDownloadVideo);
			if(audioDownloadProps != null) {
				DownloadWindow newDownloadAudio = new DownloadWindow(audioDownloadProps);
				containerWindow.addDownloadWindow(newDownloadAudio);
				linkDownloads(newDownloadVideo, newDownloadAudio, containerWindow);
				linkDownloads(newDownloadAudio, newDownloadVideo, containerWindow);
			}
			containerWindow.toFront();
			window.dispose();
		}
	}
	
	
	/*** merger of audio and video file ***/
	void linkDownloads(DownloadWindow lastwindowtofinish, DownloadWindow firstwindowtofinish, DownloadWindowsContainer containerWindow) {
		Download lastdownloadtofinish = lastwindowtofinish.getDownload();
		Download firstdownloadtofinish = firstwindowtofinish.getDownload();
		firstdownloadtofinish.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, boolean suspend, long infos) {
				// TODO Auto-generated method stub
				if(complete && (infos >= downloadProps.getSize())) {
					if(!lastdownloadtofinish.isAlive()) {
						containerWindow.removeDownloadWindow(lastwindowtofinish);
						String audiopathfilename = downloadDirs.getDestinationDir() + audioDownloadProps.getFilename();
						String videopathfilename = downloadDirs.getDestinationDir() + downloadProps.getFilename();
						String outputpathfilename = downloadDirs.getDestinationDir() + downloadProps.getFilename() + ".merging." + downloadProps.getSubType();
						String[] cmd = {downloadDirs.getFfmpeg(), "-i", audiopathfilename, "-i", videopathfilename, "-acodec", "copy", "-vcodec", "copy", outputpathfilename};
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

			@Override
			public void init(Thread thread) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MainWindow(false);
	}

}

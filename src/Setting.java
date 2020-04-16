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
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;


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
	private JLabel labelTemp = new JLabel("Temp");
	private JTextField parameterTemp = new JTextField();
	private JButton buttonTemp = new JButton("...");
	private JPanel containerTemp = new JPanel();
	private JLabel labelFileName = new JLabel("Filename");
	private JTextField parameterFileName = new JTextField();
	private JLabel labelSizeFileName = new JLabel("", JLabel.CENTER);
	private JPanel containerFileName = new JPanel();
	private JLabel labelSubDownloads = new JLabel("SubDownloads");
	private JSpinner paramaterSubDownloads = new JSpinner(new SpinnerNumberModel(10, 1, 20, 1));
	private JPanel containerSubDownloads = new JPanel();
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
    private int largeurSubDownloads = 40;
    private int largeurButton = (largeurParameter - largeurRigidArea)/4;
    
    private Dimension dimLabel = new Dimension(largeurLabel, hauteurWidget);
    private Dimension dimParameter = new Dimension(largeurParameter, hauteurWidget);
    private Dimension dimAdded = new Dimension(largeurAdded, hauteurWidget);
    private Dimension dimHRigidArea = new Dimension(largeurRigidArea, 0);
    private Dimension dimVRigidArea = new Dimension(0, largeurRigidArea);
    
    private String filename;//nom final du fichier à télécharger
    private String logoFileName;//nom du fichier image qui servira de logo
    private long filesize;//taille du fichier à télécharger
    private URL url;//url du fichier à télécharger
	
	public Setting() {
        
		buildSetting();        
        labelIconUrl.setVisible(false);
        
        this.setContentPane(container);
        SwingUtilities.updateComponentTreeUI(container);
        this.pack();
        this.setVisible(true);
	}
	
	public Setting(String url) {
		
		buildSetting();
		this.parameterUrl.setText(url);
		labelIconUrl.setVisible(false);
		
		this.setContentPane(container);
        SwingUtilities.updateComponentTreeUI(container);
        this.pack();
        this.setVisible(true);
	}
	
	public Setting(String filename, String fileType, long contentLength, String url) {
		
		buildSetting();
		this.parameterUrl.setText(url);
		this.buttonUrl.setVisible(false);
		this.labelIconUrl.setVisible(true);
		this.logoFileName = drawLogo(fileType.split("/")[0], fileType.split("/")[1], this.labelIconUrl);
		this.labelSizeFileName.setText(DownloadControl.getInstance().convertSize(contentLength));
		this.filename = filename + "." + fileType.split("/")[1];
		this.parameterFileName.setText(this.filename);
		this.filesize = contentLength;
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		container.add(containerBottom);
		
		this.setContentPane(container);
        SwingUtilities.updateComponentTreeUI(container);
        this.pack();
        this.setVisible(true);
	}
	
	
	void buildSetting() {
		
		Font font = new Font("Serif", Font.BOLD, 12);
		
		//changeLookAndFell();
		//setUIFont(new FontUIResource(font));
		UIManager.put("Label.font", font);//modification de la police des labels
		UIManager.put("Button.font", font);//modification de la police des boutons
		
		this.setTitle("Create Download");
		try {
			this.setIconImage(ImageIO.read(new File(DownloadControl.getInstance().getPathLogo())));
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
        
        labelTemp.setPreferredSize(dimLabel);
        parameterTemp.setEditable(false);
        parameterTemp.setPreferredSize(dimParameter);
        buttonTemp.setPreferredSize(dimAdded);
        buttonTemp.setContentAreaFilled(false);
        containerTemp.setLayout(new BoxLayout(containerTemp, BoxLayout.LINE_AXIS));
        containerTemp.add(Box.createRigidArea(dimHRigidArea));
        containerTemp.add(labelTemp);
        containerTemp.add(parameterTemp);
        containerTemp.add(Box.createRigidArea(dimHRigidArea));
        containerTemp.add(buttonTemp);
        containerTemp.add(Box.createRigidArea(dimHRigidArea));
        
        parameterDestination.setText(DownloadControl.getInstance().getHomeDirectory());
        parameterTemp.setText(DownloadControl.getInstance().getTempDirectory());
        /*if(System.getProperty("os.name").matches("Linux")) {
        	parameterDestination.setText(System.getenv("HOME"));
        	parameterTemp.setText("/tmp");
        }
        else if(System.getProperty("os.name").matches("Windows")){
        	parameterDestination.setText(System.getenv("%HOMEPATH%"));
        	parameterTemp.setText(System.getenv("%TEMP%"));
        }*/
        
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
        
        labelSubDownloads.setPreferredSize(dimLabel);
        paramaterSubDownloads.setPreferredSize(new Dimension(largeurSubDownloads, hauteurWidget));
        containerSubDownloads.setLayout(new BoxLayout(containerSubDownloads, BoxLayout.LINE_AXIS));
        containerSubDownloads.add(Box.createRigidArea(dimHRigidArea));
        containerSubDownloads.add(labelSubDownloads);
        containerSubDownloads.add(paramaterSubDownloads);
        containerSubDownloads.add(Box.createRigidArea(new Dimension(largeurAdded+largeurParameter+largeurRigidArea-largeurSubDownloads, hauteurWidget)));
        
        buttonCancel.setPreferredSize(new Dimension(largeurButton, hauteurWidget));
        buttonCancel.setContentAreaFilled(false);
        buttonStart.setPreferredSize(new Dimension(largeurButton, hauteurWidget));
        buttonStart.setContentAreaFilled(false);
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
        containerBottom.add(containerTemp);
        containerBottom.add(Box.createRigidArea(dimVRigidArea));
        containerBottom.add(containerFileName);
        containerBottom.add(Box.createRigidArea(dimVRigidArea));
        containerBottom.add(containerSubDownloads);
        containerBottom.add(Box.createRigidArea(new Dimension(largeurWidget, hauteurWidget)));
        containerBottom.add(containerStart);
        containerBottom.add(Box.createRigidArea(dimVRigidArea));
        
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        container.add(containerUrl);
        
        //creation des ecouteurs de boutons
        buttonUrl.addActionListener(new ButtonListenerUrl());
        buttonDestination.addActionListener(new ButtonListener());
        buttonTemp.addActionListener(new ButtonListener());
        buttonCancel.addActionListener(new ButtonListenerCancel());
        parameterFileName.addKeyListener(new FileNameListener());
        buttonStart.addActionListener(new startButtonListener());
        
        window = this;

	}
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub        
        new Setting();
	}
	
	
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
	
	String drawLogo(String type, String subtype, JLabel labelicon) {
		
		String filelogo = "inconnu";
		String imagesDirectory = DownloadControl.getInstance().getImagesDirectory();
		if(!type.isEmpty()) {
			File dir = new File(imagesDirectory);
			File[] fileslist = dir.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return (name.contains(type) || (!subtype.isEmpty() && name.contains(subtype)));
				}
			});
			if(fileslist.length >= 2) {
				filelogo = subtype;
			}
			else if(fileslist.length == 1 && fileslist[0].getName().contains(type)) {
				filelogo = type;
			}
		}
		filelogo += ".jpg";
		try {
			Image img = ImageIO.read(new File(imagesDirectory + filelogo)).getScaledInstance(hauteurWidget, hauteurWidget, Image.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(img);
			labelicon.setIcon(icon);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		labelicon.setVisible(true);
		
		return filelogo;
	}
	
	// Decodes a URL encoded string using UTF-8
	String decodeString(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			return value;//e1.printStackTrace();
		}
	}

	//classe d'écoute du bouton ok
	class ButtonListenerUrl implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if(parameterUrl.getText().isEmpty()) {
				container.add(containerErreur);
				labelErreur.setText("Ce champs est vide !!!");
				parameterUrl.requestFocus();
			}
			else {
				try {
					url = new URL(parameterUrl.getText());
					DownloadControl control = DownloadControl.getInstance();
					FileProperties properties = new FileProperties();
					int request = control.requestFileProperties(url, properties);
					if(request == HttpURLConnection.HTTP_OK) {
						container.remove(containerErreur);
						buttonUrl.setVisible(false);
						filename = decodeString(Paths.get(parameterUrl.getText()).getFileName().toString());
						parameterFileName.setText(filename);
						logoFileName = drawLogo(properties.getType(), properties.getSubType(), labelIconUrl);
						filesize = properties.getSize();
						if(filesize < 0) {
							paramaterSubDownloads.setValue(1);
							paramaterSubDownloads.setEnabled(false);
						}
						labelSizeFileName.setText(DownloadControl.getInstance().convertSize(filesize));
						container.add(containerBottom);
					}
					else {
						container.add(containerErreur);
						if(request == HttpURLConnection.HTTP_UNAVAILABLE) {
							labelErreur.setText("Service indisponible !!!");
						}
						else if(request == HttpURLConnection.HTTP_NOT_FOUND) {
							labelErreur.setText("Fichier non trouvé !!!");
						}
						else if(request == HttpURLConnection.HTTP_UNAUTHORIZED) {
							labelErreur.setText("Accès au fichier non autorizé !!!");
						}
						else if(request == -2) {
							labelErreur.setText("Réseau indisponible !!!");
						}
					}
				} catch (MalformedURLException e2) {
					// TODO Auto-generated catch block
					container.add(containerErreur);
					labelErreur.setText("Url mal formée !!!");
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
				if(e.getSource() == buttonDestination) {
					parameterDestination.setText(choix.getSelectedFile().getAbsolutePath());
				}
				else {
					parameterTemp.setText(choix.getSelectedFile().getAbsolutePath());
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
			//Pattern p3 = Pattern.compile("([a-zéèàA-Z_0-9\\-]*(\\.)?)*");
			// && (p3.matcher(parameterFileName.getText()).matches())
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
	
	//classe d'écoute du bouton Lauch
	class startButtonListener implements ActionListener{
		
		public void actionPerformed(ActionEvent ev) {
			DownloadWindow newDownload = new DownloadWindow(logoFileName, url, parameterFileName.getText(), parameterDestination.getText()+"/", parameterTemp.getText()+"/", filesize,(int) paramaterSubDownloads.getValue());
			DownloadWindowsContainer containerWindow = DownloadWindowsContainer.getInstance();
			containerWindow.addDownloadWindow(newDownload);
			containerWindow.toFront();
			window.dispose();
		}
	}
	
}


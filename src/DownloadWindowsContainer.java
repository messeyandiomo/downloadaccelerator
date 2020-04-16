import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class DownloadWindowsContainer extends JFrame {
	
	private static DownloadWindowsContainer instance = null;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuDownload = new JMenu("Download");
	private JMenuItem menuNewDownload = new JMenuItem("New");
	private JPanel containerDownloads = new JPanel();
	private JScrollPane scrollPane;
	private JPanel container = new JPanel();
	private int height = 0, width = 0, heightScrollPane = 0, downloadCount = 0, downloadLimit = 5;
		
	private DownloadWindowsContainer() {
		
		try {
			this.setIconImage(ImageIO.read(new File(DownloadControl.getInstance().getPathLogo())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Rectangle graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int positionX = (int) graphicEnvironment.getWidth();
		int positionY = (int) graphicEnvironment.getHeight();
		
		menuNewDownload.addActionListener(new NewDownloadListener());
		menuDownload.add(menuNewDownload);
		menuBar.add(menuDownload);
		this.setJMenuBar(menuBar);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(positionX, positionY);
		containerDownloads.setLayout(new BoxLayout(containerDownloads, BoxLayout.PAGE_AXIS));
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		
	}
	
	public static DownloadWindowsContainer getInstance() {
		
		if(instance == null) {
			instance = new DownloadWindowsContainer();
		}
				
		return instance;
	}
	
	
	//ajouter une fenètre de téléchargement
	public DownloadWindowsContainer addDownloadWindow(JInternalFrame downloadwindow) {
		
		Insets bordure = downloadwindow.getInsets();
		
		containerDownloads.add(downloadwindow, 0);
		downloadCount++;
		
		if(width == 0) {
			width =downloadwindow.getWidth() + bordure.left + bordure.right;
		}
		height += downloadwindow.getHeight() + bordure.bottom + bordure.top;
		if(downloadCount <= downloadLimit) {
			containerDownloads.setPreferredSize(new Dimension(width, height));
			this.setContentPane(containerDownloads);
		}
		else {
			if(downloadCount == (1+downloadLimit)) {
				scrollPane = new JScrollPane(containerDownloads);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				heightScrollPane = height;
				scrollPane.setBounds(0, 0, width, heightScrollPane);
				scrollPane.getViewport().setPreferredSize(new Dimension(width, heightScrollPane));
				container.add(scrollPane);
				this.setContentPane(container);
			}
			containerDownloads.setPreferredSize(new Dimension(width, height));
		}
		if(this.isVisible() == false) {//la fenetre n'est pas visible
			this.setVisible(true);
		}
		this.pack();
		
		return instance;
	}
	
	
	//enlever une fenètre de téléchargement
	public DownloadWindowsContainer removeDownloadWindow(JInternalFrame downloadwindow) {
		
		//Insets bordure = downloadwindow.getInsets();
		
		height -= downloadwindow.getHeight();
		containerDownloads.remove(downloadwindow);
		downloadwindow.dispose();
		downloadCount--;
		if(downloadCount == downloadLimit) {
			container.remove(scrollPane);
			this.setContentPane(containerDownloads);
		}
		containerDownloads.setPreferredSize(new Dimension(width, height));
		this.pack();
		
		return instance;
		
	}
	
	
	class NewDownloadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			new  Setting();
		}
		
	}

}

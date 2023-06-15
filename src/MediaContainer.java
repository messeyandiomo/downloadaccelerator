import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class MediaContainer extends JFrame {
	
	private JPanel containerOfMedia = new JPanel();
	private JScrollPane scrollPane;
	private JPanel container = new JPanel();
	private int height = 0, width = 0, heightScrollPane = 0, numberOfMedia = 0, maxOfMedia = 5;
	private MediaContainer myself =null;


	public MediaContainer() {
		
		
		try {
			this.setIconImage(ImageIO.read(new File(DownloadDirectories.getPathLogo())));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Rectangle graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int positionX = (int) graphicEnvironment.getWidth()/2;
		int positionY = 0;
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(positionX, positionY);
		containerOfMedia.setLayout(new BoxLayout(containerOfMedia, BoxLayout.PAGE_AXIS));
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		myself = this;
	}
	
	
	public MediaContainer addMedia(JInternalFrame media) {
		
		Insets bordures = media.getInsets();
		
		containerOfMedia.add(media, 0);
		numberOfMedia++;
		
		if(width == 0)
			width = media.getWidth() + bordures.left + bordures.right;
		height += media.getHeight() + bordures.bottom + bordures.top;
		
		if(numberOfMedia <= maxOfMedia) {
			containerOfMedia.setPreferredSize(new Dimension(width, height));
			this.setContentPane(containerOfMedia);
		}
		else {
			if(numberOfMedia == (1 + maxOfMedia)) {
				scrollPane = new JScrollPane(containerOfMedia);
				scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				heightScrollPane = height;
				scrollPane.setBounds(0, 0, width, heightScrollPane);
				scrollPane.getViewport().setPreferredSize(new Dimension(width, heightScrollPane));
				container.add(scrollPane);
				this.setContentPane(container);
			}
			containerOfMedia.setPreferredSize(new Dimension(width, height));
		}
		
		if(this.isVisible() == false)//window is not visible
			this.setVisible(true);
		
		this.pack();
		
		return myself;
	}
	
	
	

	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		MediaContainer mediacontainer = new MediaContainer();
		String webpage = Common.getWebPage("https://www.youtube.com/watch?v=d56mG7DezGs");
		Youtube.getPlayerUrl(webpage);
		mediacontainer.addMedia(new Media("Coolio - Gangsta's Paradise (feat. L.V.) [Official Music Video]", "video/mp4", 120000, 1024, 712, 248000, "images/defaultposter.jpg"));
		mediacontainer.addMedia(new Media("Geo Masso A muto 1981 Cameroun", "video/mp4", 120000, 1024, 712, 248000, "images/defaultposter.jpg"));
		mediacontainer.addMedia(new Media("Mary J. Blige - Be Without You (Official Music Video)", "video/mp4", 120000, 1024, 712, 248000, "images/defaultposter.jpg"));
		mediacontainer.addMedia(new Media("Megan Thee Stallion - Captain Hook [Official Video]", "video/mp4", 120000, 1024, 712, 248000, "images/defaultposter.jpg"));
		mediacontainer.addMedia(new Media("Michael Jackson - You Rock My World (Official Video)", "video/mp4", 120000, 1024, 712, 248000, "images/defaultposter.jpg"));
		mediacontainer.addMedia(new Media("Missy Elliott - All N My Grill [Official Music Video]", "video/mp4", 120000, 1024, 712, 248000, "images/defaultposter.jpg"));
		mediacontainer.addMedia(new Media("DJ Khaled - I'm The One ft. Justin Bieber-Quavo-Chance the Rapper-Lil Wayne", "video/mp4", 120000, 1024, 712, 248000, "images/defaultposter.jpg"));
	}*/

}

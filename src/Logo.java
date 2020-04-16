import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Logo extends JPanel{

	Image image;
	
	public Logo(Image img) {
		image = img;
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}

}

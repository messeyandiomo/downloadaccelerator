import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Logo extends JPanel{

	Image image;
	
	public Logo(String logofilename, Dimension dimlogo) {
		try {
			image = ImageIO.read(new File(logofilename));
			this.setPreferredSize(dimlogo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}

}

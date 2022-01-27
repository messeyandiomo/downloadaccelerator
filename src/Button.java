import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Button extends JButton {
	
	private Download download;
	private JButton button;

	public Button(String imageiconpath, int width, int height) {
		// TODO Auto-generated constructor stub
		super();
		this.setPreferredSize(new Dimension(width, height));
		int iconwidth = (int)(3*height/4);
		try {
			Image img = ImageIO.read(new File(imageiconpath)).getScaledInstance(iconwidth, iconwidth, Image.SCALE_DEFAULT);
			this.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	
	public Button(Download download, String imageiconpath, int width, int height, boolean resume) {
		
		this(imageiconpath, width, height);
		this.download = download;
		this.button = this;
		
		this.download.addObserver(new Observer() {
			
			@Override
			public void update(boolean complete, long infos) {
				// TODO Auto-generated method stub
				if(complete) {
					if(button.isVisible())
						button.setVisible(false);
				}
				else {
					if(!resume) {
						if(button.isVisible())
							button.setVisible(false);
					}
					else {
						if(!button.isVisible())
							button.setVisible(true);
					}
				}
			}
		});
	}

	
}

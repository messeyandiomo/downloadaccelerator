import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

@SuppressWarnings("serial")
public class Media extends JInternalFrame {
	
	private Label title;
	private Label type;
	private Label duration;
	private Label resolution;
	private Label size;
	private Media myself;
	
	private JPanel containerOfAttributes = new JPanel();//This contains vertically title on top, type, duration, resolution and size on bottom 
	private JPanel containerOfResolution = new JPanel();//This one is layout horizontally and contains type, duration, resolution and size
	private JPanel container = new JPanel();//This has a horizontal layout and contains containerOfPhoto and containerOfAttributes
	
	private Color initialBackgroundColor;
	
	public Media(String title, String type, long duration, int width, int height, long size, String mediaphoto) {
		
		Rectangle graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		Dimension dimMedia = new Dimension(graphicEnvironment.width/3, graphicEnvironment.height/6);
		Dimension dimLogo = new Dimension(dimMedia.width/8, dimMedia.height/3);
		Dimension dimAttributes = new Dimension(dimMedia.width -dimLogo.width, dimLogo.height);
		Dimension dimResolution = new Dimension(dimAttributes.width, dimLogo.height/3);
		
		Logo photo = new Logo(mediaphoto, dimLogo);
		this.title = new Label(title, 100);
		this.type = new Label(type, 50);
		this.duration = new Label(convertDuration(duration), 20);
		this.resolution = new Label(width + "x" + height, 10);
		this.size = new Label(DownloadControl.convertSize(size), 20);
		
		this.initialBackgroundColor = container.getBackground();
		
		this.title.setBackground(Color.WHITE);
		this.type.setBackground(Color.WHITE);
		this.duration.setBackground(Color.WHITE);
		this.resolution.setBackground(Color.WHITE);
		this.size.setBackground(Color.WHITE);
		
		this.title.changeFont(new Font("Serif", Font.BOLD, 12));
		this.type.changeFont(new Font("Serif", Font.PLAIN, 10));
		this.duration.changeFont(new Font("Serif", Font.PLAIN, 10));
		this.resolution.changeFont(new Font("Serif", Font.PLAIN, 10));
		this.size.changeFont(new Font("Serif", Font.PLAIN, 10));
		
		containerOfResolution.setLayout(new BoxLayout(containerOfResolution, BoxLayout.LINE_AXIS));
		containerOfResolution.add(this.type);
		containerOfResolution.add(Box.createRigidArea(new Dimension(8, 0)));
		containerOfResolution.add(this.duration);
		containerOfResolution.add(Box.createRigidArea(new Dimension(8, 0)));
		containerOfResolution.add(this.resolution);
		containerOfResolution.add(Box.createRigidArea(new Dimension(8, 0)));
		containerOfResolution.add(this.size);
		containerOfResolution.setPreferredSize(dimResolution);
		containerOfResolution.setBackground(Color.WHITE);
		
		containerOfAttributes.setLayout(new BoxLayout(containerOfAttributes, BoxLayout.PAGE_AXIS));
		containerOfAttributes.add(this.title);
		containerOfAttributes.add(containerOfResolution);
		containerOfAttributes.setPreferredSize(dimAttributes);
		containerOfAttributes.setBackground(Color.WHITE);
		
		container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
		container.add(photo);
		container.add(containerOfAttributes);
		container.setBackground(Color.WHITE);
		
		myself = this;
		
		this.addMouseListener(new MediaMouseListener());
		this.setContentPane(container);
		((BasicInternalFrameUI)getUI()).setNorthPane(null);
		this.pack();
		this.setVisible(true);
	}
	
	
	class MediaMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			myself.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			title.setBackground(initialBackgroundColor);
			type.setBackground(initialBackgroundColor);
			duration.setBackground(initialBackgroundColor);
			resolution.setBackground(initialBackgroundColor);
			size.setBackground(initialBackgroundColor);
			containerOfResolution.setBackground(initialBackgroundColor);
			containerOfAttributes.setBackground(initialBackgroundColor);
			container.setBackground(initialBackgroundColor);
			
			myself.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			title.setBackground(Color.WHITE);
			type.setBackground(Color.WHITE);
			duration.setBackground(Color.WHITE);
			resolution.setBackground(Color.WHITE);
			size.setBackground(Color.WHITE);
			containerOfResolution.setBackground(Color.WHITE);
			containerOfAttributes.setBackground(Color.WHITE);
			container.setBackground(Color.WHITE);
			
			myself.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	/** conversion of duration from millisecond to hour, minutes and seconds **/
	String convertDuration(long duration){
		
		String retour = null;
		long millisecondes = duration%1000;
		long secondes = (duration - millisecondes)/1000;
		
		if(secondes >= 0){
			if (secondes < 60) {
				retour = format(secondes) + "s";
			}
			else if (secondes >= 60) {
				long minutes = (secondes - secondes%60)/60;
				secondes = secondes%60;
				if (minutes < 60) {
					retour = format(minutes) + "min" + format(secondes) + "s";
				}
				else if (minutes >= 60) {
					long heures = (minutes - minutes%60)/60;
					minutes = minutes%60;
					retour = format(heures) + "h" + format(minutes) + "min" + format(secondes) + "s";
				}
			}
		}
		
		return retour;
	}
	
	
	String format(long mesure){
		if(mesure < 10)
			return "0" + mesure;
		else
			return "" + mesure;
	}

}

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
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.fasterxml.jackson.databind.JsonNode;

@SuppressWarnings("serial")
public class Media extends JInternalFrame {
	
	private Label titleLabel;
	private Label typeLabel;
	private Label durationLabel;
	private Label resolutionLabel;
	private Label sizeLabel;
	
	private JsonNode mediaProperties = null;
	private Media myself;
	
	private JPanel containerOfAttributes = new JPanel();//This contains vertically title on top, type, duration, resolution and size on bottom 
	private JPanel containerOfResolution = new JPanel();//This one is layout horizontally and contains type, duration, resolution and size
	private JPanel container = new JPanel();//This has a horizontal layout and contains containerOfPhoto and containerOfAttributes
	
	private Color initialBackgroundColor;
	
	
	public Media(JsonNode mediaproperties) {
		
		Rectangle graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		Dimension dimMedia = new Dimension(graphicEnvironment.width/3, graphicEnvironment.height/6);
		Dimension dimLogo = new Dimension(dimMedia.width/8, dimMedia.height/3);
		Dimension dimAttributes = new Dimension(dimMedia.width -dimLogo.width, dimLogo.height);
		Dimension dimResolution = new Dimension(dimAttributes.width, dimLogo.height/3);
		
		this.setMediaProperties(mediaproperties);
		Logo photo = new Logo(mediaproperties.get("poster").asText(), dimLogo);
		this.titleLabel = new Label(mediaproperties.get("title").asText(), 100);
		this.typeLabel = new Label(mediaproperties.get("mimeType").asText().split(";")[0], 50);
		if(mediaproperties.get("approxDurationMs") != null) {
			long duration = Long.parseLong(mediaproperties.get("approxDurationMs").asText());
			if(duration > 0)
				this.durationLabel = new Label(convertDuration(duration), 20);
			else
				this.durationLabel = new Label("", 20);
		}
		else
			this.durationLabel = new Label("", 20);
		
		if((mediaproperties.get("width") != null) && (mediaproperties.get("height") != null)) {
			int width = mediaproperties.get("width").asInt();
			int height = mediaproperties.get("height").asInt();
			if((width > 0) && (height > 0))
				this.resolutionLabel = new Label(width + "x" + height, 10);
			else
				this.resolutionLabel = new Label("", 10);
		}
		else
			this.resolutionLabel = new Label("", 10);
		
		if(mediaproperties.get("contentLength") != null) {
			long size = Long.parseLong(mediaproperties.get("contentLength").asText());
			if(size > 0)
				this.sizeLabel = new Label(DownloadControl.convertSize(size), 20);
			else
				this.sizeLabel = new Label("", 20);
		}
		else
			this.sizeLabel = new Label("", 20);
		
		this.initialBackgroundColor = container.getBackground();
		
		this.titleLabel.setBackground(Color.WHITE);
		this.typeLabel.setBackground(Color.WHITE);
		this.durationLabel.setBackground(Color.WHITE);
		this.resolutionLabel.setBackground(Color.WHITE);
		this.sizeLabel.setBackground(Color.WHITE);
		
		this.titleLabel.changeFont(new Font("Serif", Font.BOLD, 12));
		this.typeLabel.changeFont(new Font("Serif", Font.PLAIN, 10));
		this.durationLabel.changeFont(new Font("Serif", Font.PLAIN, 10));
		this.resolutionLabel.changeFont(new Font("Serif", Font.PLAIN, 10));
		this.sizeLabel.changeFont(new Font("Serif", Font.PLAIN, 10));
		
		containerOfResolution.setLayout(new BoxLayout(containerOfResolution, BoxLayout.LINE_AXIS));
		containerOfResolution.add(this.typeLabel);
		containerOfResolution.add(Box.createRigidArea(new Dimension(8, 0)));
		containerOfResolution.add(this.durationLabel);
		containerOfResolution.add(Box.createRigidArea(new Dimension(8, 0)));
		containerOfResolution.add(this.resolutionLabel);
		containerOfResolution.add(Box.createRigidArea(new Dimension(8, 0)));
		containerOfResolution.add(this.sizeLabel);
		containerOfResolution.setPreferredSize(dimResolution);
		containerOfResolution.setBackground(Color.WHITE);
		
		containerOfAttributes.setLayout(new BoxLayout(containerOfAttributes, BoxLayout.PAGE_AXIS));
		containerOfAttributes.add(this.titleLabel);
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
	
	
	public JsonNode getMediaProperties() {
		return mediaProperties;
	}


	public void setMediaProperties(JsonNode mediaProperties) {
		this.mediaProperties = mediaProperties;
	}


	class MediaMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			myself.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			/** get url or decryption of signature cipher **/
			JsonNode mediaproperties = myself.getMediaProperties();
			if(mediaproperties.get("url") != null) {
				if(mediaproperties.get("audioUrl") != null) {
					if((mediaproperties.get("contentLength") != null) && (mediaproperties.get("audioContentLength") != null))
						new Setting(mediaproperties.get("title").asText(), mediaproperties.get("mimeType").asText().split(";")[0], Long.parseLong(mediaproperties.get("contentLength").asText()), mediaproperties.get("url").asText(), mediaproperties.get("audioMimeType").asText().split(";")[0], Long.parseLong(mediaproperties.get("audioContentLength").asText()), mediaproperties.get("audioUrl").asText());
				}
				else {
					if(mediaproperties.get("contentLength") != null)
						new Setting(mediaproperties.get("title").asText(), mediaproperties.get("mimeType").asText().split(";")[0], Long.parseLong(mediaproperties.get("contentLength").asText()), mediaproperties.get("url").asText());
					else
						new Setting(mediaproperties.get("url").asText());
				}
				
			}
			else if(mediaproperties.get("signatureCipher") != null) {
				if(mediaproperties.get("audioSignatureCipher") != null) {
					if((mediaproperties.get("contentLength") != null) && (mediaproperties.get("audioContentLength") != null))
						new Setting(mediaproperties.get("title").asText(), mediaproperties.get("mimeType").asText().split(";")[0], Long.parseLong(mediaproperties.get("contentLength").asText()), mediaproperties.get("signatureCipher").asText(), mediaproperties.get("playerUrl").asText(), mediaproperties.get("audioMimeType").asText().split(";")[0], Long.parseLong(mediaproperties.get("audioContentLength").asText()), mediaproperties.get("audioSignatureCipher").asText(), null, null);
				}
				else {
					if(mediaproperties.get("contentLength") != null)
						new Setting(mediaproperties.get("title").asText(), mediaproperties.get("mimeType").asText().split(";")[0], Long.parseLong(mediaproperties.get("contentLength").asText()), mediaproperties.get("signatureCipher").asText(), mediaproperties.get("playerUrl").asText(), null, null);
				}
			}
			/** close the container of medias **/
			((MediaContainer) SwingUtilities.getWindowAncestor(myself)).dispose();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			titleLabel.setBackground(initialBackgroundColor);
			typeLabel.setBackground(initialBackgroundColor);
			durationLabel.setBackground(initialBackgroundColor);
			resolutionLabel.setBackground(initialBackgroundColor);
			sizeLabel.setBackground(initialBackgroundColor);
			containerOfResolution.setBackground(initialBackgroundColor);
			containerOfAttributes.setBackground(initialBackgroundColor);
			container.setBackground(initialBackgroundColor);
			
			myself.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			titleLabel.setBackground(Color.WHITE);
			typeLabel.setBackground(Color.WHITE);
			durationLabel.setBackground(Color.WHITE);
			resolutionLabel.setBackground(Color.WHITE);
			sizeLabel.setBackground(Color.WHITE);
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

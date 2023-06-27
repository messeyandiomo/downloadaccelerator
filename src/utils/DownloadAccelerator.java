package utils;

import gui.MainWindow;

public class DownloadAccelerator extends Thread {
	
private String message = "";
	
	public DownloadAccelerator(String message) {
		// TODO Auto-generated constructor stub
		super();
		this.setMessage(message);
		this.start();
	}
	
	
	public void run() {
		if(message.length() == 0)
			new MainWindow(false);
		else {
			String args[] = this.message.split(";;");
			if(args.length == 1)
	    		new MainWindow(args[0], false);
			else if(args.length == 4)//This is for media that is not ciphered and  contains both audio and video
	    		new MainWindow(args[0], args[1], Long.parseLong(args[2]), args[3]);
			else if(args.length == 7) {
				if(isNumeric(args[5]))//This is or media which is not ciphered and have audio and video separated
					new MainWindow(args[0], args[1], Long.parseLong(args[2]), args[3], args[4], Long.parseLong(args[5]), args[6]);
				else//This is for media which is ciphered and contains both audio and video
					new MainWindow(args[0], args[1], Long.parseLong(args[2]), args[3], args[4]);
			}
			else if(args.length == 10)//This is for media which is ciphered and has audio and video separated; the last argument is web page code
				new MainWindow(args[0], args[1], Long.parseLong(args[2]), args[3], args[4], args[5], Long.parseLong(args[6]), args[7]);
			
		}
	}
	
	
	
	boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        Long.parseLong(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}


	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}

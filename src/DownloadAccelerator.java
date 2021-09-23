
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
			new Setting();
		else {
			String args[] = this.message.split(";;");
			if(args.length == 1)
	    		new Setting(args[0]);
			else if(args.length == 4)
	    		new Setting(args[0], args[1], Integer.parseInt(args[2]), args[3]);
			else if(args.length == 6)
	    		new Setting(args[0], args[1], Integer.parseInt(args[2]), args[3], args[4], args[5]);
		}
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

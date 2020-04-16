import javax.swing.JProgressBar;

@SuppressWarnings("serial")
public class SubDownloadProgressBar extends JProgressBar {

	private long filesize = 0;
	private long currentsize = 0;
	private int barLength;
	
	public SubDownloadProgressBar(int arg0, int arg1, long filesize) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
		this.setFilesize(filesize);
		barLength = arg1; 
	}

	/**
	 * @return the filesize
	 */
	public long getFilesize() {
		return filesize;
	}

	/**
	 * @param filesize the filesize to set
	 */
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	/**
	 * @return the currentsize
	 */
	public long getCurrentsize() {
		return currentsize;
	}

	/**
	 * @param currentsize the currentsize to set
	 */
	public void setCurrentsize(long currentsize) {
		this.currentsize += currentsize;
	}
	
	
	public long update(long sizeadded) {
		this.setCurrentsize(sizeadded);
		this.setValue((int) ((currentsize*barLength)/filesize));
		return sizeadded;
	}

}

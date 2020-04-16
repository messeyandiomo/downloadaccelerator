
public class FileProperties {
	
	private long size = 0;
	private String type = "";
	private String subtype = "";

	public FileProperties() {
		// TODO Auto-generated constructor stub
	}
	
	public void setSize(long filesize) {
		this.size = filesize;
	}
	
	public void setType(String filetype) {
		this.type = filetype;
	}
	
	public void setSubType(String filesubtype) {
		this.subtype = filesubtype;
	}
	
	public long getSize() {
		return this.size;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getSubType() {
		return this.subtype;
	}

}

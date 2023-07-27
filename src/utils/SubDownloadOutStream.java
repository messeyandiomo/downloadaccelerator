package utils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class SubDownloadOutStream extends FileOutputStream {
	
	private static HashMap<String, SubDownloadOutStream> outStreamDB = new HashMap<>(); 
	
	
	private SubDownloadOutStream(String name) throws FileNotFoundException {
		super(name);
		// TODO Auto-generated constructor stub
	}

	private SubDownloadOutStream(File file) throws FileNotFoundException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	private SubDownloadOutStream(FileDescriptor fdObj) {
		super(fdObj);
		// TODO Auto-generated constructor stub
	}

	private SubDownloadOutStream(String name, boolean append) throws FileNotFoundException {
		super(name, append);
		// TODO Auto-generated constructor stub
	}

	private SubDownloadOutStream(File file, boolean append) throws FileNotFoundException {
		super(file, append);
		// TODO Auto-generated constructor stub
	}
	
	
	
	public synchronized static SubDownloadOutStream get(String name) {
		
		SubDownloadOutStream result = null;
		if(outStreamDB.containsKey(name))
			result = outStreamDB.get(name);
		else {
			try {
				result = new SubDownloadOutStream(name, true);
				outStreamDB.put(name, result);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public synchronized static SubDownloadOutStream get(File file) {
		
		return get(file.getName());
	}
	
	
	public synchronized static void destroy(String name) {
		SubDownloadOutStream todestroy = get(name);
		if(todestroy != null) {
			outStreamDB.remove(name, todestroy);
			try {
				todestroy.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized static void destroy(File file) {
		destroy(file.getName());
	}
	
	
	public synchronized void write(byte[] memory, int offset, int len) {
		try {
			super.write(memory, offset, len);
			super.flush();
			super.getFD().sync();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

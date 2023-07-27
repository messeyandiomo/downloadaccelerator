package utils;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SubDownloadInStream extends InputStream {
	
	public SubDownloadInStream(long firstoctet, long downloaded, long size, URL url) {
		// TODO Auto-generated constructor stub
		HttpURLConnection connexion = null;
		try {
			connexion = (HttpURLConnection) (url.openConnection());
			connexion.setRequestProperty("Range", "bytes=" + firstoctet + "-" + (firstoctet + size - downloaded - 1));
			connexion.setConnectTimeout(10000);
			connexion.setReadTimeout(10000);
			if(connexion.getResponseCode() == HttpURLConnection.HTTP_PARTIAL)
				connexion.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public int read() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}

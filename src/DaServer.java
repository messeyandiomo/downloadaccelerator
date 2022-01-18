import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
 
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
 
public class DaServer {
	
	private static DaServer instance = null;
	
	private DaServer() {
		InetSocketAddress addr = new InetSocketAddress("localhost", 1985);
		HttpServer server;
		try {
			server = HttpServer.create(addr, 0);
			server.createContext("/", new Gestionnaire());
			server.setExecutor(Executors.newCachedThreadPool());
			server.start();
			System.out.println("Le serveur en ecoute sur le port: "+addr.getPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static DaServer getInstance() {
		
		if(instance == null) {
			instance = new DaServer();
		}
		
		return instance;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args[0].equalsIgnoreCase("client"))
			new DownloadAccelerator("");
		else if(args[0].equalsIgnoreCase("server"))
			DaServer.getInstance();
	}
	
}


 
class Gestionnaire implements HttpHandler {
 
  public void handle(HttpExchange exchange) throws IOException {
    String methodeRequete = exchange.getRequestMethod();
    if (methodeRequete.equalsIgnoreCase("GET")) {
      Headers reponseEntete = exchange.getResponseHeaders();
      reponseEntete.set("Content-Type", "text/plain");
      exchange.sendResponseHeaders(200, 0);
 
      OutputStream reponse = exchange.getResponseBody();
      Headers requeteEntete = exchange.getRequestHeaders();
      Set<String> keySet = requeteEntete.keySet();
      Iterator<String> iter = keySet.iterator();
      while (iter.hasNext()) {
        String key = iter.next();
        List<String> values = requeteEntete.get(key);
        String s = key + " = " + values.toString() + "";
        reponse.write(s.getBytes());
      }
      reponse.close();
    }
    else if(methodeRequete.equalsIgnoreCase("POST")) {
    	BufferedReader in = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
    	new DownloadAccelerator(in.readLine());
    }
  }
}

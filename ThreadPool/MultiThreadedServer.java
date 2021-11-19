package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedServer {
 
	public final static int PORT = 7;
	
	public static void main(String[] args) {
		ExecutorService pool = Executors.newFixedThreadPool(2);
		try (ServerSocket server = new ServerSocket(PORT)) {
			while (true) {  
				try {
					System.out.println("[Server] MultiThreaded Echo Server is waiting for access");
					Socket connection = server.accept();
					Callable<Void> task = new EchoServerThread(connection);
					pool.submit(task);
					} catch (IOException ex) {} 
				}
			} catch (IOException ex) {
				System.err.println("[Server] Couldn't start server");
				}
		}

	private static class EchoServerThread implements Callable<Void> {
		private Socket connection;
		
		EchoServerThread(Socket connection) {
			this.connection = connection;
		}
    
    public Void call() {
      try {
    	  Writer out = new OutputStreamWriter(connection.getOutputStream());
    	  System.out.println("[Server] [Client IP Address : "+ connection.getInetAddress() + " Port Number : " + connection.getPort() + "] is connected.");
    	  BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    	  PrintWriter pw = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
    	  
    	  String line = null;
    	  while ((line = br.readLine()) != null) {
    		  System.out.println("[Server] "+ "[Client IP Address : "+ connection.getInetAddress() + " Port Number : " + connection.getPort() +"] Client Say : " + line);
    		  pw.println("[Server] "+ line);
        	  pw.flush();
    	  }
    	  
    	  System.out.println("[Server] " + "[Client IP Address : "+ connection.getInetAddress() + " Port Number : " + connection.getPort() + "] Completed Input/Output, closed streams and sockets with clients.");
			pw.close();
			br.close();
			try {
				if(connection != null)
					connection.close();
			}
			catch(IOException e) {
			}
      } catch (IOException ex) {
        System.err.println(ex);
      } finally {
        try {
          connection.close();
        } catch (IOException e) {
          // ignore;
        }
      }
	return null;
    }
  }
}
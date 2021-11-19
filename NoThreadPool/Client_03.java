package project;

import java.net.*;
import java.io.*;

public class Client_03 {
	public static void main(String[] args) {
		String hostname = "localhost";
		if (args.length > 0) {
			hostname = args[0];
			}
		PrintWriter networkOut = null;
		BufferedReader networkIn = null;
		
		try {
			Socket theSocket = new Socket(hostname, 7);
			networkIn = new BufferedReader(new InputStreamReader(theSocket.getInputStream()));
			BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
			networkOut = new PrintWriter(theSocket.getOutputStream());
			System.out.println("[Client] Connection successfully connected to the MultiThreadedServer.");

			while (true) {
				String theLine = userIn.readLine();
				if (theLine.equals("exit")) 
					break;
				networkOut.println(theLine);
				networkOut.flush();
				System.out.println(networkIn.readLine());
				}
			}
		catch (IOException ex) {
			System.err.println(ex);
			}
		finally {
			try {
				if (networkIn != null) 
					networkIn.close(); 
				if (networkOut != null) 
					networkOut.close(); 
				}
			catch (IOException ex) {}
			}
		}
}
package ba.bitcamp.mustafaademovic.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ba.bitcamp.logger.Logger;

public class ConnectionListener {

	private static final int PORT = 8080;

	public static void main(String[] args) {

		ExecutorService pool = Executors.newFixedThreadPool(10);

		HashMap<String, String> logs = new HashMap<String, String>();

		logs.put("applicationLog", "application");
		logs.put("warning", "warning");
		logs.put("error", "error");

		try {

			new Logger(logs);

		} catch (FileNotFoundException e1) {

			System.out.println("Could not initialize logger");

			System.exit(1);
		}

		ServerSocket server = null;

		Socket client;
	
		try {
			
			server = new ServerSocket(PORT);
			
			while (true) {				

				client = server.accept();

				Logger.log("applicationLog", client.getInetAddress().getHostAddress() + " just connected!");

				pool.submit(new Connection(client));

			}

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}

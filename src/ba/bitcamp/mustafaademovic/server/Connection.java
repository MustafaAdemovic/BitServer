package ba.bitcamp.mustafaademovic.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import ba.bitcamp.logger.Logger;

public class Connection implements Runnable {

	private Socket client;

	public Connection(Socket client) {
		
		this.client = client;
	}

	@Override
	public void run() {
		
		BufferedReader br = null;
		
		PrintStream writer = null;

		try { // Trying to open streams
			
			writer = new PrintStream(client.getOutputStream());
			
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));

		} catch (IOException e) {
			
			Logger.log("error", e.getMessage());
			
			closeClient();
			
			return;
		}
		
		String catchGet = "";
		
		try { // Trying to send messages back to client.
			
			while ((catchGet = br.readLine()) != null) {

				if (catchGet.contains("GET"))
					break;

				if (catchGet.isEmpty())
					break;

			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		if (!catchGet.contains("GET")) {
			
			Logger.log("warning", "GET was not found!");
			
			Response.error(writer, "Invalid request");
			
			closeClient();
			
			return;
		}
		String fileName = getFileName(catchGet);
		
		FileInputStream fis = null;
		
		try {
			
			fis = new FileInputStream(fileName);
			
		} catch (FileNotFoundException e) {
			
			Response.error(writer, "This is not the page you are looking for!");
			
			Logger.log("warning", "Client requested missing file " + e.getMessage());
			
			closeClient();
			
			return;
		}
		
		BufferedReader fileReader = new BufferedReader(new InputStreamReader(fis));
		
		String fileLine = "";
		
		StringBuilder sb = new StringBuilder();

		try {
			
			while ((fileLine = fileReader.readLine()) != null) {
				
				sb.append(fileLine);
			}
			
		} catch (IOException e) {
			
			Response.serverError(writer, "A well trained group of monkeys is trying to fix the problem");
			
			Logger.log("error", e.getMessage());
			
			closeClient();
			
			return;
		}
		
		Response.ok(writer, sb.toString());
		
		closeClient();
	}
	private void closeClient(){
		
		try {
			
			client.close();
			
		} catch (IOException e) {
			
			Logger.log("warning", e.getMessage());
			
			e.printStackTrace();
		}
	}
	
	private String getFileName(String request) {
		
		String[] parts = request.split(" ");
		
		String fileName = null;

		for (int i = 0; i < parts.length; i++) {
			
			if (parts[i].equals("GET")) {
				
				fileName = parts[i + 1];
				
				break;
			}
		}
		
		String basePath = "." + File.separator + "html" + File.separator;
		
		if (fileName == null || fileName.equals("/"))
			
			return  basePath + "index.html";

		if (!fileName.contains("."))
			
			return fileName + ".html";

		return basePath + fileName;
	}
}
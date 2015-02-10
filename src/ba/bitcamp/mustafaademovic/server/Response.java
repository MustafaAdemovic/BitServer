package ba.bitcamp.mustafaademovic.server;

import java.io.PrintStream;

public class Response {
	

	public static void ok(PrintStream writter, String str) {

		writter.println("HTTP/1.1 200 OK");
		
		sendContent(writter, str);
	}

	
	public static void error(PrintStream writter, String str) {

		writter.println("HTTP/1.1 404 Not Found");

		sendContent(writter, str);
	}
	

	public static void serverError(PrintStream writter, String str) {

		writter.println("HTTP/1.1 500 Internal Server Error");

		sendContent(writter, str);
	}
	

	private static void sendContent(PrintStream writter, String str) {

		writter.println("Content-Type: text/html");

		writter.println();

		writter.println(str);
		
	}
}

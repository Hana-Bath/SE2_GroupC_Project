package newbank.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExampleClient extends Thread {

	private Socket server;
	private PrintWriter bankServerOut;
	private BufferedReader userInput;
	private Thread bankServerResponseThread;
	private volatile boolean running = true;

	public ExampleClient(String ip, int port) throws UnknownHostException, IOException {
		server = new Socket(ip, port);
		userInput = new BufferedReader(new InputStreamReader(System.in));
		bankServerOut = new PrintWriter(server.getOutputStream(), true);

		bankServerResponseThread = new Thread() {
			private BufferedReader bankServerIn = new BufferedReader(new InputStreamReader(server.getInputStream()));

			public void run() {
				try {
					String response;
					while (running && (response = bankServerIn.readLine()) != null) {
						System.out.println(response);
					}
				} catch (IOException e) {
					if (running) {
						e.printStackTrace();
					}
				} finally {
					closeResources();
				}
			}
		};
		bankServerResponseThread.start();
	}

	public void run() {
		try {
			while (running) {
				String command = userInput.readLine();
				if (command.equalsIgnoreCase("LOGOUT")) {
					bankServerOut.println(command);
					running = false;
					break;
				}
				bankServerOut.println(command);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeResources();
		}
	}

	private void closeResources() {
		running = false;
		try {
			if (userInput != null) {
				userInput.close();
			}
			if (bankServerOut != null) {
				bankServerOut.close();
			}
			if (server != null && !server.isClosed()) {
				server.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		ExampleClient client = new ExampleClient("localhost", 14002);
		client.start();
		client.join(); // Wait for the client thread to finish
		System.out.println("Client has exited.");
	}
}

package newbank.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import newbank.IRepositories.IAnimator;
import newbank.IRepositories.ICustomerHandler;
import newbank.repositories.Animator;
import newbank.repositories.CustomerHandler;
import newbank.repositories.NewBank;
import newbank.repositories.NewBankClientHandler;
import newbank.repositories.PromptUser;
import newbank.repositories.DisplayCustomer;

public class NewBankServer extends Thread {

	private ServerSocket server;
	private IAnimator animator;

	public NewBankServer(int port, IAnimator animator) throws IOException {
		this.server = new ServerSocket(port);
		this.animator = animator;
	}

	@Override
	public void run() {
		// starts up a new client handler thread to receive incoming connections and process requests
		System.out.println("New Bank Server listening on " + server.getLocalPort());
		try {
			while (true) {
				Socket s = server.accept();
				NewBank bank = NewBank.getBank();
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				PromptUser promptUser = new PromptUser();
				DisplayCustomer displayCustomer = new DisplayCustomer();
				ICustomerHandler customerHandler = new CustomerHandler(bank, out, promptUser, displayCustomer);
				NewBankClientHandler clientHandler = new NewBankClientHandler(s, animator, customerHandler);
				clientHandler.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		IAnimator animator = new Animator();  // Create an instance of the Animator class
		// starts a new NewBankServer thread on a specified port number
		new NewBankServer(14002, animator).start();
	}
}

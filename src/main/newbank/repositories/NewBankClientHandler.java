package newbank.repositories;

import newbank.IRepositories.INewBankClientHandler;
import newbank.IRepositories.IAnimator;
import newbank.IRepositories.ICustomerHandler;
import newbank.dtos.Customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NewBankClientHandler extends Thread implements INewBankClientHandler {

    private static final int MAX_ATTEMPTS = 3;
    private NewBank bank;
    private BufferedReader in;
    private PrintWriter out;
    private IAnimator animator;
    private Socket socket;
    private ICustomerHandler customerHandler;
    private Map<String, Runnable> adminCommands;
    private Map<String, Runnable> userCommands;

    public NewBankClientHandler(Socket s, IAnimator animator, ICustomerHandler customerHandler) throws IOException {
        this.bank = NewBank.getBank();
        this.socket = s;
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.out = new PrintWriter(s.getOutputStream(), true);
        this.animator = animator;
        this.customerHandler = customerHandler;
        initializeCommands();
    }

    private void initializeCommands() {
        adminCommands = new HashMap<>();
        adminCommands.put("1", this::createCustomer);
        adminCommands.put("2", this::updateCustomer);
        adminCommands.put("3", this::deleteCustomer);
        adminCommands.put("4", this::readCustomer);
        adminCommands.put("5", this::displayAllCustomers);
        adminCommands.put("6", this::logout);

        userCommands = new HashMap<>();
        userCommands.put("LOGOUT", this::logout);
    }

    @Override
    public void run() {
        try {
            animator.displayGreeting(out);
            String initialCommand = in.readLine();
            initialCommand = validateInitialCommand(initialCommand);

            while (true) {
                Customer customer = handleCommand(initialCommand);

                if (customer != null) {
                    if (customer.isAdmin()) {
                        processAdminCommands();
                    } else {
                        processUserCommands(customer);
                    }
                    break;
                } else {
                    initialCommand = validateInitialCommand(in.readLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String validateInitialCommand(String initialCommand) throws IOException {
        while (!"LOGIN".equals(initialCommand) && !"REGISTER".equals(initialCommand)) {
            out.println("Command not recognised. Please enter command LOGIN or REGISTER");
            out.flush();
            initialCommand = in.readLine();
        }
        return initialCommand;
    }

    private Customer handleCommand(String initialCommand) throws IOException {
        return "LOGIN".equals(initialCommand) ? login() : register();
    }

    private Customer login() throws IOException {
        Customer customer = null;
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            out.println("Enter Username");
            out.flush();
            String userName = in.readLine();

            out.println("Enter Password");
            out.flush();
            String password = in.readLine();

            customer = validateLogin(userName, password);
            customer = processLoginResult(customer);

            if (customer != null) {
                return customer;
            } else {
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    out.println("Too many failed login attempts. The application will now terminate.");
                    out.flush();
                    closeResources();
                    System.exit(1);
                }
            }
        }
        return null;
    }

    private Customer validateLogin(String userName, String password) {
        out.println("Checking Details...");
        out.flush();
        return bank.checkLogInDetails(userName, password);
    }

    private Customer processLoginResult(Customer customer) {
        if (customer == null) {
            out.println("Log In Failed. Invalid Credentials, please try again.");
            out.println("Please type LOGIN if you already have an account, or REGISTER if you need to create one.");
            out.flush();
        } else {
            out.println("Log In Successful. What do you want to do?");
            out.flush();
        }
        return customer;
    }

    private Customer register() throws IOException {
        Customer customer = null;
        int attempts = 0;
        boolean validDetails = false;

        while (attempts < MAX_ATTEMPTS && !validDetails) {
            out.println("Please enter a valid username (6 to 15 characters, no spaces)");
            out.flush();
            String userName = in.readLine();

            out.println("Please enter a valid password (9 to 15 characters, including uppercase letters, lowercase letters, and numbers, with no spaces)");
            out.flush();
            String password = in.readLine();

            out.println("Verifying Details...");
            out.flush();

            if (bank.isPasswordValid(password) && bank.isUserNameValid(userName)) {
                customer = bank.registerCustomer(userName, UserAuthentication.hashPassword(password));
                out.println("Registration successful. You can now log in.");
                out.flush();
                validDetails = true;
            } else {
                out.println("Registration failed. The username and password entered must be valid, please try again.");
                out.flush();
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    out.println("Too many failed registration attempts. The application will now terminate.");
                    out.flush();
                    closeResources();
                    System.exit(1);
                }
            }
        }
        return login();
    }

    private void processUserCommands(Customer customer) throws IOException {
        if (customer != null) {
            out.println("LOGOUT - logout");
            out.flush();

            while (true) {
                String request = in.readLine();
                if (request == null) {
                    break;
                }
                Runnable command = userCommands.get(request.toUpperCase());
                if (command != null) {
                    command.run();
                } else {
                    String response = bank.processRequest(customer, request);
                    out.println(response);
                    out.flush();
                }
            }
            animator.displayGoodbye(out);
            out.flush();
            try {
                Thread.sleep(100); // Small delay to ensure message is sent
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processAdminCommands() throws IOException {
        while (true) {
            out.println("Admin Panel - Choose an option:");
            out.println("1. Create Customer");
            out.println("2. Update Customer");
            out.println("3. Delete Customer");
            out.println("4. Read Customer");
            out.println("5. Display All Customers");
            out.println("6. Logout");
            out.flush();

            String adminCommand = in.readLine();

            Runnable command = adminCommands.get(adminCommand);
            if (command != null) {
                command.run();
                if ("6".equals(adminCommand)) {
                    return;
                }
            } else {
                out.println("Invalid command. Please choose a valid option.");
                out.flush();
            }
        }
    }

    private void createCustomer() {
        try {
            customerHandler.createCustomer();
        } catch (IOException e) {
            out.println("Error creating customer: " + e.getMessage());
        }
    }

    private void updateCustomer() {
        try {
            String userNameToUpdate = promptUser("Enter customer's username to update:");
            String newPassword = promptUser("Enter new password:");
            customerHandler.updateCustomer(userNameToUpdate, newPassword);
        } catch (IOException e) {
            out.println("Error updating customer: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        try {
            customerHandler.deleteCustomer();
        } catch (IOException e) {
            out.println("Error deleting customer: " + e.getMessage());
        }
    }

    private void readCustomer() {
        try {
            customerHandler.readCustomer();
        } catch (IOException e) {
            out.println("Error reading customer: " + e.getMessage());
        }
    }

    private void displayAllCustomers() {
        try {
            customerHandler.displayAllCustomers();
        } catch (IOException e) {
            out.println("Error displaying customers: " + e.getMessage());
        }
    }

    private void logout() {
        animator.displayGoodbye(out);
        out.flush();
        try {
            Thread.sleep(100); // Small delay to ensure message is sent
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String promptUser(String prompt) throws IOException {
        out.println(prompt);
        out.flush();
        return in.readLine();
    }
}

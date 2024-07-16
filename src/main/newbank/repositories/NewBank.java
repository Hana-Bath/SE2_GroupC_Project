package newbank.repositories;

import newbank.IRepositories.INewBank;
import newbank.dtos.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewBank implements INewBank {

    private static final NewBank bank = new NewBank();
    private HashMap<String, Customer> customers;

    private NewBank() {
        customers = new HashMap<>();
        initializeFakeData();
        displayAdminCredentials();
    }

    private void initializeFakeData() {
        customers.put("admin", new Customer("admin", "password", true, "Admin", "User", "admin@example.com",
                "123-456-7890", "123 Admin St, City, State, 12345", "1970-01-01", "2022-01-01", 1000, "email",false));
        customers.put("user1", new Customer("user1", "password1", false, "John", "Doe", "john.doe@example.com",
                "111-111-1111", "123 Main St, City, State, 12345", "1990-05-15", "2022-01-02", 100, "phone",false));
        customers.put("user2", new Customer("user2", "password2", false, "Jane", "Smith", "jane.smith@example.com",
                "222-222-2222", "456 Elm St, City, State, 67890", "1985-07-20", "2022-01-03", 150, "email",false));
        customers.put("user3", new Customer("user3", "password3", false, "Alice", "Brown", "alice.brown@example.com",
                "333-333-3333", "789 Oak St, City, State, 13579", "1995-12-25", "2022-01-04", 200, "sms",false));
        customers.put("user4", new Customer("user4", "password4", false, "Bob", "Johnson", "bob.johnson@example.com",
                "444-444-4444", "101 Pine St, City, State, 24680", "1980-07-07", "2022-01-05", 250, "email",false));
        customers.put("user5", new Customer("user5", "password5", false, "Charlie", "Davis", "charlie.davis@example.com",
                "555-555-5555", "202 Maple St, City, State, 36912", "1992-10-10", "2022-01-06", 300, "phone",false));
        customers.put("user6", new Customer("user6", "password6", false, "Diana", "Clark", "diana.clark@example.com",
                "666-666-6666", "303 Birch St, City, State, 75319", "1978-03-03", "2022-01-07", 350, "email",false));
        customers.put("user7", new Customer("user7", "password7", false, "Eve", "Miller", "eve.miller@example.com",
                "777-777-7777", "404 Cedar St, City, State, 95137", "1988-08-08", "2022-01-08", 400, "sms",false));
        customers.put("user8", new Customer("user8", "password8", false, "Frank", "Wilson", "frank.wilson@example.com",
                "888-888-8888", "505 Walnut St, City, State, 15973", "1994-04-04", "2022-01-09", 450, "phone",false));
        customers.put("user9", new Customer("user9", "password9", false, "Grace", "Moore", "grace.moore@example.com",
                "999-999-9999", "606 Poplar St, City, State, 75384", "1986-06-06", "2022-01-10", 500, "email",false));
        customers.put("user10", new Customer("user10", "password10", false, "Henry", "Taylor", "henry.taylor@example.com",
                "000-000-0000", "707 Willow St, City, State, 68425", "1991-09-09", "2022-01-11", 550, "phone",false));
    }

    private void displayAdminCredentials() {
        System.out.println("Admin Username: admin");
        System.out.println("Admin Password: password");
    }

    public static NewBank getBank() {
        return bank;
    }

    @Override
    public synchronized Customer registerCustomer(String userName, String password) {
        Customer newCustomer = new Customer(userName, password, false, "New", "Customer", "new.customer@example.com",
                "333-333-3333", "789 Oak St, City, State, 13579", "2000-01-01", "2022-01-04", 50, "email",false);
        customers.put(userName, newCustomer);
        return newCustomer;
    }

    @Override
    public synchronized boolean updateCustomer(String userName, String newPassword) {
        if (customers.containsKey(userName)) {
            Customer existingCustomer = customers.get(userName);
            existingCustomer.setPassword(newPassword);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean deleteCustomer(String userName) {
        return customers.remove(userName) != null;
    }

    @Override
    public synchronized Customer readCustomer(String userName) {
        return customers.get(userName);
    }

    @Override
    public synchronized List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public synchronized Customer checkLogInDetails(String userName, String password) {
        // Check username
        if (customers.containsKey(userName)) {
            Customer customer = customers.get(userName);
            // Check password
            if (UserAuthentication.checkPassword(password, customer.getPassword())) {
                return customer;
            }
        }
        return null;
    }

    @Override
    public synchronized boolean deactivateCustomer(String userName) {
        Customer customer = customers.get(userName);
        if (customer != null) {
            customer.setDeactivated(true);
            return true;
        }
        return false;
    }
    @Override
    public synchronized boolean deleteCustomerPermanently(String userName) {
        return customers.remove(userName) != null;
    }

    public synchronized String processRequest(Customer customer, String request) {
        // Splits user input on a space to get command parameters
        String[] requestParams = request.split("\\s+");
        String command = requestParams[0];

        if (customers.containsKey(customer.getKey())) {
            switch (command) {
                // log customer out
                case "LOGOUT":
                    if (requestParams.length != 1) {
                        return "Not enough or too many arguments have been supplied for this command";
                    }
                    return "END";

                case "DEACTIVATE":
                    if (requestParams.length != 1) {
                        return "Not enough or too many arguments have been supplied for this command";
                    }
                    customer.setDeactivated(true);
                    return "Your account has been deactivated successfully.";

                default:
                    return "FAIL";
            }
        }
        return "FAIL";
    }

    public boolean isPasswordValid(String password) {
        String[] badPasswords = {"PASSWORD", "password", "123456789"};
        int upperCharLimit = 15;
        int lowerCharLimit = 9;

        // False if password is common example of bad password
        for (String x : badPasswords) {
            if (x.equals(password)) {
                return false;
            }
        }

        // Check password matches Regex pattern and is in character range
        Pattern pattern = Pattern.compile("(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(^\\S+$)");
        Matcher matcher = pattern.matcher(password);
        boolean match = matcher.find();
        int strLen = password.length();

        return match && strLen >= lowerCharLimit && strLen <= upperCharLimit;
    }

    public boolean isUserNameValid(String userName) {
        boolean duplicate = customers.containsKey(userName);
        int upperCharLimit = 15;
        int lowerCharLimit = 6;
        int strLen = userName.length();

        return !duplicate && strLen >= lowerCharLimit && strLen <= upperCharLimit && !userName.contains(" ");
    }
}

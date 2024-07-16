package newbank.repositories;

import newbank.IRepositories.ICustomerHandler;
import newbank.IRepositories.INewBank;
import newbank.IRepositories.IPromptUser;
import newbank.IRepositories.IDisplayCustomer;
import newbank.dtos.Customer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CustomerHandler implements ICustomerHandler {
    private INewBank bank;
    private PrintWriter out;
    private IPromptUser promptUser;
    private IDisplayCustomer displayCustomer;

    public CustomerHandler(INewBank bank, PrintWriter out, IPromptUser promptUser, IDisplayCustomer displayCustomer) {
        this.bank = bank;
        this.out = out;
        this.promptUser = promptUser;
        this.displayCustomer = displayCustomer;
    }

    @Override
    public void createCustomer() throws IOException {
        String userName = promptUser.promptUser(out, "Enter new customer's username:");
        String password = promptUser.promptUser(out, "Enter new customer's password:");
        Customer newCustomer = bank.registerCustomer(userName, password);
        if (newCustomer != null) {
            out.println("Customer created successfully.");
        } else {
            out.println("Failed to create customer. Please try again.");
        }
        out.flush();
    }

    @Override
    public void updateCustomer(String userName, String newPassword) throws IOException {
        boolean success = bank.updateCustomer(userName, newPassword);
        if (success) {
            out.println("Customer updated successfully.");
        } else {
            out.println("Failed to update customer. Please try again.");
        }
        out.flush();
    }

    @Override
    public void deleteCustomer() throws IOException {
        String userName = promptUser.promptUser(out, "Enter customer's username to delete:");
        boolean success = bank.deleteCustomer(userName);
        if (success) {
            out.println("Customer deleted successfully.");
        } else {
            out.println("Failed to delete customer. Please try again.");
        }
        out.flush();
    }

    @Override
    public boolean deleteAccount(String userName) throws IOException {
        return bank.deleteCustomerPermanently(userName);
    }


    @Override
    public void readCustomer() throws IOException {
        String userName = promptUser.promptUser(out, "Enter customer's username to read:");
        Customer customer = bank.readCustomer(userName);
        if (customer != null) {
            displayCustomer.displayCustomerDetails(out, customer);
        } else {
            out.println("Customer not found. Please try again.");
        }
        out.flush();
    }

    @Override
    public void displayAllCustomers() throws IOException {
        List<Customer> customers = bank.getAllCustomers();
        out.println(String.format("%-15s%-15s%-15s%-25s%-15s%-35s%-15s%-25s%-20s%-25s",
                "Customer ID", "First Name", "Last Name", "Email Address", "Phone Number", "Address",
                "Date of Birth", "Account Creation Date", "Loyalty Points", "Preferred Contact Method"));
        out.println(String.format("%-15s%-15s%-15s%-25s%-15s%-35s%-15s%-25s%-20s%-25s",
                "------------", "----------", "---------", "-------------", "------------", "-------",
                "------------", "-------------------", "--------------", "-----------------------"));
        for (Customer customer : customers) {
            out.println(String.format("%-15s%-15s%-15s%-25s%-15s%-35s%-15s%-25s%-20s%-25s",
                    customer.getKey(), customer.getFirstName(), customer.getLastName(), customer.getEmailAddress(),
                    customer.getPhoneNumber(), customer.getAddress(), customer.getDateOfBirth(),
                    customer.getAccountCreationDate(), customer.getLoyaltyPoints(), customer.getPreferredContactMethod()));
        }
        out.flush();
    }
}

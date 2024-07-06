package newbank.repositories;

import newbank.IRepositories.IDisplayCustomer;
import newbank.dtos.Customer;
import java.io.PrintWriter;

public class DisplayCustomer implements IDisplayCustomer {
    @Override
    public void displayCustomerDetails(PrintWriter out, Customer customer) {
        out.println("Customer details: " + customer.getKey());
        out.println("First Name: " + customer.getFirstName());
        out.println("Last Name: " + customer.getLastName());
        out.println("Email Address: " + customer.getEmailAddress());
        out.println("Phone Number: " + customer.getPhoneNumber());
        out.println("Address: " + customer.getAddress());
        out.println("Date of Birth: " + customer.getDateOfBirth());
        out.println("Account Creation Date: " + customer.getAccountCreationDate());
        out.println("Loyalty Points/Status: " + customer.getLoyaltyPoints());
        out.println("Preferred Contact Method: " + customer.getPreferredContactMethod());
    }
}

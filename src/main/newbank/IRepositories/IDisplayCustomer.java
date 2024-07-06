package newbank.IRepositories;

import newbank.dtos.Customer;
import java.io.PrintWriter;

public interface IDisplayCustomer {
    void displayCustomerDetails(PrintWriter out, Customer customer);
}

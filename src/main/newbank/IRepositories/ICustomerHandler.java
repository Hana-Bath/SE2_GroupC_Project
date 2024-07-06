package newbank.IRepositories;

import java.io.IOException;

public interface ICustomerHandler {
    void createCustomer() throws IOException;
    void updateCustomer(String userName, String newPassword) throws IOException;
    void deleteCustomer() throws IOException;
    void readCustomer() throws IOException;
    void displayAllCustomers() throws IOException;
}

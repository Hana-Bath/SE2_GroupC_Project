package newbank.IRepositories;



import newbank.dtos.Customer;

import java.util.List;

public interface INewBank {
    Customer registerCustomer(String userName, String password);
    boolean updateCustomer(String userName, String newPassword);
    boolean deleteCustomer(String userName);
    Customer readCustomer(String userName);
    List<Customer> getAllCustomers();
}

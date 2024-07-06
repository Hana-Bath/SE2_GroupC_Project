package newbank.IRepositories;

public interface ICustomer {
    String getPassword();
    void setPassword(String password);
    String getKey();
    String getFirstName();
    String getLastName();
    String getEmailAddress();
    String getPhoneNumber();
    String getAddress();
    String getDateOfBirth();
    String getAccountCreationDate();
    int getLoyaltyPoints();
    String getPreferredContactMethod();
    boolean isAdmin(); // Include this method
}

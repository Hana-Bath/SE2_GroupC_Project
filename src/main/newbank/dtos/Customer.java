package newbank.dtos;

import newbank.IRepositories.ICustomer;

public class Customer implements ICustomer {
    private String key;
    private String password;
    private boolean isAdmin;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String address;
    private String dateOfBirth;
    private String accountCreationDate;
    private int loyaltyPoints;
    private String preferredContactMethod;
    private boolean isDeactivated;

    public Customer(String key, String password, boolean isAdmin, String firstName, String lastName, String emailAddress,
                    String phoneNumber, String address, String dateOfBirth, String accountCreationDate, int loyaltyPoints,
                    String preferredContactMethod,boolean isDeactivated) {
        this.key = key;
        this.password = password;
        this.isAdmin = isAdmin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.accountCreationDate = accountCreationDate;
        this.loyaltyPoints = loyaltyPoints;
        this.preferredContactMethod = preferredContactMethod;
        this.isDeactivated = isDeactivated;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String getAccountCreationDate() {
        return accountCreationDate;
    }

    @Override
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    @Override
    public String getPreferredContactMethod() {
        return preferredContactMethod;
    }


    @Override
    public boolean isDeactivated() {
        return isDeactivated;
    }

    @Override
    public void setDeactivated(boolean deactivated) {
        isDeactivated = deactivated;
    }
}

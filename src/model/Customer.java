package model;

// ENCAPSULATION: All fields private, accessed via getters/setters
public class Customer {

    private String customerId;
    private String name;
    private String phone;
    private String pin;
    private Account account; // HAS-A relationship

    public Customer(String customerId, String name, String phone, String pin, Account account) {
        this.customerId = customerId;
        this.name       = name;
        this.phone      = phone;
        this.pin        = pin;
        this.account    = account;
    }

    public boolean checkPin(String inputPin) { return this.pin.equals(inputPin); }

    public String  getCustomerId() { return customerId; }
    public String  getName()       { return name; }
    public String  getPhone()      { return phone; }
    public Account getAccount()    { return account; }
    public void    setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return "Customer[" + customerId + "] " + name + " | " + account.getAccountType() + " Account";
    }
}
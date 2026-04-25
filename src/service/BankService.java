package service;

import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.SavingsAccount;
import model.Transactionable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// SINGLETON: Only one BankService instance exists
// Implements Transactionable interface (ABSTRACTION)
public class BankService implements Transactionable {

    private static BankService instance;

    private Map<String, Customer> customers = new HashMap<>();
    private int accountCounter = 1001;

    // Private constructor - no one can do "new BankService()"
    private BankService() {
        loadSampleData();
    }

    // SINGLETON: Global access point
    public static BankService getInstance() {
        if (instance == null) {
            instance = new BankService();
        }
        return instance;
    }

    private void loadSampleData() {
        createAccount("Arun Kumar",   "9876543210", "1234", "savings", 5000);
        createAccount("Priya Sharma", "8765432109", "5678", "current", 10000);
        createAccount("Rahul Mehta",  "7654321098", "9999", "savings", 2000);
    }

    public Customer createAccount(String name, String phone, String pin,
                                   String type, double initialDeposit) {
        String accNumber = "ACC" + accountCounter++;
        String custId    = "CUST" + accountCounter;

        // POLYMORPHISM: Create the right Account subclass based on type
        Account account;
        if (type.equalsIgnoreCase("current")) {
            account = new CurrentAccount(accNumber, name, initialDeposit);
        } else {
            account = new SavingsAccount(accNumber, name, initialDeposit);
        }

        Customer customer = new Customer(custId, name, phone, pin, account);
        customers.put(accNumber, customer);
        return customer;
    }

    @Override
    public boolean deposit(String accountNumber, double amount) {
        Customer customer = customers.get(accountNumber);
        if (customer == null) return false;
        return customer.getAccount().deposit(amount);
    }

    @Override
    public boolean withdraw(String accountNumber, double amount, String pin) {
        Customer customer = customers.get(accountNumber);
        if (customer == null || !customer.checkPin(pin)) return false;
        return customer.getAccount().withdraw(amount);
    }

    @Override
    public boolean transfer(String fromAcc, String toAcc, double amount, String pin) {
        Customer sender   = customers.get(fromAcc);
        Customer receiver = customers.get(toAcc);
        if (sender == null || receiver == null) return false;
        if (!sender.checkPin(pin)) return false;

        boolean withdrawn = sender.getAccount().withdraw(amount);
        if (!withdrawn) return false;

        receiver.getAccount().deposit(amount);
        sender.getAccount().addTransaction(
            "Transferred Rs." + amount + " to " + toAcc + " | Balance: Rs." + sender.getAccount().getBalance());
        receiver.getAccount().addTransaction(
            "Received Rs." + amount + " from " + fromAcc + " | Balance: Rs." + receiver.getAccount().getBalance());
        return true;
    }

    // POLYMORPHISM in action: calls the correct subclass calculateInterest()
    public double applyInterest(String accountNumber) {
        Customer customer = customers.get(accountNumber);
        if (customer == null) return -1;
        Account acc      = customer.getAccount();
        double interest  = acc.calculateInterest();
        acc.deposit(interest);
        acc.addTransaction("Interest credited: Rs." + interest);
        return interest;
    }

    public Customer      getCustomer(String accountNumber) { return customers.get(accountNumber); }
    public List<Customer> getAllCustomers()                 { return new ArrayList<>(customers.values()); }
    public boolean        accountExists(String accNum)     { return customers.containsKey(accNum); }
}
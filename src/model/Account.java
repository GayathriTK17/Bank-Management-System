package model;

import java.util.ArrayList;
import java.util.List;

// ABSTRACTION: Abstract class - cannot be instantiated directly
public abstract class Account {

    // ENCAPSULATION: private fields
    private String accountNumber;
    private String holderName;
    private double balance;
    private List<String> transactionHistory;

    public Account(String accountNumber, String holderName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = initialDeposit;
        this.transactionHistory = new ArrayList<>();
        transactionHistory.add("Account opened with Rs." + initialDeposit);
    }

    // ABSTRACTION: Subclasses MUST implement these
    public abstract double calculateInterest();
    public abstract String getAccountType();

    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        balance += amount;
        transactionHistory.add("Deposited Rs." + amount + " | Balance: Rs." + balance);
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) return false;
        balance -= amount;
        transactionHistory.add("Withdrew Rs." + amount + " | Balance: Rs." + balance);
        return true;
    }

    // ENCAPSULATION: Getters
    public String getAccountNumber()            { return accountNumber; }
    public String getHolderName()               { return holderName; }
    public double getBalance()                  { return balance; }
    public List<String> getTransactionHistory() { return transactionHistory; }

    // ENCAPSULATION: Setter
    public void setBalance(double balance)      { this.balance = balance; }

    public void addTransaction(String note)     { transactionHistory.add(note); }

    @Override
    public String toString() {
        return "[" + getAccountType() + "] " + holderName + " | Acc: " + accountNumber + " | Balance: Rs." + balance;
    }
}
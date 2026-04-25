package model;

// ABSTRACTION via Interface - BankService must implement all these
public interface Transactionable {
    boolean deposit(String accountNumber, double amount);
    boolean withdraw(String accountNumber, double amount, String pin);
    boolean transfer(String fromAcc, String toAcc, double amount, String pin);
}
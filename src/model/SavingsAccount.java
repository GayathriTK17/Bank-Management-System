package model;

// INHERITANCE: SavingsAccount extends Account
public class SavingsAccount extends Account {

    private static final double INTEREST_RATE = 4.0;
    private static final double MIN_BALANCE   = 500.0;

    public SavingsAccount(String accountNumber, String holderName, double initialDeposit) {
        super(accountNumber, holderName, initialDeposit); // calls parent constructor
    }

    // POLYMORPHISM: Overriding abstract method - 4% interest
    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE / 100;
    }

    @Override
    public String getAccountType() { return "Savings"; }

    // POLYMORPHISM: Overriding withdraw - savings has minimum balance rule
    @Override
    public boolean withdraw(double amount) {
        if (getBalance() - amount < MIN_BALANCE) return false;
        return super.withdraw(amount); // calls parent's withdraw
    }

    public double getInterestRate() { return INTEREST_RATE; }
    public double getMinBalance()   { return MIN_BALANCE; }
}
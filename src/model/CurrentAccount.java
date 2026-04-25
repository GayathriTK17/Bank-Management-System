package model;

// INHERITANCE: CurrentAccount also extends Account
public class CurrentAccount extends Account {

    private static final double INTEREST_RATE   = 2.0;
    private static final double OVERDRAFT_LIMIT = 1000.0;

    public CurrentAccount(String accountNumber, String holderName, double initialDeposit) {
        super(accountNumber, holderName, initialDeposit);
    }

    // POLYMORPHISM: Different interest rate for current account
    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE / 100;
    }

    @Override
    public String getAccountType() { return "Current"; }

    // POLYMORPHISM: Current accounts allow overdraft up to Rs.1000
    @Override
    public boolean withdraw(double amount) {
        if (getBalance() - amount < -OVERDRAFT_LIMIT) return false;
        setBalance(getBalance() - amount);
        addTransaction("Withdrew Rs." + amount + " | Balance: Rs." + getBalance());
        return true;
    }

    public double getOverdraftLimit() { return OVERDRAFT_LIMIT; }
}
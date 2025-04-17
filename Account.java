// --- Account.java (Modified for GUI) ---
import java.util.*;
import java.time.LocalDateTime; // Import for timestamp
import java.time.format.DateTimeFormatter; // For formatting timestamp

class Account {
    private String username; // Can represent the account holder's name
    private String password; // Reminder: Hash in a real system
    private String accountNumber;
    private double balance = 0.0;
    private List<String> transactionLog = new LinkedList<>(); // Using LinkedList is fine too
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Formatter for timestamp

    Account(String username, String password, String accountNumber) {
        this.username = username;
        this.password = password;
        this.accountNumber = accountNumber;
        // Balance starts at 0.0 by default
    }

    // Used by BankSystem.loginUser
    public boolean validatePassword(String input) {
        return this.password.equals(input);
    }

    // Used by GUI and BankSystem
    public double getBalance() {
        return balance;
    }

    // Called by BankSystem.performDeposit and BankSystem.performTransfer
    // No transaction logging here; BankSystem handles it.
    public void deposit(double amount) {
        if (amount > 0) { // Basic validation
            balance += amount;
        }
    }

    // Called by BankSystem.performWithdraw and BankSystem.performTransfer
    // Returns true on success, false on failure (insufficient funds)
    // No transaction logging here; BankSystem handles it.
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) { // Basic validation
            balance -= amount;
            return true;
        }
        return false; // Insufficient funds or invalid amount
    }

    // Used by GUI and BankSystem
    public String getAccountNumber() {
        return accountNumber;
    }

    // Used by GUI to display welcome message, reports, etc.
    public String getName() {
        return username;
    }

    // Called by BankSystem methods (deposit, withdraw, transfer) to record activity
    public void addTransaction(String record) {
        // Add a timestamp to the transaction record
        String timestamp = LocalDateTime.now().format(dtf);
        transactionLog.add(timestamp + " - " + record);
        // Optional: Limit the size of the log if needed
        // if (transactionLog.size() > 100) { // Example limit
        //     transactionLog.remove(0); // Remove the oldest entry
        // }
    }

    // *** MODIFIED: Renamed to getTransactions for consistency with BankSystem suggestion ***
    // *** Returns a defensive copy of the log for the GUI to display ***
    public List<String> getTransactions() {
        // Return a new list containing the same elements
        // This prevents the GUI or other external code from modifying the internal log
        return new ArrayList<>(this.transactionLog);
    }

    // --- Methods Removed ---

    // Removed: GUI uses getTransactions() to fetch data and display it.
    // public void viewTransactionLog() { ... }

    // Removed: Redundant with validatePassword.
    // public boolean checkPassword(String enteredPassword) { ... }

    // Removed: Transfer logic is centralized in BankSystem.performTransfer
    // public void transferTo(Account recipient, double amount) { ... }

    // Removed: Redundant with viewTransactionLog / getTransactions
    // public void viewTransactions() { ... }
}
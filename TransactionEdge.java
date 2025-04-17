// --- TransactionEdge.java ---
// Assuming this class exists and is simple
public class TransactionEdge {
    String targetAccount;
    double amount;

    public TransactionEdge(String targetAccount, double amount) {
        this.targetAccount = targetAccount;
        this.amount = amount;
    }

    // Getters might be useful if needed elsewhere, but not strictly required by the current BankSystem code
    public String getTargetAccount() {
        return targetAccount;
    }

    public double getAmount() {
        return amount;
    }
}
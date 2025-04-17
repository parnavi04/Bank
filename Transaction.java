public class Transaction {
    private String type;
    private double amount;
    private String targetAccount;

    public Transaction(String type, double amount) {
        this(type, amount, null);
    }

    public Transaction(String type, double amount, String targetAccount) {
        this.type = type;
        this.amount = amount;
        this.targetAccount = targetAccount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getTargetAccount() {
        return targetAccount;
    }
}

import java.util.*;

public class AdminDashboard {
    public static void show(Map<String, Account> accounts) {
        System.out.println("\n-- Admin Dashboard --");
        System.out.println("All Accounts (sorted by balance):\n");

        PriorityQueue<Account> sorted = new PriorityQueue<>((a, b) -> Double.compare(b.getBalance(), a.getBalance()));
        sorted.addAll(accounts.values());

        while (!sorted.isEmpty()) {
            Account acc = sorted.poll();
            System.out.println("Account: " + acc.getAccountNumber() + ", Holder: " + acc.getName() + ", Balance: Rs." + acc.getBalance());
        }

        System.out.println();
    }
}

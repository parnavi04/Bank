// --- BankSystem.java (Updated) ---
import java.util.*;
import java.util.stream.Collectors;

public class BankSystem {
    private Map<String, Account> accounts = new HashMap<>();
    private Map<String, List<TransactionEdge>> transactionGraph = new HashMap<>();
    private final String adminUsername = "admin";
    private final String adminPassword = "admin123";
    // Keep scanner for potential future command-line admin tools, but GUI is primary
    private Scanner scanner = new Scanner(System.in);

    // Getter for accounts needed by GUI
    public Map<String, Account> getAccounts() {
        return accounts;
    }

    // Getter for transactionGraph needed by GUI
    public Map<String, List<TransactionEdge>> getTransactionGraph() {
        return transactionGraph;
    }

    // Creates account, returns account number or null on failure
    public String createAccount(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
             return null; // Indicate failure (e.g., empty input)
        }
        String accountNumber;
        do {
            accountNumber = String.valueOf(new Random().nextInt(900000) + 100000);
        } while (accounts.containsKey(accountNumber));

        accounts.put(accountNumber, new Account(username, password, accountNumber));
        return accountNumber;
    }

    // Logs in user, returns Account object or null on failure
    public Account loginUser(String accNum, String pass) {
         if (!accounts.containsKey(accNum)) {
            return null; // Invalid account number
        }
        Account acc = accounts.get(accNum);
        if (!acc.validatePassword(pass)) {
            return null; // Incorrect password
        }
        return acc;
    }

    // Logs in admin, returns true on success, false on failure
    public boolean adminLogin(String uname, String pass) {
         return uname.equals(adminUsername) && pass.equals(adminPassword);
    }

    // Performs deposit, returns new balance or -1 on error
    public double performDeposit(Account acc, double amount) {
        if (amount <= 0) return -1; // Invalid amount
        acc.deposit(amount);
        // Add transaction record AFTER successful operation
        acc.addTransaction("Deposited: " + String.format("%.2f", amount));
        return acc.getBalance();
    }

    // Performs withdrawal, returns new balance or -1 on error/insufficient funds
    public double performWithdraw(Account acc, double amount) {
         if (amount <= 0) return -1; // Invalid amount
        if (acc.withdraw(amount)) {
            // Add transaction record AFTER successful operation
            acc.addTransaction("Withdrawn: " + String.format("%.2f", amount));
            return acc.getBalance();
        } else {
            // Optionally add a transaction log for the failed attempt if desired
            // acc.addTransaction("Withdrawal Failed (Insufficient Funds): " + String.format("%.2f", amount));
            return -1; // Indicate insufficient funds or other error
        }
    }

    // Performs transfer, returns true on success, false on failure
     public boolean performTransfer(Account fromAcc, String recipientAccNum, double amount) {
        if (amount <= 0 || !accounts.containsKey(recipientAccNum)) {
            return false; // Invalid amount or recipient doesn't exist
        }
        Account toAcc = accounts.get(recipientAccNum);
        if (fromAcc.getAccountNumber().equals(toAcc.getAccountNumber())) {
             return false; // Cannot transfer to self
        }

        if (fromAcc.withdraw(amount)) {
            toAcc.deposit(amount);
            // Add transaction edges and logs AFTER successful operation
            addTransactionEdge(fromAcc.getAccountNumber(), recipientAccNum, amount);
            fromAcc.addTransaction("Transferred Out: " + String.format("%.2f", amount) + " to " + recipientAccNum);
            toAcc.addTransaction("Transferred In: " + String.format("%.2f", amount) + " from " + fromAcc.getAccountNumber());
            return true; // Success
        } else {
            // Optionally log the failed attempt
            // fromAcc.addTransaction("Transfer Failed (Insufficient Funds): " + String.format("%.2f", amount) + " to " + recipientAccNum);
            return false; // Insufficient funds
        }
    }

    // *** UPDATED METHOD ***
    // Renamed and now calls acc.getTransactions()
    public List<String> getAccountTransactions(Account acc) {
        if (acc == null) {
            return new ArrayList<>(); // Return empty list if account is null
        }
        // Calls the method in Account.java which should return a defensive copy
        return acc.getTransactions();
    }


    // Returns transaction graph representation as String
     public String getTransactionGraphAsString() {
        StringBuilder sb = new StringBuilder("=== Transaction Graph ===\n");
        if (transactionGraph.isEmpty()) {
            return sb.append("No transactions recorded yet.\n").toString();
        }
        // Consider sorting accounts for consistent output if needed
        List<String> sortedKeys = new ArrayList<>(transactionGraph.keySet());
        Collections.sort(sortedKeys);

        for (String acc : sortedKeys) {
            sb.append("From ").append(acc).append(":\n");
            // Sort edges for consistent output if needed
            List<TransactionEdge> edges = transactionGraph.get(acc);
            // edges.sort(Comparator.comparing(TransactionEdge::getTargetAccount)); // Optional sort
            for (TransactionEdge edge : edges) {
                sb.append("  -> To ").append(edge.targetAccount)
                  .append(" | Amount: ").append(String.format("%.2f", edge.amount)).append("\n");
            }
        }
        return sb.toString();
    }


    // Traces transaction path using BFS, returns path or null
    public List<String> traceTransactionPath(String start, String end) {
        // Input validation
        if (!accounts.containsKey(start) || !accounts.containsKey(end)) {
            return null; // One or both accounts don't exist
        }
        if (start.equals(end)) {
             List<String> selfPath = new ArrayList<>();
             selfPath.add(start);
             return selfPath; // Path to self is just the account itself
        }


        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>(); // Stores <child, parent>
        Queue<String> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);
        parent.put(start, null); // Start node has no parent in the path context

        while (!queue.isEmpty()) {
            String current = queue.poll();

            // Check if we reached the end
            // This check is more efficient here than at the beginning of the loop
             if (current.equals(end)) {
                // Reconstruct path
                List<String> path = new LinkedList<>(); // Use LinkedList for efficient adds at beginning
                String crawl = end;
                while (crawl != null) {
                    path.add(0, crawl); // Add to the beginning
                    crawl = parent.get(crawl);
                }
                return path; // Path found
             }


            // Explore neighbors (accounts transacted TO from current)
            if (transactionGraph.containsKey(current)) {
                for (TransactionEdge edge : transactionGraph.get(current)) {
                    String neighbor = edge.targetAccount;
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        parent.put(neighbor, current); // Record path: current -> neighbor
                        queue.add(neighbor);
                    }
                }
            }
        }

        return null; // No path found
    }

    // Searches account using binary search (requires sorted list), returns details or null
    public String searchAccountDetails(String target) {
        if (accounts.isEmpty()) return null; // No accounts to search

        List<Account> sortedAccounts = new ArrayList<>(accounts.values());
        // Sort by account number for binary search
        sortedAccounts.sort(Comparator.comparing(Account::getAccountNumber));

        int left = 0;
        int right = sortedAccounts.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Avoid potential overflow
            Account midAccount = sortedAccounts.get(mid);
            int cmp = midAccount.getAccountNumber().compareTo(target);

            if (cmp == 0) {
                // Found the account
                return "Account Found:\n" +
                       "  Number: " + midAccount.getAccountNumber() + "\n" +
                       "  Name: " + midAccount.getName() + "\n" +
                       "  Balance: " + String.format("%.2f", midAccount.getBalance());
            } else if (cmp < 0) {
                // Target is in the right half
                left = mid + 1;
            } else {
                // Target is in the left half
                right = mid - 1;
            }
        }
        return null; // Not Found
    }

    // Returns accounts sorted by balance as a String
    public String getAccountsSortedByBalance() {
        List<Account> list = new ArrayList<>(accounts.values());
        if (list.isEmpty()) {
            return "No accounts available.";
        }
        // Use internal mergeSort
        mergeSort(list, 0, list.size() - 1);

        StringBuilder sb = new StringBuilder("=== Accounts Sorted by Balance ===\n");
        for (Account acc : list) {
            sb.append(acc.getAccountNumber())
              .append(" | Name: ").append(acc.getName())
              .append(" | Balance: ").append(String.format("%.2f", acc.getBalance()))
              .append("\n");
        }
        return sb.toString();
    }

    // Adds an edge to the transaction graph
    public void addTransactionEdge(String from, String to, double amount) {
        // Ensure 'from' account exists in the graph keys, even if it has no outgoing edges yet
        transactionGraph.computeIfAbsent(from, k -> new ArrayList<>());

        // Now add the edge
        transactionGraph.get(from).add(new TransactionEdge(to, amount));

        // Also ensure 'to' account exists as a key if you want to trace paths *from* it later
        // even if it hasn't made any transactions itself yet.
        transactionGraph.computeIfAbsent(to, k -> new ArrayList<>());
    }


    // --- Merge Sort Implementation (for sorting accounts by balance) ---
    private void mergeSort(List<Account> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2; // Avoid potential overflow
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private void merge(List<Account> list, int left, int mid, int right) {
        // Create temporary lists for left and right subarrays
        List<Account> leftTmp = new ArrayList<>(list.subList(left, mid + 1));
        List<Account> rightTmp = new ArrayList<>(list.subList(mid + 1, right + 1));

        int i = 0; // Index for leftTmp
        int j = 0; // Index for rightTmp
        int k = left; // Index for merged list (original list)

        // Merge the temp arrays back into the original list[left..right]
        while (i < leftTmp.size() && j < rightTmp.size()) {
            if (leftTmp.get(i).getBalance() <= rightTmp.get(j).getBalance()) {
                list.set(k, leftTmp.get(i));
                i++;
            } else {
                list.set(k, rightTmp.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements of leftTmp, if any
        while (i < leftTmp.size()) {
            list.set(k, leftTmp.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of rightTmp, if any
        while (j < rightTmp.size()) {
            list.set(k, rightTmp.get(j));
            j++;
            k++;
        }
    }
}
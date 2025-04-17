// --- BankSystemGUI.java (Complete and Updated) ---
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.stream.Collectors;

public class BankSystemGUI extends JFrame {

    private BankSystem bankSystem; // Instance of the backend logic
    private CardLayout cardLayout;
    private JPanel mainPanel;

    // Panels for different screens
    private JPanel homePanel;
    private JPanel createAccountPanel;
    private JPanel userLoginPanel;
    private JPanel adminLoginPanel;
    private JPanel userDashboardPanel;
    private JPanel adminDashboardPanel;

    // Fields to hold logged-in user info (if any)
    private Account loggedInAccount = null;

    // Components that need updating
    private JLabel userBalanceLabel; // To show balance on user dashboard

    public BankSystemGUI() {
        bankSystem = new BankSystem(); // Initialize the backend

        setTitle("DSA Bank");
        setSize(600, 450); // Adjusted size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create all the panels
        createHomePanel();
        createCreateAccountPanel();
        createUserLoginPanel();
        createAdminLoginPanel();
        createUserDashboardPanel(); // Create structure, update content on login
        createAdminDashboardPanel();  // Create structure

        // Add panels to the CardLayout container
        mainPanel.add(homePanel, "Home");
        mainPanel.add(createAccountPanel, "CreateAccount");
        mainPanel.add(userLoginPanel, "UserLogin");
        mainPanel.add(adminLoginPanel, "AdminLogin");
        mainPanel.add(userDashboardPanel, "UserDashboard");
        mainPanel.add(adminDashboardPanel, "AdminDashboard");

        add(mainPanel); // Add the main panel to the frame

        // Show the home panel initially
        cardLayout.show(mainPanel, "Home");
    }

    // --- Panel Creation Methods ---

    private void createHomePanel() {
        homePanel = new JPanel(new GridBagLayout()); // Use GridBagLayout for centering
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Each component on a new line
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50); // Padding (top, left, bottom, right)

        JLabel titleLabel = new JLabel("=== Welcome to DSA Bank ===", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton createBtn = new JButton("1. Create Account");
        JButton userLoginBtn = new JButton("2. User Login");
        JButton adminLoginBtn = new JButton("3. Admin Login");
        JButton exitBtn = new JButton("0. Exit");

        // Set preferred size for buttons to make them uniform
        Dimension buttonSize = new Dimension(200, 40);
        createBtn.setPreferredSize(buttonSize);
        userLoginBtn.setPreferredSize(buttonSize);
        adminLoginBtn.setPreferredSize(buttonSize);
        exitBtn.setPreferredSize(buttonSize);

        // Add components with constraints
        gbc.weighty = 0.1; // Add some space above title
        homePanel.add(new JLabel(), gbc); // Spacer

        gbc.weighty = 0; // Reset weighty
        homePanel.add(titleLabel, gbc);

        gbc.insets = new Insets(5, 50, 5, 50); // Smaller vertical spacing for buttons
        homePanel.add(createBtn, gbc);
        homePanel.add(userLoginBtn, gbc);
        homePanel.add(adminLoginBtn, gbc);
        homePanel.add(exitBtn, gbc);

        gbc.weighty = 0.1; // Add some space below buttons
        homePanel.add(new JLabel(), gbc); // Spacer


        // Action Listeners
        createBtn.addActionListener(e -> cardLayout.show(mainPanel, "CreateAccount"));
        userLoginBtn.addActionListener(e -> cardLayout.show(mainPanel, "UserLogin"));
        adminLoginBtn.addActionListener(e -> cardLayout.show(mainPanel, "AdminLogin"));
        exitBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Thank you for using DSA Bank!");
            System.exit(0);
        });
    }

    private void createCreateAccountPanel() {
        createAccountPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel userLabel = new JLabel("Enter Username:");
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Create Password:");
        JPasswordField passField = new JPasswordField(20);
        JButton createBtn = new JButton("Create Account");
        JButton backBtn = new JButton("Back");

        // Layout components
        gbc.gridx = 0; gbc.gridy = 0; createAccountPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; createAccountPanel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; createAccountPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; createAccountPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; createAccountPanel.add(backBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; createAccountPanel.add(createBtn, gbc);


        // Action Listeners
        createBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String accountNumber = bankSystem.createAccount(username, password);
            if (accountNumber != null) {
                JOptionPane.showMessageDialog(this, "✅ Account created successfully!\nYour Account Number: " + accountNumber, "Success", JOptionPane.INFORMATION_MESSAGE);
                userField.setText(""); // Clear fields
                passField.setText("");
                cardLayout.show(mainPanel, "Home"); // Go back home
            } else {
                // This case might happen if createAccount is modified to handle duplicates differently,
                // or for other unexpected errors.
                 JOptionPane.showMessageDialog(this, "Failed to create account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backBtn.addActionListener(e -> {
            userField.setText(""); // Clear fields on back
            passField.setText("");
            cardLayout.show(mainPanel, "Home");
        });
    }

     private void createUserLoginPanel() {
        userLoginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel accNumLabel = new JLabel("Account Number:");
        JTextField accNumField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; userLoginPanel.add(accNumLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; userLoginPanel.add(accNumField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; userLoginPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; userLoginPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; userLoginPanel.add(backBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; userLoginPanel.add(loginBtn, gbc);

        // Action Listeners
        loginBtn.addActionListener(e -> {
            String accNum = accNumField.getText().trim(); // Trim input
            String password = new String(passField.getPassword());

            if (accNum.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Account number and password cannot be empty.", "Login Failed", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loggedInAccount = bankSystem.loginUser(accNum, password);

            if (loggedInAccount != null) {
                // JOptionPane.showMessageDialog(this, "Login Successful!\nWelcome " + loggedInAccount.getName(), "Success", JOptionPane.INFORMATION_MESSAGE); // Can omit this popup
                accNumField.setText(""); // Clear fields
                passField.setText("");
                updateUserDashboard(); // Update dashboard with user info
                cardLayout.show(mainPanel, "UserDashboard");
            } else {
                // Check if account exists before saying wrong password
                 if (!bankSystem.getAccounts().containsKey(accNum)) {
                     JOptionPane.showMessageDialog(this, "⚠️ Invalid account number.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                 } else {
                     JOptionPane.showMessageDialog(this, "⚠️ Incorrect password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                 }
                 passField.setText(""); // Clear only password on failure
            }
        });

        backBtn.addActionListener(e -> {
             accNumField.setText(""); // Clear fields on back
             passField.setText("");
             cardLayout.show(mainPanel, "Home");
        });
    }

     private void createAdminLoginPanel() {
        adminLoginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;


        JLabel userLabel = new JLabel("Admin Username:");
        JTextField userField = new JTextField(20);
        JLabel passLabel = new JLabel("Admin Password:");
        JPasswordField passField = new JPasswordField(20);
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; adminLoginPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; adminLoginPanel.add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; adminLoginPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; adminLoginPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; adminLoginPanel.add(backBtn, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER; adminLoginPanel.add(loginBtn, gbc);


        // Action Listeners
        loginBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (bankSystem.adminLogin(username, password)) {
                 // JOptionPane.showMessageDialog(this, "👑 Welcome Admin!", "Admin Login Success", JOptionPane.INFORMATION_MESSAGE); // Can omit this popup
                 userField.setText(""); // Clear fields
                 passField.setText("");
                 cardLayout.show(mainPanel, "AdminDashboard");
            } else {
                 JOptionPane.showMessageDialog(this, "⚠️ Access Denied. Invalid Credentials.", "Admin Login Failed", JOptionPane.ERROR_MESSAGE);
                 passField.setText(""); // Clear only password
            }
        });

        backBtn.addActionListener(e -> {
            userField.setText(""); // Clear fields on back
            passField.setText("");
            cardLayout.show(mainPanel, "Home");
        });
    }

    // --- Dashboards (created once, updated as needed) ---

    private void createUserDashboardPanel() {
        userDashboardPanel = new JPanel(new BorderLayout(10, 10)); // Use BorderLayout
        userDashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Top Panel for Welcome Message and Balance
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel welcomeLabel = new JLabel("Welcome!"); // Will be updated
        userBalanceLabel = new JLabel("Balance: ---"); // Placeholder
        topPanel.add(welcomeLabel);
        topPanel.add(Box.createHorizontalStrut(20)); // Spacer
        topPanel.add(userBalanceLabel);
        userDashboardPanel.add(topPanel, BorderLayout.NORTH);

        // Center Panel for Buttons using GridLayout
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns, gaps
        JButton checkBalanceBtn = new JButton("1. Check Balance");
        JButton depositBtn = new JButton("2. Deposit");
        JButton withdrawBtn = new JButton("3. Withdraw");
        JButton transferBtn = new JButton("4. Transfer Money");
        JButton viewTransBtn = new JButton("5. View Transactions");
        JButton tracePathBtn = new JButton("6. Trace Transaction Path");
        JButton logoutBtn = new JButton("0. Logout");

        buttonPanel.add(checkBalanceBtn);
        buttonPanel.add(depositBtn);
        buttonPanel.add(withdrawBtn);
        buttonPanel.add(transferBtn);
        buttonPanel.add(viewTransBtn);
        buttonPanel.add(tracePathBtn);

        // Panel for Logout Button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutBtn.setPreferredSize(new Dimension(150, 30));
        bottomPanel.add(logoutBtn);


        userDashboardPanel.add(buttonPanel, BorderLayout.CENTER);
        userDashboardPanel.add(bottomPanel, BorderLayout.SOUTH);

        // --- Action Listeners for User Dashboard ---

        checkBalanceBtn.addActionListener(e -> {
            if (loggedInAccount != null) {
                updateBalanceLabel(); // Refresh the label
                JOptionPane.showMessageDialog(this, "💰 Current Balance: " + String.format("%.2f", loggedInAccount.getBalance()), "Balance", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        depositBtn.addActionListener(e -> {
             if (loggedInAccount == null) return;
             String amountStr = JOptionPane.showInputDialog(this, "Enter amount to deposit:", "Deposit", JOptionPane.QUESTION_MESSAGE);
             if (amountStr != null && !amountStr.trim().isEmpty()) { // Check if not null or empty
                 try {
                     double amount = Double.parseDouble(amountStr);
                     if (amount <= 0) {
                         JOptionPane.showMessageDialog(this, "⚠️ Please enter a positive amount.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                         return;
                     }
                     double newBalance = bankSystem.performDeposit(loggedInAccount, amount);
                     if (newBalance >= 0) {
                         updateBalanceLabel();
                         JOptionPane.showMessageDialog(this, "✅ Deposit Successful!\nNew Balance: " + String.format("%.2f", newBalance), "Success", JOptionPane.INFORMATION_MESSAGE);
                     } else {
                         // This case shouldn't happen with current logic unless performDeposit changes
                         JOptionPane.showMessageDialog(this, "⚠️ Deposit failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                     }
                 } catch (NumberFormatException ex) {
                     JOptionPane.showMessageDialog(this, "⚠️ Invalid amount entered. Please enter numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
                 }
             }
        });

         withdrawBtn.addActionListener(e -> {
            if (loggedInAccount == null) return;
            String amountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:", "Withdraw", JOptionPane.QUESTION_MESSAGE);
             if (amountStr != null && !amountStr.trim().isEmpty()) { // Check if not null or empty
                try {
                    double amount = Double.parseDouble(amountStr);
                     if (amount <= 0) {
                         JOptionPane.showMessageDialog(this, "⚠️ Please enter a positive amount.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                         return;
                     }
                    double newBalance = bankSystem.performWithdraw(loggedInAccount, amount);
                    if (newBalance >= 0) {
                        updateBalanceLabel();
                        JOptionPane.showMessageDialog(this, "✅ Withdrawal Successful!\nNew Balance: " + String.format("%.2f", newBalance), "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // Specific message for insufficient funds
                        JOptionPane.showMessageDialog(this, "⚠️ Withdrawal Failed.\nInsufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "⚠️ Invalid amount entered. Please enter numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

         transferBtn.addActionListener(e -> {
            if (loggedInAccount == null) return;

            // Use a custom panel for multiple inputs
            JTextField recipientField = new JTextField(15);
            JTextField amountField = new JTextField(10);
            // Use GridBagLayout for better alignment in the popup
            JPanel transferPanel = new JPanel(new GridBagLayout());
            GridBagConstraints popupGbc = new GridBagConstraints();
            popupGbc.insets = new Insets(2, 2, 2, 2);
            popupGbc.anchor = GridBagConstraints.WEST;

            popupGbc.gridx = 0; popupGbc.gridy = 0; transferPanel.add(new JLabel("Recipient Account #:"), popupGbc);
            popupGbc.gridx = 1; popupGbc.gridy = 0; transferPanel.add(recipientField, popupGbc);
            popupGbc.gridx = 0; popupGbc.gridy = 1; transferPanel.add(new JLabel("Amount:"), popupGbc);
            popupGbc.gridx = 1; popupGbc.gridy = 1; transferPanel.add(amountField, popupGbc);


            int result = JOptionPane.showConfirmDialog(this, transferPanel, "Transfer Money", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                 String recipientAccNum = recipientField.getText().trim();
                 String amountStr = amountField.getText().trim();

                 if (recipientAccNum.isEmpty() || amountStr.isEmpty()) {
                     JOptionPane.showMessageDialog(this, "⚠️ Recipient Account# and Amount cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                     return;
                 }

                 try {
                     double amount = Double.parseDouble(amountStr);
                      if (amount <= 0) {
                         JOptionPane.showMessageDialog(this, "⚠️ Please enter a positive amount.", "Invalid Amount", JOptionPane.WARNING_MESSAGE);
                         return;
                     }
                     if (!bankSystem.getAccounts().containsKey(recipientAccNum)) {
                         JOptionPane.showMessageDialog(this, "⚠️ Recipient account number does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                         return;
                     }
                     if (recipientAccNum.equals(loggedInAccount.getAccountNumber())) {
                         JOptionPane.showMessageDialog(this, "⚠️ Cannot transfer to your own account.", "Error", JOptionPane.WARNING_MESSAGE);
                         return;
                     }

                     boolean success = bankSystem.performTransfer(loggedInAccount, recipientAccNum, amount);
                     if (success) {
                         updateBalanceLabel();
                         JOptionPane.showMessageDialog(this, "💸 Transfer Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                     } else {
                         // Specific message for insufficient funds during transfer
                         JOptionPane.showMessageDialog(this, "⚠️ Transfer Failed. Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
                     }

                 } catch (NumberFormatException ex) {
                     JOptionPane.showMessageDialog(this, "⚠️ Invalid amount entered. Please enter numbers only.", "Input Error", JOptionPane.ERROR_MESSAGE);
                 }
            }
        });

        viewTransBtn.addActionListener(e -> {
            if (loggedInAccount == null) return;
            // *** THIS LINE IS UPDATED ***
            List<String> transactions = bankSystem.getAccountTransactions(loggedInAccount);
            // *** END UPDATE ***

            JTextArea textArea = new JTextArea(15, 50); // Increased width slightly
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Use monospaced for alignment
            textArea.setEditable(false);
            textArea.setWrapStyleWord(true); // Wrap lines nicely
            textArea.setLineWrap(true);

            if (transactions.isEmpty()) {
                textArea.setText("No transactions recorded yet.");
            } else {
                // Join transactions with newline characters for display
                textArea.setText(String.join("\n", transactions));
            }
             // Set caret position to the top for better viewing
            textArea.setCaretPosition(0);
            JScrollPane scrollPane = new JScrollPane(textArea);
            // Make the dialog box slightly larger
            scrollPane.setPreferredSize(new Dimension(550, 300));
             JOptionPane.showMessageDialog(this, scrollPane, "Transaction Log for " + loggedInAccount.getAccountNumber(), JOptionPane.INFORMATION_MESSAGE);
        });

        tracePathBtn.addActionListener(e -> {
            if (loggedInAccount == null) return;
            String destinationAccNum = JOptionPane.showInputDialog(this, "Enter destination account number to trace path from your account ("+ loggedInAccount.getAccountNumber() +"):" , "Trace Path", JOptionPane.QUESTION_MESSAGE);
            if (destinationAccNum != null && !destinationAccNum.trim().isEmpty()) {
                destinationAccNum = destinationAccNum.trim(); // Trim input
                 if (!bankSystem.getAccounts().containsKey(destinationAccNum)) {
                    JOptionPane.showMessageDialog(this, "⚠️ Destination account does not exist.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                 if (destinationAccNum.equals(loggedInAccount.getAccountNumber())) {
                     JOptionPane.showMessageDialog(this, "Path to self: " + loggedInAccount.getAccountNumber(), "Trace Result", JOptionPane.INFORMATION_MESSAGE);
                     return;
                 }

                List<String> path = bankSystem.traceTransactionPath(loggedInAccount.getAccountNumber(), destinationAccNum);
                if (path != null) {
                    JOptionPane.showMessageDialog(this, "Path found:\n" + String.join(" -> ", path), "Trace Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                     JOptionPane.showMessageDialog(this, "⚠️ No direct or indirect transaction path found.", "Trace Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        logoutBtn.addActionListener(e -> {
            loggedInAccount = null; // Clear logged in user
            JOptionPane.showMessageDialog(this, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
            // Reset user dashboard potentially (or just hide it)
            userBalanceLabel.setText("Balance: ---");
             Component[] components = userDashboardPanel.getComponents();
            if (components[0] instanceof JPanel) { // Find the top panel
                 //JPanel topPanel = (JPanel) components[0];
                 Component[] topComponents = topPanel.getComponents();
                 if (topComponents[0] instanceof JLabel) { // Welcome Label
                     ((JLabel)topComponents[0]).setText("Welcome!");
                 }
            }
            cardLayout.show(mainPanel, "Home");
        });
    }


     private void createAdminDashboardPanel() {
        adminDashboardPanel = new JPanel(new BorderLayout(10, 10));
        adminDashboardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("--- Admin Dashboard ---", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        adminDashboardPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // Single column layout
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40)); // Add horizontal padding

        JButton viewGraphBtn = new JButton("1. View Transaction Graph");
        JButton tracePathBtn = new JButton("2. Trace Path Between Accounts");
        JButton searchAccBtn = new JButton("3. Search Account (Binary Search)");
        JButton viewReportsBtn = new JButton("4. View Reports (Sorted by Balance)");
        JButton logoutBtn = new JButton("0. Logout");

        buttonPanel.add(viewGraphBtn);
        buttonPanel.add(tracePathBtn);
        buttonPanel.add(searchAccBtn);
        buttonPanel.add(viewReportsBtn);
        buttonPanel.add(Box.createVerticalStrut(20)); // Add space before logout
        buttonPanel.add(logoutBtn);

        adminDashboardPanel.add(buttonPanel, BorderLayout.CENTER);

        // --- Action Listeners for Admin Dashboard ---

        viewGraphBtn.addActionListener(e -> {
            String graphData = bankSystem.getTransactionGraphAsString();
            JTextArea textArea = new JTextArea(15, 50);
            textArea.setText(graphData);
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Good for text graphs
             textArea.setCaretPosition(0); // Start view from top
            JScrollPane scrollPane = new JScrollPane(textArea);
             scrollPane.setPreferredSize(new Dimension(550, 300)); // Make dialog larger
            JOptionPane.showMessageDialog(this, scrollPane, "Transaction Graph", JOptionPane.INFORMATION_MESSAGE);
        });

        tracePathBtn.addActionListener(e -> {
            // Use a custom panel for multiple inputs
            JTextField startField = new JTextField(15);
            JTextField endField = new JTextField(15);
             // Use GridBagLayout for better alignment in the popup
            JPanel tracePanel = new JPanel(new GridBagLayout());
            GridBagConstraints popupGbc = new GridBagConstraints();
            popupGbc.insets = new Insets(2, 2, 2, 2);
            popupGbc.anchor = GridBagConstraints.WEST;

            popupGbc.gridx = 0; popupGbc.gridy = 0; tracePanel.add(new JLabel("Source Account #:"), popupGbc);
            popupGbc.gridx = 1; popupGbc.gridy = 0; tracePanel.add(startField, popupGbc);
            popupGbc.gridx = 0; popupGbc.gridy = 1; tracePanel.add(new JLabel("Destination Account #:"), popupGbc);
            popupGbc.gridx = 1; popupGbc.gridy = 1; tracePanel.add(endField, popupGbc);


            int result = JOptionPane.showConfirmDialog(this, tracePanel, "Trace Transaction Path", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

             if (result == JOptionPane.OK_OPTION) {
                String startAcc = startField.getText().trim();
                String endAcc = endField.getText().trim();

                if (startAcc.isEmpty() || endAcc.isEmpty()) {
                     JOptionPane.showMessageDialog(this, "⚠️ Please enter both source and destination account numbers.", "Input Error", JOptionPane.WARNING_MESSAGE);
                     return;
                }
                 if (!bankSystem.getAccounts().containsKey(startAcc) || !bankSystem.getAccounts().containsKey(endAcc)) {
                    JOptionPane.showMessageDialog(this, "⚠️ One or both account numbers do not exist.", "Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                 if (startAcc.equals(endAcc)) {
                     JOptionPane.showMessageDialog(this, "Path to self: " + startAcc, "Trace Result", JOptionPane.INFORMATION_MESSAGE);
                     return;
                 }

                 List<String> path = bankSystem.traceTransactionPath(startAcc, endAcc);
                 if (path != null) {
                    JOptionPane.showMessageDialog(this, "Path found:\n" + String.join(" -> ", path), "Trace Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                     JOptionPane.showMessageDialog(this, "⚠️ No direct or indirect transaction path found between these accounts.", "Trace Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        searchAccBtn.addActionListener(e -> {
            String targetAccNum = JOptionPane.showInputDialog(this, "Enter account number to search:", "Search Account", JOptionPane.QUESTION_MESSAGE);
            if (targetAccNum != null && !targetAccNum.trim().isEmpty()) {
                String result = bankSystem.searchAccountDetails(targetAccNum.trim());
                if (result != null) {
                    // Display result in a non-editable text area for better formatting
                     JTextArea resultArea = new JTextArea(result);
                     resultArea.setEditable(false);
                     resultArea.setOpaque(false); // Make background transparent
                     JOptionPane.showMessageDialog(this, resultArea, "Search Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                     JOptionPane.showMessageDialog(this, "⚠️ Account not found.", "Search Result", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        viewReportsBtn.addActionListener(e -> {
            String reportData = bankSystem.getAccountsSortedByBalance();
            JTextArea textArea = new JTextArea(15, 50);
            textArea.setText(reportData);
            textArea.setEditable(false);
             textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
             textArea.setCaretPosition(0); // Start view from top
            JScrollPane scrollPane = new JScrollPane(textArea);
             scrollPane.setPreferredSize(new Dimension(550, 300)); // Make dialog larger
            JOptionPane.showMessageDialog(this, scrollPane, "Accounts Sorted by Balance", JOptionPane.INFORMATION_MESSAGE);
        });

        logoutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Admin logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "Home");
        });
    }

    // --- Helper Methods ---

    private void updateUserDashboard() {
        if (loggedInAccount != null) {
            // Update welcome message and balance label
            // Find the top panel which should be the first component added (BorderLayout.NORTH)
            Component[] components = userDashboardPanel.getComponents();
            if (components.length > 0 && components[0] instanceof JPanel) {
                 JPanel topPanel = (JPanel) components[0];
                 // Find the Welcome Label (should be the first component in topPanel)
                 if (topPanel.getComponentCount() > 0 && topPanel.getComponent(0) instanceof JLabel) {
                     ((JLabel)topPanel.getComponent(0)).setText("Welcome, " + loggedInAccount.getName() + " (" + loggedInAccount.getAccountNumber() + ")");
                 }
                 updateBalanceLabel(); // Update balance
            }
        }
    }

    // Updates the userBalanceLabel specifically
    private void updateBalanceLabel() {
         if (loggedInAccount != null && userBalanceLabel != null) {
             // Use String.format for consistent currency formatting
             userBalanceLabel.setText(String.format("💰 Balance: %.2f", loggedInAccount.getBalance()));
         }
    }


    // --- Main Method to Launch the GUI ---
    public static void main(String[] args) {
        // Ensure GUI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            BankSystemGUI gui = new BankSystemGUI();
            gui.setVisible(true);
        });
    }
}
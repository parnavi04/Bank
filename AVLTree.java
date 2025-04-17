public class AVLTree {
    private AVLNode root;

    public void insert(Account account) {
        root = insert(root, account);
    }

    private AVLNode insert(AVLNode node, Account account) {
        if (node == null) return new AVLNode(account);

        if (account.getAccountNumber().compareTo(node.data.getAccountNumber()) < 0)
            node.left = insert(node.left, account);
        else
            node.right = insert(node.right, account);

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }

    private int height(AVLNode n) {
        return (n == null) ? 0 : n.height;
    }

    private int getBalance(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode balance(AVLNode node) {
        int balance = getBalance(node);

        if (balance > 1) {
            if (getBalance(node.left) < 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        } else if (balance < -1) {
            if (getBalance(node.right) > 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    public void displayInOrder() {
        displayInOrder(root);
    }

    private void displayInOrder(AVLNode node) {
        if (node != null) {
            displayInOrder(node.left);
            System.out.println("Account: " + node.data.getAccountNumber() + " | Balance: Rs." + node.data.getBalance());
            displayInOrder(node.right);
        }
    }
}
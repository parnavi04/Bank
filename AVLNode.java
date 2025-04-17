class AVLNode {
    Account data;
    AVLNode left, right;
    int height;

    public AVLNode(Account data) {
        this.data = data;
        this.height = 1;
    }
}
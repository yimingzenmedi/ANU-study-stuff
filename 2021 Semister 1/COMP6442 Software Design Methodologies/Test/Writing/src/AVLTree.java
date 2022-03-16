/**
 * 
 * 
 * The given code is provided to assist you to complete the required tasks. But the 
 * given code is often incomplete. You have to read and understand the given code 
 * carefully, before you can apply the code properly. You might need to implement 
 * additional procedures, such as error checking and handling, in order to apply the 
 * code properly.
 */


public class AVLTree {
    Node root;

    public AVLTree() {
        this.root = null;
    }

    /**
     * This function is to check whether the given BST tree is an AVL tree. The main idea is checking the properties of
     * AVL tree.
     * hint: you can use the helper function "getBalance()"
     *
     * @param root root node of the given BST tree
     * @return boolean value
     * **/
    boolean isBalanced(Node root){
        // TODO: Complete this method
        // START YOUR CODE

        if (root == null) {
            return true;
        }
        int balance = getBalance(root);
        if (balance < -1 || balance > 1) {
            return false;
        }
        if (!isBalanced(root.left)) {
            return false;
        }
        if (!isBalanced(root.right)) {
            return false;
        }
        return true;
        // END YOUR CODE
//        return false;
    }
    /**
     * This helper function is to obtain each node's height.
     *
     * @param node node of the given BST tree
     * @return int the height of the input node
     * **/
    public int getHeight(Node node){
        // TODO: Complete this method
        // START YOUR CODE
        if (node == null || node.key == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        int leftHeight = 1, rightHeight = 1;
        if (node.left != null) {
            leftHeight += getHeight(node.left);
        }
        if (node.right != null) {
            rightHeight += getHeight(node.right);
        }
        return Math.max(leftHeight, rightHeight);

        // END YOUR CODE
    }

    /**
     * This function is to find the balance factor of each node.
     * hint: you can implement the helper function "getHieght()".
     *
     * @param node node of the given BST tree
     * @return int the balance of the node
     * **/
    public int getBalance(Node node){
        // TODO: Complete this method
        // START YOUR CODE

        int leftHeight = 0, rightHeight = 0;
        if (node.left != null) {
            leftHeight = getHeight(node.left);
        }
        if (node.right != null) {
            rightHeight = getHeight(node.right);
        }
        System.out.println("\nNode: " + node.key + ", L: " + (node.left != null ? node.left.key : null) + ", R: "+(node.right !=null?node.right.key:null));
        System.out.println("RH: " + rightHeight + ", LH: " + leftHeight+", BLC: " + (rightHeight - leftHeight));
        return rightHeight - leftHeight;

        // END YOUR CODE
//        return 0;
    }

    /**
     * This implementation of BST insertion follows the pseudo code in the lecture slide.
     * Node that we do not use recursion or insertion fixup to fix the balance of AVL tree.
     *
     * @param key key of inserted node
     * @return inserted node (not the entire tree)
     */
    public Node insert(Integer key) {
        Node parent = null;
        Node current = this.root;
        while (current != null) {
            if(current.key.compareTo(key) < 0) {
                parent = current;
                current = current.right;
            }else if (current.key.compareTo(key) > 0){
                parent = current;
                current = current.left;
            }
        }

        if (parent == null && current == null) {
            this.root = new Node(key);  // no parent = root is empty
            return root;
        }else {
            Node newNode = new Node(key);

            if(parent.key.compareTo(key) < 0) {
                parent.right = newNode;
                newNode.parent = parent;
            }else if(parent.key.compareTo(key) > 0) {
                parent.left = newNode;
                newNode.parent = parent;
            }
            return newNode;
        }
    }

    public class Node {
        Integer key;
        Node parent;
        Node left;
        Node right;

        public Node(Integer key) {
            this.key = key;
            this.parent = null;
            this.left = null;
            this.right = null;
        }
    }
}

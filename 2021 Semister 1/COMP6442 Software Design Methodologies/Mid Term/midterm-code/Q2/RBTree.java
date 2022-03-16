/**
 * Skeleton code for Red Black Tree.
 * You are required to implement the search & insert methods, where the keys are intervals.
 * You may add additional helper methods.  
 *
 * The given code is provided to assist you to complete the required tasks. But the 
 * given code is often incomplete. You have to read and understand the given code 
 * carefully, before you can apply the code properly. You might need to implement 
 * additional procedures, such as error checking and handling, in order to apply the 
 * code properly.
 *
 * @param <T> data type
 */

public class RBTree<T extends Comparable<T>> {
	
	Node<T> root; // The root node of the tree

	/**
	 * Initialize empty RBTree
	 */
	public RBTree() {
		root = null;
	}


	/**
	 * (Safely) insert a key into the tree
	 * Note that if a node with duplicate key is inserted into the red-black tree, 
	 * it will replace the old node with the same key.
	 * 
	 * @param key T The key of the new node being inserted.
	 */
	public void insert(Node.Interval<T> key) {
		Node<T> node = new Node<T>(key);

		// TODO: Complete this method
		// START YOUR CODE



		// END YOUR CODE

	}

	/**
	 * Returns a node if the key of the node is {@code key}.
	 * Otherwise, return null.
	 *
	 * @param key T The key we are looking for
	 * @return
	 */
	public Node<T> search(Node.Interval<T> key) {
		// TODO: Complete this method
		// START YOUR CODE




		return null;
		// END YOUR CODE
	}

	/**
	 * Return the result of a pre-order traversal of the tree
	 * 
	 * @param tree Tree we want to pre-order traverse
	 * @return pre-order traversed tree
	 */
	private String preOrder(Node<T> tree) {
		if (tree != null && tree.key != null) {
			String leftStr = preOrder(tree.left);
			String rightStr = preOrder(tree.right);
			return "("+tree.key.startTime + "," + tree.key.endTime +")" + (leftStr.isEmpty() ? leftStr : " " + leftStr)
					+ (rightStr.isEmpty() ? rightStr : " " + rightStr);
		}

		return "";
	}

	public String preOrder() {
		return preOrder(root);
	}

}
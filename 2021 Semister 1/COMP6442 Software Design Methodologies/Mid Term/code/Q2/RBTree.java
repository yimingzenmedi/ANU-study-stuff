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

//		insertNode(root, key);



		// Insert node into tree
		if (root == null) {
			root = node;
		} else {
			if(search(node.key) != null) return;
			insertRecurse(root, node);
		}

		// Fix tree
		while (compareNodes(root.key, node.key) != 0 && node.parent.colour == Colour.RED) {
			boolean left  = node.parent == node.parent.parent.left; // Is parent a left node
			Node<T> uncle = left ? node.parent.parent.right : node.parent.parent.left; // Get opposite "uncle" node to parent

			if (uncle.colour == Colour.RED) {
				// Case 1: Recolour

//                System.out.println("CASE 1: " + node.key);
				node.parent.colour = Colour.BLACK;
				node.parent.parent.colour = Colour.RED;
				uncle.colour = Colour.BLACK;

				// Check if violated further up the tree
				node = node.parent.parent;
			} else {
				if (node.key == (left ? node.parent.right.key : node.parent.left.key)) {
					// Case 2: Left Rotation, uncle is right node, node is on the right / Right Rotation, uncle is left node, node is on the left
					node = node.parent;
					if (left) {
						// Perform left rotation
						if (node.key == root.key)
							root = node.right; // Update root
						rotateLeft(node);
					} else {
						// This is part of the "then" clause where left and right are swapped
						// Perform right rotation
						// TODO: Implement this part
						// ########## YOUR CODE STARTS HERE ##########

//                        System.out.println("Case 2: " + node.key);
						if (node.key == root.key) {
							root = node.left;
						}
						rotateRight(node);

						// ########## YOUR CODE ENDS HERE ##########
					}
				}
				// Adjust colours to ensure correctness after rotation
				node.parent.colour = Colour.BLACK;
				node.parent.parent.colour = Colour.RED;

				// Case 3 : Right Rotation, uncle is right node, node is on the left / Left Rotation, uncle is left node, node is on the right
				// TODO: Complete this part
				if (left) {
					// Perform right rotation
					// ########## YOUR CODE STARTS HERE ##########

//                    System.out.println("Case 3.1: " + node.key);
					rotateRight(node.parent.parent);

					// ########## YOUR CODE ENDS HERE ##########
				} else {
					// This is part of the "then" clause where left and right are swapped
					// Perform left rotation
					// ########## YOUR CODE STARTS HERE ##########

//                    System.out.println("Case 3.2: " + node.key);
					rotateLeft(node.parent.parent);

					// ########## YOUR CODE ENDS HERE ##########
				}
			}
		}

		// Ensure property 2 (root and leaves are black) holds
		root.colour = Colour.BLACK;
		System.out.println(this.preOrder());

		// END YOUR CODE

	}

	public void rotateLeft(Node<T> x) {
		// Make parent (if it exists) and right branch point to each other
		if (x.parent != null) {
			// Determine whether this node is the left or right child of its parent
			if (x.parent.left.key == x.key) {
				x.parent.left = x.right;
			} else {
				x.parent.right = x.right;
			}
		}
		x.right.parent = x.parent;

		x.parent = x.right;
		// Take right node's left branch
		x.right = x.parent.left;
		x.right.parent = x;
		// Take the place of the right node's left branch
		x.parent.left = x;
	}

	public void rotateRight(Node<T> x) {
		// TODO: Implement this function
		// HINT: It is the mirrored version of rotateLeft()
		// ########## YOUR CODE STARTS HERE ##########

		if (x.parent != null) {
			// Determine whether this node is the left or right child of its parent
			if (x.parent.left.key == x.key) {
				x.parent.left = x.left;
			} else {
				x.parent.right = x.left;
			}
		}
		x.left.parent = x.parent;

		x.parent = x.left;
		// Take left node's right branch
		x.left = x.parent.right;
		x.left.parent = x;
		// Take the place of the left node's right branch
		x.parent.right = x;

		// ########## YOUR CODE ENDS HERE ##########
	}

	private void insertRecurse(Node<T> root, Node<T> x) {
		int cmp = compareNodes(root.key, x.key);

		if (cmp > 0) {
			if (root.left.key == null) {
				root.left = x;
				x.parent = root;
			} else {
				insertRecurse(root.left, x);
			}
		} else if (cmp < 0) {
			if (root.right.key == null) {
				root.right = x;
				x.parent = root;
			} else {
				insertRecurse(root.right, x);
			}
		}
		// Do nothing if the tree already has a node with the same key.
	}

	public void insertNode(Node<T> node, Node.Interval<T> key) {

//		// empty tree:
//		if (node == null || node.key == null) {
//			node = new Node<>(key);
//			node.colour = Colour.BLACK;
//			node.left = new Node<>();
//			node.right = new Node<>();
//			return;
//		}
//
//		if (compareNodes(node.key, key) < 1) {
//			if (node.left == null || node.left.key == null) {
//				node.left = new Node<>(key);
//				node.left.left = new Node<>();
//				node.left.right = new Node<>();
//				// TODO: 2021/4/19 check color
// 			} else {
//				insertNode(node.left, key);
//			}
//		} else {
//			if (node.right == null || node.right.key == null) {
//				node.right = new Node<>(key);
//				node.right.left = new Node<>();
//				node.right.right = new Node<>();
//				// TODO: 2021/4/19 check color
//			} else {
//				insertNode(node.right, key);
//			}
//		}
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

		return find(root, key);

		// END YOUR CODE
	}

	private Node<T> find(Node<T> x, Node.Interval<T> v) {
		if (x.key == null)
			return null;

		int cmp = compareNodes(v, x.key);
		if (cmp < 0)
			return find(x.left, v);
		else if (cmp > 0)
			return find(x.right, v);
		else
			return x;
	}


	private int compareNodes(Node.Interval<T> n1, Node.Interval<T> n2) {
		if (n1.startTime.compareTo(n2.startTime) == 0 &&
				n1.endTime.compareTo(n2.endTime) == 0) {
			return 0;
		} else if (
				n1.startTime.compareTo(n2.startTime) < 0 ||
						(n1.startTime.compareTo(n2.startTime) == 0 &&
								n1.endTime.compareTo(n2.endTime) < 0)
		) {

			return -1;
		} else {
			return 1;
		}
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
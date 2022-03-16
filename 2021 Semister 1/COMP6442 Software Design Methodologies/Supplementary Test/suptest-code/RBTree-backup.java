import java.util.Map;

/**
 * Skeleton code for Red Black Tree.
 * You are required to implement the search & insert methods, where the keys are PersonNames 
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
	 * Note that if a node with duplicate key (but different value) is inserted into the red-black tree,
	 * it must replace the old node of the same key.
	 *
	 * @param key T The key of the new node being inserted.
	 * @param value Object The value of the new node being inserted.
	 */
//	public void insert(Node.PersonName<T> key, Object value) {
//		Node<T> node = new Node<T>(key, value);
//
//		// TODO: Complete this method
//		// START YOUR CODE
//
//		if (root == null) {
//			root = node;
//		} else {
//			if (search(node.key) != null) {
//				System.out.println("Search node key is null");
//				return;
//			}
//			insertRecurse(root, node);
//		}
//
//		// Fix tree
//		System.out.println("insert cmp: " + compareNodes(root.key, node.key));
//		while (compareNodes(root.key, node.key) != 0 && node.parent.colour == Colour.RED) {
////		while (node.parent.colour == Colour.RED) {
//			boolean left  = node.parent == node.parent.parent.left; // Is parent a left node
//			Node<T> uncle = left ? node.parent.parent.right : node.parent.parent.left; // Get opposite "uncle" node to parent
//
//			if (uncle.colour == Colour.RED) {
//				// Case 1: Recolour
//
////                System.out.println("CASE 1: " + node.key);
//				node.parent.colour = Colour.BLACK;
//				node.parent.parent.colour = Colour.RED;
//				uncle.colour = Colour.BLACK;
//
//				// Check if violated further up the tree
//				node = node.parent.parent;
//			} else {
//				if (node.key == (left ? node.parent.right.key : node.parent.left.key)) {
//					// Case 2: Left Rotation, uncle is right node, node is on the right / Right Rotation, uncle is left node, node is on the left
//					node = node.parent;
//					if (left) {
//						// Perform left rotation
//						if (node.key == root.key)
//							root = node.right; // Update root
//						rotateLeft(node);
//					} else {
//						// This is part of the "then" clause where left and right are swapped
//						// Perform right rotation
//						// TODO: Implement this part
//						// ########## YOUR CODE STARTS HERE ##########
//
////                        System.out.println("Case 2: " + node.key);
//						if (node.key == root.key) {
//							root = node.left;
//						}
//						rotateRight(node);
//
//						// ########## YOUR CODE ENDS HERE ##########
//					}
//				}
//				// Adjust colours to ensure correctness after rotation
//				node.parent.colour = Colour.BLACK;
//				node.parent.parent.colour = Colour.RED;
//
//				// Case 3 : Right Rotation, uncle is right node, node is on the left / Left Rotation, uncle is left node, node is on the right
//				// TODO: Complete this part
//				if (left) {
//					// Perform right rotation
//					// ########## YOUR CODE STARTS HERE ##########
//
////                    System.out.println("Case 3.1: " + node.key);
//					rotateRight(node.parent.parent);
//
//					// ########## YOUR CODE ENDS HERE ##########
//				} else {
//					// This is part of the "then" clause where left and right are swapped
//					// Perform left rotation
//					// ########## YOUR CODE STARTS HERE ##########
//
////                    System.out.println("Case 3.2: " + node.key);
//					rotateLeft(node.parent.parent);
//
//					// ########## YOUR CODE ENDS HERE ##########
//				}
//			}
//		}
//
//		// Ensure property 2 (root and leaves are black) holds
//		root.colour = Colour.BLACK;
//		System.out.println(this.preOrder());
//		// END YOUR CODE
//
//	}

    public void insert(Node.PersonName<T> key, Object value) {
        Node<T> x = new Node<T>(key, value);
        System.out.println("\nInserting: " + key.firstName + " " + key.lastName + ", value: " + value.toString());
        // TODO: Complete this function
        // You need to finish cases 1, 2 and 3; the rest has been done for you

        // Insert node into tree
        if (root == null) {
            root = x;
            System.out.println("is root: " + value.toString());
        } else {
            if (search(x.key) != null) {
                search(x.key).value = value;
                System.out.println("update value: " + value.toString());
            } else {
                System.out.println("new node: " + value.toString());
                insertRecurse(root, x);
            }
        }

        // Fix tree
        while (x.key != root.key && x.parent.colour == Colour.RED) {
            boolean left  = x.parent == x.parent.parent.left; // Is parent a left node
            Node<T> uncle = left ? x.parent.parent.right : x.parent.parent.left; // Get opposite "uncle" node to parent

            if (uncle.colour == Colour.RED) {
                // Case 1: Recolour
                // TODO: Implement this part
                // ########## YOUR CODE STARTS HERE ##########

//                System.out.println("CASE 1: " + x.key);
                x.parent.colour = Colour.BLACK;
                x.parent.parent.colour = Colour.RED;
                uncle.colour = Colour.BLACK;

                // ########## YOUR CODE ENDS HERE ##########
                // Check if violated further up the tree
                x = x.parent.parent;
            } else {
                if (x.key == (left ? x.parent.right.key : x.parent.left.key)) {
                    // Case 2: Left Rotation, uncle is right node, x is on the right / Right Rotation, uncle is left node, x is on the left
                    x = x.parent;
                    if (left) {
                        // Perform left rotation
                        if (x.key == root.key)
                            root = x.right; // Update root
                        rotateLeft(x);
                    } else {
                        // This is part of the "then" clause where left and right are swapped
                        // Perform right rotation
                        // TODO: Implement this part
                        // ########## YOUR CODE STARTS HERE ##########

//                        System.out.println("Case 2: " + x.key);
                        if (x.key == root.key) {
                            root = x.left;
                        }
                        rotateRight(x);

                        // ########## YOUR CODE ENDS HERE ##########
                    }
                }
                // Adjust colours to ensure correctness after rotation
                x.parent.colour = Colour.BLACK;
                x.parent.parent.colour = Colour.RED;

                // Case 3 : Right Rotation, uncle is right node, x is on the left / Left Rotation, uncle is left node, x is on the right
                // TODO: Complete this part
                if (left) {
                    // Perform right rotation
                    // ########## YOUR CODE STARTS HERE ##########

//                    System.out.println("Case 3.1: " + x.key);
                    rotateRight(x.parent.parent);

                    // ########## YOUR CODE ENDS HERE ##########
                } else {
                    // This is part of the "then" clause where left and right are swapped
                    // Perform left rotation
                    // ########## YOUR CODE STARTS HERE ##########

//                    System.out.println("Case 3.2: " + x.key);
                    rotateLeft(x.parent.parent);

                    // ########## YOUR CODE ENDS HERE ##########
                }
            }
        }

        // Ensure property 2 (root and leaves are black) holds
        root.colour = Colour.BLACK;
        System.out.println(this.preOrder());
    }

	private void insertRecurse(Node<T> root, Node<T> x) {
		int cmp = compareNodes(root.key, x.key);
		if (cmp == 0) {
//			System.out.println("==== 0 root.value: " + root.value.toString()+ ", x.value: " + x.value.toString());
			root.value = x.value;
		} else if (cmp > 0) {
//			System.out.println(">>>> 0 root.value: " + root.value.toString()+ ", x.value: " + x.value.toString());

			if (root.left.key == null) {
				root.left = x;
				x.parent = root;
			} else {
				insertRecurse(root.left, x);
			}
		} else {
//			System.out.println("<<<< 0 root.value: " + root.value.toString()+ ", x.value: " + x.value.toString());

			if (root.right.key == null) {
				root.right = x;
				x.parent = root;
			} else {
				insertRecurse(root.right, x);
			}
		}
	}


	private int compareNodes(Node.PersonName<T> n1, Node.PersonName<T> n2) {
		String firstName1 = n1.firstName.toString();
		String lastName1 = n1.lastName.toString();
		String firstName2 = n2.firstName.toString();
		String lastName2 = n2.lastName.toString();
//		System.out.println("\nf name 1: " + firstName1 + ", l name 1: " + lastName1);
//		System.out.println("f name 2: " + firstName2 + ", l name 2: " + lastName2);

		int lenF1 = firstName1.length();
		int lenF2 = firstName2.length();
		int shorterF = Math.min(lenF1, lenF2);
		int lenL1 = lastName1.length();
		int lenL2 = lastName2.length();
		int shorterL = Math.min(lenL1, lenL2);

		for (int i = 0; i < shorterL; i++) {
			char c1 = lastName1.charAt(i);
			char c2 = lastName2.charAt(i);
			if (c1 < c2) {
				return -1;
			} else if (c1 > c2) {
				return 1;
			}
		}

		if (lenL1 > shorterL) {
			return 1;
		}

		if (lenL2 > shorterL) {
			return -1;
		}

		for (int j = 0; j < shorterF; j ++) {
			char c1 = firstName1.charAt(j);
			char c2 = firstName2.charAt(j);
			if (c1 < c2) {
				return -1;
			} else if (c1 > c2) {
				return 1;
			}
		}

		if (lenF1 > shorterF) {
			return 1;
		}

		if (lenF2 > shorterF) {
			return -1;
		}

		return 0;
	}

	/**
	 * Returns a node if the key of the node is {@code key}.
	 * Otherwise, return null.
	 *
	 * @param key T The key we are looking for
	 * @return
	 */
	public Node<T> search(Node.PersonName<T> key) {
		// TODO: Complete this method
		// START YOUR CODE

//		System.out.println("Search! " + key.firstName.toString() + " " + key.lastName.toString());

		return find(root, key);


//		return null;
		// END YOUR CODE
	}

	private Node<T> find(Node<T> x, Node.PersonName<T> v) {
		if (x.key == null)
			return null;

		int cmp = compareNodes(v, x.key);
//		System.out.println("find cmp: " + cmp);
		if (cmp < 0)
			return find(x.left, v);
		else if (cmp > 0)
			return find(x.right, v);
		else {
//			System.out.println("Equal: key: " + x.key.firstName.toString() + " " + x.key.lastName.toString() + ", value: " + x.value.toString());
			return x;
		}
	}


    public void rotateLeft(Node<T> x) {
        // Make parent (if it exists) and right branch point to each other
        System.out.println("x.parent: " + (x.parent == null ? null : x.parent.value));
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
            System.out.println("left: " + tree.left.value);
            System.out.println("right: " + tree.right.value);
            return "("+tree.key.firstName + "," + tree.key.lastName + "," + tree.value +")" + (leftStr.isEmpty() ? leftStr : " " + leftStr)
					+ (rightStr.isEmpty() ? rightStr : " " + rightStr);
		}

		return "";
	}

	public String preOrder() {
		return preOrder(root);
	}

}

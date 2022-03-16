/** This class is a part of {@code package tree}. Do not change the package structure.
 * Make sure that your IDE do not change it to for example {@code package src.tree}.
 * If it happens, please revert it once you finish the implementation.
 */

/**
 * Binary search tree with integer keys (values). {@code insert} method is
 * provided.
 */
public class BST {
	Node root;

	/**
	 * Q2 - Task1 TODO: Implement "find" method. The method should return "true" if
	 * a tree contains the node with value, otherwise return "false". You can define
	 * additional functions in class BST or Node if you need.
	 * 
	 * @param value
	 * @return return true if the tree contains the node with value otherwise false
	 */
	public Boolean find(int value) {

		// start your code

		return isIn(this.root, value);

		// end your code
	}

	private boolean isIn(Node node, int value) {
		if (node == null) {
			return false;
		}

		if (node.value == value) {
			return true;
		}

		if (isIn(node.left, value)) {
			return true;
		}

		if (isIn(node.right, value)) {
			return true;
		}

		return false;
	}

	/**
	 * Q2 - Task2 TODO: Implement "delete" method. Find the node with {@code value}
	 * and remove it from the tree. If the target node has two children, use
	 * successor to replace the target node. You can define additional functions in
	 * class BST or Node if you need.
	 * 
	 * Do not care about the case where the target node does not exist.
	 * 
	 * @param value value of the target node
	 */
	public void delete(int value) {

		// start your code



		// end your code
	}

	public void deleteNode(Node node, int value) {
		if (node == null) {
			return;
		}

		if (node.value == value) {
			// case 1:
			if (node.left == null && node.right == null) {
				if (node.parent.left == node) {
					node.parent.left = null;
				} else if (node.parent.right == node) {
					node.parent.right = null;
				}
				node.parent = null;
			}
			// case 2:
			else if (node.left != null && node.right == null) {
				if (node.parent.left == node) {
					node.parent.left = node.left;
				} else if (node.parent.right == node) {
					node.parent.right = node.left;
				}
				node.left.parent = node.parent;
				node.parent = null;
			} else if (node.right != null && node.left == null) {
				if (node.parent.left == node) {
					node.parent.left = node.right;
				} else if (node.parent.right == node) {
					node.parent.right = node.right;
				}
				node.right.parent = node.parent;
				node.parent = null;
			}
			// case 3:
			else if (node.left != null && node.right != null) {
				Node successor = node.right;
				while (successor.left != null) {
					successor = successor.left;
				}
				successor.parent.left = null;
				successor.parent = node.parent;
				successor.left = node.left;
				successor.right = node.right;
				if (node.parent.left == node) {
					node.parent.left = successor;
				} else if (node.parent.right == node) {
					node.parent.right = successor;
				}
				node.parent = null;
			}
		}
	}

	/**
	 * Q2 - Task3 TODO: Implement "sumEvenNodes" function. The method should return
	 * print the sum of the nodes that have an even number of direct children (zero
	 * is an even number). You can define additional functions in class BST or Node
	 * if you need.
	 * 
	 * @return Sum of the nodes that have an even number of direct children
	 */
	public int sumEvenNodes() {
		//start your code
		
		return getEvenNodes(this.root);
		
		//end your code
	}

	private int getEvenNodes(Node node) {
		int sum = 0;
		if (node == null) {
			return 0;
		}

		// left child:
		if (node.left != null) {
			sum += getEvenNodes(node.left);
		}

		// right child:
		if (node.right != null) {
			sum += getEvenNodes(node.right);
		}

		// get children number:
		int nodes = 0;
		if (node.left != null) {
			nodes += 1;
		}
		if (node.right != null) {
			nodes += 1;
		}

		// is even node:
		if (nodes % 2 == 0) {
			sum += node.value;
		}
		return sum;
	}

	public class Node {
		public Integer value;
		public Node parent;
		public Node left;
		public Node right;

		public Node(Integer value) {
			this.value = value;
			this.parent = null;
			this.left = null;
			this.right = null;
		}
	}

	public BST() {
		root = null;
	}

	/**
	 * Insert a new node based on an input value. Do not modify the method.
	 * 
	 * Do not consider the case where a tree already has the node with the same
	 * value.
	 * 
	 * @param value value of inserted node
	 * @return inserted node (not the entire tree)
	 */
	public Node insert(int value) {
		Node parent = null;
		Node current = root;
		while (current != null) {
			if (current.value < value) {
				parent = current;
				current = current.right;
			} else if (current.value > value) {
				parent = current;
				current = current.left;
			}
		}

		if (parent == null && current == null) {
			root = new Node(value); // no parent = root is empty
			return root;
		} else {
			Node newNode = new Node(value);

			if (parent.value < value) {
				parent.right = newNode;
				newNode.parent = parent;
			} else if (parent.value > value) {
				parent.left = newNode;
				newNode.parent = parent;
			}
			return newNode;
		}
	}

}
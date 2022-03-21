/*
 * Given a binary search tree, implement a method to find the sum of the values of all the nodes that have an odd number of direct children. You can define additional methods of BST and Node classes to complete the task. The method signature is: public Integer oddNodeSum()
 * 
 */

public class BST {
	
	Node root;

	/**
	 * Please implement this method and feel free to add additional helper methods
	 * @return
	 */
	public Integer oddNodeSum() {
		// START YOUR CODE
		return this.root.oddNodeSum();
		// END YOUR CODE
	}
	
	public BST() {
		this.root = null;
	}

	/**
	 * This implementation of insert follows the pseudo code in the lecture slide.
	 * Node that we did not use recursion here.
	 * 
	 * @param value value of inserted node
	 * @return inserted node (not the entire tree)
	 */
	public Node insert(Integer value) {
		Node parent = null;
		Node current = this.root;
		while (current != null) {
			if(current.value.compareTo(value) < 0) {
				parent = current;
				current = current.right;
			}else if (current.value.compareTo(value) > 0){
				parent = current;
				current = current.left;
			}
		}
		
		if (parent == null && current == null) {
			this.root = new Node(value);  // no parent = root is empty
			return root;
		}else {
			Node newNode = new Node(value);
			
			if(parent.value.compareTo(value) < 0) {
				parent.right = newNode;
				newNode.parent = parent;
			}else if(parent.value.compareTo(value) > 0) {
				parent.left = newNode;
				newNode.parent = parent;
			}
			return newNode;
		}
	}
	
	public class Node {

		Integer value;
		Node parent;
		Node left;
		Node right;

		public Node(Integer value) {
			this.value = value;
			this.parent = null;
			this.left = null;
			this.right = null;
		}

		public Integer oddNodeSum() {
			Integer sum = 0;
			if (this.left == null && this.right != null) {
				sum += this.value;
				sum += this.right.oddNodeSum();
			} else if (this.left != null && this.right == null) {
				sum += this.value;
				sum += this.left.oddNodeSum();
			} else if (this.left != null && this.right != null) {
				sum += this.left.oddNodeSum();
				sum += this.right.oddNodeSum();
			}
			return sum;
		}
	}
}
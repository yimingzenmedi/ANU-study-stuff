/**
 * Skeleton code for Red Black Tree. You are required to implement the search & insert methods,
 * where the keys are PersonNames You may add additional helper methods.
 *
 * The given code is provided to assist you to complete the required tasks. But the given code is
 * often incomplete. You have to read and understand the given code carefully, before you can apply
 * the code properly. You might need to implement additional procedures, such as error checking and
 * handling, in order to apply the code properly.
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

  public int comparename1(Node<T> a, Node<T> b) {
    if (a.key.lastName.compareTo(b.key.lastName) < 0) {
      return -1;
    } else if (a.key.lastName.compareTo(b.key.lastName) == 0
        && a.key.firstName.compareTo(b.key.firstName) < 0) {
      return -1;
    } else if (a.key.lastName.compareTo(b.key.lastName) == 0
        && a.key.firstName.compareTo(b.key.firstName) == 0) {
      return 0;
    } else {
      return 1;
    }
  }

  public int comparename2(Node.PersonName<T> a, Node<T> b) {
    if (a.lastName.compareTo(b.key.lastName) < 0) {
      return -1;
    } else if (a.lastName.compareTo(b.key.lastName) == 0
        && a.firstName.compareTo(b.key.firstName) < 0) {
      return -1;
    } else if (a.lastName.compareTo(b.key.lastName) == 0
        && a.firstName.compareTo(b.key.firstName) == 0) {
      return 0;
    } else {
      return 1;
    }

  }


  private void insertRecurse(Node<T> root, Node<T> x) {
    int cmp = comparename1(root, x);

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

    else if (cmp == 0) {
      root.value = x.value;
    }
    // Do nothing if the tree already has a node with the same key.
  }

  /**
   * (Safely) insert a key into the tree Note that if a node with duplicate key (but different
   * value) is inserted into the red-black tree, it must replace the old node of the same key.
   *
   * @param key T The key of the new node being inserted.
   * @param value Object The value of the new node being inserted.
   */
  public void insert(Node.PersonName<T> key, Object value) {
    Node<T> node = new Node<T>(key, value);

    // TODO: Complete this method
    // START YOUR CODE
    Node<T> x = node;
    Node<T> duplicate;

    if (root == null) {
      root = x;
    } else {
      duplicate = search(x.key);
      if (duplicate != null) {
        duplicate.value = value;
        return;
      }
      insertRecurse(root, x);
    }

    // Fix tree
    while (x.key != root.key && x.parent.colour == Colour.RED) {
      boolean left = x.parent == x.parent.parent.left; // Is parent a left node
      Node<T> uncle = left ? x.parent.parent.right : x.parent.parent.left; // Get opposite "uncle"
                                                                           // node to parent

      if (uncle.colour == Colour.RED) {
        // Case 1: Recolour
        // TODO: Implement this part
        // ########## YOUR CODE STARTS HERE ##########
        x.parent.colour = Colour.BLACK;
        uncle.colour = Colour.BLACK;
        x.parent.parent.colour = Colour.RED;


        // ########## YOUR CODE ENDS HERE ##########
        // Check if violated further up the tree
        x = x.parent.parent;
      } else {
        if (x.key == (left ? x.parent.right.key : x.parent.left.key)) {
          // Case 2: Left Rotation, uncle is right node, x is on the right / Right Rotation, uncle
          // is left node, x is on the left
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
            if (x.key == root.key)
              root = x.left;
            rotateRight(x);


            // ########## YOUR CODE ENDS HERE ##########
          }
        }
        // Adjust colours to ensure correctness after rotation
        x.parent.colour = Colour.BLACK;
        x.parent.parent.colour = Colour.RED;

        // Case 3 : Right Rotation, uncle is right node, x is on the left / Left Rotation, uncle is
        // left node, x is on the right
        // TODO: Complete this part
        if (left) {
          // Perform right rotation
          // ########## YOUR CODE STARTS HERE ##########
          if (x.parent.parent.key == root.key)
            root = x.parent;
          rotateRight(x.parent.parent);

          // ########## YOUR CODE ENDS HERE ##########
        } else {
          // This is part of the "then" clause where left and right are swapped
          // Perform left rotation
          // ########## YOUR CODE STARTS HERE ##########
          if (x.parent.parent.key == root.key)
            root = x.parent;
          rotateLeft(x.parent.parent);


          // ########## YOUR CODE ENDS HERE ##########
        }
      }
    }

    // Ensure property 2 (root and leaves are black) holds
    root.colour = Colour.BLACK;


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
    // Take right node's left branch
    x.left = x.parent.right;
    x.left.parent = x;
    // Take the place of the right node's left branch
    x.parent.right = x;


    // ########## YOUR CODE ENDS HERE ##########
  }


  /**
   * Returns a node if the key of the node is {@code key}. Otherwise, return null.
   *
   * @param key T The key we are looking for
   * @return
   */
  public Node<T> search(Node.PersonName<T> key) {
    // TODO: Complete this method
    // START YOUR CODE
    return find(root, key);
  }

  private Node<T> find(Node<T> x, Node.PersonName<T> v) {
    if (x.key == null)
      return null;

    int cmp = comparename2(v, x);
    if (cmp < 0)
      return find(x.left, v);
    else if (cmp > 0)
      return find(x.right, v);
    else
      return x;
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
      return "(" + tree.key.firstName + "," + tree.key.lastName + "," + tree.value + ")"
          + (leftStr.isEmpty() ? leftStr : " " + leftStr)
          + (rightStr.isEmpty() ? rightStr : " " + rightStr);
    }

    return "";
  }

  public String preOrder() {
    return preOrder(root);
  }

}

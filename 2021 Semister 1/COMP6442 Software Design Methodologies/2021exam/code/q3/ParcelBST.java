import parcel.Parcel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ParcelBST extends BinarySearchTree<Parcel> implements Iterable<Parcel>  {

    public ParcelBST(Parcel value, ParcelBST leftNode, ParcelBST rightNode) {
        super(value, leftNode, rightNode);
    }

    public ParcelBST(BinarySearchTree<Parcel> parcelBST) {
        super(parcelBST.value,parcelBST.leftNode,parcelBST.rightNode);
    }

    public ParcelBST(Tree<Parcel> parcelTree) {
        super(parcelTree.value,parcelTree.leftNode,parcelTree.rightNode);
    }

    public ParcelBST(Parcel value) {
        super(value);
    }


    @Override
    public Iterator<Parcel> iterator() {
        return new IteratorPreOrder();
    }

    class IteratorPreOrder implements Iterator<Parcel> {
        // Hint: value, left/right sub-trees can be accessed by:
        //    ParcelBST.this.value    ParcelBST.this.leftNode   ParcelBST.this.rightNode
        // Or equivalently directly:
        //    value   leftNode    rightNode


        // You may add methods and variables here if you wish
        Parcel current;
//        Parcel left;
//        Parcel right;


        public IteratorPreOrder() {
            // You may add code here if you wish

            this.current = next();
//            this.left = leftNode == null ? null : leftNode.value;
//            this.right = rightNode == null ? null : rightNode.value;
        }

        @Override
        public boolean hasNext() {
            // TODO
            // START YOUR CODE

            System.out.println("hasNext");


//            System.out.println("value:\n" + display(4));

//            System.out.println("value:" + value.toString() + ", left:" + leftNode.toString()+ ", right:"+ rightNode.toString());

            if (this.current == null) {
                return false;
            }

            if (current.getId() != value.getId() && current.compareTo(value) < 0) {
                return true;
            } else {
                if (leftNode != null && !leftNode.isEmpty()) {
//                    return compare(current, (ParcelBST) leftNode);
                }
                if (rightNode != null && !rightNode.isEmpty()) {
//                    return compare(current, (ParcelBST) rightNode);
                }
            }

            return true;
            // END YOUR CODE
        }


        @Override
        public Parcel next() {
            // TODO
            // START YOUR CODE

            System.out.println("NEXT");
            if (current == null) {
                current = value;
                Tree<Parcel> left = leftNode;
                if (!left.isEmpty()) {
                    current.compareTo(left.value) < 0
                }
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

//            if (current)

            return null;
        }
    }
}

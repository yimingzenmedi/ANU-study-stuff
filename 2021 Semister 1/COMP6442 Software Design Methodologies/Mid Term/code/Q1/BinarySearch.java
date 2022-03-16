/**
 * Skeleton code for Binary Search.
 * You are required to implement the binary search method using proper recursion.
 *
 * The given code is provided to assist you to complete the required tasks. But the 
 * given code is often incomplete. You have to read and understand the given code 
 * carefully, before you can apply the code properly. You might need to implement 
 * additional procedures, such as error checking and handling, in order to apply the 
 * code properly.
 */

public class BinarySearch<T extends Comparable<T>>{

    /*
	 * Given a sorted 2D matrix A (sorted in all columns and rows) and a target key, implement 
	 * the search method to find and return the Element with the key that matches the target 
	 * within the range [minX, maxX]x[minY, maxY], otherwise return null.
     * You must use binary search with proper recursion in the columns and rows of A simultaneously.      
	 * 
     * @param A is a sorted 2d array, such that A[i][0].key < ...< A[i][n-1].key and A[0][j].key < ...< A[n-1][j].key for all i, j
     * @param minX is the minimum index in the first coordinate to be searched in A
     * @param maxX is the maximum index in the first coordinate to be searched in A
     * @param minY is the minimum index in the second coordinate to be searched in A
     * @param maxY is the maximum index in the second coordinate to be searched in A
     * @param target is the target key
     * @return the object with the matched key if exist, otherwise return null.
     */
    public Element<T> search(Element<T>[][] A, int minX, int maxX, int minY, int maxY, T target){
        tracker.calltracking(minX,maxX,minY,maxY); //Do not modify this method. Otherwise, your answers may not be marked correctly
        // TODO: Complete this method
        // START YOUR CODE

//        printArrayKey(A);

        Element[][] subA = new Element[maxY - minY + 1][maxX - minX + 1];

        for (int i = minY; i < maxY + 1; i++) {
            if (maxX + 1 - minX >= 0) System.arraycopy(A[i], minX, subA[i - minY], 0, maxX + 1 - minX);
        }


        printArrayKey(subA);

        Element<T> ele = searchElement(subA, target);

        System.out.println(ele == null ? null : ele.key.toString());
        return ele;
        // END YOUR CODE
    }

    private Element<T> searchElement(Element[][] A, T target) {
        System.out.println("\nTarget:" + target);
        int y = A.length;
        if (y == 0) {
            System.out.println("Empty matrix");
            return null;
        }

        int x = A[0].length;
        int midY = y / 2;
        int midX = x / 2;

        System.out.println("A: x:" + x + ", y: " + y + ", midX: " + midX + ", midY: " + midY);

        Element<T> midEle = A[midY][midX];
        System.out.println(midEle == null ? null : midEle.key);
        int cmp = target.compareTo(midEle.key);
        if (cmp == 0) {
            System.out.println("Get! " + midEle.key);
            return midEle;
        } else if (cmp < 0) {
            Element<T> midRowStart = A[midY][0];
            int cmpRowStart = target.compareTo(midRowStart.key);
            if (cmpRowStart >= 0) {
                Element[][] subA = new Element[1][midX];
                for (int i = 0; i < midX; i++) {
                    subA[0][i] = A[midY][i];
                }
                return searchElement(subA, target);
            } else {
                Element[][] subA = new Element[midY - 1][x];
                for (int i = 0; i < midY - 1; i++) {
                    for (int j = 0; j < x; j++) {
                        subA[i][j] = A[i][j];
                    }
                }
                return searchElement(subA, target);
            }
        } else {
            Element<T> midRowEnd = A[midY][x - 1];
            int cmpRowEnd = target.compareTo(midRowEnd.key);
            if (cmpRowEnd <= 0) {
                Element[][] subA = new Element[1][x - midX - 1];
                for (int i = midX + 1; i < x; i++) {
                    subA[0][i - midX - 1] = A[midY][i];
                }
                return searchElement(subA, target);
            } else {
                Element[][] subA = new Element[y - midY - 1][x];
                for (int i = y - midY + 1; i < y; i++) {
                    for (int j = 0; j < x; j++) {
                        subA[i - y + midY - 1][j] = A[i][j];
                    }
                }
                return searchElement(subA, target);
            }
        }

    }

//    private Element<T> searchElement(Element[][] A, T target) {
//        System.out.println("\nTarget:" + target);
//        int y = A.length;
//        if (y == 0) {
//            System.out.println("Y = 0");
//            return null;
//        }
//        int x = A[0].length;
//
//        int midY = y / 2;
//        int midX = x / 2;
//
//        System.out.println("A: x:" + x + ", y: " + y + ", midX: " + midX + ", midY: " + midY);
//
//
//        Element<T> midEle = A[midY][midX];
//        int cmp = target.compareTo(midEle.key);
//        if (cmp == 0) {
//            System.out.println("Get");
//            return midEle;
//        } else if (x == 1 && y == 1) {
//            System.out.println("Null");
//            return null;
//        } else if (y == 1 && x > 1) {
//            if (cmp < 0) {
//                System.out.println("Line Smaller");
//
//                Element[][] subA = new Element[y][midX];
//                for (int i = 0; i < y; i++) {
//                    for (int j = 0; j < midX; j++) {
//                        subA[i][j] = A[i][j];
//                    }
//                }
//                System.out.println("SubA:");
//                printArrayKey(subA);
//                return searchElement(subA, target);
//            } else {
//                System.out.println("Line Larger");
//
//                Element[][] subA = new Element[y][x - midX - 1];
//                for (int i = 0; i < y; i++) {
//                    System.out.println("i: " + i);
//                    for (int j = midX + 1; j < x; j++) {
//                        System.out.println("j: " + j + ", j - midX - 1: " + (j - midX - 1));
//
//                        subA[i][j - midX - 1] = A[i][j];
//                    }
//                }
//                System.out.println("SubA:");
//                printArrayKey(subA);
//                return searchElement(subA, target);
//            }
//        } else if (cmp < 0) {
//            System.out.println("Smaller");
//            Element<T> lineStart = A[midY][0];
//            Element[][] subA;
//            if (target.compareTo(lineStart.key) < 0) {
//                System.out.println("Previous lines");
//                subA = new Element[midY - 1][x];
//                for (int i = 0; i < midY - 1; i++) {
//                    for (int j = 0; j < x; j++) {
//                        subA[i][j] = A[i][j];
//                    }
////                System.arraycopy(A[i], 0, subA[i], 0, midX);
//                }
//            } else {
//                System.out.println("Current line");
//                subA = new Element[1][x];
//                for (int j = 0; j < x; j++) {
//                    subA[0][j] = A[midY][j];
//                }
////                System.arraycopy(A[i], 0, subA[i], 0, midX);
//            }
//
//            System.out.println("SubA:");
//            printArrayKey(subA);
//            return searchElement(subA, target);
//        } else {
//            System.out.println("Larger");
//
//            Element[][] subA = new Element[y - midY][x];
//            for (int i = midY; i < y; i++) {
//                for (int j = 0; j < x; j++) {
//                    subA[i-midY][j] = A[i][j];
//                }
//
////                if (x - midX >= 0) System.arraycopy(A[i], midX, subA[i - midY], 0, x - midX);
//            }
//            System.out.println("SubA:");
//            printArrayKey(subA);
//            return searchElement(subA, target);
//        }
//    }


    private void printArrayKey(Element[][] array) {
        System.out.println("Keys:");
        for (Element[] row : array) {
            StringBuilder s = new StringBuilder();
            for (Element e : row) {
                s.append('\t').append(e.key);
            }
            s.append("\n");
            System.out.println(s.toString());
        }
    }

    private void printArrayValue(Element[][] array) {
        System.out.println("Values:");
        for (Element[] row : array) {
            StringBuilder s = new StringBuilder();
            for (Element e : row) {
                s.append('\t').append(e.value);
            }
            s.append("\n");
            System.out.println(s.toString());
        }
    }
}
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
	 * Given a sorted 3D matrix A (sorted in every coordinate in ascending order) and a target key, implement 
	 * the binary search method to find and return the Element with the key that matches the target 
	 * within the range [minX, maxX]x[minY, maxY]x[minZ, maxZ], otherwise return null.
     * You must use binary search with proper recursion in the columns and rows of A simultaneously.      
	 * 
     * @param A is a sorted 3D array, such that 
	 *    A[x][y][0].key < ...< A[x][y][n-1].key and A[0][y][z].key < ...< A[n-1][y][z].key 
	 *       and A[x][0][z].key < ...< A[x][n-1][z].key, for all x, y, z in {0,...,n-1}.
     * @param minX is the minimum index in the first coordinate to be searched in A
     * @param maxX is the maximum index in the first coordinate to be searched in A
     * @param minY is the minimum index in the second coordinate to be searched in A
     * @param maxY is the maximum index in the second coordinate to be searched in A
     * @param minZ is the minimum index in the third coordinate to be searched in A
     * @param maxZ is the maximum index in the third coordinate to be searched in A	 
     * @param target is the target key
     * @return the object with the matched key if exist, otherwise return null.
     */
    public Element<T> search(Element<T>[][][] A, int minX, int maxX, int minY, int maxY, int minZ, int maxZ, T target){
        tracker.calltracking(minX,maxX,minY,maxY,minZ,maxZ); //Do not modify this method. Otherwise, your answers may not be marked correctly
        // TODO: Complete this method
        // START YOUR CODE

		Element[][][] subA = new Element[maxZ - minZ + 1][maxY - minY + 1][maxX - minX + 1];

		for (int i = minY; i < maxY + 1; i++) {
			if (maxX + 1 - minX >= 0) {
				System.arraycopy(A[i], minX, subA[i - minY], 0, maxX + 1 - minX);
			}
		}


//		printArrayKey(subA);

		Element<T> ele = searchElement(subA, target);

		System.out.println(ele == null ? null : ele.key.toString());
		return ele;

//        return null;
        // END YOUR CODE
    }

	private Element<T> searchElement(Element[][][] A, T target) {
		System.out.println("\nTarget:" + target);
		int z = A.length;
		if (z == 0) {
			System.out.println("Empty matrix: z");
			return null;
		}

		int y = A[0].length;
		if (y == 0) {
			System.out.println("Empty matrix: y");
			return null;
		}

		int x = A[0][0].length;
		if (x == 0) {
			System.out.println("Empty matrix: x");
			return null;
		}

		int midZ = z / 2;
		int midY = y / 2;
		int midX = x / 2;
		System.out.println("A: x: " + x + ", y: " + y + ", z: " + z + ", midX: " + midX + ", midY: " + midY + ", midZ: " + midZ);

		Element<T> midEle = A[midZ][midY][midX];
		System.out.println(midEle == null ? null : midEle.key);
		int cmp = target.compareTo(midEle.key);
		if (cmp == 0) {
			System.out.println("Get! " + midEle.key);
			return midEle;
		} else if (cmp < 0) {
			System.out.println("Smaller");

			if (z > 1) {
				Element<T> midPlaneStart = A[midZ][midY][0];
				int cmpPlaneStart = target.compareTo(midPlaneStart.key);
				if (cmpPlaneStart >= 0) {
					Element[][][] subA = new Element[1][midY][midX];
					for (int i = 0; i < midY; i++) {
						for (int j = 0; j < midX; j++) {
							subA[0][i][j] = A[midZ][i][j];
						}
					}
					return searchElement(subA, target);
				} else {
					Element[][][] subA = new Element[midZ - 1][y][x];
					for (int i = 0; i < midZ - 1; i++) {
						for (int j = 0; j < y; j++) {
							for (int k = 0; k < x; k++) {
								subA[i][j][k] = A[i][j][k];
							}
						}
					}
					return searchElement(subA, target);
				}
			} else {
				Element<T> midRowStart = A[0][midY][0];
				int cmpRowStart = target.compareTo(midRowStart.key);
				if (cmpRowStart >= 0) {
					Element[][][] subA = new Element[1][1][midX];
					for (int i = 0; i < midX; i++) {
						subA[0][0][i] = A[0][midY][i];
					}
					return searchElement(subA, target);
				} else {
					Element[][][] subA = new Element[1][midY - 1][x];
					for (int i = 0; i < midY - 1; i++) {
						for (int j = 0; j < x; j++) {
							subA[0][i][j] = A[0][i][j];
						}
					}
					return searchElement(subA, target);
				}
			}
		} else {
			System.out.println("Larger");
			if (z > 1) {
				System.out.println("Not plane");
				Element<T> midPlaneEnd = A[midZ][y - 1][x - 1];
				int cmpPlaneEnd = target.compareTo(midPlaneEnd.key);
				if (cmpPlaneEnd <= 0) {
					System.out.println("Not plane: smaller");

					Element[][][] subA = new Element[1][y - midY - 1][x];
					for (int i = midY + 1; i < y; i++) {
						for (int j = 0; j < x; j++) {
							subA[0][i - midY - 1][j] = A[midZ][i][j];
						}
					}
					return searchElement(subA, target);
				} else {
					System.out.println("Not plane: larger. midZ: " + midZ + ", z: " + z);

					Element[][][] subA = new Element[z - midZ - 1][y][x];
					for (int i = z - midZ + 1; i < z; i++) {
						for (int j = 0; j < y; j++) {
							for (int k = 0; k < x; k++) {
								subA[i - z + midZ - 1][j][k] = A[i][j][k];
							}
						}
					}
					return searchElement(subA, target);
				}
			} else {
				System.out.println("Plane");

				Element<T> midRowEnd = A[0][midY][x - 1];
				int cmpRowEnd = target.compareTo(midRowEnd.key);
				if (cmpRowEnd <= 0) {
					System.out.println("Plane: smaller");

					Element[][][] subA = new Element[1][1][x - midX - 1];
					for (int i = midX + 1; i < x; i++) {
						subA[0][0][i - midX - 1] = A[0][midY][i];
					}
					return searchElement(subA, target);
				} else {
					System.out.println("Plane: larger");

					Element[][][] subA = new Element[1][y - midY - 1][x];
					for (int i = y - midY + 1; i < y; i++) {
						for (int j = 0; j < x; j++) {
							subA[0][i - y + midY - 1][j] = A[0][i][j];
						}
					}
					return searchElement(subA, target);
				}
			}
		}

	}
}
//package parser;
import java.util.Arrays;

/**
 * This captures the state of the pointer and the trace of its movement.
 * IMPORTANT: This class is incomplete. Please look for "TODO" comments.
 *
 * 
 */
public class Screen {
    public static final String NON_VISITED_MARK = "#";
    public static final String VISITED_MARK = ".";

	public int noOfRows;
	public int noOfColumns;
	public Pointer pointer;
	public String[][] trace;
	
	public Screen (int noOfRows, int noOfColumns, Pointer pointer) throws OutOfScreenException {
		this.noOfRows = noOfRows;
		this.noOfColumns = noOfColumns;
		this.pointer = pointer;
		
		this.trace = new String[noOfRows][noOfColumns];
		for (String[] row : trace) {
			Arrays.fill(row, NON_VISITED_MARK);
		}
		
		// mark initial position
		if (this.pointer.isPenDown) this.markVisistedPos(pointer.position);
	}
	
	/**
	 * Mark the current position as visited.
	 * @param p Position
	 * @throws Exception 
	 */
	public void markVisistedPos(Position p) throws OutOfScreenException {
		if (p.x >= 0 && p.x < noOfRows && p.y >= 0 && p.y < noOfColumns) {
			trace[p.x][p.y] = VISITED_MARK;
		} else {
			throw new OutOfScreenException();
		}
	}
	
	/**
	 * NOTE: It inserts a new line character after every row including the last one.
	 * @see {@link ScreenTest} for the expected result
	 * @return a string representing the screen including pointer's visited positions, its current position and direction.
	 */
	public String trace() {
		// TODO: Add your implementation here.
		// Hints: append "\n" to create a new line in string
		//       Note that {@link ScreenTest} doesn't require complete implementation of parser.
		//       Check the expected outcome in ScreenTest.java
		int x = pointer.position.x;
		int y = pointer.position.y;
		Direction direction = pointer.direction;

		for (int i = 0; i < noOfRows; i++) {
			boolean ok = false;
			for (int j = 0; j < noOfColumns; j++) {
				if (trace[i][j].equals("<") || trace[i][j].equals(">") || trace[i][j].equals("v") || trace[i][j].equals("^")) {
					trace[i][j] = NON_VISITED_MARK;
					ok = true;
				}
				if (ok) break;
			}
			if (ok) break;
		}

		if (direction == Direction.NORTH) {
			trace[x][y] = "^";
		} else if (direction == Direction.SOUTH) {
			trace[x][y] = "v";
		} else if (direction == Direction.WEST) {
			trace[x][y] = "<";
		} else if (direction == Direction.EAST) {
			trace[x][y] = ">";
		}

		StringBuilder res = new StringBuilder();
		for (String[] i : trace) {
			for (String j : i) {
				res.append(j);
			}
			res.append("\n");
		}
		return res.toString();
//		return null;
	}
}

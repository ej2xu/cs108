// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

package assign1;

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int minRow = grid.length;
		int minCol = minRow > 0 ? grid[0].length : 0;
		int maxRow = 0, maxCol = 0;
		boolean found = false;	
		for (int row=0; row<grid.length; row++)
			for (int col=0; col<grid[0].length; col++) {
				if (grid[row][col]==ch) {
					if (!found) {
						found = true;
						minRow = row;
					}
					maxRow=row+1;
					if (col<minCol) minCol = col;
					if (col>=maxCol) maxCol = col+1;
				}
			}
		if (!found) return 0; 
		else return (maxRow-minRow) * (maxCol-minCol);
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int numPlus=0;
		for (int row=0; row<grid.length; row++)
			for (int col=0; col<grid[0].length; col++)
				if (validPlus(row, col)) numPlus++;
		return numPlus;
	}
	
	private boolean validPlus(int row, int col) {
		int left = numRep(row, col, 0, -1);
		int right = numRep(row, col, 0, 1);
		int down = numRep(row, col, 1, 0);
		int up = numRep(row, col, -1, 0);
		return (left != 0 && left == right && left == up && left == down);
	}
	
	private int numRep(int row, int col, int shiftRow, int shiftCol) {
		int nextRow = row + shiftRow;
		int nextCol = col + shiftCol;
		if (validSlot(nextRow, nextCol) && grid[row][col] == grid[nextRow][nextCol])
			return 1 + numRep(nextRow, nextCol, shiftRow, shiftCol);
		else return 0;
	}
	
	private boolean validSlot(int row, int col) {
		return (row >= 0 && row < grid.length && col >= 0 && col < grid[0].length);
	}
}
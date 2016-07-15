//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.
package assign1;

public class TetrisGrid {
	private boolean[][] grid;
	
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		int rowLen = grid.length;
		int colLen = grid[0].length;
		boolean[][] result = new boolean[rowLen][colLen];
		int count = 0, resCol = 0;
		for (int col=0; col<colLen; col++) {
			for (int row=0; row<rowLen; row++) {
				result[row][resCol] = grid[row][col];
				if (grid[row][col]) count++;
			}
			if (count != rowLen) resCol++;
			count = 0;
		}
		for (int col=resCol; col<colLen; col++)
			for (int row=0; row<rowLen; row++)
				result[row][col] = false;
		grid = result;
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return grid;
	}
}

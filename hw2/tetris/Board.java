// Board.java
package tetris;

import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	private int[] heights;
	private int[] widths;
	private int maxHeight;
	private boolean hasPlaced = false;
	
	private boolean[][] xGrid;
	private int[] xHeights;
	private int[] xWidths;
	private int xMaxHeight;
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		widths = new int[height];
		heights = new int[width];
		maxHeight = 0;
		
		xGrid = new boolean[width][height];
        xHeights = new int[width];
        xWidths = new int[height];
	}
	
	public int getWidth() {
		return width;
	}	

	public int getHeight() {
		return height;
	}
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int[] yHeights = new int[width];
			int[] yWidths = new int[height];
			int yMaxHeight = 0;
			
			for (int j = 0; j < height; j++)
				for (int i = 0; i < width; i++)				
					if (grid[i][j]) {
						yWidths[j]++;
						if (j + 1 > yHeights[i]) yHeights[i] = j + 1;
						if (yHeights[i] > yMaxHeight) yMaxHeight = yHeights[i];
					}
			if (!Arrays.equals(heights, yHeights) || !Arrays.equals(widths, yWidths) || !(maxHeight == yMaxHeight))
				 throw new RuntimeException("insane board problem");
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int y = 0;
		int[] skirt = piece.getSkirt();
		for (int i = 0; i < piece.getWidth(); i++) {
			int h = heights[x + i] - skirt[i];
			if (h >= 0 && h > y) y = h;
		}
		return y;
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		return (x >= width || y >= height || y < 0 || x < 0 || grid[x][y]);
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		hasPlaced = true;
		committed = false;
		backup();
		int result = PLACE_OK;
		
		if (x + piece.getWidth() > width || y + piece.getHeight() > height || x < 0 || y < 0)
			return PLACE_OUT_BOUNDS;
		
		for (TPoint p: piece.getBody()) {
			int curX = p.x + x;
			int curY = p.y + y;			
			if (grid[curX][curY]) return PLACE_BAD;
			grid[curX][curY] = true;
			widths[curY]++;
			if (widths[curY] == width) result = PLACE_ROW_FILLED;
			if (heights[curX] < curY + 1) heights[curX] = curY + 1;
			if (heights[curX] > maxHeight) maxHeight = heights[curX]; 		
		}
		sanityCheck();
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		if (!hasPlaced) {
			committed = false;
			backup();
		}
		int rowsCleared = 0;
		int toRow = 0;
		for (int row = 0; row < maxHeight; row++) {
			while (widths[row] == width) {
				row++;
				rowsCleared++;				
			}			
			for (int col = 0; col < width; col++)
				grid[col][toRow] = grid[col][row];			
			widths[toRow] = widths[row];			
			toRow++;
		}
		
		while(toRow < maxHeight){
            widths[toRow] = 0;
            for(int col = 0; col < width; col++)
                grid[col][toRow] = false;
            toRow++;
        }		

		maxHeight -= rowsCleared;
		// recalculate heights for possible existing gap due to row clears
		Arrays.fill(heights, 0);
		for (int j = maxHeight - 1; j >= 0; j--) {
			for (int i = 0; i < width; i++) {				
				if (grid[i][j]) {
					if (j + 1 > heights[i]) heights[i] = j + 1;
				}
			}
		}
		
		sanityCheck();
		return rowsCleared;
	}

	private void backup() {
		for(int i = 0; i < width; i++)
			System.arraycopy(grid[i], 0, xGrid[i], 0, height);
		System.arraycopy(heights, 0, xHeights, 0, width);
		System.arraycopy(widths, 0, xWidths, 0, height);
		xMaxHeight = maxHeight;
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (!committed) {			
			boolean[][] temp = grid;
            grid = xGrid;
            xGrid = temp;

            int[] tempX = heights;
            heights = xHeights;
            xHeights = tempX;

            int[] tempY = widths;
            widths = xWidths;
            xWidths = tempY;
			maxHeight = xMaxHeight;
			
			hasPlaced = false;
			committed = true;
			sanityCheck();
		}
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		hasPlaced = false;
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}



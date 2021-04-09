package game1024;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Stack;
import java.util.Random;

public class NumberGame implements NumberSlider {
	private int board[][];
	private int WinVal;
	private int h;
	private int w;
	public static int numTurns;
	private boolean fullBoard;
	private Stack<int[][]> current=new Stack<int[][]>();
	private Stack<int[][]> redo=new Stack<int[][]>();

	/*************************************************************************************************************************
	 * Reset the game logic to handle a board of a given dimension
	 *
	 * @param height
	 *            the number of rows in the board
	 * @param width
	 *            the number of columns in the board
	 * @param winningValue
	 *            the value that must appear on the board to win the game
	 * @throws IllegalArgumentException
	 *             when the winning value is not power of two or negative
	 *************************************************************************************************************************/
	public void resizeBoard(int height, int width, int winningValue) {
		h = height;
		w = width;
		board = new int[h][w];
		numTurns=0;
		if (winningValue <= 0 || Math.log(winningValue) < 1) {
			throw new IllegalArgumentException();
		} else if (winningValue > 0 && Math.log(winningValue) > 0)
			WinVal = winningValue;

	}

	/*************************************************************************************************************************
	 * Remove all numbered tiles from the board and place TWO non-zero values at random location
	 *************************************************************************************************************************/
	public void reset() {
		numTurns=0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				board[i][j] = 0;
			}
		}
		this.placeRandomValue();
		this.placeRandomValue();
	}

	/*************************************************************************************************************************
	 * Set the game board to the desired values given in the 2D array. This
	 * method should use nested loops to copy each element from the provided
	 * array to your own internal array. Do not just assign the entire array
	 * object to your internal array object. Otherwise, your internal array may
	 * get corrupted by the array used in the JUnit test file. This method is
	 * mainly used by the JUnit tester.
	 * 
	 * @param ref
	 *************************************************************************************************************************/
	public void setValues(int[][] ref) {
		int[][] board = new int[ref.length][ref[0].length];
		for (int i = 0; i < ref.length; i++) {
			for (int j = 0; j < ref[i].length; j++) {
				board[i][j] = ref[i][j];
			}
		}

	}

	/*************************************************************************************************************************
	 * Insert one random tile into an empty spot on the board.
	 *
	 * @return a Cell object with its row, column, and value attributes
	 *         initialized properly
	 *
	 * @throws IllegalStateException
	 *             when the board has no empty cell
	 *************************************************************************************************************************/
	public Cell placeRandomValue() {
		Cell n = new Cell();
		Random randGen = new Random();
		int a = randGen.nextInt(h);
		int b = randGen.nextInt(w);
		int val = randGen.nextInt(2)+1;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == 0) {
					fullBoard = false;
				}
			}
		}
		while (fullBoard == false) {
			a = randGen.nextInt(h);
			b = randGen.nextInt(w);
			val = randGen.nextInt(2)+1;
			if (board[a][b] == 0) {
				val=val*2;
				board[a][b] = val;
				n = new Cell(a, b, val);
				fullBoard = true;
				return n;
			}

		}
		return n;
	}
/*************************************************************************************************************************
 * Checks board if there is a valid move
 * @return true if there is a valid move
 * @return false if there is not a valid move
 *************************************************************************************************************************/
	public boolean checkValidMove() {
		boolean p=false;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == 0) {
					p=true;
					return true;
				} else if (board[i][j] != 0) {
					if (j>0 && j<board[0].length-1){
						if(board[i][j + 1] == board[i][j] || board[i][j - 1] == board[i][j]) {
							p=true;
						return true;
						}
					}
					if (i>0 && i<board.length-1){
						if(board[i - 1][j] == board[i][j] || board[i + 1][j] == board[i][j]) {
							p=true;
						return true;
						}
					}
					if(i==0 && board[i + 1][j] == board[i][j]){
						p=true;
						return true;
					}
					if(j==0 &&  board[i][j+1] == board[i][j]){
						p=true;
						return true;
					}
					if (j == board[i].length && board[i][j-1] == board[i][j]){
						p=true;
						return true;
					}		
				}else{ 
					p= false;
				}
			}
		}
		return p;
	}

	/*************************************************************************************************************************
	 * Slide all the tiles in the board in the requested direction
	 * 
	 * @param dir
	 *            move direction of the tiles
	 *
	 * @return true when the board changes
	 *************************************************************************************************************************/
	public boolean slide(SlideDirection dir) {
		boolean slid = false;
		boolean swap = false;
		if (checkValidMove() == true) {
		if (dir.equals(SlideDirection.RIGHT)) {
			for (int r= 0; r <= w - 1; r++) {
				swap = false;
				for (int c = w - 1; c >= 0; c--) {
					if (board[r][c] > 0) {
						if (c > 0 && board[r][c - 1] == board[r][c]) {
							board[r][c] = (board[r][c] * 2);
							board[r][c - 1] = 0;
							swap = true;
							slid = true;

						}
						while (c < w - 1 && board[r][c + 1] == 0) {
							board[r][c + 1] = board[r][c];
							board[r][c] = 0;
							c++;
							slid = true;
							if (swap == false&&c < w- 1 && board[r][c + 1] == board[r][c]) {
								board[r][c + 1] = (board[r][c] * 2);
								board[r][c] = 0;
								slid = true;
							}
						}
					}
				}
			}
		} else if (dir.equals(SlideDirection.LEFT)) {
			for (int r= 0; r <= h - 1; r++) {
				swap = false;
				for (int c = 0; c <= w - 1; c++) {
					if (board[r][c] > 0) {
						if (c < w - 1 && board[r][c + 1] == board[r][c]) {
							board[r][c] = (board[r][c] * 2);
							board[r][c + 1] = 0;
							swap = true;
							slid = true;
						}
						while (c > 0 && board[r][c - 1] == 0) {
							board[r][c - 1] = board[r][c];
							board[r][c] = 0;
							c--;
							slid = true;
							if (swap == false&&c > 0 && board[r][c - 1] == board[r][c] ) {
								board[r][c - 1] = (board[r][c] * 2);
								board[r][c] = 0;
								slid = true;
							}
						}

					}
				}
			}
		} else if (dir.equals(SlideDirection.UP)) {
			for (int c = 0; c <= w - 1; c++) {
				swap = false;
				for (int r = 0; r <= h - 1; r++) {
					if (board[r][c] > 0) {
						if (r < h - 1 && board[r + 1][c] == board[r][c]) {
							board[r][c] = (board[r][c] * 2);
							board[r + 1][c] = 0;
							swap = true;
							slid = true;
						}
						while (r > 0 && board[r - 1][c] == 0) {
							board[r - 1][c] = board[r][c];
							board[r][c] = 0;
							r--;
							slid = true;
							if (swap == false&&r > 0 && board[r - 1][c] == board[r][c]) {
								board[r - 1][c] = (board[r][c] * 2);
								board[r][c] = 0;
								slid = true;
							}
						}
					}
				}
			}
		} else if (dir.equals(SlideDirection.DOWN)) {
			for (int c = 0; c <= w- 1; c++) {
				swap = false;
				for (int r = h - 1; r >= 0; r--) {
					if (board[r][c] > 0) {
						if (r > 0 && board[r - 1][c] == board[r][c]) {
							board[r][c] = (board[r][c] * 2);
							board[r - 1][c] = 0;
							swap = true;
							slid = true;
						}
						while (r <h - 1 && board[r + 1][c] == 0) {
							board[r + 1][c] = board[r][c];
							board[r][c] = 0;
							r++;
							slid = true;
							if (swap == false&&r <h - 1 && board[r + 1][c] == board[r][c]) {
								board[r + 1][c] = (board[r][c] * 2);
								board[r][c] = 0;
								slid = true;
							}
						}

					}
				}
			}

		} 

		}else if(checkValidMove()==false){
			slid= false;
		}
		if (slid == true) {
			numTurns++;
			this.placeRandomValue();
			int[][] copy=copyBoard();
			current.push(copy);
		}
		return slid;
	}


	/*************************************************************************************************************************
	 * creates an arraylist of cells of the tiles that are not empty
	 * @return an arraylist of Cells. Each cell holds the (row,column) and value
	 *         of a tile
	 *************************************************************************************************************************/
	public ArrayList<Cell> getNonEmptyTiles() {
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] != 0) {
					cells.add(new Cell(i, j, board[i][j]));
				}
			}
		}
		return cells;
	}

	/*************************************************************************************************************************
	 * Returns the current state of the game
	 * 
	 * @return one of the possible values of GameStatus enum
	 *************************************************************************************************************************/
	public GameStatus getStatus() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j] == WinVal) {
					return GameStatus.USER_WON;
				}
				if (board[i][j] != WinVal && checkValidMove()==true) {
					return GameStatus.IN_PROGRESS;
				}
			}
		}
		return GameStatus.USER_LOST;
	}
	/*************************************************************************************************************************
	 * Returns a copy of the board for undo method
	 * 
	 * @return copy of current board
	 *************************************************************************************************************************/
	public int[][] copyBoard() {
		int[][] copy = new int[board.length][board[0].length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				copy[i][j] = board[i][j];
			}
		}
		return copy;
	}
	/*************************************************************************************************************************
	 * Undo the most recent action, i.e. restore the board to its previous
	 * state. Calling this method multiple times will ultimately restore the gam
	 * to the very first initial state of the board holding two random values.
	 * Further attempt to undo beyond this state will throw an
	 * IllegalStateException.
	 *
	 * @throws IllegalStateException
	 *             when undo is not possible
	 *************************************************************************************************************************/
	public void undo() {
		if (current.isEmpty() != true) {
			int[][] co=copyBoard();
			redo.push(co);
			board = current.pop();
			numTurns--;
		} else
			throw new IllegalStateException();
	}
	/*************************************************************************************************************************
	 * Returns a copy of the board for redo method
	 * 
	 * @return copy of current board
	 *************************************************************************************************************************/
	public void redo(){
		if (redo.isEmpty() != true) {
			int[][] co=copyBoard();
			current.push(co);
			board = redo.pop();
			numTurns++;
		}else
			throw new IllegalStateException();
	}
	

}

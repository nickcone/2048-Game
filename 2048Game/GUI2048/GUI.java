package GUI2048;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import game1024.Cell;
import game1024.GameStatus;
import game1024.NumberGame;
import game1024.NumberSlider;
import game1024.SlideDirection;

public class GUI extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Instance variables to keep track of the current game **/
	private NumberSlider game;
	private JLabel[][] display;
	private JFrame frame;
	private JPanel titlePanel;
	private int winningVal;
	/** JTextArea items **/
	JTextArea results;
	JTextArea Highscores;
	/** JMenu items **/
	JMenuItem quitItem;
	JMenuItem newGameItem;
	JMenuItem resizeItem;
	/** JButton items **/
	JButton resetBoardItem;

	/*************************************************************************************************************************
	 * GUI Front end for NumberGame/NumberSlider class
	 * 
	 * @author Nicholas Cone
	 * @version March 22, 2017
	*************************************************************************************************************************/
	public GUI() {
		frame = new JFrame("2048 Game");
		game = new NumberGame();
		//gets user input for rows, columns, and winning value
		String y = JOptionPane.showInputDialog(frame, "How many columns?", null);
		String x = JOptionPane.showInputDialog(frame, "How many rows?", null);
		String v = JOptionPane.showInputDialog(frame,
				"What would you like to set the winning value to?\n"
						+ "In order for the winning value to be reached, \n"
						+ "Please choose from the following:2,4,8,16,32,64,128,256,512,1024,2048,4096,8192,etc",
				null);
		int c = Integer.valueOf(y);
		int r = Integer.valueOf(x);
		int w = Integer.valueOf(v);

		game.resizeBoard(r, c, w);
		//grid = new int[r][c];
		display = new JLabel[r][c];
		winningVal = w;

		frame.addKeyListener(new DirectionListener());
		BorderLayout mainLayout = new BorderLayout();
		frame.setLayout(mainLayout);

		titlePanel = new JPanel();
		GridLayout bLayout = new GridLayout(r, c, 2, 2);
		titlePanel.setLayout(bLayout);
		Font f = new Font(Font.MONOSPACED, Font.BOLD, 50);
		if (r > 18 && c > 18) {
			f = new Font(Font.SANS_SERIF, Font.BOLD, 40);
		}
		game.placeRandomValue();
		game.placeRandomValue();

		for (int k = 0; k < display.length; k++) {
			for (int m = 0; m < display[k].length; m++) {
				display[k][m] = new JLabel("0", JLabel.CENTER);
				display[k][m].setPreferredSize(new Dimension(100, 100));
				display[k][m].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				display[k][m].setFont(f);
				titlePanel.add(display[k][m]);

			}
		}
		frame.setFocusable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar menus = new JMenuBar();
		frame.setJMenuBar(menus);

		JMenu fileMenu = new JMenu("Settings");
		menus.add(fileMenu);

		newGameItem = new JMenuItem("New Game");
		newGameItem.addActionListener(this);

		results = new JTextArea(20, 20);
		JScrollPane scrollPane = new JScrollPane(results);

		Highscores = new JTextArea(20, 20);
		JScrollPane scroll = new JScrollPane(Highscores);

		resizeItem = new JMenuItem("Resize Board");
		resizeItem.addActionListener(this);

		resetBoardItem = new JButton("Reset board to new game");
		resetBoardItem.addActionListener(this);

		quitItem = new JMenuItem("Quit");
		quitItem.addActionListener(this);

		fileMenu.add(newGameItem);
		fileMenu.add(resizeItem);
		fileMenu.add(quitItem);
		renderBoard();
		frame.add(resetBoardItem, BorderLayout.SOUTH);
		frame.add(scroll, BorderLayout.WEST);
		frame.add(scrollPane, BorderLayout.EAST);
		frame.add(titlePanel, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	/*************************************************************************************************************************
	 * Handles updating the JLabels with the current value of the game
	*************************************************************************************************************************/
	private void renderBoard() {
		results.setText("Current number of turns made: " + NumberGame.numTurns);
		Highscores.setText(" Controls: \n" + " undo: u \n redo: r \n slide: Arrow keys");
		/* reset all the 2D array elements to ZERO */
		for (int k = 0; k < display.length; k++) {
			for (int m = 0; m < display[k].length; m++) {
				display[k][m].setText(" ");
				display[k][m].setBackground(Color.BLACK);
				display[k][m].setOpaque(true);
			}
		}
		if (game.getStatus() == GameStatus.USER_LOST) {
			JOptionPane.showMessageDialog(frame, "Sorry, there is no more possible moves on the current board \n"
					+ "Go to settings to start a new game ");
		}
		/* fill in the 2D array using information for non-empty tiles */
		for (Cell c : game.getNonEmptyTiles()) {
			String h = "" + c.value;
			display[c.row][c.column].setText(h);
			//changes the color of Jlabels, depending on the value
			if (c.value == 2 || c.value == 4) {
				display[c.row][c.column].setBackground(Color.LIGHT_GRAY);
				display[c.row][c.column].setOpaque(true);
			}
			if (c.value == 8) {
				display[c.row][c.column].setBackground(Color.GRAY);
				display[c.row][c.column].setOpaque(true);
			}
			if (c.value == 16) {
				display[c.row][c.column].setBackground(Color.RED);
				display[c.row][c.column].setOpaque(true);
			}
			if (c.value == 32) {
				display[c.row][c.column].setBackground(Color.CYAN);
				display[c.row][c.column].setOpaque(true);
			}
			if (c.value == 64) {
				display[c.row][c.column].setBackground(Color.BLUE);
				display[c.row][c.column].setOpaque(true);
			}
			if (c.value == 128) {
				display[c.row][c.column].setBackground(Color.magenta);
				display[c.row][c.column].setOpaque(true);
			}
			if (c.value == 256) {
				display[c.row][c.column].setBackground(Color.ORANGE);
				display[c.row][c.column].setOpaque(true);
			}
			if (c.value == 512) {
				display[c.row][c.column].setBackground(Color.yellow);
				display[c.row][c.column].setOpaque(true);
			}
			if (c.value == 1024) {
				display[c.row][c.column].setBackground(Color.white);
				display[c.row][c.column].setOpaque(true);
			}
			if (c.value == 2048 || c.value == 4096) {
				display[c.row][c.column].setBackground(Color.BLACK);
				display[c.row][c.column].setForeground(Color.WHITE);
				display[c.row][c.column].setOpaque(true);
			}

			if (c.value == winningVal) {
				display[c.row][c.column].setBorder(BorderFactory.createLineBorder(Color.GREEN));
				JOptionPane.showMessageDialog(frame,
						"Congrats, you beat the game!:)\n" + "Go to settings to start a new game ");
			}
		}
	}

	/*************************************************************************************************************************
	 * Handles all button and menu selections
	 * 
	 * @param e
	 *            the component that was pressed
	 *************************************************************************************************************************/
	public void actionPerformed(ActionEvent e) {

		// Quits the game if you click quitItem
		if (e.getSource() == quitItem) {
			System.exit(1);
		}
		// Resets the game to a new one.
		if (e.getSource() == newGameItem) {
			game.reset();
			renderBoard();
		}
		if (e.getSource() == resetBoardItem) {
			new GUI();
		}

		if (e.getSource() == resizeItem) {
			new GUI();
		}
	}

	/*************************************************************************************************************************
	 * Handles all key selections
	 * 
	 * @param e
	 *            the key that was pressed
	 *************************************************************************************************************************/
	private class DirectionListener implements KeyListener {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				game.slide(SlideDirection.UP);
				renderBoard();
				frame.repaint();
				break;
			case KeyEvent.VK_DOWN:
				game.slide(SlideDirection.DOWN);
				renderBoard();
				frame.repaint();
				break;
			case KeyEvent.VK_LEFT:
				game.slide(SlideDirection.LEFT);
				renderBoard();
				frame.repaint();
				break;
			case KeyEvent.VK_RIGHT:
				game.slide(SlideDirection.RIGHT);
				renderBoard();
				frame.repaint();
				break;
			case KeyEvent.VK_U:
				try {
					game.undo();
					renderBoard();
					results.setText("Current number of turns made: " + NumberGame.numTurns);
				} catch (IllegalStateException exp) {
					results.setText("Can't undo that far \n" + "Current number of turns made: " + NumberGame.numTurns);
				}
				frame.repaint();
				break;
			case KeyEvent.VK_R:
				try {
					game.redo();
					renderBoard();
					results.setText("Current number of turns made: " + NumberGame.numTurns);
				} catch (IllegalStateException exp) {
					results.setText("Can't redo that far \n" + "Current number of turns made: " + NumberGame.numTurns);
				}
				frame.repaint();
				break;
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub

		}
	}

	public static void main(String[] args) {
		new GUI();
	}
}

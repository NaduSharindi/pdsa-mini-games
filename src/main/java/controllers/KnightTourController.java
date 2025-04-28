package controllers;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import models.entities.Position;
import models.exceptions.DatabaseException;
import services.KnightTourService;
import utils.constants.KnightTourConstant;
import views.KnightTourView;

public class KnightTourController {
	private KnightTourService service;
	private KnightTourView view;
	private long start;

	/**
	 * Constructor to initialize the dependencies
	 * 
	 * @param service
	 * @param view
	 */
	public KnightTourController(KnightTourService service, KnightTourView view) {
		this.service = service;
		this.view = view;
	}

	/**
	 * Initialize and starts a new game round
	 */
	public void showView() {
		this.view.initUI();
		this.view.setVisible(true);
		this.initNewRound();
	}

	/**
	 * Initialize new game round data
	 */
	private void initNewRound() {
		// set the start time
		start = System.nanoTime();

		// starts game with randomly selected location
		Random rand = new Random();
		this.view.setKnightRow(rand.nextInt(KnightTourConstant.BOARD_SIZE));
		this.view.setKnightCol(rand.nextInt(KnightTourConstant.BOARD_SIZE));

		// set service manual movement to -1 for unvisited
		for (int i = 0; i < KnightTourConstant.BOARD_SIZE; i++) {
			Arrays.fill(this.service.getUserManualMovement()[i], -1);
		}

		// add event listeners
		this.addEventListeners();
	}

	/**
	 * Add event listeners for
	 */
	private void addEventListeners() {
		// initialize a new game button event
		this.view.getNewGameButton().addActionListener(e -> {
			this.showView();
		});

		// initialize game panel buttons events
		for (int row = 0; row < KnightTourConstant.BOARD_SIZE; row++) {
			for (int col = 0; col < KnightTourConstant.BOARD_SIZE; col++) {
				final int currentRow = row;
				final int currentCol = col;

				this.view.getBoardButtons()[row][col].addActionListener(e -> {
					handleMove(currentRow, currentCol);
				});
			}
		}

		// initialize brute force algorithm solutions find button event
		this.view.getBruteForceBtn().addActionListener(event -> {
			this.useBruteForceAlgorithm();
		});

		// initialize the warnsdorff algorithm solutions find button event listener
		this.view.getWarnsdorffBtn().addActionListener(event -> {
			this.warnsdorffAlgorithm();
		});
	}

	/**
	 * event handler for find solutions using warnsdorff algorithm
	 */
	private void warnsdorffAlgorithm() {
		try {
			// find solution from service
			this.service.useWarnsdorffAlgorithm(this.view.getKnightRow(), this.view.getKnightCol());
			// map solution into the game map
			this.mapSolution();

			// save data into the database
			long end = System.nanoTime();
			this.saveResult("COMPUTER", "Warnsdorff Algorithm", this.service.getCalculatedMovements(), end - start);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * event handler for find solutions using brute force algorithm
	 */
	private void useBruteForceAlgorithm() {
		try {
			// find solution from service
			this.service.useBruteForceAlgorithm(this.view.getKnightRow(), this.view.getKnightCol());
			// map solution into the game map
			this.mapSolution();

			// save data into the database
			long end = System.nanoTime();
			this.saveResult("COMPUTER", "Brute Force Algorithm", this.service.getCalculatedMovements(), end - start);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * map the solution into game map
	 */
	private void mapSolution() {
		try {
			for (int i = 0; i < KnightTourConstant.BOARD_SIZE; i++) {
				for (int j = 0; j < KnightTourConstant.BOARD_SIZE; j++) {
					// remove click events from the board game panel
					if (this.view.getBoardButtons()[i][j] != null) {
						for (ActionListener listener : this.view.getBoardButtons()[i][j].getActionListeners()) {
							this.view.getBoardButtons()[i][j].removeActionListener(listener);
						}
					}

					// add text to the button
					if (this.service.getCalculatedMovements()[i][j] != -1) {
						this.view.getBoardButtons()[i][j]
								.setText(String.valueOf(this.service.getCalculatedMovements()[i][j]));
					}
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Handle the user knight movement
	 * 
	 * @param row
	 * @param col
	 */
	private void handleMove(int row, int col) {
		try {
			int rowDiff = Math.abs(row - view.getKnightRow());
			int colDiff = Math.abs(col - view.getKnightCol());
			boolean isValidMove = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);

			if (!isValidMove) {
				JOptionPane.showMessageDialog(view, "Invalid move! Please move in an L-shape.", "Invalid Move",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (this.service.getVisited()[row][col]) {
				JOptionPane.showMessageDialog(view, "You already visited this square", "Already Visited",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Move the knight
			this.view.setKnightRow(row);
			this.view.setKnightCol(col);
			this.service.getVisited()[row][col] = true;
			this.service.setVisitedCount(this.service.getVisitedCount() + 1);

			this.view.getBoardButtons()[row][col].setBackground(new Color(173, 216, 230));

			// save the manual movement for save data in db later
			this.service.getUserManualMovement()[row][col] = this.service.getVisitedCount();

			// check for win
			if (this.service.getVisitedCount() == 5) {
				JOptionPane.showMessageDialog(view, "Congratulations! You completed the Knight's Tour.", "Victory",
						JOptionPane.INFORMATION_MESSAGE);
				// end time of game
				long end = System.nanoTime();

				// save result in database
				this.saveResult(null, "MANUAL", this.service.getUserManualMovement(), end - start);
			}

			// check game wins or lost
			if (!this.hasAnyValidMove(row, col)) {
				// No moves left
				JOptionPane.showMessageDialog(this.view, "No valid moves left. Game Over.", "Lost",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this.view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * Method to save data in the database
	 * 
	 * @param playerName
	 * @param algorithm
	 * @param gameData
	 * @param timeTaken
	 * @throws DatabaseException
	 */
	private void saveResult(String playerName, String algorithm, int[][] gameData, long timeTaken)
			throws DatabaseException {
		// save data in the database
		if (playerName == null || playerName.isBlank()) {
			playerName = JOptionPane.showInputDialog(view, "Enter your name to save result", "Save Result",
					JOptionPane.INFORMATION_MESSAGE);
		}

		// convert path data into path list
		List<Position> pathData = new ArrayList<Position>();
		for (int i = 0; i < KnightTourConstant.BOARD_SIZE; i++) {
			for (int j = 0; j < KnightTourConstant.BOARD_SIZE; j++) {
				pathData.add(new Position(i, j, gameData[i][j]));
			}
		}

		// save data in the database
		this.service.saveResult(playerName, algorithm, pathData, timeTaken);
	}

	/**
	 * Check knight next movement is valid or not
	 * 
	 * @return
	 */
	private boolean hasAnyValidMove(int row, int col) {
		int[] rowMoves = { -2, -1, 1, 2, 2, 1, -1, -2 };
		int[] colMoves = { 1, 2, 2, 1, -1, -2, -2, -1 };

		for (int i = 0; i < 8; i++) {
			int newRow = row + rowMoves[i];
			int newCol = col + colMoves[i];

			if (this.isInBounds(newRow, newCol) && !this.service.getVisited()[newRow][newCol]) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Check knight movement on boundaries
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isInBounds(int row, int col) {
		return row >= 0 && row < KnightTourConstant.BOARD_SIZE && col >= 0 && col < KnightTourConstant.BOARD_SIZE;
	}

}

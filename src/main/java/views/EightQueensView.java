/**
 * 
 */
package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * 
 */
public class EightQueensView extends JPanel {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int SIZE = 8;
    private JButton[][] boardButtons;
    private JButton submitButton;
    private JButton resetButton;
    private JLabel feedbackLabel;
    private JTextField playerNameField;
    private JLabel solutionsFoundLabel;

	public EightQueensView() {
		setLayout(new BorderLayout());
        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        boardButtons = new JButton[SIZE][SIZE];
        
       // Initialize chessboard buttons
        for(int row=0; row<SIZE; row++) {
            for(int column=0; column<SIZE; column++) {
                JButton btn = new JButton();
                btn.setBackground((row+column)%2 == 0 ? Color.WHITE : new Color(139, 69, 19));
                btn.setFont(new Font("Arial", Font.BOLD, 24));
                boardButtons[row][column] = btn;
                boardPanel.add(btn);
            }
        }
        
        //Control panel
        JPanel controlPanel = new JPanel(new FlowLayout());
        playerNameField = new JTextField(10);
        submitButton = new JButton("Submit Solution");
        resetButton = new JButton("Reset Board");
        solutionsFoundLabel = new JLabel("Solutions found: 0");
        
        
        controlPanel.add(new JLabel("Player Name:"));
        controlPanel.add(playerNameField);
        controlPanel.add(submitButton);
        controlPanel.add(resetButton);
        controlPanel.add(solutionsFoundLabel);
        
        //Feedback Label
        feedbackLabel = new JLabel("Welcome to the Eight Queens Puzzle!");
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        add(feedbackLabel, BorderLayout.SOUTH);
        
	}
	
	/*
	 * ActionListeners for each button
	 */
	
	public void setBoardButtonListener(ActionListener listener) {
		for(int row = 0; row < SIZE; row++) {
			for(int column = 0; column < SIZE; column++) {
				boardButtons[row][column].addActionListener(listener);
			}
		}
	}
	
	public void setSubmitButtonListener(ActionListener listener) {
		submitButton.addActionListener(listener);
	}
	
	public void setResetButtonListener(ActionListener listener) {
		resetButton.addActionListener(listener);
	}
	
	/*
	 * Get current board state as 
	 * int[] (column = index, value = row of queen, -1 if none)
	 */
	
	public int[] getBoardState() {
		int[] positions = new int[SIZE];
		for(int column = 0; column < SIZE; column++) {
			positions[column] = -1; // default ( no queens in column)
			for(int row = 0; row < SIZE; row++) {
				if("Q".equals(boardButtons[row][column].getText())) {
					positions[column] = row;
					break;
				}
			}
		}
		return positions;
	}

	
	/*
	 * Set a queen at (row, column)
	 */
	
	public void setQueen(int row, int column,boolean present) {
		boardButtons[row][column].setText(present ? "Q" : "");
	}
	
	/*
	 * Reset board
	 */
	
	public void resetBoard() {
		for(int row = 0; row < SIZE; row++) {
			for(int column = 0; column < SIZE; column++) {
				boardButtons[row][column].setText("");
			}
		}
	}
	
    public JButton[][] getBoardButtons() {
        return boardButtons;
    }
	
	public String getPlayerName() {
		return playerNameField.getText().trim();
	}
	
	public void setFeedback(String message) {
		feedbackLabel.setText(message);
	}
	
	public void setSolutionsFound(int count) {
		solutionsFoundLabel.setText("Solutions Found: " + count);
	}
	
	/*
	 * toggle queen on button click
	 */
	
	public void toggleQueen(int row, int column) {
		if("Q".equals(boardButtons[row][column].getText())) {
			boardButtons[row][column].setText("");
		} else {
			// remove any other queen in this column
			for(int r = 0 ; r < SIZE; r++) {
				boardButtons[r][column].setText("");
			}
			boardButtons[row][column].setText("Q");
		}
	}
}

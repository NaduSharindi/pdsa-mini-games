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
    private JButton backToMenuButton;
    private JLabel welcomeLabel;
    private JLabel feedbackLabel;
    private JTextField playerNameField;
    private JLabel solutionsFoundLabel;
    private JLabel remainingSolutionsLabel;
    private JLabel sequentialTimeLabel;
    private JLabel threadedTimeLabel;

	public EightQueensView() {
		setLayout(new BorderLayout());
        welcomeLabel = new JLabel("Welcome to the Eight Queens Puzzle!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 28));
        add(welcomeLabel, BorderLayout.NORTH);
        
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
        add(boardPanel, BorderLayout.CENTER);
        
        // Game information panel
        Font bigFont = new Font("Arial", Font.BOLD, 16);
        Color blue = new Color(0, 0, 180);
        
        solutionsFoundLabel = new JLabel("Solutions found: 0");
        solutionsFoundLabel.setFont(bigFont);
        solutionsFoundLabel.setForeground(blue);
        remainingSolutionsLabel = new JLabel("Remaining: 92");
        remainingSolutionsLabel.setFont(bigFont);
        remainingSolutionsLabel.setForeground(blue);
        sequentialTimeLabel = new JLabel("Sequential Time: 0ms");
        sequentialTimeLabel.setFont(bigFont);
        sequentialTimeLabel.setForeground(blue);
        threadedTimeLabel = new JLabel("Threaded Time: 0ms");
        threadedTimeLabel.setFont(bigFont);
        threadedTimeLabel.setForeground(blue);
        
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Player Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

        playerNameField = new JTextField(12);
        playerNameField.setMaximumSize(new Dimension(360, 30));
        playerNameField.setFont(new Font("Arial", Font.PLAIN, 18));
        
        Dimension buttonSize = new Dimension(180, 40);
        submitButton = new JButton("Submit Solution");
        submitButton.setPreferredSize(buttonSize);
        submitButton.setMaximumSize(buttonSize);
        submitButton.setFont(new Font("Arial", Font.BOLD, 18));
        submitButton.setBackground(Color.BLUE); 
        submitButton.setForeground(Color.WHITE);
        resetButton = new JButton("Reset Board");
        resetButton.setPreferredSize(buttonSize);
        resetButton.setMaximumSize(buttonSize);
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setBackground(Color.YELLOW); 
        resetButton.setForeground(Color.BLACK);
        backToMenuButton = new JButton("Back to Menu");
        backToMenuButton.setPreferredSize(buttonSize);
        backToMenuButton.setMaximumSize(buttonSize);
        backToMenuButton.setFont(new Font("Arial", Font.BOLD, 18));
        backToMenuButton.setBackground(Color.green);
        backToMenuButton.setForeground(Color.BLACK);

        playerPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        playerPanel.add(nameLabel);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        playerPanel.add(playerNameField);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        playerPanel.add(submitButton);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        playerPanel.add(resetButton);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        playerPanel.add(backToMenuButton);
        playerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(BorderFactory.createEmptyBorder(0, 32, 0, 0));
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setOpaque(false);

        eastPanel.add(Box.createRigidArea(new Dimension(0, 24)));
        eastPanel.add(solutionsFoundLabel);
        eastPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        eastPanel.add(remainingSolutionsLabel);
        eastPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        eastPanel.add(sequentialTimeLabel);
        eastPanel.add(Box.createRigidArea(new Dimension(0, 18)));
        eastPanel.add(threadedTimeLabel);
        eastPanel.add(Box.createRigidArea(new Dimension(0, 32)));
        eastPanel.add(playerPanel);
        eastPanel.add(Box.createVerticalGlue());

        add(eastPanel, BorderLayout.EAST);
        
       
        feedbackLabel = new JLabel(" ");
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerPanel.add(feedbackLabel);  
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
	
	public void setBackToMenuButtonListener(ActionListener listener) {
	    backToMenuButton.addActionListener(listener);
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
    
	public JButton getBackToMenuButton() {
	    return backToMenuButton;
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
	
	public void setRemainingSolutions(int remaining) {
	    remainingSolutionsLabel.setText("Remaining: " + remaining);
	}
	
	public void setSequentialTimeLabel(long timeMs) {
	    sequentialTimeLabel.setText("Sequential Time: " + timeMs + " ms");
	}
	public void setThreadedTimeLabel(long timeMs) {
	    threadedTimeLabel.setText("Threaded Time: " + timeMs + " ms");
	}

	
	public void setFeedback(String message, boolean isError) {
	    feedbackLabel.setText(message);
	    if (isError) {
	        feedbackLabel.setForeground(Color.RED);
	        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 16)); // for errors
	    } else {
	        feedbackLabel.setForeground(new Color(0, 100, 0));
	        feedbackLabel.setFont(new Font("Arial", Font.BOLD, 16)); // for information
	    }
	}
	
	public void clearPlayerNameField() {
		playerNameField.setText("");
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

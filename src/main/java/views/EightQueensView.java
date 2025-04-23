/**
 * 
 */
package views;

import javax.swing.*;
import java.awt.*;

/**
 * 
 */
public class EightQueensView extends JPanel {
	
    private static final int SIZE = 8;
    private JButton[][] board;
    private JButton submitBtn;
    private JButton resetBtn;
    private JLabel feedbackLbl;
    private JTextField playerNameField;
    private JLabel solutionsLbl;

	public EightQueensView() {
		setLayout(new BorderLayout());
		
        // Chessboard
        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        board = new JButton[SIZE][SIZE];
        for(int row=0; row<SIZE; row++) {
            for(int col=0; col<SIZE; col++) {
                JButton btn = new JButton();
                btn.setBackground((row+col)%2 == 0 ? Color.WHITE : new Color(139, 69, 19));
                btn.setFont(new Font("Arial", Font.BOLD, 24));
                board[row][col] = btn;
                boardPanel.add(btn);
            }
        }
	}
}

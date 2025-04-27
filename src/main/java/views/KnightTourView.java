package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import utils.constants.KnightTourConstant;
import utils.constants.TravelingSalesManConstants;

public class KnightTourView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// Game UI components
	private JButton[][] boardButtons;
	private int knightRow, knightCol;
	private ImageIcon knightIcon;
	private JPanel boardPanel;
	private JButton newGameButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KnightTourView frame = new KnightTourView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public KnightTourView() {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 600, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
	}

	/**
	 * Initialize the game window
	 */
	public void initUI() {
		// clear the content pane to remove all
		this.contentPane.removeAll();

		// initialize new game window data
		this.setTitle("Knight's Tour Game");
		this.setSize(600, 650);
		this.setLayout(new BorderLayout());

		// Top panel with New Game button
		JPanel topPanel = new JPanel();
		newGameButton = new JButton("New Game");
		topPanel.add(newGameButton);
		this.contentPane.add(topPanel, BorderLayout.NORTH);

		// game panel and game buttons
		boardButtons = new JButton[KnightTourConstant.BOARD_SIZE][KnightTourConstant.BOARD_SIZE];
		boardPanel = new JPanel(new GridLayout(KnightTourConstant.BOARD_SIZE, KnightTourConstant.BOARD_SIZE));

		// initialize the game board
		for (int row = 0; row < KnightTourConstant.BOARD_SIZE; row++) {
			for (int col = 0; col < KnightTourConstant.BOARD_SIZE; col++) {
				JButton button = new JButton();
				button.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
				button.setOpaque(true);
				button.setBorderPainted(false);
				boardButtons[row][col] = button;
				boardPanel.add(button);
			}
		}

		this.contentPane.add(boardPanel, BorderLayout.CENTER);
		
		knightIcon = new ImageIcon((new ImageIcon(this.getClass().getResource("/gameicons/knight-tour.png"))).getImage()
				.getScaledInstance(20, 20, Image.SCALE_FAST));
	}

	// getters and setters for view
	public JButton[][] getBoardButtons() {
		return boardButtons;
	}

	public void setBoardButtons(JButton[][] boardButtons) {
		this.boardButtons = boardButtons;
	}

	public int getKnightRow() {
		return knightRow;
	}

	public void setKnightRow(int knightRow) {
		//removing the old icon
		boardButtons[this.knightRow][this.knightCol].setIcon(null);
		this.knightRow = knightRow;
		boardButtons[this.knightRow][this.knightCol].setIcon(knightIcon);
	}

	public int getKnightCol() {
		return knightCol;
	}

	public void setKnightCol(int knightCol) {
		//removing the old icon
		boardButtons[this.knightRow][this.knightCol].setIcon(null);
		this.knightCol = knightCol;
		boardButtons[this.knightRow][this.knightCol].setIcon(knightIcon);
	}

	public JButton getNewGameButton() {
		return newGameButton;
	}

	public void setNewGameButton(JButton newGameButton) {
		this.newGameButton = newGameButton;
	}
}

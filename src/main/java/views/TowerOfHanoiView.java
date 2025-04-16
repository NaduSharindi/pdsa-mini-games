package views;

import java.awt.*;

import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import services.TowerOfHanoiService;

/**
 * TowerOfHanoiView - Java Swing-based GUI for the Tower of Hanoi game.
 */
public class TowerOfHanoiView extends JFrame {
    private static final long serialVersionUID = 1L;

    // UI Components
    private JPanel mainPanel;
    private JTextField playerNameField;
    private JLabel diskCountLabel;
    private JTextField moveCountField;
    private JTextArea moveSequenceArea;
    private JButton submitButton;
    private JButton solveButton;
    private JButton resetButton;
    private JLabel resultLabel;
    private JComboBox<String> algorithmSelector;
    private PegPanel pegPanel;

    /**
     * Constructor to initialize the frame
     */
    public TowerOfHanoiView() {
        setTitle("Tower of Hanoi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window
        initComponents(); // Initialize all GUI components
    }

    /**
     * Initializes and arranges all UI components in the frame
     */
    private void initComponents() {
        // Main container panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Tower of Hanoi Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Left panel for input controls
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));

        // Player name input
        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerPanel.add(new JLabel("Player Name:"));
        playerNameField = new JTextField(15);
        playerPanel.add(playerNameField);

        // Game configuration controls
        JPanel gamePanel = new JPanel(new GridLayout(6, 1, 5, 10));
        diskCountLabel = new JLabel("Number of Disks: ?");
        diskCountLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gamePanel.add(diskCountLabel);

        // Move count input
        JPanel moveCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        moveCountPanel.add(new JLabel("Number of Moves:"));
        moveCountField = new JTextField(5);
        moveCountPanel.add(moveCountField);
        gamePanel.add(moveCountPanel);

        // Algorithm selector
        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        algorithmPanel.add(new JLabel("Algorithm:"));
        algorithmSelector = new JComboBox<>(new String[] {
            "Recursive (3 pegs)",
            "Iterative (3 pegs)",
            "Frame-Stewart (4 pegs)"
        });
        algorithmPanel.add(algorithmSelector);
        gamePanel.add(algorithmPanel);

        // Move sequence input area
        JLabel moveSeqLabel = new JLabel("Move Sequence (format: A->B, one per line):");
        moveSeqLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gamePanel.add(moveSeqLabel);

        moveSequenceArea = new JTextArea(8, 15);
        moveSequenceArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(moveSequenceArea);
        gamePanel.add(scrollPane);

        // Button panel for Submit, Solve, and Reset
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        submitButton = new JButton("Submit Answer");
        solveButton = new JButton("Show Solution");
        resetButton = new JButton("New Game");
        buttonPanel.add(submitButton);
        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton);

        // Result label
        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultLabel = new JLabel("");
        resultLabel.setForeground(Color.BLUE);
        resultPanel.add(resultLabel);

        // Assemble left side of the layout
        leftPanel.add(playerPanel, BorderLayout.NORTH);
        leftPanel.add(gamePanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Peg display panel (visual representation of pegs/disks)
        pegPanel = new PegPanel();

        // Listener to update number of pegs based on selected algorithm
        algorithmSelector.addActionListener(e -> {
            if (algorithmSelector.getSelectedIndex() == 2) { // Frame-Stewart
                pegPanel.setNumberOfPegs(4);
            } else {
                pegPanel.setNumberOfPegs(3);
            }
        });

        // Add all main components to the main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(pegPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    /**
     * Updates the disk count label and PegPanel visualization.
     */
    public void updateDiskCount(int count) {
        diskCountLabel.setText("Number of Disks: " + count);
        pegPanel.setNumberOfDisks(count);
    }

    /**
     * Displays the result of the game (success/failure) with a message.
     */
    public void showResult(boolean success, String message) {
        resultLabel.setText(message);
        resultLabel.setForeground(success ? new Color(0, 128, 0) : Color.RED);
    }

    /**
     * Fills the move sequence area with a list of move steps.
     */
    public void displayMoveSequence(List<TowerOfHanoiService.Move> moves) {
        StringBuilder sb = new StringBuilder();
        for (TowerOfHanoiService.Move move : moves) {
            sb.append(move.toString()).append("\n");
        }
        moveSequenceArea.setText(sb.toString());
    }

    // Listener registration methods for external control (MVC pattern)
    public void addSubmitListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }

    public void addSolveListener(ActionListener listener) {
        solveButton.addActionListener(listener);
    }

    public void addResetListener(ActionListener listener) {
        resetButton.addActionListener(listener);
    }

    // Getters for UI input values
    public String getPlayerName() {
        return playerNameField.getText();
    }

    public String getMoveCountText() {
        return moveCountField.getText();
    }

    public String getMoveSequenceText() {
        return moveSequenceArea.getText();
    }

    public int getSelectedAlgorithmIndex() {
        return algorithmSelector.getSelectedIndex();
    }

    public PegPanel getPegPanel() {
        return pegPanel;
    }

    public JTextField getMoveCountField() {
        return moveCountField;
    }

    /**
     * Clears all input fields and result display
     */
    public void resetInputs() {
        moveCountField.setText("");
        moveSequenceArea.setText("");
        resultLabel.setText("");
    }
}

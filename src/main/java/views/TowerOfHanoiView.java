package views;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Tower of Hanoi View 
 * 
 */
public class TowerOfHanoiView extends JPanel {
    private static final long serialVersionUID = 1L;

    // UI Components
    private JTextField playerNameField;
    private JLabel diskCountLabel;
    private JTextField moveCountField;
    private JTextArea moveSequenceArea;
    private JButton checkAnswerButton;
    private JButton newGameButton;
    private JButton backButton;
    private JButton autoSolveButton;
    private JRadioButton threePegsRadio;
    private JRadioButton fourPegsRadio;
    private ButtonGroup pegCountGroup;
    private JLabel resultLabel;
    private PegPanel pegPanel;

    /**
     * Constructor to initialize the panel
     */
    public TowerOfHanoiView() {
        initComponents();
    }

    /**
     * Initializes and arranges all UI components in the panel
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top title panel with styled heading
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Tower of Hanoi Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Control panel (right side) with grid layout for form elements
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setPreferredSize(new Dimension(300, 400));
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createTitledBorder("Game Controls")
        ));

        // Game information panel
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 6));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Game Information"));

        // Player name input with label
        JPanel playerPanel = new JPanel(new BorderLayout(2, 0));
        playerPanel.add(new JLabel("Player Name:"), BorderLayout.WEST);
        playerNameField = new JTextField(15);
        playerNameField.setPreferredSize(new Dimension(80, 16));
        playerPanel.add(playerNameField, BorderLayout.CENTER);
        infoPanel.add(playerPanel);

        // Peg selection panel
        JPanel pegSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pegSelectionPanel.setBorder(BorderFactory.createTitledBorder("Peg Count"));

        threePegsRadio = new JRadioButton("3 Pegs", true);
        fourPegsRadio = new JRadioButton("4 Pegs");
        pegCountGroup = new ButtonGroup();
        pegCountGroup.add(threePegsRadio);
        pegCountGroup.add(fourPegsRadio);

        // Style the radio buttons
        threePegsRadio.setFont(new Font("Arial", Font.PLAIN, 14));
        fourPegsRadio.setFont(new Font("Arial", Font.PLAIN, 14));

        pegSelectionPanel.add(threePegsRadio);
        pegSelectionPanel.add(fourPegsRadio);
        infoPanel.add(pegSelectionPanel);

        // Game status labels (disk count and optimal move count)
        JPanel statusPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        diskCountLabel = new JLabel("Disks: 0");
        diskCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        //optimalMoveCountLabel = new JLabel("Optimal Moves: 0");
        //optimalMoveCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        statusPanel.add(diskCountLabel);
        //statusPanel.add(optimalMoveCountLabel);
        infoPanel.add(statusPanel);

        // Move count input with validation hint
        JPanel moveCountPanel = new JPanel(new BorderLayout(5, 0));
        moveCountPanel.add(new JLabel("Your Move Count:"), BorderLayout.WEST);
        moveCountField = new JTextField(5);
        moveCountField.setPreferredSize(new Dimension(50, 22));
        moveCountPanel.add(moveCountField, BorderLayout.CENTER);
        infoPanel.add(moveCountPanel);

        // Input area for instructions
        JPanel instructionPanel = new JPanel(new BorderLayout(0, 5));
        instructionPanel.setBorder(BorderFactory.createTitledBorder("Move Sequence"));
        
        JLabel instructionLabel = new JLabel(
            "<html>Enter moves in format: A-B, B-C, etc.<br>"  +
            "Each move should be separated by a comma.</html>"
        );
        instructionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionPanel.add(instructionLabel, BorderLayout.NORTH);
        
        // Move sequence text area with scroll capability
        moveSequenceArea = new JTextArea(8, 20);
        moveSequenceArea.setLineWrap(true);
        moveSequenceArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(moveSequenceArea);
        instructionPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel with consistent styling
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        checkAnswerButton = new JButton("Check Answer");
        checkAnswerButton.setBackground(new Color(65, 105, 225));
        checkAnswerButton.setForeground(Color.WHITE);
        checkAnswerButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        autoSolveButton = new JButton("Auto Solve");
        autoSolveButton.setBackground(new Color(255, 140, 0));
        autoSolveButton.setForeground(Color.WHITE);
        autoSolveButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        newGameButton = new JButton("New Game");
        newGameButton.setBackground(new Color(46, 139, 87));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        backButton = new JButton("Back to Menu");
        backButton.setBackground(new Color(178, 34, 34));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        buttonPanel.add(checkAnswerButton);
        buttonPanel.add(autoSolveButton);
        buttonPanel.add(newGameButton);
        buttonPanel.add(backButton);

        // Result display panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Result"));
        resultLabel = new JLabel("");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultPanel.add(resultLabel, BorderLayout.CENTER);

        // Assemble right control panel
        controlPanel.add(infoPanel, BorderLayout.NORTH);
        controlPanel.add(instructionPanel, BorderLayout.CENTER);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create visualization panel (left side)
        pegPanel = new PegPanel();
        pegPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createTitledBorder("Tower of Hanoi Visualization")
        ));

        // Add peg selection listeners
        ActionListener pegSelectionListener = e -> {
            int pegCount = getSelectedPegCount();
            pegPanel.setNumberOfPegs(pegCount);
            pegPanel.resetPegs();
        };
        threePegsRadio.addActionListener(pegSelectionListener);
        fourPegsRadio.addActionListener(pegSelectionListener);

        // Add all components to main panel
        add(titlePanel, BorderLayout.NORTH);
        add(pegPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        add(resultPanel, BorderLayout.SOUTH);
    }

    /**
     * Gets the selected peg count (3 or 4)
     */
    public int getSelectedPegCount() {
        return fourPegsRadio.isSelected() ? 4 : 3;
    }

    /**
     * Sets the peg count (3 or 4)
     */
    public void setPegCount(int count) {
        if (count == 4) {
            fourPegsRadio.setSelected(true);
        } else {
            threePegsRadio.setSelected(true);
        }
        pegPanel.setNumberOfPegs(count);
    }

    /**
     * Updates the disk count label and visualization
     */
    public void setDiskCount(int count) {
        diskCountLabel.setText("Disks: " + count);
        pegPanel.setDiskCount(count);
        pegPanel.resetPegs();
    }
    
    /**
     * Updates the optimal move count label
     */
   /* public void setOptimalMoveCount(int count) {
        optimalMoveCountLabel.setText("Optimal Moves: " + count);
    }*/

    /**
     * Gets the player name from input field
     */
    public String getPlayerName() {
        return playerNameField.getText().trim();
    }

    /**
     * Gets the move count from input field
     */
    public int getMoveCount() {
        try {
            return Integer.parseInt(moveCountField.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Sets the move count in the input field
     */
    public void setMoveCount(int count) {
        moveCountField.setText(String.valueOf(count));
    }

    /**
     * Gets the move sequence from text area
     */
    public String getMoveSequence() {
        return moveSequenceArea.getText().trim();
    }

    /**
     * Sets the move sequence in the text area
     */
    public void setMoveSequence(String sequence) {
        moveSequenceArea.setText(sequence);
    }

    /**
     * Resets all game inputs
     */
    public void resetGame() {
        moveCountField.setText("");
        moveSequenceArea.setText("");
        resultLabel.setText("");
        pegPanel.resetPegs();
    }

    /**
     * Add listener for check answer button
     */
    public void addCheckAnswerListener(ActionListener listener) {
        checkAnswerButton.addActionListener(listener);
    }

    /**
     * Add listener for auto solve button
     */
    public void addAutoSolveListener(ActionListener listener) {
        autoSolveButton.addActionListener(listener);
    }

    /**
     * Add listener for new game button
     */
    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }

    /**
     * Add listener for back button
     */
    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
    
    /**
     * Add listener for peg selection changes
     */
    public void addPegSelectionListener(ActionListener listener) {
        threePegsRadio.addActionListener(listener);
        fourPegsRadio.addActionListener(listener);
    }

    /**
     * Display message to user
     */
    public void showMessage(String message) {
        resultLabel.setText(message);
    }

    /**
     * Sets success message style
     */
    public void showSuccess(String message) {
        resultLabel.setText(message);
        resultLabel.setForeground(new Color(0, 128, 0));
    }

    /**
     * Sets error message style
     */
    public void showError(String message) {
        resultLabel.setText(message);
        resultLabel.setForeground(new Color(178, 34, 34));
    }

    /**
     * Animate the solution using the pegPanel
     */
    public void animateSolution(List<String> moves) {
        pegPanel.animateSolution(moves);
    }
}

package views;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import services.TowerOfHanoiService;

public class TowerOfHanoiView extends JFrame {
    private static final long serialVersionUID = 1L;
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

    public TowerOfHanoiView() {
        setTitle("Tower of Hanoi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Tower of Hanoi Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        JPanel playerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerPanel.add(new JLabel("Player Name:"));
        playerNameField = new JTextField(15);
        playerPanel.add(playerNameField);

        JPanel gamePanel = new JPanel(new GridLayout(6, 1, 5, 10));
        diskCountLabel = new JLabel("Number of Disks: ?");
        diskCountLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gamePanel.add(diskCountLabel);

        JPanel moveCountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        moveCountPanel.add(new JLabel("Number of Moves:"));
        moveCountField = new JTextField(5);
        moveCountPanel.add(moveCountField);
        gamePanel.add(moveCountPanel);

        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        algorithmPanel.add(new JLabel("Algorithm:"));
        algorithmSelector = new JComboBox<>(new String[] {
            "Recursive (3 pegs)", 
            "Iterative (3 pegs)", 
            "Frame-Stewart (4 pegs)"
        });
        algorithmPanel.add(algorithmSelector);
        gamePanel.add(algorithmPanel);

        JLabel moveSeqLabel = new JLabel("Move Sequence (format: A->B, one per line):");
        moveSeqLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gamePanel.add(moveSeqLabel);

        moveSequenceArea = new JTextArea(8, 15);
        moveSequenceArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(moveSequenceArea);
        gamePanel.add(scrollPane);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        submitButton = new JButton("Submit Answer");
        solveButton = new JButton("Show Solution");
        resetButton = new JButton("New Game");
        buttonPanel.add(submitButton);
        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton);

        JPanel resultPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        resultLabel = new JLabel("");
        resultLabel.setForeground(Color.BLUE);
        resultPanel.add(resultLabel);

        leftPanel.add(playerPanel, BorderLayout.NORTH);
        leftPanel.add(gamePanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        pegPanel = new PegPanel();

        // Algorithm selection listener for dynamic peg count
        algorithmSelector.addActionListener(e -> {
            if (algorithmSelector.getSelectedIndex() == 2) { // Frame-Stewart
                pegPanel.setNumberOfPegs(4);
            } else {
                pegPanel.setNumberOfPegs(3);
            }
        });

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(pegPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    public void updateDiskCount(int count) {
        diskCountLabel.setText("Number of Disks: " + count);
        pegPanel.setNumberOfDisks(count);
    }

    public void showResult(boolean success, String message) {
        resultLabel.setText(message);
        resultLabel.setForeground(success ? new Color(0, 128, 0) : Color.RED);
    }

    public void displayMoveSequence(List<TowerOfHanoiService.Move> moves) {
        StringBuilder sb = new StringBuilder();
        for (TowerOfHanoiService.Move move : moves) {
            sb.append(move.toString()).append("\n");
        }
        moveSequenceArea.setText(sb.toString());
    }

    public void addSubmitListener(ActionListener listener) {
        submitButton.addActionListener(listener);
    }
    public void addSolveListener(ActionListener listener) {
        solveButton.addActionListener(listener);
    }
    public void addResetListener(ActionListener listener) {
        resetButton.addActionListener(listener);
    }

    public String getPlayerName() { return playerNameField.getText(); }
    public String getMoveCountText() { return moveCountField.getText(); }
    public String getMoveSequenceText() { return moveSequenceArea.getText(); }
    public int getSelectedAlgorithmIndex() { return algorithmSelector.getSelectedIndex(); }
    public PegPanel getPegPanel() { return pegPanel; }
    public JTextField getMoveCountField() { return moveCountField; }
    public void resetInputs() {
        moveCountField.setText("");
        moveSequenceArea.setText("");
        resultLabel.setText("");
    }
}

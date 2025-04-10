package views;

import javax.swing.*;
import java.awt.*;

public class TowerOfHanoiView extends JFrame {

    private JPanel pegPanel1, pegPanel2, pegPanel3;
    private JButton startButton, resetButton, autoSolveButton;
    private JLabel moveCounterLabel;

    public TowerOfHanoiView() {
        setTitle("Tower of Hanoi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255)); // AliceBlue

        // Title
        JLabel title = new JLabel("Tower of Hanoi", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(65, 105, 225)); // RoyalBlue
        add(title, BorderLayout.NORTH);

        // Pegs Panel
        JPanel pegsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        pegsPanel.setOpaque(false);
        pegPanel1 = createPegPanel("Peg A");
        pegPanel2 = createPegPanel("Peg B");
        pegPanel3 = createPegPanel("Peg C");

        pegsPanel.add(pegPanel1);
        pegsPanel.add(pegPanel2);
        pegsPanel.add(pegPanel3);
        add(pegsPanel, BorderLayout.CENTER);

        // Control Panel
        JPanel controls = new JPanel();
        controls.setBackground(new Color(245, 245, 245));
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        startButton = new JButton("Start");
        resetButton = new JButton("Reset");
        autoSolveButton = new JButton("Auto Solve");
        moveCounterLabel = new JLabel("Moves: 0");

        controls.add(startButton);
        controls.add(resetButton);
        controls.add(autoSolveButton);
        controls.add(moveCounterLabel);

        add(controls, BorderLayout.SOUTH);
        //setVisible(true);
    }

    private JPanel createPegPanel(String name) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(name));
        panel.setBackground(new Color(255, 255, 255));
        return panel;
    }

    // Getters to access buttons and panels from controller
    public JButton getStartButton() { return startButton; }
    public JButton getResetButton() { return resetButton; }
    public JButton getAutoSolveButton() { return autoSolveButton; }

    public JPanel getPegPanel1() { return pegPanel1; }
    public JPanel getPegPanel2() { return pegPanel2; }
    public JPanel getPegPanel3() { return pegPanel3; }

    public JLabel getMoveCounterLabel() { return moveCounterLabel; }

    // Optional: Add method to update discs later

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TowerOfHanoiView::new);
    }
}

package views;

import javax.swing.*;

import java.awt.*;
import utils.dsa.Stack; // Ensure this import points to your custom Stack

public class TowerOfHanoiView extends JFrame {

    private static final long serialVersionUID = 1L;

    private PegPanel pegPanel1, pegPanel2, pegPanel3;
    private JButton startButton, resetButton, autoSolveButton;
    private JLabel moveCounterLabel;

    private static final Color PEG_COLOR = new Color(255, 0, 0); // Red
    private static final int PEG_WIDTH = 10;
    private static final int PEG_HEIGHT = 150;
    private static final int BASE_WIDTH = 100;
    private static final int BASE_HEIGHT = 20;
    private static final Color BASE_COLOR = new Color(255, 165, 0); // Orange

    public TowerOfHanoiView() {
        setTitle("Tower of Hanoi");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(0, 0, 0)); // Dark background

        // Title
        JLabel title = new JLabel("Tower of Hanoi", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(173, 216, 230)); // LightBlue
        add(title, BorderLayout.NORTH);

        // Pegs Panel
        JPanel pegsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 50)); // Use FlowLayout for positioning
        pegsPanel.setOpaque(false);
        pegPanel1 = new PegPanel("A");
        pegPanel2 = new PegPanel("B");
        pegPanel3 = new PegPanel("C");

        pegsPanel.add(pegPanel1);
        pegsPanel.add(pegPanel2);
        pegsPanel.add(pegPanel3);
        add(pegsPanel, BorderLayout.CENTER);

        // Control Panel
        JPanel controls = new JPanel();
        controls.setBackground(new Color(50, 50, 50)); // Darker control panel
        controls.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        startButton = new JButton("Start");
        resetButton = new JButton("Reset");
        autoSolveButton = new JButton("Auto Solve");
        moveCounterLabel = new JLabel("Moves: 0");
        moveCounterLabel.setForeground(Color.WHITE); // White text for move counter

        controls.add(startButton);
        controls.add(resetButton);
        controls.add(autoSolveButton);
        controls.add(moveCounterLabel);

        add(controls, BorderLayout.SOUTH);
    }

    // Custom JPanel to represent a peg
    public class PegPanel extends JPanel {
        private String name;
        private Stack<JPanel> discs = new Stack<>(); // Use your custom Stack

        public PegPanel(String name) {
            this.name = name;
            setPreferredSize(new Dimension(BASE_WIDTH + 40, PEG_HEIGHT + BASE_HEIGHT + 20)); // Adjust size
            setOpaque(false); // Make it transparent
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, PEG_HEIGHT - 30)); // Stack discs from bottom
            setAlignmentY(BOTTOM_ALIGNMENT);
        }

        public void addDisc(JPanel disc) {
            discs.push(disc);
            add(disc);
            revalidate();
            repaint();
        }

        public JPanel removeDisc() {
            if (!discs.isEmpty()) {
                JPanel disc = discs.pop();
                remove(disc);
                revalidate();
                repaint();
                return disc;
            }
            return null;
        }

        public Stack<JPanel> getDiscs() { // Use your custom Stack
            return discs;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(BASE_COLOR);
            int baseX = getWidth() / 2 - BASE_WIDTH / 2;
            int baseY = getHeight() - BASE_HEIGHT;
            g2d.fillRect(baseX, baseY, BASE_WIDTH, BASE_HEIGHT);

            g2d.setColor(PEG_COLOR);
            int pegX = getWidth() / 2 - PEG_WIDTH / 2;
            int pegY = baseY - PEG_HEIGHT;
            g2d.fillRect(pegX, pegY, PEG_WIDTH, PEG_HEIGHT);

            g2d.setColor(Color.WHITE);
            g2d.drawString(name, getWidth() / 2 - 10, getHeight() - 5); // Label the peg
        }
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getAutoSolveButton() {
        return autoSolveButton;
    }

    public PegPanel getPegPanel1() {
        return pegPanel1;
    }

    public PegPanel getPegPanel2() {
        return pegPanel2;
    }

    public PegPanel getPegPanel3() {
        return pegPanel3;
    }

    public JLabel getMoveCounterLabel() {
        return moveCounterLabel;
    }

    public void renderPegs(Stack<JPanel> pegAStack, Stack<JPanel> pegBStack, Stack<JPanel> pegCStack) {
        pegPanel1.removeAll();
        pegPanel2.removeAll();
        pegPanel3.removeAll();

        // Clear existing discs from PegPanels
        pegPanel1.discs.clear();
        pegPanel2.discs.clear();
        pegPanel3.discs.clear();

        addDiscsToPegPanel(pegPanel1, pegAStack);
        addDiscsToPegPanel(pegPanel2, pegBStack);
        addDiscsToPegPanel(pegPanel3, pegCStack);

        repaint();
        revalidate();
    }

    private void addDiscsToPegPanel(PegPanel pegPanel, Stack<JPanel> discStack) {
        Stack<JPanel> tempStack = new Stack<>(); // Use your custom Stack
        while (!discStack.isEmpty()) {
            tempStack.push(discStack.pop());
        }
        while (!tempStack.isEmpty()) {
            JPanel disc = tempStack.pop();
            pegPanel.addDisc(disc);
            discStack.push(disc);
        }
    }

    public void clearPegs() {
        pegPanel1.removeAll();
        pegPanel2.removeAll();
        pegPanel3.removeAll();
        pegPanel1.discs.clear();
        pegPanel2.discs.clear();
        pegPanel3.discs.clear();
        repaint();
        revalidate();
    }

    public void updateMoveCounter(int moves) {
        moveCounterLabel.setText("Moves: " + moves);
    }

    public void addDiscToPeg(PegPanel pegPanel, JPanel discComponent) {
        pegPanel.addDisc(discComponent);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TowerOfHanoiView::new);
    }
}
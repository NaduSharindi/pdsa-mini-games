package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import utils.dsa.Stack;
import java.util.List;

public class PegPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int PEG_WIDTH = 10;
    private static final int DISK_HEIGHT = 20;
    private static final int MIN_DISK_WIDTH = 40;
    private static final int MAX_DISK_WIDTH = 200;

    private int numberOfDisks = 5;
    private int numberOfPegs = 3;
    private Stack<Integer>[] pegs;

    @SuppressWarnings("unchecked")
    public PegPanel() {
        setPreferredSize(new Dimension(500, 350));
        setNumberOfPegs(3);
    }

    public void setNumberOfDisks(int numberOfDisks) {
        this.numberOfDisks = numberOfDisks;
        resetPegs();
        repaint();
    }

    public void setNumberOfPegs(int n) {
        this.numberOfPegs = n;
        pegs = new Stack[n];
        for (int i = 0; i < n; i++) pegs[i] = new Stack<>();
        resetPegs();
        repaint();
    }

    public int getNumberOfPegs() {
        return numberOfPegs;
    }

    public Stack<Integer>[] getPegs() {
        return pegs;
    }

    private void resetPegs() {
        for (Stack<Integer> peg : pegs) peg.clear();
        for (int i = numberOfDisks; i > 0; i--) pegs[0].push(i);
    }

    public void reset() {
        resetPegs();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int baseY = getHeight() - 50;
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(50, baseY, getWidth() - 100, 20);

        int pegSpacing = (getWidth() - 100) / numberOfPegs;
        for (int i = 0; i < numberOfPegs; i++) {
            int pegX = 50 + pegSpacing/2 + i * pegSpacing;
            g2d.fillRect(pegX - PEG_WIDTH/2, 100, PEG_WIDTH, baseY - 100);
            g2d.setColor(Color.BLACK);
            g2d.drawString(String.valueOf((char)('A' + i)), pegX, baseY + 40);
            g2d.setColor(Color.DARK_GRAY);
        }

        for (int pegIndex = 0; pegIndex < pegs.length; pegIndex++) {
            Stack<Integer> peg = pegs[pegIndex];
            int pegX = 50 + pegSpacing/2 + pegIndex * pegSpacing;
            Stack<Integer> tempStack = new Stack<>();
            Stack<Integer> clonedStack = new Stack<>();
            while (!peg.isEmpty()) {
                int disk = peg.pop();
                tempStack.push(disk);
                clonedStack.push(disk);
            }
            while (!clonedStack.isEmpty()) peg.push(clonedStack.pop());
            int diskY = baseY;
            while (!tempStack.isEmpty()) {
                int diskSize = tempStack.pop();
                diskY -= DISK_HEIGHT;
                int diskWidth = MIN_DISK_WIDTH + (MAX_DISK_WIDTH - MIN_DISK_WIDTH) * diskSize / numberOfDisks;
                float hue = (float) diskSize / numberOfDisks;
                g2d.setColor(Color.getHSBColor(hue, 0.8f, 0.8f));
                g2d.fillRect(pegX - diskWidth/2, diskY, diskWidth, DISK_HEIGHT);
                g2d.setColor(Color.BLACK);
                g2d.drawRect(pegX - diskWidth/2, diskY, diskWidth, DISK_HEIGHT);
            }
        }
    }

    public void moveDisk(char sourcePeg, char destPeg) {
        int sourceIndex = sourcePeg - 'A';
        int destIndex = destPeg - 'A';
        if (pegs[sourceIndex].isEmpty()) return;
        int disk = pegs[sourceIndex].peek();
        if (!pegs[destIndex].isEmpty() && disk > pegs[destIndex].peek()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Invalid move: Cannot place a larger disk on a smaller disk.",
                "Invalid Move", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        pegs[destIndex].push(pegs[sourceIndex].pop());
        repaint();
    }

    public void animateMoves(List<String> moveSequence) {
        new Thread(() -> {
            for (String move : moveSequence) {
                if (move.length() >= 3) {
                    char src = move.charAt(0);
                    char dst = move.charAt(move.length() - 1);
                    javax.swing.SwingUtilities.invokeLater(() -> moveDisk(src, dst));
                    try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            }
        }).start();
    }
}

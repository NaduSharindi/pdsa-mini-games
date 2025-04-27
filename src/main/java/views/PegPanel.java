package views;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import utils.dsa.LinkedList;

public class PegPanel extends JPanel {
    private int diskCount;
    private int numberOfPegs = 3; // Default to 3 pegs
    private int[][] pegs; // pegs[i][j] = disk size on peg i at position j
    private final int PEG_WIDTH = 10;
    private final int DISK_HEIGHT = 20;
    private final int MAX_DISK_WIDTH = 200;
    private final int PEG_HEIGHT = 200;
    private final char[] PEG_LABELS = {'A', 'B', 'C', 'D'}; // Added 'D' for 4-peg version
    
    public PegPanel() {
        setPreferredSize(new Dimension(600, 300));
        diskCount = 0;
        pegs = new int[4][10]; // Support up to 4 pegs, max 10 disks
    }
    
    /**
     * Set the number of disks for the Tower of Hanoi
     * @param count Number of disks (5-10)
     */
    public void setDiskCount(int count) {
        this.diskCount = count;
        resetPegs();
    }
    
    /**
     * Set the number of pegs (3 or 4)
     * @param count Number of pegs
     */
    public void setNumberOfPegs(int count) {
        this.numberOfPegs = (count == 4) ? 4 : 3; // Only allow 3 or 4
        resetPegs();
        repaint();
    }
    
    /**
     * Reset all pegs and place disks on the first peg
     */
    public void resetPegs() {
        // Clear all pegs
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                pegs[i][j] = 0;
            }
        }
        
        // Add disks to first peg
        for (int i = 0; i < diskCount; i++) {
            pegs[0][i] = diskCount - i;
        }
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Calculate peg positions based on number of pegs
        int pegSpacing = width / (numberOfPegs + 1);
        int[] pegX = new int[numberOfPegs];
        for (int i = 0; i < numberOfPegs; i++) {
            pegX[i] = (i + 1) * pegSpacing;
        }
        
        // Draw base
        g2d.setColor(Color.BLACK);
        g2d.fillRect(pegSpacing / 2, height - 50, width - pegSpacing, 10);
        
        // Draw pegs and disks
        for (int i = 0; i < numberOfPegs; i++) {
            // Draw peg
            g2d.setColor(Color.BLACK);
            g2d.fillRect(pegX[i] - PEG_WIDTH / 2, height - 50 - PEG_HEIGHT, PEG_WIDTH, PEG_HEIGHT);
            
            // Draw peg label
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString(String.valueOf(PEG_LABELS[i]), pegX[i] - 5, height - 20);
            
            // Draw disks
            for (int j = 0; j < diskCount; j++) {
                if (pegs[i][j] > 0) {
                    drawDisk(g2d, pegX[i], height - 50 - (j + 1) * DISK_HEIGHT, pegs[i][j]);
                }
            }
        }
    }
    
    /**
     * Draw a disk with size-appropriate width and color
     */
    private void drawDisk(Graphics2D g2d, int x, int y, int size) {
        int diskWidth = size * (MAX_DISK_WIDTH / diskCount) / 2;
        g2d.setColor(getDiskColor(size));
        g2d.fillRoundRect(x - diskWidth, y, diskWidth * 2, DISK_HEIGHT, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x - diskWidth, y, diskWidth * 2, DISK_HEIGHT, 10, 10);
    }
    
    /**
     * Get a color for the disk based on its size
     */
    private Color getDiskColor(int size) {
        float hue = (float) size / diskCount;
        return Color.getHSBColor(hue, 0.8f, 0.9f);
    }
    
    /**
     * Animate a solution sequence
     * @param moves List of moves in format "A-B"
     */
    public void animateSolution(List<String> moves) {
        // Reset pegs before animation
        resetPegs();
        
        Timer timer = new Timer(500, null);
        final int[] moveIndex = {0};
        
        timer.addActionListener(e -> {
            if (moveIndex[0] < moves.size()) {
                String move = moves.get(moveIndex[0]);
                int fromPeg = move.charAt(0) - 'A';
                int toPeg = move.charAt(2) - 'A';
                
                // Check if move is valid for current peg configuration
                if (fromPeg >= 0 && fromPeg < numberOfPegs && toPeg >= 0 && toPeg < numberOfPegs) {
                    // Find top disk in source peg
                    int fromDisk = -1;
                    int fromPos = -1;
                    for (int i = diskCount - 1; i >= 0; i--) {
                        if (pegs[fromPeg][i] > 0) {
                            fromDisk = pegs[fromPeg][i];
                            fromPos = i;
                            break;
                        }
                    }
                    
                    if (fromDisk != -1) {
                        // Remove disk from source peg
                        pegs[fromPeg][fromPos] = 0;
                        
                        // Find first empty position in destination peg
                        int toPos = 0;
                        while (toPos < diskCount && pegs[toPeg][toPos] > 0) {
                            toPos++;
                        }
                        
                        // Add disk to destination peg
                        pegs[toPeg][toPos] = fromDisk;
                    }
                } else {
                    System.out.println("Invalid move: " + move + " for " + numberOfPegs + " pegs");
                }
                
                repaint();
                moveIndex[0]++;
            } else {
                timer.stop();
            }
        });
        
        timer.start();
    }
    
    /**
     * Get the current number of pegs
     */
    public int getNumberOfPegs() {
        return numberOfPegs;
    }
}

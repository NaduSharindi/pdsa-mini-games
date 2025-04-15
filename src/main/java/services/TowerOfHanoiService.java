package services;

import utils.dsa.Stack;
import views.TowerOfHanoiView;
import views.TowerOfHanoiView.PegPanel;

import javax.swing.*;
import java.awt.*;

public class TowerOfHanoiService {

    private Stack<JPanel> pegA;
    private Stack<JPanel> pegB;
    private Stack<JPanel> pegC;
    private int moveCounter = 0;

    public TowerOfHanoiService() {
        pegA = new Stack<>();
        pegB = new Stack<>();
        pegC = new Stack<>();
    }

    public void initializeDiscs(int numberOfDiscs) {
        pegA = new Stack<>();
        pegB = new Stack<>();
        pegC = new Stack<>();

        for (int i = numberOfDiscs; i >= 1; i--) {
            JPanel disc = createDisc(i);
            pegA.push(disc);
        }
    }

    public void resetGame() {
        pegA = new Stack<>();
        pegB = new Stack<>();
        pegC = new Stack<>();
        moveCounter = 0;
    }

    public void solveHanoi(int n, Stack<JPanel> source, Stack<JPanel> target, Stack<JPanel> auxiliary, TowerOfHanoiView view) throws InterruptedException {
        if (n == 1) {
            moveDisc(source, target);
            moveCounter++;
            updateView(view);
            return;
        }

        solveHanoi(n - 1, source, auxiliary, target, view);
        moveDisc(source, target);
        moveCounter++;
        updateView(view);
        solveHanoi(n - 1, auxiliary, target, source, view);
    }

    private void moveDisc(Stack<JPanel> source, Stack<JPanel> target) {
        if (!source.isEmpty()) {
            target.push(source.pop());
        }
    }

    private void updateView(TowerOfHanoiView view) throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            view.renderPegs(pegA, pegB, pegC);
            view.updateMoveCounter(moveCounter);
        });
        Thread.sleep(500); // Slow down for visualization
    }

    private JPanel createDisc(int size) {
        JPanel disc = new JPanel();
        int width = 30 + size * 20;
        int height = 20;
        disc.setPreferredSize(new Dimension(width, height));
        Color color;
        switch (size) {
            case 1:
                color = new Color(204, 255, 204); // Light Green (Smallest)
                break;
            case 2:
                color = new Color(0, 255, 127);   // Spring Green (Medium)
                break;
            case 3:
                color = new Color(221, 160, 221); // Plum (Largest)
                break;
            default:
                color = Color.LIGHT_GRAY;
                break;
        }
        disc.setBackground(color);
        disc.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return disc;
    }

    // Getters
    public Stack<JPanel> getPegA() {
        return pegA;
    }

    public Stack<JPanel> getPegB() {
        return pegB;
    }

    public Stack<JPanel> getPegC() {
        return pegC;
    }
}
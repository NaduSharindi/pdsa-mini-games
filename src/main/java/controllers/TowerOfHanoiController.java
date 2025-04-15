package controllers;

import services.TowerOfHanoiService;
import utils.dsa.Stack;
import views.TowerOfHanoiView;
import views.TowerOfHanoiView.PegPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class TowerOfHanoiController {

    private TowerOfHanoiService service;
    private TowerOfHanoiView view;
    private static final int NUMBER_OF_DISCS = 3; // Define the number of discs

    public TowerOfHanoiController(TowerOfHanoiService service, TowerOfHanoiView view) {
        this.service = service;
        this.view = view;
        initListeners();
    }

    public void showView() {
        this.view.setVisible(true);
    }

    private void initListeners() {

        view.getStartButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.resetGame();
                service.initializeDiscs(NUMBER_OF_DISCS); // Initialize with the correct number
                System.out.println("Peg A after initialization (Start): " + service.getPegA());
                System.out.println("Peg B after initialization (Start): " + service.getPegB());
                System.out.println("Peg C after initialization (Start): " + service.getPegC());
                view.renderPegs(service.getPegA(), service.getPegB(), service.getPegC());
                view.updateMoveCounter(0);
            }
        });

        view.getResetButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.resetGame();
                view.clearPegs();
                view.updateMoveCounter(0);
            }
        });

        view.getAutoSolveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.resetGame();
                service.initializeDiscs(NUMBER_OF_DISCS); // Initialize for auto-solve as well
                view.renderPegs(service.getPegA(), service.getPegB(), service.getPegC());
                view.updateMoveCounter(0);

                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                        solveHanoiVisual(NUMBER_OF_DISCS, service.getPegA(), service.getPegC(), service.getPegB(), view);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            }
        });
    }

    private void solveHanoiVisual(int n, Stack<JPanel> sourceStack, Stack<JPanel> targetStack, Stack<JPanel> auxiliaryStack, TowerOfHanoiView view) throws InterruptedException {
        if (n == 1) {
            moveDiscVisual(sourceStack, targetStack, view);
            return;
        }
        solveHanoiVisual(n - 1, sourceStack, auxiliaryStack, targetStack, view);
        moveDiscVisual(sourceStack, targetStack, view);
        solveHanoiVisual(n - 1, auxiliaryStack, targetStack, sourceStack, view);
    }

    private void moveDiscVisual(Stack<JPanel> sourceStack, Stack<JPanel> targetStack, TowerOfHanoiView view) throws InterruptedException {
        if (!sourceStack.isEmpty()) {
            JPanel disc = sourceStack.pop();
            PegPanel sourcePegPanel = getPegPanelForStack(sourceStack, view);
            PegPanel targetPegPanel = getPegPanelForStack(targetStack, view);

            if (sourcePegPanel != null && targetPegPanel != null) {
                sourcePegPanel.removeDisc();
                targetPegPanel.addDisc(disc);
                view.updateMoveCounter(Integer.parseInt(view.getMoveCounterLabel().getText().split(": ")[1]) + 1);
                Thread.sleep(500); // Delay for visualization
            } else {
                targetStack.push(disc); // If panel not found, just update the stack (shouldn't happen)
            }
            view.repaint();
            view.revalidate();
        }
    }

    private PegPanel getPegPanelForStack(Stack<JPanel> stack, TowerOfHanoiView view) {
        if (stack == service.getPegA()) {
            return view.getPegPanel1();
        } else if (stack == service.getPegB()) {
            return view.getPegPanel2();
        } else if (stack == service.getPegC()) {
            return view.getPegPanel3();
        }
        return null;
    }
}
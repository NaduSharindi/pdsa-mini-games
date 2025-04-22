package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import models.entities.TowerOfHanoiResult;
import services.TowerOfHanoiService;
import views.TowerOfHanoiView;

public class TowerOfHanoiController {
    private final TowerOfHanoiService service;
    private final TowerOfHanoiView view;
    private JFrame frame;

    public TowerOfHanoiController(TowerOfHanoiService service, TowerOfHanoiView view) {
        this.service = service;
        this.view = view;
        
        // Setup frame for view
        frame = new JFrame("Tower of Hanoi");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 650);
        frame.add(view);
        frame.setLocationRelativeTo(null);
        
        
        // Initialize listeners
        this.view.addNewGameListener(new NewGameListener());
        this.view.addCheckAnswerListener(new CheckAnswerListener());
        this.view.addAutoSolveListener(new AutoSolveListener());
        this.view.addBackListener(e -> hideView());
        
        // Add peg selection listener to update the optimal move count when peg count changes
        this.view.addPegSelectionListener(e -> {
            int pegCount = view.getSelectedPegCount();
            service.setPegCount(pegCount);
            newGame();
        });
        
        // Initialize the game
        newGame();
        
    }

    public void showView() {
        frame.setVisible(true);
    }
    
    public void hideView() {
        frame.setVisible(false);
    }

    private void newGame() {
    	 service.setPegCount(view.getSelectedPegCount());
        service.generateNewPuzzle();
        int diskCount = service.getCurrentDiskCount();
        view.setDiskCount(diskCount);
        view.resetGame();
        //updateOptimalMoveCount();
        
        // Show algorithm based on peg count
        int pegCount = service.getPegCount();
        if (pegCount == 4) {
            view.showAlgorithmMessage("Frame-Stewart");
        } else {
            view.showAlgorithmMessage(service.getCurrent3PegAlgorithm());
        }
    }
    
    /**
     * Updates the optimal move count display based on current peg selection
     */
    /*private void updateOptimalMoveCount() {
        int pegCount = view.getSelectedPegCount();
        service.setPegCount(pegCount);
        int moveCount = service.getOptimalMoveCount(pegCount);
        view.setOptimalMoveCount(moveCount);
    }*/
    
    //new game listener
    
    class NewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            newGame();
        }
    }
    
    
    
    //checkAnswer button
    class CheckAnswerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Check Answer Clicked!");
            String playerName = view.getPlayerName();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please enter your name");
                return;
            }
            
            int moveCount;
            try {
                moveCount = view.getMoveCount();
                if (moveCount <= 0) {
                    JOptionPane.showMessageDialog(view, "Please enter a valid move count");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Please enter a valid move count");
                return;
            }
            
            String moveSequence = view.getMoveSequence();
            if (moveSequence.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please enter your move sequence");
                return;
            }
            
            int pegCount = view.getSelectedPegCount();
            boolean isCorrect = service.checkAnswer(playerName, moveCount, moveSequence, pegCount);
            
            if (isCorrect) {
                TowerOfHanoiResult lastResult = service.getLastResult();
                String algoName = (pegCount == 4) ? "Frame-Stewart" : 
                                service.getCurrent3PegAlgorithm();
                long time = (pegCount == 4) ? lastResult.getFrameStewartTime() :
                                          lastResult.getRecursiveTime();
                
                if (lastResult.isOptimal()) {
                    view.showSuccess(String.format(
                        "Optimal answer! %s algorithm took %.3f ms",
                        algoName, time/1_000_000.0
                    ));
                } else {
                    view.showSuccess(String.format(
                        "Correct but non-optimal answer! %s algorithm took %.3f ms (Optimal: %d moves)",
                        algoName, time/1_000_000.0, service.getOptimalMoveCount(pegCount)
                    ));
                }
                view.animateSolution(service.getOptimalMoves(pegCount));
            } else {
                view.showError("Sorry, your answer is incorrect. Try again.");
            }
        }
    }
    
    // Auto solve button
    class AutoSolveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int pegCount = view.getSelectedPegCount();
            List<String> solutionMoves = service.getOptimalMoves(pegCount);
            
            // Format the moves as a comma-separated string
            StringBuilder formattedMoves = new StringBuilder();
            for (int i = 0; i < solutionMoves.size(); i++) {
                formattedMoves.append(solutionMoves.get(i));
                if (i < solutionMoves.size() - 1) {
                    formattedMoves.append(", ");
                }
            }
            
            // Set the move sequence in the text area
            view.setMoveSequence(formattedMoves.toString());
            
            // Set the optimal move count
            view.setMoveCount(service.getOptimalMoveCount(pegCount));
            
            // Animate the solution
            view.animateSolution(solutionMoves);
            
            TowerOfHanoiResult lastResult = service.getLastResult();
            if (pegCount == 4) {
                view.showAlgoSessionMsg("Frame-Stewart", lastResult.getFrameStewartTime());
            } else {
                view.showAlgoSessionMsg(service.getCurrent3PegAlgorithm(), lastResult.getRecursiveTime());
            }
            
            // Show success message with algorithm info
            String algorithmInfo = pegCount == 3 ? 
                "using recursive/iterative algorithms" : 
                "using Frame-Stewart algorithm";
            view.showSuccess("Auto-solved with " + service.getOptimalMoveCount(pegCount) + 
                             " moves " + algorithmInfo);
        }
    }
    
    public List<TowerOfHanoiResult> getAllResults() {
        return service.getAllResults();
    }
}

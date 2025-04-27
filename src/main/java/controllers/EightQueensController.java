package controllers;

import services.EightQueensService;
import views.EightQueensView;
import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class EightQueensController {
	
    private EightQueensView view;
    private EightQueensService service;
    private JFrame frame;
    
    private long sequentialTime;
    private long threadedTime;

    public EightQueensController(EightQueensView view, EightQueensService service) {
        this.view = view;
        this.service = service;
        sequentialTime =  service.findAllSolutionsSequential();
        threadedTime = service.findAllSolutionsThreaded();
        initListeners();
        updateSolutionsFound();
    }
    
    public void showView() {
        if (frame == null) {
            frame = new JFrame("Eight Queens Puzzle");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(view); 
            frame.setPreferredSize(new Dimension(800, 600));
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true); 
    }

    public void hideView() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose(); 
            frame = null;  
        }
    }
    
    private void initListeners() {
        // Board button listener
    	
    	view.setBoardButtonListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			JButton btn = (JButton) e.getSource();
    			for(int row = 0; row < 8; row++) {
    				for(int column = 0; column < 8; column++) {
    					if(view.getBoardButtons()[row][column] == btn) {
    						view.toggleQueen(row, column);
    						return;
    					}
    				}
    			}
    		}
    	});
    	
    	/*
    	 * Submit button listener
    	 */
    	
    	view.setSubmitButtonListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			processSubmit();
    		}
    	});
    	
    	/*
    	 * Reset button listener
    	 */
    	
    	view.setResetButtonListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			view.resetBoard();
    			view.setFeedback("Board reset. Try again!");
    		}
    	});
    	
    	view.setBackToMenuButtonListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        
    	        hideView(); 
    	        
    	    }
    	});

    }
    
    private void processSubmit() {
        String playerName = view.getPlayerName().trim();

        // Validate name field (empty)
        if (playerName.isEmpty()) {
            view.setFeedback("Please enter your name.", true);
            return;
        }

        // Validate name format (3-20 alphanumeric with optional ._- in middle)
        String nameRegex = "^[a-zA-Z0-9]([._-]?[a-zA-Z0-9]){2,19}$";
        if (!playerName.matches(nameRegex)) {
            view.setFeedback("Invalid name. Use 3-20 letters/numbers, may include . _ - (no special chars at start/end)", true);
            return;
        }

        // Validate all 8 queens placed
        int[] positions = view.getBoardState();
        for (int pos : positions) {
            if (pos == -1) {
                view.setFeedback("Place one queen in each column!", true);
                return;
            }
        }

        try {
            // Check solution validity
            if (!service.validatePlayerSolution(positions)) {
                view.setFeedback("Incorrect solution. Try again!", true);
            } 
            //Check for duplicate recognition
            else if (service.isSolutionRecognized(positions)) {
                view.setFeedback("Solution already claimed! Try another configuration.", true);
            } 
            // Save valid new solution
            else {
                service.savePlayerSolution(positions, playerName);
                view.setFeedback("Congratulations " + playerName + "! Solution recorded.", false);
                
                //  Update counters
                updateSolutionsFound();
                

                // Reset UI
                view.resetBoard();
                view.clearPlayerNameField();
            }
        } catch (RuntimeException ex) {
            view.setFeedback("Database Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    
    private void updateSolutionsFound() {
        int found = service.getSolutionCount();
        int total = 92; 
        int remaining = total - found;
        view.setSolutionsFound(found);
        view.setRemainingSolutions(remaining);
    
        view.setSequentialTimeLabel(sequentialTime); 
        view.setThreadedTimeLabel(threadedTime);
        if (found == 92) {
            service.resetAllRecognizedSolutions();
            view.setFeedback("All 92 solutions found! Flags reset for new players.", false);
        }
    }
    

}

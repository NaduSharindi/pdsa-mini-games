package controllers;

import services.EightQueensService;
import views.EightQueensView;
import models.exceptions.DatabaseException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class EightQueensController {
	
    private EightQueensView view;
    private EightQueensService service;
    private JFrame frame;

    public EightQueensController(EightQueensView view, EightQueensService service) {
        this.view = view;
        this.service = service;
        service.findAllSolutionsSequential();
        initListeners();
        updateSolutionsFound();
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
    }
    
    private void processSubmit() {
    	String playerName = view.getPlayerName();
    	if(playerName.isEmpty()) {
    		view.setFeedback("Please enter your name");
    		return;
    	}
    	
    	int[] positions = view.getBoardState();
    	for(int pos : positions) {
    		if(pos == -1) {
    			view.setFeedback("Please 1 queen each column");
    			return;
    		}
    	}
    	
    	try {
    		if(!service.validatePlayerSolution(positions)) {
    			view.setFeedback("Incorrect solution. Try again!");
    		} else if(service.isSolutionRecognized(positions)) {
    			view.setFeedback("This solution has already been recognized. Try new solution!");
    		} else {
    			long timeTaken = 0;
    			
    			service.savePlayerSolution(positions, playerName, timeTaken);
    			view.setFeedback("Congratulations! Your solution is correct and it is recorded.");
    			updateSolutionsFound();
    			view.resetBoard();
    		}
    	} catch(RuntimeException ex) {
    		view.setFeedback("Database error: " + ex.getMessage());
    	}
    }
    
    private void updateSolutionsFound() {
    	int count = service.getSolutionCount();
    	view.setSolutionsFound(count);
    }
    
    public void showView() {
        if (frame == null) {
            frame = new JFrame("Eight Queens Puzzle");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(view); // Add the view to the frame
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true); // Show the frame
    }

    public void hideView() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose(); // Optional: Clean up resources
            frame = null; // Reset for next use
        }
    }

}

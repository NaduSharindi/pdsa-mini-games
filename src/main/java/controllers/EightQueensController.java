package controllers;

import services.EightQueensService;
import views.EightQueensView;
import models.exceptions.DatabaseException;
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
        initListeners();
    }
    
    private void initListeners() {
        // Add your event listeners here
    }
    
    public void showView() {
        frame.setVisible(true);
    }
    
    public void hideView() {
        frame.setVisible(false);
    }

}

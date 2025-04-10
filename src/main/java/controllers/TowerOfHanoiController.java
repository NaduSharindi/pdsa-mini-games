package controllers;

import services.TowerOfHanoiService;
import views.TowerOfHanoiView;



public class TowerOfHanoiController {
	
	private TowerOfHanoiService service;
	private TowerOfHanoiView view;
	
	/**
	 * Constructor method for traveling sales man game
	 * 
	 * @param service
	 * @param view
	 */
	public TowerOfHanoiController(TowerOfHanoiService service, TowerOfHanoiView view) {
		// TODO Auto-generated constructor stub
		this.service = service;
		this.view = view;
	}
	
	/**
	 * UI method for show view
	 */
	public void showView() {
		this.view.setVisible(true);
	}

}
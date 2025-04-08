package controllers;

import services.TravelingSalesManService;
import views.TravelingSalesManView;

public class TravelingSalesManController {
	private TravelingSalesManService service;
	private TravelingSalesManView view;
	
	/**
	 * Constructor method for traveling sales man game
	 * 
	 * @param service
	 * @param view
	 */
	public TravelingSalesManController(TravelingSalesManService service, TravelingSalesManView view) {
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

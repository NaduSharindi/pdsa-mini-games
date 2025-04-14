package controllers;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import services.TravelingSalesManService;
import utils.constants.TravelingSalesManConstants;
import utils.dsa.graph.Edge;
import utils.dsa.graph.Graph;
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
		this.view.initUI();
		this.view.setVisible(true);
		this.initNewRound();
	}

	/**
	 * Initialize new game round
	 */
	public void initNewRound() {
		// set action listeners for buttons
		clearWindow();

		// weight data for new game round
		Graph<String> newRoundData = this.service.initNewRound();

		// add graph weights to JLabels
		for (int i = 0; i < TravelingSalesManConstants.NO_OF_SOURCE_CITIES; i++) {
			List<Edge<String>> edgesList = newRoundData.getNeighbors("" + (char) (i + 65));
			for (int j = 0; j < TravelingSalesManConstants.NO_OF_DESTINATION_CITIES; j++) {
				for (Edge<String> edge : edgesList) {
					if (("" + (char) (j + 65)).equals(edge.getDestination())) {
						this.view.getWeightsLbl()[i][j].setText(String.valueOf(edge.getWeight()));
						break;
					}
				}
			}
		}

		// button listeners
		initializeBtnsListeners();
	}

	/**
	 * Initialize buttons listeners
	 */
	public void initializeBtnsListeners() {
		for (JToggleButton btn : this.view.getSourceCitiesBtn()) {
			btn.addActionListener(event -> {
				sourceBtnAction(btn);
			});
		}

		for (JToggleButton btn : this.view.getDestinationCitiesBtn()) {
			btn.addActionListener(event -> {
				destinationBtnAction(btn);
			});
		}
	}

	/**
	 * source buttons actions
	 * 
	 * @param btn
	 */
	private void sourceBtnAction(JToggleButton btn) {
		//check any destination button is selected before source city is selected
		for (JToggleButton currentBtn : this.view.getDestinationCitiesBtn()) {
			if (currentBtn.isSelected()) {
				btn.setSelected(false);
				JOptionPane.showMessageDialog(view, "can not change source location. destination is selected", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		//source btn action
		btn.setSelected(true);
		deSelectToggles(btn, this.view.getSourceCitiesBtn());

		// set source cities
		this.view.getUserSourceTxtBx().setText(btn.getText());
		this.view.getCalcSourceTxtBx().setText(btn.getText());
	}

	/**
	 * destination button actions
	 * 
	 * @param btn
	 */
	private void destinationBtnAction(JToggleButton btn) {
		// check user is select source city before selecting destination city
		if (this.view.getUserSourceTxtBx().getText().isEmpty()) {
			JOptionPane.showMessageDialog(view, "select a source city first", "Error", JOptionPane.ERROR_MESSAGE);
			btn.setSelected(false);
			return;
		}
		// check destination buttons are de-selecting or not
		if (!btn.isSelected()) {
			btn.setSelected(true);
			JOptionPane.showMessageDialog(view, btn.getText() + " is already selected", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// get the user selected distance
		String userSelectedPath = this.view.getUserSelectedPathTxtBx().getText();

		// set user selected path and calculate distance
		if (userSelectedPath.isEmpty()) {
			String sourceVertex = this.view.getUserSourceTxtBx().getText()
					.substring(this.view.getUserSourceTxtBx().getText().length() - 1);
			String destinationVertex = btn.getName();

			// check if source has edge to destination and source and destination are similar
			Double edgeWeight = this.service.getWeight(sourceVertex, destinationVertex);
			if (edgeWeight == null) {
				btn.setSelected(false);
				JOptionPane.showMessageDialog(view,
						"City " + sourceVertex + " to City " + destinationVertex + " has no path to travel", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else if (edgeWeight == 0) {
				btn.setSelected(false);
				JOptionPane.showMessageDialog(view,
						"City " + sourceVertex + " and City " + destinationVertex + " are similar", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.view.getUserSelectedPathTxtBx().setText(sourceVertex + "-" + destinationVertex + ",\n");
			
			//calculate user selected path distance
			
		} else {
			String[] pairs = userSelectedPath.split(",\n");
			String sourceVertex = pairs[pairs.length - 1].split("-")[1];
			String destinationVertex = btn.getName();

			// check if source has edge to destination and source and destination are similar
			Double edgeWeight = this.service.getWeight(sourceVertex, destinationVertex);
			if (edgeWeight == null) {
				btn.setSelected(false);
				JOptionPane.showMessageDialog(view,
						"City " + sourceVertex + " to City " + destinationVertex + " has no path to travel", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else if (edgeWeight == 0) {
				btn.setSelected(false);
				JOptionPane.showMessageDialog(view,
						"City " + sourceVertex + " and City " + destinationVertex + " are similar", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.view.getUserSelectedPathTxtBx().setText(
					this.view.getUserSelectedPathTxtBx().getText() + sourceVertex + "-" + destinationVertex + ",\n");
			
			//calculate user selected path distance
		}

	}

	/**
	 * clear game window
	 */
	private void clearWindow() {
		// clear source buttons
		for (JToggleButton btn : this.view.getSourceCitiesBtn()) {
			btn.setSelected(false);
			for (ActionListener al : btn.getActionListeners()) {
				btn.removeActionListener(al);
			}
		}

		// clear destination buttons
		for (JToggleButton btn : this.view.getDestinationCitiesBtn()) {
			btn.setSelected(false);
			for (ActionListener al : btn.getActionListeners()) {
				btn.removeActionListener(al);
			}
		}

		// clear control panel
		this.view.getUserSourceTxtBx().setText("");
		this.view.getUserDistanceTxtBx().setText("");
		this.view.getUserSelectedPathTxtBx().setText("");
		this.view.getCalcSourceTxtBx().setText("");
		this.view.getCalcDistanceTxtBx().setText("");
		this.view.getCalcSelectedPathTxtBx().setText("");
	}

	/**
	 * de-select game window buttons
	 */
	private void deSelectToggles(JToggleButton btn, JToggleButton[] btnArray) {
		for (JToggleButton currentBtn : btnArray) {
			if (!currentBtn.getName().equals(btn.getName())) {
				currentBtn.setSelected(false);
			}
		}
	}
}

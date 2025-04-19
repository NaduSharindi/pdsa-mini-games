package controllers;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import models.exceptions.DatabaseException;
import net.bytebuddy.asm.Advice.This;
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
		// source toggle button listeners
		for (JToggleButton btn : this.view.getSourceCitiesBtn()) {
			btn.addActionListener(event -> {
				sourceBtnAction(btn);
			});
		}

		// destination toggle button listeners
		for (JToggleButton btn : this.view.getDestinationCitiesBtn()) {
			btn.addActionListener(event -> {
				destinationBtnAction(btn);
			});
		}

		// find shortest path button listener
		this.view.getShortestPathBtn().addActionListener(event -> {
			findShortestPathBtnAction(this.view.getShortestPathBtn());
		});
	}

	/**
	 * source buttons actions
	 * 
	 * @param btn
	 */
	private void sourceBtnAction(JToggleButton btn) {
		// check any destination button is selected before source city is selected
		for (JToggleButton currentBtn : this.view.getDestinationCitiesBtn()) {
			if (currentBtn.isSelected()) {
				btn.setSelected(false);
				JOptionPane.showMessageDialog(view, "can not change source location. destination is selected", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		// source btn action
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

		// check user selected manually home city
		if (this.view.getUserSourceTxtBx().getText().substring(this.view.getUserSourceTxtBx().getText().length() - 1)
				.equals(btn.getName())) {
			btn.setSelected(false);
			JOptionPane.showMessageDialog(view, "game will automatically select home return path", "Error",
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

			// check if source has edge to destination and source and destination are
			// similar
			Double edgeWeight = this.service.getWeight(sourceVertex, destinationVertex);
			Double edgeReturnWeight = this.service.getWeight(destinationVertex, sourceVertex);

			if (edgeWeight == null || edgeReturnWeight == null) {
				btn.setSelected(false);
				JOptionPane.showMessageDialog(view,
						"City " + sourceVertex + " to City " + destinationVertex + " has no path to travel", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			} else if (edgeWeight == 0 || edgeReturnWeight == 0) {
				btn.setSelected(false);
				JOptionPane.showMessageDialog(view,
						"City " + sourceVertex + " and City " + destinationVertex + " are similar", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// set user selected path
			this.view.getUserSelectedPathTxtBx().setText(
					sourceVertex + "-" + destinationVertex + ",\n" + destinationVertex + "-" + sourceVertex + ",\n");

			// calculate user selected path distance
			if (this.view.getUserDistanceTxtBx().getText().isEmpty()) {
				this.view.getUserDistanceTxtBx().setText(String.valueOf(edgeWeight + edgeReturnWeight));
			} else {
				try {
					this.view.getUserDistanceTxtBx()
							.setText(String.valueOf(Double.parseDouble(this.view.getUserDistanceTxtBx().getText())
									+ edgeWeight + edgeReturnWeight));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			String[] pairs = userSelectedPath.split(",\n");
			String sourceVertex = pairs[pairs.length - 1].split("-")[0];
			String destinationVertex = btn.getName();

			// check if source has edge to destination and source and destination are
			// similar
			Double edgeWeight = this.service.getWeight(sourceVertex, destinationVertex);
			Double edgeReturnWeight = this.service.getWeight(destinationVertex, pairs[pairs.length - 1].split("-")[1]);
			Double oldReturnWeight = this.service.getWeight(sourceVertex, pairs[pairs.length - 1].split("-")[1]);
			if (edgeWeight == null || edgeReturnWeight == null || oldReturnWeight == null) {
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
			// set selected user path
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < pairs.length; i++) {
				if (i == pairs.length - 1) {
					sb.append(sourceVertex + "-" + destinationVertex + ",\n");
					sb.append(destinationVertex + "-" + pairs[pairs.length - 1].split("-")[1] + ",\n");
					break;
				}
				sb.append(pairs[i] + ",\n");
			}

			this.view.getUserSelectedPathTxtBx().setText(sb.toString());

			// calculate user selected path distance
			if (this.view.getUserDistanceTxtBx().getText().isEmpty()) {
				this.view.getUserDistanceTxtBx()
						.setText(String.valueOf(edgeWeight + edgeReturnWeight - oldReturnWeight));
			} else {
				try {
					this.view.getUserDistanceTxtBx()
							.setText(String.valueOf(Double.parseDouble(this.view.getUserDistanceTxtBx().getText())
									+ edgeWeight + edgeReturnWeight - oldReturnWeight));
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}

	}

	/**
	 * find shortest path button actions
	 * 
	 * @param btn
	 */
	private void findShortestPathBtnAction(JButton btn) {
		// check user is select any source city and destination cities
		if (this.view.getUserSelectedPathTxtBx().getText().isEmpty()) {
			JOptionPane.showMessageDialog(view, "select destination cities", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// show JOPtionPane algorithm to select algorithm through the user
		String[] choices = { "Brute Force Algorithm", "Held Karp Algorithm", "Genetic Algorithm" };
		JComboBox<String> combobox = new JComboBox<String>(choices);

		if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(view, combobox, "Select an algorithm",
				JOptionPane.OK_CANCEL_OPTION)) {
			switch ((String) combobox.getSelectedItem()) {
			case "Brute Force Algorithm":
				useBruteForceAlgorithm();
				break;
			case "Held Karp Algorithm":
				useHeldKarpAlgorithm();
				break;
			case "Genetic Algorithm":
				useGeneticAlgorithm();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * call BruteForceAlgorithm
	 */
	private void useBruteForceAlgorithm() {
		try {
			// calculate time taken for algorithm
			long start = System.currentTimeMillis();
			// run algorithm
			this.service.useBruteForceAlgorithm(this.getSourceVertex(), this.getUserSelectedVertices());
			long end = System.currentTimeMillis();

			long timeTaken = end - start;

			// set selected path and distance
			this.view.getCalcSelectedPathTxtBx().setText(this.service.getCalculatedPath());
			this.view.getCalcDistanceTxtBx().setText(String.valueOf(this.service.getCalculatedDistance()));

			if (Double.parseDouble(this.view.getUserDistanceTxtBx().getText()) <= this.service
					.getCalculatedDistance()) {
				JOptionPane.showMessageDialog(view, "You win", "Win", JOptionPane.INFORMATION_MESSAGE);
				String playerName = JOptionPane.showInputDialog(view, "Enter your name to save result", "Save Result",
						JOptionPane.INFORMATION_MESSAGE);
				if (!playerName.isBlank()) {
					try {
						this.service.saveResult("BruteForce Algorithm", timeTaken, playerName,
								this.view.getCalcSelectedPathTxtBx().getText());
					} catch (DatabaseException e) {
						JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				this.showView();

			} else {
				JOptionPane.showMessageDialog(view, "Computer win", "Win", JOptionPane.INFORMATION_MESSAGE);
				this.showView();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * call HeldKarp algorithm
	 */
	private void useHeldKarpAlgorithm() {
		try {
			// calculate time taken for algorithm
			long start = System.currentTimeMillis();
			// run algorithm
			List<String> userSelectedVertices = this.getUserSelectedVertices();
			userSelectedVertices.add(0, this.getSourceVertex());
			this.service.useHeldKarpAlgorithm(userSelectedVertices);
			long end = System.currentTimeMillis();

			long timeTaken = end - start;

			// set selected path and distance
			this.view.getCalcSelectedPathTxtBx().setText(this.service.getCalculatedPath());
			this.view.getCalcDistanceTxtBx().setText(String.valueOf(this.service.getCalculatedDistance()));

			if (Double.parseDouble(this.view.getUserDistanceTxtBx().getText()) <= this.service
					.getCalculatedDistance()) {
				JOptionPane.showMessageDialog(view, "You win", "Win", JOptionPane.INFORMATION_MESSAGE);
				String playerName = JOptionPane.showInputDialog(view, "Enter your name to save result", "Save Result",
						JOptionPane.INFORMATION_MESSAGE);
				if (!playerName.isBlank()) {
					try {
						this.service.saveResult("Held Karp Algorithm", timeTaken, playerName,
								this.view.getCalcSelectedPathTxtBx().getText());
					} catch (DatabaseException e) {
						JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				this.showView();

			} else {
				JOptionPane.showMessageDialog(view, "Computer win", "Win", JOptionPane.INFORMATION_MESSAGE);
				this.showView();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * call genetic algorithm
	 */
	private void useGeneticAlgorithm() {
		try {
			//calculate the time taken for algorithm
			long start = System.currentTimeMillis();
			//run algorithm
			this.service.useGeneticAlgorithm(this.getSourceVertex(), this.getUserSelectedVertices());
			long end = System.currentTimeMillis();
	
			long timeTaken = end - start;
			
			//set the selected path and distance
			this.view.getCalcSelectedPathTxtBx().setText(this.service.getCalculatedPath());
			this.view.getCalcDistanceTxtBx().setText(String.valueOf(this.service.getCalculatedDistance()));
			
			if (Double.parseDouble(this.view.getUserDistanceTxtBx().getText()) <= this.service
					.getCalculatedDistance()) {
				JOptionPane.showMessageDialog(view, "You win", "Win", JOptionPane.INFORMATION_MESSAGE);
				String playerName = JOptionPane.showInputDialog(view, "Enter your name to save result", "Save Result",
						JOptionPane.INFORMATION_MESSAGE);
				if (!playerName.isBlank()) {
					try {
						this.service.saveResult("Held Karp Algorithm", timeTaken, playerName,
								this.view.getCalcSelectedPathTxtBx().getText());
					} catch (DatabaseException e) {
						JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				this.showView();
			} else {
				JOptionPane.showMessageDialog(view, "Computer win", "Win", JOptionPane.INFORMATION_MESSAGE);
				this.showView();
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Get user selected source vertex
	 * 
	 * @return
	 */
	private String getSourceVertex() {
		return this.view.getCalcSourceTxtBx().getText()
				.substring(this.view.getCalcSourceTxtBx().getText().length() - 1);
	}

	/**
	 * Get user selected vertices
	 */
	private List<String> getUserSelectedVertices() {
		List<String> selectedVertices = new ArrayList<String>();
		String[] pairs = this.view.getUserSelectedPathTxtBx().getText().split(",\n");
		for (String currentPair : pairs) {
			if (currentPair.equals(pairs[pairs.length - 1])) {
				break;
			}
			selectedVertices.add(currentPair.split("-")[1]);
		}
		return selectedVertices;
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

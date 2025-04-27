package controllers;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import models.exceptions.DatabaseException;
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
		try {
			// clear game window first
			this.clearWindow();

			// weight data for new game round
			Graph<String> newRoundData = this.service.initNewRound();

			// add graph weights to JLabels
			for (int i = 0; i < TravelingSalesManConstants.NO_OF_SOURCE_CITIES; i++) {
				List<Edge<String>> edgesList = newRoundData.getNeighbors(String.valueOf((char) (i + 65)));
				if (edgesList == null)
					throw new NoSuchElementException("source vertex is not found in graph");

				for (int j = 0; j < TravelingSalesManConstants.NO_OF_DESTINATION_CITIES; j++) {
					for (Edge<String> edge : edgesList) {
						if ((String.valueOf((char) (j + 65))).equals(edge.getDestination())) {
							this.view.getWeightsLbl()[i][j].setText(String.valueOf(edge.getWeight()));
							break;
						}
					}
				}
			}

			// button listeners
			this.initializeBtnsListeners();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Initialize buttons listeners
	 */
	public void initializeBtnsListeners() {
		// source toggle button listeners
		for (JToggleButton btn : this.view.getSourceCitiesBtn()) {
			btn.addActionListener(event -> {
				this.sourceBtnAction(btn);
			});
		}

		// destination toggle button listeners
		for (JToggleButton btn : this.view.getDestinationCitiesBtn()) {
			btn.addActionListener(event -> {
				this.destinationBtnAction(btn);
			});
		}

		// find shortest path button listener
		this.view.getShortestPathBtn().addActionListener(event -> {
			this.findShortestPathBtnAction(this.view.getShortestPathBtn());
		});

		// new game button listener
		this.view.getNewGameBtn().addActionListener(event -> {
			this.showView();
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
		this.deSelectToggles(btn, this.view.getSourceCitiesBtn());

		// set selected source city to text boxes
		this.view.getUserSourceTxtBx().setText(btn.getText());
		this.view.getCalcSourceTxtBx().setText(btn.getText());
	}

	/**
	 * destination button actions
	 * 
	 * @param btn
	 */
	private void destinationBtnAction(JToggleButton btn) {
		try {
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
			if (this.getSourceVertex().equals(btn.getName())) {
				btn.setSelected(false);
				JOptionPane.showMessageDialog(view, "game will automatically select home return path", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// get the user selected distance
			String userSelectedPath = this.view.getUserSelectedPathTxtBx().getText();

			// set user selected path and calculate distance
			if (userSelectedPath.isEmpty()) {
				String sourceVertex = this.getSourceVertex();
				String destinationVertex = btn.getName();

				// check if source has edge to destination and source and destination are
				// similar
				Double edgeWeight = this.service.getWeight(sourceVertex, destinationVertex);
				Double edgeReturnWeight = this.service.getWeight(destinationVertex, sourceVertex);

				// set user selected path
				this.view.getUserSelectedPathTxtBx().setText(sourceVertex + "-" + destinationVertex + ",\n"
						+ destinationVertex + "-" + sourceVertex + ",\n");

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
				Double edgeReturnWeight = this.service.getWeight(destinationVertex,
						pairs[pairs.length - 1].split("-")[1]);
				Double oldReturnWeight = this.service.getWeight(sourceVertex, pairs[pairs.length - 1].split("-")[1]);

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

				// set the selected user path
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
		} catch (Exception e) {
			btn.setSelected(false);
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(view, "user is not selected a path", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// show JOPtionPane algorithm to select algorithm through the user
		String[] choices = { "Brute Force Algorithm", "Held Karp Algorithm", "Genetic Algorithm" };
		JComboBox<String> combobox = new JComboBox<String>(choices);

		// according to user selected algorithm find shortest path and find minimum
		// distance
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
				JOptionPane.showMessageDialog(view, "select algorithm to find a shortest path", "Alert",
						JOptionPane.WARNING_MESSAGE);
				break;
			}
		}
	}

	/**
	 * call BruteForceAlgorithm
	 */
	private void useBruteForceAlgorithm() {
		try {
			// start time
			long start = System.nanoTime();
			// run algorithm
			this.service.useBruteForceAlgorithm(this.getSourceVertex(), this.getUserSelectedVertices());
			// end time
			long end = System.nanoTime();

			// calculate time taken for algorithm
			long timeTaken = end - start;

			// set selected path and distance
			this.view.getCalcSelectedPathTxtBx().setText(this.service.getCalculatedPath());
			this.view.getCalcDistanceTxtBx().setText(String.valueOf(this.service.getCalculatedDistance()));
			this.showResult("BruteForce Algorithm", timeTaken);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * call HeldKarp algorithm
	 */
	private void useHeldKarpAlgorithm() {
		try {
			// start time
			long start = System.nanoTime();

			// run algorithm
			List<String> userSelectedVertices = this.getUserSelectedVertices();
			userSelectedVertices.add(0, this.getSourceVertex());
			this.service.useHeldKarpAlgorithm(userSelectedVertices);

			// calculate time taken for algorithm
			long end = System.nanoTime();
			long timeTaken = end - start;

			// set selected path and distance
			this.view.getCalcSelectedPathTxtBx().setText(this.service.getCalculatedPath());
			this.view.getCalcDistanceTxtBx().setText(String.valueOf(this.service.getCalculatedDistance()));
			this.showResult("Held Karp Algorithm", timeTaken);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * call genetic algorithm
	 */
	private void useGeneticAlgorithm() {
		try {
			// start time
			long start = System.nanoTime();
			// run algorithm
			this.service.useGeneticAlgorithm(this.getSourceVertex(), this.getUserSelectedVertices());
			// end time
			long end = System.nanoTime();

			// calculate the time taken for algorithm
			long timeTaken = end - start;

			// set the selected path and distance
			this.view.getCalcSelectedPathTxtBx().setText(this.service.getCalculatedPath());
			this.view.getCalcDistanceTxtBx().setText(String.valueOf(this.service.getCalculatedDistance()));
			this.showResult("Genetic Algorithm", timeTaken);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Show final result for each 3 different algorithm
	 * 
	 * @param algorithm
	 * @param timeTaken
	 */
	private void showResult(String algorithm, long timeTaken) {
		if (Double.parseDouble(this.view.getUserDistanceTxtBx().getText()) <= this.service.getCalculatedDistance()) {
			JOptionPane.showMessageDialog(view, "You win", "Win", JOptionPane.INFORMATION_MESSAGE);
			String playerName = JOptionPane.showInputDialog(view, "Enter your name to save result", "Save Result",
					JOptionPane.INFORMATION_MESSAGE);
			if (playerName != null && !playerName.isBlank()) {
				try {
					this.service.saveResult(algorithm, timeTaken, playerName,
							this.view.getCalcSelectedPathTxtBx().getText());
				} catch (DatabaseException e) {
					JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(view, "Computer win", "Win", JOptionPane.INFORMATION_MESSAGE);
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
		// remove action listeners, selections from source buttons
		for (JToggleButton btn : this.view.getSourceCitiesBtn()) {
			btn.setSelected(false);
			for (ActionListener al : btn.getActionListeners()) {
				btn.removeActionListener(al);
			}
		}

		// remove action listeners, selections from destination buttons
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
	 * de-select game window selected buttons except the given button
	 */
	private void deSelectToggles(JToggleButton btn, JToggleButton[] btnArray) {
		for (JToggleButton currentBtn : btnArray) {
			if (!currentBtn.getName().equals(btn.getName())) {
				currentBtn.setSelected(false);
			}
		}
	}
}

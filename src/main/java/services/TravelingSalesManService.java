package services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import models.entities.TravelingSalesManResult;
import models.exceptions.DatabaseException;
import utils.DatabaseConnection;
import utils.constants.TravelingSalesManConstants;
import utils.dsa.bruteforce.BruteForce;
import utils.dsa.genetic.Genetic;
import utils.dsa.graph.Edge;
import utils.dsa.graph.Graph;
import utils.dsa.heldkarp.HeldKarp;

public class TravelingSalesManService {
	private Graph<String> graphObj;
	private String calculatedPath;
	private Double calculatedDistance;

	/**
	 * Initialize new service object
	 */
	public TravelingSalesManService() {
		graphObj = new Graph<String>();
		calculatedPath = "";
		calculatedDistance = -1d;
	}

	/**
	 * Initialize new game round
	 */
	public Graph<String> initNewRound() {
		graphObj = new Graph<String>();
		Random rand = new Random();

		// initialize graph with sources, destinations and random weights
		for (int i = 0; i < TravelingSalesManConstants.NO_OF_SOURCE_CITIES; i++) {
			for (int j = 0; j < TravelingSalesManConstants.NO_OF_DESTINATION_CITIES; j++) {
				String sourceCity = String.valueOf((char) (i + 65));
				String destinationCity = String.valueOf((char) (j + 65));
				if (i > j) {
					graphObj.addEdge(sourceCity, destinationCity, rand.nextInt(51) + 50, false);
				} else if (i == j) {
					graphObj.addEdge(sourceCity, destinationCity, 0, true);
				}
			}
		}
		return graphObj;
	}

	/**
	 * Get current graph object source to destination weight. If the edge is not
	 * found its return null
	 * 
	 * @param source
	 * @param destination
	 * @return
	 */
	public Double getWeight(String source, String destination) {
		Edge<String> findEdge = this.graphObj.getEdge(source, destination);
		if (findEdge == null)
			throw new NoSuchElementException("edge is not found for given source and destination");
		return findEdge.getWeight();
	}

	/**
	 * Use brute force algorithm to solve the shortest path
	 * 
	 * @param sourceVertex
	 * @param userSelectedVertices
	 */
	public void useBruteForceAlgorithm(String sourceVertex, List<String> userSelectedVertices) {
		BruteForce<String> algorithm = new BruteForce<String>(graphObj);
		algorithm.permute(userSelectedVertices, sourceVertex, 0);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < algorithm.getBestRoute().size() - 1; i++) {
			result.append(algorithm.getBestRoute().get(i)).append("-").append(algorithm.getBestRoute().get(i + 1))
					.append(",\n");
		}
		calculatedPath = result.toString();
		calculatedDistance = algorithm.getMinDistance();
	}

	/**
	 * Use Held-Karp Algorithm to solve the shortest path
	 * 
	 * @param selectedNodes
	 */
	public void useHeldKarpAlgorithm(List<String> selectedNodes) {
		HeldKarp<String> algorithm = new HeldKarp<String>(graphObj);
		algorithm.calculate(selectedNodes);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < algorithm.getBestRoute().size() - 1; i++) {
			result.append(algorithm.getBestRoute().get(i)).append("-").append(algorithm.getBestRoute().get(i + 1))
					.append(",\n");
		}
		calculatedPath = result.toString();
		calculatedDistance = algorithm.getMinCost();
	}

	/**
	 * Use genetic algorithm to solve and find the shortest path
	 * 
	 * @param sourceVertex
	 * @param selectedVertices
	 */
	public void useGeneticAlgorithm(String sourceVertex, List<String> selectedVertices) {
		Genetic<String> algorithm = new Genetic<String>(graphObj);
		algorithm.calculate(selectedVertices, sourceVertex);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < algorithm.getBestRoute().size() - 1; i++) {
			result.append(algorithm.getBestRoute().get(i)).append("-").append(algorithm.getBestRoute().get(i + 1))
					.append(",\n");
		}
		calculatedPath = result.toString();
		calculatedDistance = algorithm.getMinDistance();
	}

	/**
	 * Save win records in database
	 * 
	 * @param timeTaken
	 * @param playerName
	 * @param calculatedMinimumPath
	 * @throws DatabaseException
	 */
	public void saveResult(String algorithm, long timeTaken, String playerName, String calculatedMinimumPath)
			throws DatabaseException {
		String[] pairs = calculatedMinimumPath.split(",\n");
		String sourceVertex = pairs[0].split("-")[0];
		List<String> destinationCities = new ArrayList<String>();
		for (int i = 0; i < pairs.length; i++) {
			if (i == pairs.length - 1) {
				break;
			}
			destinationCities.add(pairs[i].split("-")[1]);

		}

		// save them in a database
		DatabaseConnection dc = DatabaseConnection.getInstance();
		dc.getDatastore()
				.save(new TravelingSalesManResult(playerName, algorithm, sourceVertex, destinationCities, timeTaken));
	}

	public String getCalculatedPath() {
		return calculatedPath;
	}

	public void setCalculatedPath(String calculatedPath) {
		this.calculatedPath = calculatedPath;
	}

	public Double getCalculatedDistance() {
		return calculatedDistance;
	}

	public void setCalculatedDistance(Double calculatedDistance) {
		this.calculatedDistance = calculatedDistance;
	}

}

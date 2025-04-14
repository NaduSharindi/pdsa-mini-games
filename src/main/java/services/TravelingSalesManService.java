package services;

import java.util.Random;

import utils.constants.TravelingSalesManConstants;
import utils.dsa.graph.Edge;
import utils.dsa.graph.Graph;

public class TravelingSalesManService {
	private Graph<String> graphObj;

	/**
	 * Initialize new service object
	 */
	public TravelingSalesManService() {
		graphObj = new Graph<String>();
	}

	/**
	 * Initialize new game round
	 */
	public Graph<String> initNewRound() {
		graphObj = new Graph<String>();
		Random rand = new Random();

		// initialize graph with sources, destinations and random numbers
		for (int i = 0; i < TravelingSalesManConstants.NO_OF_SOURCE_CITIES; i++) {
			for (int j = 0; j < TravelingSalesManConstants.NO_OF_DESTINATION_CITIES; j++) {
				if (i > j) {
					graphObj.addEdge(String.valueOf((char) (i + 65)), String.valueOf((char) (j + 65)),
							rand.nextInt(51) + 50, false);
				} else if (i == j) {
					graphObj.addEdge(String.valueOf((char)(i + 65)), String.valueOf((char)(j + 65)), 0, true);
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
		if (findEdge != null) {
			return findEdge.getWeight();
		} else {
			return null;
		}
	}
}

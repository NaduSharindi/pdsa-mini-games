package utils.dsa.bruteforce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utils.dsa.graph.Graph;

public class BruteForce<N> {
	private Double minDistance;
	private List<N> bestRoute;
	private Graph<N> graph;

	/**
	 * initialize the initial value
	 */
	public BruteForce(Graph<N> graph) {
		this.minDistance = Double.MAX_VALUE;
		bestRoute = new ArrayList<N>();
		this.graph = graph;
	}

	/**
	 * Method to calculate best route and distance
	 * 
	 * @param selectedNodes
	 * @param homeNode
	 * @param start
	 */
	public void permute(List<N> selectedNodes, N homeNode, int start) {
		if (start == selectedNodes.size()) {
			// get distance from home to selected cities and home
			double distance = calculateRouteDistance(selectedNodes, homeNode);
			if (distance < minDistance) {
				minDistance = distance;
				bestRoute = new ArrayList<N>();
				bestRoute.add(homeNode);
				bestRoute.addAll(selectedNodes);
				bestRoute.add(homeNode);
			}
			return;
		}

		for (int i = start; i < selectedNodes.size(); i++) {
			Collections.swap(selectedNodes, i, start);
			permute(selectedNodes, homeNode, start + 1);
			//reverse track
			Collections.swap(selectedNodes, i, start);
		}
	}

	/**
	 * method to calculate route distance from home node
	 */
	private Double calculateRouteDistance(List<N> nodes, N homeNode) {
		double total = 0;
		N currentNode = homeNode;

		// get total distance from home to last node
		for (N node : nodes) {
			total += graph.getEdge(currentNode, node).getWeight();
			currentNode = node;
		}

		// add distance of last node to home node
		total += graph.getEdge(currentNode, homeNode).getWeight();
		return total;
	}

	/**
	 * getters and setters for algorithm
	 */
	public Graph<N> getGraph() {
		return graph;
	}

	public void setGraph(Graph<N> graph) {
		this.graph = graph;
	}

	public List<N> getBestRoute() {
		return bestRoute;
	}

	public void setBestRoute(List<N> bestRoute) {
		this.bestRoute = bestRoute;
	}

	public Double getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(Double minDistance) {
		this.minDistance = minDistance;
	}
}

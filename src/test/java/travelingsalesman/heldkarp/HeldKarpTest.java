package travelingsalesman.heldkarp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import utils.dsa.graph.Graph;
import utils.dsa.heldkarp.HeldKarp;

public class HeldKarpTest {

	/**
	 * Test the algorithm with 4 cities
	 */
	@Test
	public void testHeldKarpFourCities() {
		Graph<String> graph = new Graph<>();
		List<String> cities = Arrays.asList("A", "B", "C", "D");

		// Add bidirectional edges (complete graph)
		graph.addEdge("A", "B", 10, false);
		graph.addEdge("A", "C", 15, false);
		graph.addEdge("A", "D", 20, false);
		graph.addEdge("B", "C", 35, false);
		graph.addEdge("B", "D", 25, false);
		graph.addEdge("C", "D", 30, false);

		HeldKarp<String> tsp = new HeldKarp<>(graph);
		tsp.calculate(cities);

		List<String> route = tsp.getBestRoute();
		double cost = tsp.getMinCost();

		assertEquals(Arrays.asList("A", "C", "D", "B", "A"), route);
		assertEquals(80.0, cost, 0.001);
	}

	/**
	 * Test the algorithm with 3 cities
	 */
	@Test
	public void testHeldKarpThreeCities() {
		Graph<String> graph = new Graph<>();
		List<String> cities = Arrays.asList("X", "Y", "Z");

		graph.addEdge("X", "Y", 5, false);
		graph.addEdge("X", "Z", 10, false);
		graph.addEdge("Y", "Z", 3, false);

		HeldKarp<String> tsp = new HeldKarp<>(graph);
		tsp.calculate(cities);

		List<String> route = tsp.getBestRoute();
		double cost = tsp.getMinCost();

		assertEquals(Arrays.asList("X", "Z", "Y", "X"), route);
		assertEquals(18.0, cost, 0.001);
	}

	/**
	 * Test the algorithm with disconnected graph
	 */
	@Test
	public void testHeldKarpDisconnectedGraph() {
		Graph<String> graph = new Graph<>();
		List<String> cities = Arrays.asList("A", "B", "C");

		// Only one edge, graph is disconnected
		graph.addEdge("A", "B", 5, true);
		graph.addEdge("B", "A", 5, true);

		HeldKarp<String> tsp = new HeldKarp<>(graph);
		tsp.calculate(cities);

		double cost = tsp.getMinCost();
		List<String> route = tsp.getBestRoute();

		assertEquals(Double.POSITIVE_INFINITY, cost, 0.001);
	}

	/**
	 * Test the algorithm with null nodes
	 */
	@Test
	public void testHeldKarpNullSelectedNodes() {
		Graph<String> graph = new Graph<>();
		graph.addVertex("A");
		HeldKarp<String> tsp = new HeldKarp<>(graph);
		assertThrows(IllegalArgumentException.class, () -> {
			tsp.calculate(null);
		});

	}
}

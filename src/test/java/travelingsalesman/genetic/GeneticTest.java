package travelingsalesman.genetic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.dsa.genetic.Genetic;
import utils.dsa.graph.Graph;

public class GeneticTest {
	private Graph<String> graph;
	private Genetic<String> ga;

	@BeforeEach
	public void setup() {
		graph = new Graph<>();

		graph.addEdge("A", "B", 1, true);
		graph.addEdge("B", "C", 2, true);
		graph.addEdge("C", "A", 3, true);
		graph.addEdge("A", "C", 3, true);
		graph.addEdge("C", "B", 2, true);
		graph.addEdge("B", "A", 1, true);

		ga = new Genetic<>(graph);
	}

	/**
	 * Test the algorithm with two nodes to check algorithm finds complete route 
	 */
	@Test
	public void testGeneticAlgorithmFindsCompleteRoute() {
		List<String> selected = Arrays.asList("B", "C");
		String home = "A";

		ga.calculate(selected, home);
		List<String> route = ga.getBestRoute();

		assertNotNull(route);
		assertEquals(4, route.size());
		assertEquals(home, route.get(0));
		assertEquals(home, route.get(route.size() - 1));
	}

	/**
	 * Test the algorithm with 2 nodes for the minimum  distance is reasonable
	 */
	@Test
	public void testGeneticAlgorithmMinDistanceReasonable() {
		List<String> selected = Arrays.asList("B", "C");
		String home = "A";

		ga.calculate(selected, home);
		Double distance = ga.getMinDistance();

		assertEquals(6.0, distance, 0.001);
	}

	/**
	 * Test the algorithm can handle larger graph
	 */
	@Test
	public void testGeneticAlgorithmHandlesLargerGraphs() {
		graph.addVertex("D");
		graph.addEdge("C", "D", 4, true);
		graph.addEdge("D", "A", 5, true);
		graph.addEdge("B", "D", 3, true);
		graph.addEdge("D", "B", 3, true);

		ga = new Genetic<>(graph);

		List<String> selected = Arrays.asList("B", "C", "D");
		String home = "A";

		ga.calculate(selected, home);
		List<String> route = ga.getBestRoute();
		Double distance = ga.getMinDistance();

		assertNotNull(route);
		assertEquals(5, route.size());
		assertTrue(distance < Double.MAX_VALUE);
	}

	/**
	 * Test the algorithm will returns max value as the distance when graph is disconnected
	 */
	@Test
	public void testGeneticAlgorithmReturnsMaxValueForDisconnectedGraph() {
		graph = new Graph<>();
		graph.addVertex("A");
		graph.addVertex("B");

		ga = new Genetic<>(graph);
		List<String> selected = Arrays.asList("B");
		String home = "A";

		ga.calculate(selected, home);
		assertEquals(Double.MAX_VALUE, ga.getMinDistance());
	}
}

package travelingsalesman.bruteforce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import utils.dsa.bruteforce.BruteForce;
import utils.dsa.graph.Graph;

public class BruteForceTest {
	/**
	 * Test algorithm with three nodes
	 */
	@Test
	public void testTSPWithThreeNodes() {
		Graph<String> graph = new Graph<>();
		graph.addEdge("A", "B", 10, true);
		graph.addEdge("B", "A", 10, true);
		graph.addEdge("A", "C", 15, true);
		graph.addEdge("C", "A", 15, true);
		graph.addEdge("B", "C", 20, true);
		graph.addEdge("C", "B", 20, true);

		BruteForce<String> bf = new BruteForce<String>(graph);
		bf.permute(Arrays.asList("B", "C"), "A", 0);

		List<String> bestRoute = bf.getBestRoute();
		Double distance = bf.getMinDistance();

		assertEquals(Arrays.asList("A", "B", "C", "A"), bestRoute);
		assertEquals(45.0, distance);
	}

	/**
	 * Test algorithm with a single node
	 */
	@Test
	public void testSingleNodeRoute() {
		Graph<String> graph = new Graph<>();
		graph.addEdge("A", "A", 0, true);

		BruteForce<String> bf = new BruteForce<>(graph);
		bf.permute(new ArrayList<String>(List.of("A")), "A", 0);
		assertEquals(List.of("A", "A", "A"), bf.getBestRoute());
		assertEquals(0.0, bf.getMinDistance());
	}

	/**
	 * Test algorithm with a single node and check exception type
	 */
	@Test
	public void testEmptySelectedNodesThrows() {
		Graph<String> graph = new Graph<>();

		BruteForce<String> bf = new BruteForce<>(graph);
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> bf.permute(List.of(), "A", 0));
		assertTrue(ex.getMessage().contains("selected nodes must contains at  least 1 element"));
	}

	/**
	 * Test algorithm with null parsing to the constructor
	 */
	@Test
	public void testNullGraphThrows() {
		assertThrows(IllegalArgumentException.class, () -> new BruteForce<String>(null));
	}

	/**
	 * Test algorithm with parsing null to selected vertices
	 */
	@Test
	public void testNullSelectedNodesThrows() {
		Graph<String> graph = new Graph<>();
		BruteForce<String> bf = new BruteForce<>(graph);

		assertThrows(IllegalArgumentException.class, () -> bf.permute(null, "A", 0));
	}

	/**
	 * Test algorithm with parse null to the home vertex
	 */
	@Test
	public void testNullHomeNodeThrows() {
		Graph<String> graph = new Graph<>();
		BruteForce<String> bf = new BruteForce<>(graph);

		assertThrows(IllegalArgumentException.class, () -> bf.permute(List.of("A", "B"), null, 0));
	}

	/**
	 * Test algorithm with parsing unreachable nodes to the algorithm
	 */
	@Test
	public void testUnreachableNodeThrows() {
		Graph<String> graph = new Graph<>();
		graph.addVertex("A");
		graph.addVertex("B");
		// No edge between A and B

		BruteForce<String> bf = new BruteForce<>(graph);
		assertThrows(IllegalStateException.class, () -> bf.permute(new ArrayList<String>(List.of("B", "C")), "A", 0));
	}
}
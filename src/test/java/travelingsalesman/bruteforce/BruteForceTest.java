package travelingsalesman.bruteforce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.dsa.bruteforce.BruteForce;
import utils.dsa.graph.Graph;

public class BruteForceTest {
	private Graph<String> graph;

    @BeforeEach
    void setUp() {
        graph = new Graph<>();
        graph.addEdge("A", "B", 10, false);
        graph.addEdge("A", "C", 15, false);
        graph.addEdge("A", "D", 20, false);
        graph.addEdge("B", "C", 35, false);
        graph.addEdge("B", "D", 25, false);
        graph.addEdge("C", "D", 30, false);
    }
    
    /**
     * test the best route and minimum distance
     */
    @Test
    void testBestRouteCalculation() {
        BruteForce<String> tsp = new BruteForce<>(graph);
        List<String> cities = Arrays.asList("B", "C", "D");
        String home = "A";

        tsp.permute(cities, home, 0);
        List<String> result = tsp.getBestRoute();

        // Assert that the route starts and ends at home
        assertEquals(home, result.get(0));
        assertEquals(home, result.get(result.size() - 1));
        assertEquals(5, result.size());

        // e.g., A -> B -> D -> C -> A = 10 + 25 + 30 + 15 = 80
        assertEquals(Arrays.asList("A", "B", "D", "C", "A"), result);
    }
    
    /**
     * test with user selected empty route
     */
    @Test
    void testNoRouteIfEmptyCities() {
        BruteForce<String> tsp = new BruteForce<>(graph);
        List<String> cities = Arrays.asList();
        String home = "A";

        tsp.permute(cities, home, 0);
        List<String> result = tsp.getBestRoute();

        // should only contain start and end (same node)
        assertEquals(Arrays.asList("A", "A"), result);
    }
    
    /**
     * test with single node graph
     */
    @Test
    void testSingleNodeGraph() {
        BruteForce<String> tsp = new BruteForce<>(graph);
        List<String> cities = List.of();  // no cities to visit
        String home = "A";
        graph.addEdge("A", "A", 0, false);
        tsp.permute(cities, home, 0);
        List<String> result = tsp.getBestRoute();

        assertEquals(List.of("A", "A"), result);
    }
    
    /**
     * test when graph cities are disconnected
     */
    @Test
    void testDisconnectedGraph() {
        graph.addEdge("A", "B", 10, false);
        graph.addEdge("B", "C", 15, false);
        List<String> cities = Arrays.asList("B", "D", "C");
        String home = "A";
        BruteForce<String> tsp = new BruteForce<>(graph);
        assertThrows(NullPointerException.class, () -> tsp.permute(cities, home, 0));
    }
    
    /**
     * test when graph all edges has equal weights
     */
    @Test
    void testEqualEdgeWeights() {
        graph.addEdge("A", "B", 1, true);
        graph.addEdge("A", "C", 1, true);
        graph.addEdge("A", "D", 1, true);
        graph.addEdge("B", "C", 1, true);
        graph.addEdge("B", "D", 1, true);
        graph.addEdge("C", "D", 1, true);

        BruteForce<String> tsp = new BruteForce<>(graph);
        List<String> cities = Arrays.asList("B", "C", "D");
        String home = "A";

        tsp.permute(cities, home, 0);
        List<String> result = tsp.getBestRoute();

        // Any route with all nodes once should be valid since all have equal cost
        assertEquals(5, result.size());
        assertEquals("A", result.get(0));
        assertEquals("A", result.get(4));
        assertTrue(result.containsAll(List.of("B", "C", "D")));
    }
    
    

}

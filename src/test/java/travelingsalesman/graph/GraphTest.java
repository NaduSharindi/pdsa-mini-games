package travelingsalesman.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.dsa.graph.Edge;
import utils.dsa.graph.Graph;

public class GraphTest {
	private Graph<String> graph;
	
	/**
	 * Initialization of the graph object
	 */
    @BeforeEach
    public void setUp() {
        graph = new Graph<>();
    }
    
    /**
     * Test add vertex method
     */
    @Test
    public void testAddVertex() {
        graph.addVertex("A");
        assertNotNull(graph.getNeighbors("A"));
        assertTrue(graph.getNeighbors("A").isEmpty());
    }
    
    
    /**
     * Test method for add directed edge
     */
    @Test
    public void testAddEdgeDirected() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", 2.5, true);

        List<Edge<String>> neighbors = graph.getNeighbors("A");
        assertEquals(1, neighbors.size());
        assertEquals("B", neighbors.get(0).getDestination());
        assertEquals(2.5, neighbors.get(0).getWeight());

        // Make sure B has no outgoing edges since it's directed
        assertTrue(graph.getNeighbors("B").isEmpty());
    }
    
    /**
     * Test method for add undirected edge
     */
    @Test
    public void testAddEdgeUndirected() {
        graph.addVertex("X");
        graph.addVertex("Y");
        graph.addEdge("X", "Y", 3.0, false);

        assertEquals("Y", graph.getNeighbors("X").get(0).getDestination());
        assertEquals("X", graph.getNeighbors("Y").get(0).getDestination());
    }
    
    /**
     * Test method to test if an error throwing when adding duplicate edge to the graph
     */
    @Test
    public void testDuplicateEdgeThrowsError() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", 5.0, true);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            graph.addEdge("A", "B", 5.0, true);
        });

        assertEquals("edge is already exists", exception.getMessage());
    }
    
    /**
     * Test method to test saved graph edge returns correctly or not
     */
    @Test
    public void testGetEdgeReturnsCorrectEdge() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", 4.2, true);

        Edge<String> edge = graph.getEdge("A", "B");
        assertNotNull(edge);
        assertEquals("B", edge.getDestination());
        assertEquals(4.2, edge.getWeight());
    }
    
    
    /**
     * Test method to test when get non exists edge from graph
     */
    @Test
    public void testGetEdgeReturnsNullIfNotExists() {
        graph.addVertex("A");
        graph.addVertex("C");

        assertNull(graph.getEdge("A", "C"));
    }
    
    /**
     * Test method to test when null parse to addVertex method 
     */
    @Test
    public void testAddVertexNullThrowsError() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            graph.addVertex(null);
        });

        assertEquals("source vertex can not be null", exception.getMessage());
    }
}

package utils.dsa.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Graph data structure for Graph
 * 
 * @param <T>
 */
public class Graph<T> {
	private Map<T, List<Edge<T>>> adjList = null;
	
	/**
	 * Constructor method for initializing the Graph data structure
	 */
	public Graph() {
		this.adjList = new HashMap<T, List<Edge<T>>>();
	}
	
	/**
	 * Add vertex to Graph
	 * 
	 * @param vertex
	 */
	public void addVertex(T vertex) {
		adjList.putIfAbsent(vertex, new ArrayList<>());
	}
	
	/**
	 * Add edge to graph
	 * 
	 * @param source
	 * @param destination
	 * @param weight
	 * @param isDirected
	 */
	public void addEdge(T source, T destination, double weight, boolean isDirected) {		
		if(isDirected) {
			adjList.putIfAbsent(source, new ArrayList<>());
			adjList.get(source).add(new Edge<T>(destination, weight));
		}else {
			adjList.putIfAbsent(source, new ArrayList<>());
			adjList.putIfAbsent(destination, new ArrayList<>());
			adjList.get(source).add(new Edge<T>(destination, weight));
			adjList.get(destination).add(new Edge<T>(source, weight));
		}
	}
	
	/**
	 * Get neighbors of a vertex
	 * 
	 * @param vertex
	 * @return
	 */
	public List<Edge<T>> getNeighbors(T vertex){
		return adjList.getOrDefault(vertex, new ArrayList<>());
	}
	
	/**
	 * Get a single edge from graph. If there is no such a edge its returns null
	 * 
	 * @param source
	 * @param destination
	 * @return
	 */
	public Edge<T> getEdge(T source, T destination){
		List<Edge<T>> edgesList = this.getNeighbors(source);
		for(Edge<T> edge: edgesList) {
			if(edge.getDestination().equals(destination)) {
				return edge;
			}
		}
		return null;
	}
}

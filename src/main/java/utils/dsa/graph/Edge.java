package utils.dsa.graph;

/**
 * Edge container class for Graph data structure
 * 
 * @param <T>
 */
public class Edge<T> {
	private T destination;
	private double weight;
	
	public Edge() {}

	public Edge(T destination, double weight) {
		super();
		this.destination = destination;
		this.weight = weight;
	}
	
	//getters and setters for edge properties
	public T getDestination() {
		return destination;
	}

	public void setDestination(T destination) {
		this.destination = destination;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}

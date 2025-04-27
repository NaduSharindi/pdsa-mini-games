package utils.dsa.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import utils.dsa.graph.Edge;
import utils.dsa.graph.Graph;

public class Genetic<N> {
	private Graph<N> graph;
	private int populationSize;
	private double mutationRate;
	private int generations;
	private Random rand;
	private List<N> bestRoute;
	private Double minDistance;

	/**
	 * Initialize the initial values
	 * 
	 * @param graph
	 */
	public Genetic(Graph<N> graph) {
		if (graph == null)
			throw new IllegalArgumentException("graph should not be null");
		this.graph = graph;
		this.populationSize = 100;
		this.mutationRate = 0.05;
		this.generations = 500;
		this.rand = new Random();
		bestRoute = new ArrayList<N>();
		minDistance = Double.MAX_VALUE;
	}

	/**
	 * Calculate the shortest path and distance from genetic algorithm
	 * 
	 * @param selectedNodes
	 * @param homeNode
	 */
	public void calculate(List<N> selectedNodes, N homeNode) {
		if (selectedNodes == null)
			throw new IllegalArgumentException("selected nodes should not be null");
		if (homeNode == null)
			throw new IllegalArgumentException("home node should not be null");
		if (selectedNodes.size() < 1)
			throw new IllegalArgumentException("selected nodes should have at least 1 nodes");

		List<List<N>> population = initializePopulation(selectedNodes);
		List<N> best = new ArrayList<>();
		double bestDistance = Double.MAX_VALUE;

		for (int gen = 0; gen < generations; gen++) {
			population.sort(Comparator.comparingDouble(this::calculateDistance));

			if (calculateDistance(population.get(0)) < bestDistance) {
				bestDistance = calculateDistance(population.get(0));
				best = population.get(0);
			}

			List<List<N>> newPopulation = new ArrayList<>();

			// Elitism: carry forward best 10%
			int eliteCount = populationSize / 10;
			newPopulation.addAll(population.subList(0, eliteCount));

			while (newPopulation.size() < populationSize) {
				List<N> parent1 = this.selectParent(population);
				List<N> parent2 = this.selectParent(population);

				List<N> child = crossover(parent1, parent2);
				mutate(child);
				newPopulation.add(child);
			}

			population = newPopulation;
		}

		// Return the best path including home to home
		List<N> completePath = new ArrayList<>();
		completePath.add(homeNode);
		completePath.addAll(best);
		completePath.add(homeNode);

		this.bestRoute = completePath;
		this.minDistance = calculatePathDistance(completePath);
	}

	/**
	 * mutate method in genetic algorithm
	 * 
	 * @param child
	 */
	private void mutate(List<N> child) {
		if (rand.nextDouble() < mutationRate) {
			int i = rand.nextInt(child.size());
			int j = rand.nextInt(child.size());
			Collections.swap(child, i, j);
		}
	}

	/**
	 * Cross over method in genetic algorithm
	 * 
	 * @param parent1
	 * @param parent2
	 * @return
	 */
	private List<N> crossover(List<N> parent1, List<N> parent2) {
		// Order crossover (OX1)
		int size = parent1.size();
		int start = rand.nextInt(size);
		int end = start + rand.nextInt(size - start);

		Set<N> segment = new HashSet<N>(parent1.subList(start, end));
		List<N> child = new ArrayList<>(Collections.nCopies(size, null));

		for (int i = start; i < end; i++) {
			child.set(i, parent1.get(i));
		}

		int currentIndex = end % size;
		for (N gene : parent2) {
			if (!segment.contains(gene)) {
				while (child.get(currentIndex) != null) {
					currentIndex = (currentIndex + 1) % size;
				}
				child.set(currentIndex, gene);
			}
		}

		return child;
	}

	/**
	 * Select parent method in genetic algorithm
	 * 
	 * @param population
	 * @return
	 */
	private List<N> selectParent(List<List<N>> population) {
		List<List<N>> tournament = new ArrayList<List<N>>();
		for (int i = 0; i < 5; i++) {
			tournament.add(population.get(rand.nextInt(population.size())));
		}
		tournament.sort(Comparator.comparingDouble(this::calculateDistance));
		return new ArrayList<>(tournament.get(0));

	}

	/**
	 * Initialize population
	 * 
	 * @param selectedNode
	 * @return
	 */
	private List<List<N>> initializePopulation(List<N> selectedNode) {
		List<List<N>> population = new ArrayList<List<N>>();
		for (int i = 0; i < populationSize; i++) {
			List<N> shuffled = new ArrayList<N>(selectedNode);
			Collections.shuffle(shuffled);
			population.add(shuffled);
		}
		return population;
	}

	/**
	 * Calculate distance method in genetic algorithm
	 * 
	 * @param path
	 * @return
	 */
	private Double calculateDistance(List<N> path) {
		Double distance = 0.0;
		for (int i = 0; i < path.size() - 1; i++) {
			Edge<N> edge = graph.getEdge(path.get(i), path.get(i + 1));
			if (edge == null)
				return Double.MAX_VALUE;
			distance += edge.getWeight();
		}

		// Return to home
		Edge<N> back = graph.getEdge(path.get(path.size() - 1), path.get(0));
		if (back == null)
			return Double.MAX_VALUE;
		distance += back.getWeight();

		return distance;
	}

	/**
	 * Get the distance in best path calculated by the algorithm
	 * 
	 * @param bestPath
	 * @return
	 */
	private Double calculatePathDistance(List<N> bestPath) {
		Double distance = 0.0;
		for (int i = 0; i < bestPath.size() - 1; i++) {
			Edge<N> edge = graph.getEdge(bestPath.get(i), bestPath.get(i + 1));
			if (edge == null)
				return Double.MAX_VALUE;
			distance += edge.getWeight();
		}

		return distance;
	}

	/**
	 * getters and setters
	 * 
	 * @return
	 */
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

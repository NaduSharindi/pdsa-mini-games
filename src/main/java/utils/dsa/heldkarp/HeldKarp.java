package utils.dsa.heldkarp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.dsa.graph.Edge;
import utils.dsa.graph.Graph;

public class HeldKarp<N> {
	private Graph<N> graph;
	List<N> bestRoute;
	Double minCost;

	public HeldKarp(Graph<N> graph) {
		this.graph = graph;
		minCost = Double.POSITIVE_INFINITY;
		bestRoute = new ArrayList<>();
	}
	
	/**
	 * Method to calculate the best route and distance
	 * 
	 * @param selectedNodes
	 */
	public void calculate(List<N> selectedNodes) {
		int n = selectedNodes.size();
		Map<N, Integer> cityIndex = new HashMap<N, Integer>();
		for (int i = 0; i < n; i++) {
			cityIndex.put(selectedNodes.get(i), i);
		}
		
        double[][] dp = new double[1 << n][n];
        int[][] parent = new int[1 << n][n];
        
        for (double[] row : dp) Arrays.fill(row, Double.POSITIVE_INFINITY);
        for (int[] row : parent) Arrays.fill(row, -1);
        
        //home city
        dp[1][0] = 0;
        
        for(int mask = 1; mask < (1 << n); mask ++) {
        	 for (int u = 0; u < n; u++) {
        		 if ((mask & (1 << u)) == 0) continue;
        		 
        		 for (int v = 0; v < n; v++) {
        			 if ((mask & (1 << v)) != 0 || graph.getEdge(selectedNodes.get(u), selectedNodes.get(v)) == null) continue;
        			 
                     int nextMask = mask | (1 << v);
                     double weight = graph.getEdge(selectedNodes.get(u), selectedNodes.get(v)).getWeight();
                     if (dp[nextMask][v] > dp[mask][u] + weight) {
                         dp[nextMask][v] = dp[mask][u] + weight;
                         parent[nextMask][v] = u;
                     }
        		 }
        	 }
        }
        
        int lastCity = -1;

        for (int i = 1; i < n; i++) {
            Edge<N> returnEdge = graph.getEdge(selectedNodes.get(i), selectedNodes.get(0));
            if (returnEdge != null) {
                double cost = dp[(1 << n) - 1][i] + returnEdge.getWeight();
                if (cost < minCost) {
                    minCost = cost;
                    lastCity = i;
                }
            }
        }
        
        // Reconstruct the path
        int mask = (1 << n) - 1;
        while (lastCity != -1) {
        	bestRoute.add(selectedNodes.get(lastCity));
            int temp = parent[mask][lastCity];
            mask ^= (1 << lastCity);
            lastCity = temp;
        }
        // return to start
        bestRoute.add(selectedNodes.get(0));
        Collections.reverse(bestRoute);
	}

	public List<N> getBestRoute() {
		return bestRoute;
	}

	public void setBestRoute(List<N> bestRoute) {
		this.bestRoute = bestRoute;
	}

	public Double getMinCost() {
		return minCost;
	}

	public void setMinCost(Double minCost) {
		this.minCost = minCost;
	}
}

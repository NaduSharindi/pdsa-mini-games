/**
 * 
 */
package services;

import models.entities.EightQueensResult;
import utils.DatabaseConnection;
import utils.dsa.ArrayList;
import utils.dsa.HashSet;

import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import models.exceptions.DatabaseException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * service class for Eight Queens puzzle game
 */
public class EightQueensService {
	
	// 8*8 chessboard
	private static final int SIZE = 8;
	//store all solutions
	private ArrayList<int[]> solutions;
	// for duplicate solution checking
	private HashSet<String> solutionSet;
	private Datastore datastore;
	
	public EightQueensService(){
        try {
            solutions = new ArrayList<>();
            solutionSet = new HashSet<>();
            datastore = DatabaseConnection.getInstance().getDatastore();
        } catch (DatabaseException e) {
            throw new RuntimeException("Database error in EightQueensService: " + e.getMessage(), e);
        }
	}
	
	/*
	 * Find all solutions using sequential backtracking
	 * and save it in database with time
	 */
	
	public long findAllSolutionsSequential() {
	    // First check if solutions exist in DB
	    if (datastore.find(EightQueensResult.class)
	            .filter("algorithmType", "Sequential")
	            .count() >= 92) {
	        
	        // Clear existing in-memory data
	        solutions = new ArrayList<>();
	        solutionSet = new HashSet<>();
	        
	        // Get time from database first (more efficient than loading all solutions)
	        EightQueensResult timeResult = datastore.find(EightQueensResult.class)
	            .filter("algorithmType", "Sequential")
	            .first();
	        long timeTaken = timeResult != null ? timeResult.getTimeTaken() : 0;
	        
	        // Load solutions
	        List<EightQueensResult> results = datastore.find(EightQueensResult.class)
	            .filter("algorithmType", "Sequential")
	            .iterator().toList();
	            
	        for (EightQueensResult result : results) {
	            if (result.getPositions() != null) {
	                solutions.add(result.getPositions());
	                solutionSet.add(serialize(result.getPositions()));
	            }
	        }
	        return timeTaken;
	    } else {
	        // Generate and save solutions
	        solutions = new ArrayList<>();
	        solutionSet = new HashSet<>();
	        long start = System.currentTimeMillis();
	        solve(0, new int[SIZE]);
	        long end = System.currentTimeMillis();
	        long timeTaken = end - start;
	        
	        // Clear any existing solutions before saving
	        datastore.delete(datastore.find(EightQueensResult.class)
	            .filter("algorithmType", "Sequential"));
	            
	        // Save new solutions
	        for (int i = 0; i < solutions.size(); i++) {
	            EightQueensResult result = new EightQueensResult(
	                solutions.get(i),
	                null,
	                timeTaken,
	                "Sequential",
	                false,
	                new Date()
	            );
	            datastore.save(result);
	            
	        }
	        return timeTaken;
	    }
	}


	
	//recursive backtracking helper
	private void solve(int column, int[] board) {
		if(column == SIZE) {
			int[] solution = board.clone();
			String key = serialize(solution);
			if(!solutionSet.contains(key)) {
				solutions.add(solution);
				solutionSet.add(key);
			}
			return;
		}
		for(int row = 0; row < SIZE; row++) {
			if(isSafe(board, column, row)) {
				board[column] = row;
				solve(column + 1, board);
			}
			if (solutions.size() == 92) return;
		}
	}
	
	// check place if can not attack queens each other
	private boolean isSafe(int[] board, int column, int row) {
		for(int b = 0; b < column; b++) {
			int a = board[b];
			if(a == row || Math.abs(a - row) == Math.abs(b - column)) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * Find all solution using Multi-threaded backtracking
	 * and save it in database with time 
	 */
	
	public long findAllSolutionsThreaded() {
	    // First check if solutions exist in DB
	    if (datastore.find(EightQueensResult.class)
	            .filter("algorithmType", "Threaded")
	            .count() >= 92) {
	        
	        // Clear existing in-memory data
	        solutions = new ArrayList<>();
	        solutionSet = new HashSet<>();
	        
	        // Get time from database first (more efficient)
	        EightQueensResult timeResult = datastore.find(EightQueensResult.class)
	            .filter("algorithmType", "Threaded")
	            .first();
	        long timeTaken = timeResult != null ? timeResult.getTimeTaken() : 0;
	        
	        // Load solutions
	        List<EightQueensResult> results = datastore.find(EightQueensResult.class)
	            .filter("algorithmType", "Threaded")
	            .iterator().toList();
	            
	        for (EightQueensResult result : results) {
	            if (result.getPositions() != null) {
	                solutions.add(result.getPositions());
	                solutionSet.add(serialize(result.getPositions()));
	            }
	        }
	        return timeTaken;
	    } else {
	        // Generate and save solutions
	        solutions = new ArrayList<>();
	        solutionSet = new HashSet<>();
	        
	        long start = System.currentTimeMillis();
	        ExecutorService executor = Executors.newFixedThreadPool(SIZE);
	        
	        for (int row = 0; row < SIZE; row++) {
	            final int startRow = row;
	            executor.submit(() -> {
	                int[] board = new int[SIZE];
	                board[0] = startRow;
	                solve(1, board);
	            });
	        }
	        
	        executor.shutdown();
	        try {
	            executor.awaitTermination(1, TimeUnit.MINUTES);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	        
	        long end = System.currentTimeMillis();
	        long timeTaken = end - start;
	        
	        // Clear any existing solutions before saving
	        datastore.delete(datastore.find(EightQueensResult.class)
	            .filter("algorithmType", "Threaded"));
	            
	        // Save new solutions
	        for (int i = 0; i < solutions.size(); i++) {
	            EightQueensResult result = new EightQueensResult(
	                solutions.get(i),
	                null,
	                timeTaken,
	                "Threaded",
	                false,
	                new Date()
	            );
	            datastore.save(result);
	        }
	        return timeTaken;
	    }
	}


	
	/*
	 * Serialize solution to string for use in hash set
	 */
	
	private String serialize(int[] board) {
	    if (board == null) {
	        return "";  
	    }
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < board.length; i++) {
			sb.append(board[i]);
			if(i < board.length - 1) sb.append(",");
		}
		return sb.toString();
	}

	
    /**
     * Validate if a player's submitted solution is correct and not already claimed.
     * @param positions  queen positions submitted by the player.
     * @return true if correct and not claimed, false otherwise.
     */
	
	public boolean validatePlayerSolution(int[] positions) {
		String key = serialize(positions);
		return solutionSet.contains(key);
	}

	
	/**
	 * Save player's correct solution in database
	 */
	
	public void savePlayerSolution(int[] positions, String playerName ) {
		String key = serialize(positions);
		if(!solutionSet.contains(key))
			return;  // not valid solution
		
		// find and update result
		EightQueensResult result = datastore.find(EightQueensResult.class)
				.filter("positions", positions)
				.first();
		if(result != null && !result.isRecognized()) {
			result.setPlayerName(playerName);
			//result.setTimeTaken(timeTaken);
			result.setRecognized(true);
			result.setTimestamp(new Date());
			datastore.save(result);
		}
	}
	
	/*
	 * check if solution already recognized by another player
	 */
	
	public boolean isSolutionRecognized(int[] positions) {
		EightQueensResult result = datastore.find(EightQueensResult.class)
				.filter("positions", positions)
				.first();
		return result != null && result.isRecognized();
	}
	
	/*
	 * reset all solutions recognition for new round
	 */
	
	public void resetAllRecognizedSolutions() {
	    // Fetch ALL solutions from the database
	    List<EightQueensResult> allResults = datastore.find(EightQueensResult.class).iterator().toList();
	    
	    for (EightQueensResult result : allResults) {
	        result.setRecognized(false);
	        result.setPlayerName(null);
	        datastore.save(result);
	    }
	}

	
	/*
	 * get number of solutions found.
	 */
	
	public int getSolutionCount() {
		int recognizedCount = 0;
		for(EightQueensResult result : datastore.find(EightQueensResult.class)) {
			if(result.isRecognized()) {
				recognizedCount++;
			}
		}
		return recognizedCount;
	}
	
	/*
	 * get all stored solutions
	 */
	
	public ArrayList<int[]> getAllSolutions() {
		return solutions;
	}

}

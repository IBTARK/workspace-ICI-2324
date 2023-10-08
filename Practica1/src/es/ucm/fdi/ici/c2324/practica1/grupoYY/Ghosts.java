package es.ucm.fdi.ici.c2324.practica1.grupoYY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Random;

import org.apache.poi.util.SystemOutLogger;

import pacman.controllers.GhostController;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController{
	
	private Game game;
    private static final String NAME = "I+D";

	private static final int SECURITY_DIST_PPILL = 45; //original 30 (mejor 45)
	private static final int SECURITY_DIST_PACMAN = 120; //original 100 (mejor 120)
	private static final double RAND_LIM = 1; //original 1 (mejor 1)
    private static final double k1 = 5000.0;  //Constant for the behavior of the chasing MsPacMan ghosts original 12000 (mejor 5000)
    private static final double k2 = 15000.0; //Constant for the spreading of the ghosts original 18000 (mejor 15000)
    private static final double k3 = 5000.0; //Constant for the behavior of the chasing MsPacMans next junction original 18000 (mejor 5000)
    
    //Name of the team
    public String getName() {
		return NAME;
	}

	//Gets the moves of every ghost
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		this.game = game;

		//Map of ghosts moves
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		
		//Array of ghosts
		GHOST[] ghosts = GHOST.values();
		//The array of ghosts is ordered by distance to pacMan
		Arrays.sort(ghosts, new Comparator<GHOST>(){
			public int compare(pacman.game.Constants.GHOST g1, pacman.game.Constants.GHOST g2) {
				 return game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g1), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g1)) - 
		        		 game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g2), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g2));
			}
		});

		int cont = 0;
		
		//The best move for every ghost is selected
		for (GHOST g : ghosts) { 
			if (game.doesGhostRequireAction(g)) {
				moves.put(g, getGhostMove(g, cont));
			}
			cont++;
		}
		return moves;
	}
	
	//Gets the best move of a given ghost
	private MOVE getGhostMove(GHOST g, int cont) {
    	int pos = game.getGhostCurrentNodeIndex(g); //Ghost position 
    	ArrayList<MOVE> possibleMoves = new ArrayList<>(); //Possible moves of the ghost

    	//The possible moves of the ghost is computed
		for (int n : game.getNeighbouringNodes(pos, game.getGhostLastMoveMade(g)))
			possibleMoves.add(game.getMoveToMakeToReachDirectNeighbour(pos, n));
    	
    	//If there is not more than one possible movement
    	if (possibleMoves.size() <= 1)
			return possibleMoves.get(0);
		
    	//Variables to select the best movement
		double newScore; 
		ArrayList<MOVE> bestMoves = new ArrayList<>();
		ArrayList<Double> bestScores = new ArrayList<>();
		
		//The best movement of the ones possible is selected
		for(MOVE m : possibleMoves) {
			int newPos = game.getNeighbour(pos, m);
			newScore = getMovementScore(g, pos, newPos, m, cont);
			//If the actual movement is better, is saved as the best one until this moment
			if(bestMoves.isEmpty()) {
				bestMoves.add(m);
				bestScores.add(newScore);
			}
			//Introduces a random factor
			else if (newScore + RAND_LIM >= bestScores.get(0)) {
				if (newScore - RAND_LIM > bestScores.get(bestScores.size()-1)) {
					bestScores.clear();
					bestMoves.clear();
				}
				else if (newScore - RAND_LIM > bestScores.get(0)){
					bestScores.remove(0);
					bestMoves.remove(0);
				}
				bestScores.add(newScore);
				bestMoves.add(m);
			}
		}
		int bestIdx = bestMoves.size() == 1 ? 0 : new Random().nextInt(bestMoves.size());
				
		return bestMoves.get(bestIdx);
	}

	//Computes the score of a given movement
	private double getMovementScore(GHOST g, int pos, int newPos, MOVE m, int cont) {
		double score = 0;
		
		//If cont <= 1 the ghost has to chase MsPacMan
		if(cont <= 1) score += scoreDistMsPacMan(g, pos, newPos, m);
		//If cont > 1 the ghost has to try to reach the next junction in MsPacMans path
		else score += scoreDistJunction(g, pos, newPos, m);
		
		//The score that depends on the rest of the ghosts is computed
		score += scoreDistGhosts(g, pos, newPos, m);
	
		return score;	
	}
	
	//Gets the score of those ghost that have to chase MsPacMan
	private double scoreDistMsPacMan(GHOST g, int pos, int newPos, MOVE m) {
		if (game.wasPacManEaten())
			return 0;
		
		double score = 0;
		//Distance form the ghost to MsPacMan in the actual position
		int dist3 = game.getShortestPathDistance(pos, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
		//Distance from the ghost to MsPacMan in the new position
		int dist4 = game.getShortestPathDistance(newPos, game.getPacmanCurrentNodeIndex(), m);
	
		
		score += -k1 / (dist3 + 5);
		score += k1 / (dist4 + 5);
		
		//If the ghost is edible or too close to a pill it has to escape from MsPacMan, so the score is reversed
		if(game.isGhostEdible(g) || isCloseToPPill()) {
			if (dist3 > SECURITY_DIST_PACMAN)
				return score;
			return -score;
		}
		return score;
	}
	
	//Gets the score of those ghost that have to reach MsPacMans next junction
	private double scoreDistJunction(GHOST g, int pos, int newPos, MOVE m) {
		if (game.wasPacManEaten())
			return 0;
		
		double score = 0;
		//Distance form the ghost to MsPacMans next junction in the actual position
		int dist3 = game.getShortestPathDistance(pos, getMsPacManNextJunction(), game.getGhostLastMoveMade(g));
		//Distance from the ghost to MsPacMans newt junction in the new position
		int dist4 = game.getShortestPathDistance(newPos, getMsPacManNextJunction(), m);

		score += -k3 / (dist3 + 5);
		score += k3 / (dist4 + 5);
		
		//If the ghost is edible or too close to a pill it has to escape from MsPacMan, so the score is reversed
		if(game.isGhostEdible(g) || isCloseToPPill()) {
			if (dist3 > SECURITY_DIST_PACMAN)
				return score;
			return -score;
		}
		return score;
	}
	
	//Gets the score that depends on the rest of the ghosts
	private double scoreDistGhosts(GHOST g, int pos, int newPos, MOVE m) {
		double score = 0;
		boolean isEdible = game.isGhostEdible(g);
		
		//The rest of the chasing ghost are taken into consideration
		for (GHOST ghost : GHOST.values()) {
			int ghostNode = game.getGhostCurrentNodeIndex(ghost); 
			
			//It the ghost is not the nearest chasing one and its edible and its not on the lair, is taken into consideration
			if (game.getGhostLairTime(g) <= 0){
				int dist5 = game.getShortestPathDistance(pos, ghostNode);
				int dist6 = game.getShortestPathDistance(newPos, ghostNode);
				score +=  k2 / (dist5 + 10);
	    		score += -k2 / (dist6 + 10);	
				score *= (isEdible == game.isGhostEdible(ghost) ? 1 : -1);
	    	}
		}
		
		return score;
	}
	
	//Checks if MsPacMan is close (within a SECURITY_DISTANCE) to a power pill
	private boolean isCloseToPPill() {
		int pacman = game.getPacmanCurrentNodeIndex();
		for (int pp : game.getActivePowerPillsIndices())
			if (SECURITY_DIST_PPILL >= game.getDistance(pacman, pp, Constants.DM.PATH))
				return true;
		return false;
	}
	
	//Gets the next junction that MsPacman is moving to
	private int getMsPacManNextJunction() {
		int actualNode =  game.getPacmanCurrentNodeIndex();
		MOVE pacManLastMove = game.getPacmanLastMoveMade();
		int[] neighbours = game.getNeighbouringNodes(actualNode, pacManLastMove); 
		
		while(neighbours.length == 1) {
			pacManLastMove = game.getPossibleMoves(actualNode, pacManLastMove)[0];
			actualNode = neighbours[0];
			neighbours = game.getNeighbouringNodes(actualNode, pacManLastMove);
		}
		
		return actualNode;
	}
}
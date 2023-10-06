package es.ucm.fdi.ici.c2324.practica1.grupoYY;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	
	private Game game;
    private static final String NAME = "I+D";

	private static final int SECURITY_DIST_PPILL = 50; //original 30 (mejor 50)
	private static final int SECURITY_DIST_PACMAN = 120; //original 100 (mejor 120)
	private static final double RAND_LIM = 10; //original 1 (mejor 10)
    private static final double k1 = 5000.0;  //original 12000 (mejor 5000)
    private static final double k2 = 18000.0; //original 18000 (mejor 18000)
    
    public String getName() {
		return NAME;
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		this.game = game;

		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		for (GHOST g : GHOST.values()) 
			if (game.doesGhostRequireAction(g))
				moves.put(g, getGhostMove(g));
		return moves;
	}
	
	private MOVE getGhostMove(GHOST g) {
    	int pos = game.getGhostCurrentNodeIndex(g); //Ghosts position 
    	ArrayList<MOVE> possibleMoves = new ArrayList<>(); //Possible moves of Ghost

		for (int n : game.getNeighbouringNodes(pos, game.getGhostLastMoveMade(g)))
			possibleMoves.add(game.getMoveToMakeToReachDirectNeighbour(pos, n));
    	
    	//If there is more than one possible movement
    	if (possibleMoves.size() <= 1)
			return possibleMoves.get(0);
		
		double newScore; 
		ArrayList<MOVE> bestMoves = new ArrayList<>();
		ArrayList<Double> bestScores = new ArrayList<>();
		
		//The best movement of the ones possible is selected
		for(MOVE m : possibleMoves) {
			int newPos = game.getNeighbour(pos, m);
			newScore = getMovementScore(g, pos, newPos, m);
			//If the actual movement is better, is saved as the best one until this moment
			if(bestMoves.isEmpty()) {
				bestMoves.add(m);
				bestScores.add(newScore);
			}
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

	private double getMovementScore(GHOST g, int pos, int newPos, MOVE m) {
		double score = 0;
		
		score += scoreDistPacman(g, pos, newPos, m);
		score += scoreDistGhosts(g, pos, newPos, m);
	
		return score;	
	}

	private double scoreDistPacman(GHOST g, int pos, int newPos, MOVE m) {
		if (game.wasPacManEaten())
			return 0;
		
		double score = 0;
		int dist3 = game.getShortestPathDistance(pos, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
		int dist4 = game.getShortestPathDistance(newPos, game.getPacmanCurrentNodeIndex(), m);
		//Score associated to the nearest chasing ghost
		score += -k1 / (dist3 + 5);
		score += k1 / (dist4 + 5);
		
		if(game.isGhostEdible(g) || isCloseToPPill()) {
			if (dist3 > SECURITY_DIST_PACMAN)
				return 0;
			return -score;
		}
		return score;
	}
	

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
	
	private boolean isCloseToPPill() {
		int pacman = game.getPacmanCurrentNodeIndex();
		for (int pp : game.getActivePowerPillsIndices())
			if (SECURITY_DIST_PPILL >= game.getDistance(pacman, pp, Constants.DM.PATH))
				return true;
		return false;
	}
}
package es.ucm.fdi.ici.c2324.practica1.grupoYY;

import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class MsPacMan extends PacmanController{
	
	private Game game;
	private double distMax = Math.sqrt(2 * game.getNumberOfNodes());

    @Override
    public MOVE getMove(Game game, long timeDue) {
    	this.game = game;
    	
    	int pos = game.getPacmanCurrentNodeIndex(); //MsPacMans position 
    	MOVE[] possibleMoves = game.getPossibleMoves(pos, game.getPacmanLastMoveMade()); //Possible moves of MsPacMan
    	
    	//If there is more than one possible movement
    	if (possibleMoves.length > 1) {
    		double maxScore = Double.MIN_VALUE, newScore; 
    		MOVE bestMove = null;
    		
    		//The best movement of the ones possible is selected
    		for(MOVE m : possibleMoves) {
    			newScore = calcScore(pos, m);
    			//If the actual movement is better, is saved as the best one until this moment
    			if(newScore > maxScore) {
    				maxScore = newScore;
    				bestMove = m;
    			}
    		}
    		
    		return bestMove;
    	}
    	
    	
        return possibleMoves[0];
    }
    
    private double calcScore(int pos, MOVE move) {
    	int newPos = game.getNeighbour(pos, move);
    	double score = scoreDistGhost(pos, newPos, move)
    				 + scorePPill(pos, newPos, move)
    				 + scorePill(pos, newPos, move);
    	
    	return score;
    }
    
    private double scoreDistGhost(int pos, int newPos, MOVE m) {
    	double score = 0;
    	GHOST nearestEdible = getNearestEdibleGhost((int) distMax), nearestChasing = getNearestChasingGhost((int) distMax);
    	
    	//If there is an edible ghost
    	if(nearestEdible != null) {
    		//Score associated to the nearest edible ghost
    		score = -1 / game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearestEdible), game.getPacmanLastMoveMade());
    		score += 1 / game.getShortestPathDistance(newPos, game.getGhostCurrentNodeIndex(nearestEdible), m);
    	}
    	
    	//If there is a chasing ghost
    	if(nearestChasing != null) {
    		//Score associated to the nearest chasing ghost
    		score +=  1 / game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearestChasing), game.getGhostLastMoveMade(nearestChasing));
    		score += -1 / game.getShortestPathDistance(newPos, game.getGhostCurrentNodeIndex(nearestEdible), m);
    	}
    	
    	return score;
    }
    
    private double scorePPill(int pos, int newPos, MOVE m) {
    	if (game.getNumberOfActivePowerPills() < 1)
    		return 0;
    	
    	double cercaniaFantasma;
    	try {
	    	cercaniaFantasma = game.getManhattanDistance(pos, 
	    			game.getGhostCurrentNodeIndex(getNearestChasingGhost((int)distMax)));
    	} catch (NullPointerException e) {
    		return 0;
    	}
    	
    	Integer nearestPPill = getNearestPowerPill(), distPPill = 0;
    	//If the are remaining power pills
    	if(nearestPPill != null) {
    		//Score associated to the nearest power pill
    		distPPill = (int) (game.getShortestPathDistance(pos, nearestPPill, game.getPacmanLastMoveMade()) - (game.getShortestPathDistance(newPos, nearestPPill, m)));
    	}
    	
    	return distPPill * cercaniaFantasma;
    }
    
    
    private double scorePill(int pos, int newPos, MOVE m) {
    	Integer nearestPPill = getNearestPowerPill(), distPPill = 0;
    	//If the are remaining power pills
    	if(nearestPPill != null) {
    		//Score associated to the nearest power pill
    		distPPill = (int) (game.getShortestPathDistance(pos, nearestPPill, game.getPacmanLastMoveMade()) - (game.getShortestPathDistance(newPos, nearestPPill, m)));
    	}
    	
    	return (game.getNumberOfPills() - game.getNumberOfActivePills()) ^ 2 * distPPill;
    }
	
	private GHOST getNearestChasingGhost(int limit) {
		double minDist = limit + 1;
		GHOST nearest = null;
		for (GHOST g : GHOST.values()) {
			double dist = game.getDistance(game.getPacmanCurrentNodeIndex(), 
										game.getGhostCurrentNodeIndex(g), 
										Constants.DM.PATH);
			if (dist < minDist && !game.isGhostEdible(g))
				nearest = g;
		}
		return nearest;
	}
	
	private GHOST getNearestEdibleGhost(int limit) {
		double minDist = limit + 1;
		GHOST nearest = null;
		for (GHOST g : GHOST.values()) {
			double dist = game.getDistance(game.getPacmanCurrentNodeIndex(), 
										game.getGhostCurrentNodeIndex(g), 
										Constants.DM.PATH);
			if (dist < minDist && game.isGhostEdible(g))
				nearest = g;
		}
		return nearest;
	}
	
	private int getNearestPill() {
		int nearest = Integer.MAX_VALUE;
		for (int i : game.getActivePillsIndices()) {
			if (i >= 0 && nearest > game.getDistance(i, game.getPacmanCurrentNodeIndex(), Constants.DM.PATH))
				nearest = i;
		}
		return (nearest < Integer.MAX_VALUE ? nearest : -1);
	}
	
	private int getNearestPowerPill() {
		int distNearestPPill = Integer.MAX_VALUE, dist;
		Integer nearestPPill = null;
		
		for(int pp : game.getActivePowerPillsIndices()) {
			dist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pp, game.getPacmanLastMoveMade());
			if(dist < distNearestPPill) {
				distNearestPPill = dist;
				nearestPPill = pp;
			}
		}
		
		return nearestPPill;
	}
}

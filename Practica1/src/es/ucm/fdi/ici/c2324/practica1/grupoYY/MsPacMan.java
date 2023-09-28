package es.ucm.fdi.ici.c2324.practica1.grupoYY;

import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class MsPacMan extends PacmanController{
	
	private Game game;
	private double distMax;

    @Override
    public MOVE getMove(Game game, long timeDue) {
    	this.game = game;
    	distMax = Math.sqrt(2 * game.getNumberOfNodes());
    	
    	
        return MOVE.NEUTRAL;
    }
    
    private double calcScore(int pos, MOVE move) {
    	int newPos = game.getNeighbour(pos, move);
    	double score = scoreDistFantasma(pos, newPos)
    				 + scorePPill(pos, newPos)
    				 + scorePill(pos, newPos);
    	
    	return score;
    }
    
    private double scorePPill(int pos, int newPos) {
    	if (game.getNumberOfActivePowerPills() < 1)
    		return 0;
    	
    	double cercaniaFantasma;
    	try {
	    	cercaniaFantasma = game.getManhattanDistance(pos, 
	    			game.getGhostCurrentNodeIndex(getNearestChasingGhost((int)distMax)));
    	} catch (NullPointerException e) {
    		return 0;
    	}
    	int distPPill = (int) (distPPill(pos) - distPPill(newPos));
    	
    	return distPPill * cercaniaFantasma;
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
}

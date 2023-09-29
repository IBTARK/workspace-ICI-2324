package es.ucm.fdi.ici.c2324.practica1.grupoYY;

import java.util.ArrayList;

import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class MsPacMan extends PacmanController{
	
	private Game game;
	private double distMax;
	private double k1 = 100000.0; //Constant of edible ghost
	private double k2 = 1000000.0; //Constant of chasing ghost
	private double k3 = 1.0/5.0; //Constant of power pills
	private double k4 = 1.0/10.0; //Constant of pills
	private double k5 = 100000;

    @Override
    public MOVE getMove(Game game, long timeDue) {
    	this.game = game;
    	distMax = Math.sqrt(2 * game.getNumberOfNodes());
    	
    	int pos = game.getPacmanCurrentNodeIndex(); //MsPacMans position 
    	MOVE[] possibleMoves = game.getPossibleMoves(pos, game.getPacmanLastMoveMade()); //Possible moves of MsPacMan
    	
    	//If there is more than one possible movement
    	if (possibleMoves.length > 1) {
    		double maxScore = Double.MIN_VALUE, newScore; 
    		MOVE bestMove = null;
    		ArrayList<Double> bestScores = null;
    		
    		//The best movement of the ones possible is selected
    		for(MOVE m : possibleMoves) {
    			ArrayList<Double> newScores = calcScore(pos, m);
    			newScore = newScores.get(0);
    			//If the actual movement is better, is saved as the best one until this moment
    			if(newScore > maxScore) {
    				maxScore = newScore;
    				bestScores = newScores;
    				bestMove = m;
    			}
    		}
    		
    		System.out.println(bestScores);
    		
    		return bestMove;
    	}
    	
    	
        return possibleMoves[0];
    }
    
    private ArrayList<Double> calcScore(int pos, MOVE move) {
    	int newPos = game.getNeighbour(pos, move);
    	
    	double scoreGhost = scoreDistGhost(pos, newPos, move);
    	double scorePPill = scorePPill(pos, newPos, move);
    	double scorePill = scorePill(pos, newPos, move);
    	double score = scoreGhost + scorePPill + scorePill;
    	
    	ArrayList<Double> scoreList = new ArrayList<Double>();
    	
    	scoreList.add(score);
    	scoreList.add(scoreGhost);
    	scoreList.add(scorePPill);
    	scoreList.add(scorePill);
    	
    	return scoreList;
    }
    
    private double scoreDistGhost(int pos, int newPos, MOVE m) {
    	double score = 0;
    	GHOST nearestEdible = getNearestEdibleGhost((int) distMax), nearestChasing = getNearestChasingGhost();
    	
    	//If there is an edible ghost and the remaining edible time is superior than 10 ticks
    	if(nearestEdible != null && game.getGhostEdibleTime(nearestEdible) > 20) {
    		//Score associated to the nearest edible ghost
    		score = -k1 / game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearestEdible), game.getPacmanLastMoveMade());
    		score += k1 / game.getShortestPathDistance(newPos, game.getGhostCurrentNodeIndex(nearestEdible));
    	}
    	
    	//If there is a chasing ghost
    	if(nearestChasing != null) {
    		//Score associated to the nearest chasing ghost
    		score +=  k2 / game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearestChasing));
    		score += -k2 / game.getShortestPathDistance(newPos, game.getGhostCurrentNodeIndex(nearestChasing));
    		

    		
			for (GHOST g : GHOST.values()) {
				int ghostNode = game.getGhostCurrentNodeIndex(g); 
				
				if (!game.isGhostEdible(g) && ghostNode < game.getNumberOfNodes() - 1){
					score +=  k5 / game.getShortestPathDistance(pos, ghostNode, game.getPacmanLastMoveMade());
		    		score += -k5 / game.getShortestPathDistance(newPos, ghostNode, game.getPacmanLastMoveMade());	
				}
			}
				
		}
    	
    	return score;
    }
    
    private double scorePPill(int pos, int newPos, MOVE m) {
    	if (game.getNumberOfActivePowerPills() < 1)
    		return 0;
    	
    	double cercaniaFantasma;
    	try {
	    	cercaniaFantasma = game.getManhattanDistance(pos, 
	    			game.getGhostCurrentNodeIndex(getNearestChasingGhost()));
    	} catch (NullPointerException e) {
    		return 0;
    	}
    	
    	Integer nearestPPill = getNearestPowerPill();
		//Score associated to the nearest power pill
		int distPPill = (game.getShortestPathDistance(pos, nearestPPill, game.getPacmanLastMoveMade()) - (game.getShortestPathDistance(newPos, nearestPPill, m)));
    	double score = (cercaniaFantasma < 10 ? 1 : -1) * distPPill * cercaniaFantasma * k3;
    	return score;
    }
    
    
    private double scorePill(int pos, int newPos, MOVE m) {
    	Integer nearestPill = getNearestPill(), distPill = 0;
    	//If the are remaining power pills
    	if(nearestPill != null) {
    		//Score associated to the nearest power pill
    		distPill = (int) (game.getShortestPathDistance(pos, nearestPill, game.getPacmanLastMoveMade()) - (game.getShortestPathDistance(newPos, nearestPill, m)));
    	}
		double score = ((game.getNumberOfPills() - game.getNumberOfActivePills()) ^ 2) * distPill * k4;
    	return score;
    }
	
	private GHOST getNearestChasingGhost() {
		double minDist = Integer.MAX_VALUE;
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
	
	private Integer getNearestPill() {
		Integer nearest = null, dist = Integer.MAX_VALUE;
		for (int i : game.getActivePillsIndices()) {
			int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), i, game.getPacmanLastMoveMade());
			if (dist > aux) {
				nearest = i;
				dist = aux;
			}
		}
		return nearest;
	}
	
	private Integer getNearestPowerPill() {
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

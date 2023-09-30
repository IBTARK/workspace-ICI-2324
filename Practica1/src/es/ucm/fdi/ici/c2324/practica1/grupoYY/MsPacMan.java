package es.ucm.fdi.ici.c2324.practica1.grupoYY;

import java.util.ArrayList;
import java.util.Arrays;

import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Constants;
import pacman.game.Game;

public class MsPacMan extends PacmanController{
	
	private Game game;
	private double distMax;
	private double k1 = 900.0; //Constant of edible ghost (0-1800)
	private double k2 = 1800.0; //Constant of nearest chasing ghost (0-1800)
	private double k3 = 100.0; //Constant of power pills (0-300)
	private double k4 = 1.0/90.0; //Constant of pills (0-1/3)
	private double k5 = 0;   //Constant of other chasing ghosts (0-1800/3)

    @Override
    public MOVE getMove(Game game, long timeDue) {
    	this.game = game;
    	distMax = Math.sqrt(2 * game.getNumberOfNodes());

		int[] pps = game.getPowerPillIndices();
		System.out.println(game.getShortestPathDistance(pps[0], pps[1]));
		System.out.println(game.getShortestPathDistance(pps[0], pps[2]));
		System.out.println(game.getShortestPathDistance(pps[0], pps[3]));
    	
    	int pos = game.getPacmanCurrentNodeIndex(); //MsPacMans position 
    	MOVE[] possibleMoves = game.getPossibleMoves(pos, game.getPacmanLastMoveMade()); //Possible moves of MsPacMan
    	
    	System.out.println("Possible movements: " + new ArrayList(Arrays.asList(possibleMoves)) + "\n\n");
    	
    	//If there is more than one possible movement
    	if (possibleMoves.length > 1) {
    		double maxScore = Double.NEGATIVE_INFINITY, newScore; 
    		MOVE bestMove = null;
    		ArrayList<String> bestScores = new ArrayList<>();
    		
    		//The best movement of the ones possible is selected
    		for(MOVE m : possibleMoves) {
    			ArrayList<String> newScores = calcScore(pos, m);
    			newScore = Double.parseDouble(newScores.get(0));
    			//If the actual movement is better, is saved as the best one until this moment
    			if(newScore > maxScore) {
    				maxScore = newScore;
    				bestScores = newScores;
    				bestMove = m;
    			}
    		}
    		
    		System.out.println("Chosen move: " + bestMove + "   Scores: " + bestScores.toString() + "\n\n\n");
    		
    		return bestMove;
    	}
    	
    	
        return possibleMoves[0];
    }
    
    private ArrayList<String> calcScore(int pos, MOVE move) {
    	int newPos = game.getNeighbour(pos, move);
    	
    	ArrayList<Double> scoreGhost = scoreDistGhost(pos, newPos, move);
    	double scorePPill = scorePPill(pos, newPos, move);
    	double scorePill = scorePill(pos, newPos, move);
    	double score = scoreGhost.get(0) + scorePPill + scorePill;
    	
    	ArrayList<String> scoreList = new ArrayList<String>();
    	
    	scoreList.add(Double.toString(score));
    	scoreList.add("Edible Ghost score: " + Double.toString(scoreGhost.get(1)));
    	scoreList.add("Chasing Ghost score: " + Double.toString(scoreGhost.get(2)));
    	scoreList.add("Other Chasing Ghosts score: " + Double.toString(scoreGhost.get(3)));
    	scoreList.add("Power pill score: " + Double.toString(scorePPill));
    	scoreList.add("Pill score: " + Double.toString(scorePill));
    	
    	System.out.println(scoreList + "\n\n");
    	
    	return scoreList;
    }
    
    //Score associated to the ghosts 
    private ArrayList<Double> scoreDistGhost(int pos, int newPos, MOVE m) {
    	double score = 0, score0 = 0;
    	GHOST nearestEdible = getNearestEdibleGhost((int) distMax), nearestChasing = getNearestChasingGhost();
    	ArrayList<Double> ghostScores = new ArrayList<Double>();
    	
    	//If there is an edible ghost and the remaining edible time is superior than 10 ticks
    	if(nearestEdible != null && game.getGhostEdibleTime(nearestEdible) > 20) {
    		int dist1 = game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearestEdible), game.getPacmanLastMoveMade());
    		int dist2 = game.getShortestPathDistance(newPos, game.getGhostCurrentNodeIndex(nearestEdible), m);
    		//Score associated to the nearest edible ghost
    		score0 = -k1 / (dist1 > 0 ? dist1 : 0.1);
    		score0 += k1 / (dist2 > 0 ? dist2 : 0.1);
    		
    		System.out.println("Dist neares edible pos: " +  dist1 + " Dist nearest edible newPos: " + dist2);
    	}
    	
    	score += score0;
    	
    	double score1 = 0, score2 = 0;
    	
    	//If there is a chasing ghost
    	if(nearestChasing != null) {
    		int dist3 = game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearestChasing));
    		int dist4 = game.getShortestPathDistance(newPos, game.getGhostCurrentNodeIndex(nearestChasing));
    		//Score associated to the nearest chasing ghost
    		score1 +=  k2 / (dist3 > 0 ? dist3 : 0.1);
    		score1 += -k2 / (dist4 > 0 ? dist4 : 0.1);
    		
    		score += score1;
    		
    		System.out.println("Dist neares chasing pos: " +  dist3 + " Dist nearest chasing newPos: " + dist4);
    		
    		//The rest of the chasing ghost are taken into consideration
			for (GHOST g : GHOST.values()) {
				int ghostNode = game.getGhostCurrentNodeIndex(g); 
				
				//It the ghost is not the nearest chasing one and its edible and its not on the lair, is taken into consideration
				if (g != nearestChasing && !game.isGhostEdible(g) && game.getGhostLairTime(g) <= 0){
					int dist5 = game.getShortestPathDistance(pos, ghostNode, game.getPacmanLastMoveMade());
					int dist6 = game.getShortestPathDistance(newPos, ghostNode, game.getPacmanLastMoveMade());
					score2 +=  k5 / (dist5 > 0 ? dist5 : 0.1);
		    		score2 += -k5 / (dist6 > 0 ? dist6 : 0.1);	
		    		
		    		System.out.println("Dist chasing pos ghost " +  g + ": " + dist5 + " Dist chasing newPos ghost " + g + ": " + dist6);
				}
			}
			
			score += score2;	
		}
    	
    	ghostScores.add(score);
    	ghostScores.add(score0);
    	ghostScores.add(score1);
    	ghostScores.add(score2);
    	
    	return ghostScores;
    }
    
    //Score associated to the power pills
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
    	int distPPill1 = game.getShortestPathDistance(pos, nearestPPill, game.getPacmanLastMoveMade());
    	int distPPill2 = game.getShortestPathDistance(newPos, nearestPPill, m);
    	System.out.println("Dist Power Pill pos: " + distPPill1 + " Dist Power Pill newPos: " + distPPill2);
    	
		double score = k3 / cercaniaFantasma;
    	return score;
    }
    
    //Score associated to the pills
    private double scorePill(int pos, int newPos, MOVE m) {
    	Integer nearestPill = getNearestPill(), distPill = 0;
    	//If the are remaining power pills
    	if(nearestPill != null) {
    		double distPill1 = game.getShortestPathDistance(pos, nearestPill, game.getPacmanLastMoveMade());
    		double distPill2 = (game.getShortestPathDistance(newPos, nearestPill, m));
    		//Score associated to the nearest power pill
    		distPill = (int) (distPill1 - distPill2);
    		
    		System.out.println("Dist Pill pos: " + distPill1 + " Dist Pill newPos: " + distPill2);
    	}
    	
    	//The fewer remaining pills there are more important it is to take them
		double score = ((game.getNumberOfPills() - game.getNumberOfActivePills()) ^ 2) * distPill * k4;
    	return Math.max(score, -20);
    }
	
    //Get the nearest chasing ghost
	private GHOST getNearestChasingGhost() {
		double minDist = Integer.MAX_VALUE;
		GHOST nearest = null;
		for (GHOST g : GHOST.values()) {
			//Only those that are not on the lair
			if(game.getGhostLairTime(g) <= 0) {
				double dist = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), Constants.DM.PATH);
				if (dist < minDist && !game.isGhostEdible(g))
					nearest = g;
			}
		}
		return nearest;
	}
	
	//Gets the nearest edible ghost
	private GHOST getNearestEdibleGhost(int limit) {
		double minDist = limit + 1;
		GHOST nearest = null;
		for (GHOST g : GHOST.values()) {
			double dist = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), Constants.DM.PATH);
			if (dist < minDist && game.isGhostEdible(g))
				nearest = g;
		}
		return nearest;
	}
	
	//Gets the nearest pill
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

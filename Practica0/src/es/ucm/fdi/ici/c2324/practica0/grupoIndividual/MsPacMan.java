package es.ucm.fdi.ici.c2324.practica0.grupoIndividual;

import java.awt.Color;

import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.GameView;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacMan  extends PacmanController {
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		int limit = 20;
		//Show way to ghosts
    	Color[] colors = {Color.RED, Color.PINK, Color.CYAN, Color.ORANGE};
		
		//Nearest non edible ghost to MsPacMan within limit distance
		GHOST nearestChasing = getNearestChasingGhostFromMsPacMan(game, limit);
		
		//If there is a none edible chasing ghost close enough from MsPacMan
		if(nearestChasing != null) {
			int ghostPos = game.getGhostCurrentNodeIndex(nearestChasing);
    		int mspacmanPos = game.getPacmanCurrentNodeIndex();
    		if(game.getGhostLairTime(nearestChasing)<=0)
    			GameView.addPoints(game,colors[nearestChasing.ordinal()],game.getShortestPath(ghostPos,mspacmanPos));
			return game.getApproximateNextMoveAwayFromTarget(mspacmanPos, ghostPos, game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		//Nearest edible ghost to MsPacMan within limit distance
		GHOST nearestEdible = getNearestEdibleGhostFromMsPacMan(game, limit);
		
		if(nearestEdible != null) {
			int ghostPos = game.getGhostCurrentNodeIndex(nearestEdible);
    		int mspacmanPos = game.getPacmanCurrentNodeIndex();
    		if(game.getGhostLairTime(nearestEdible)<=0)
    			GameView.addPoints(game,colors[nearestEdible.ordinal()],game.getShortestPath(ghostPos,mspacmanPos));
			return game.getApproximateNextMoveAwayFromTarget(mspacmanPos, ghostPos, game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		
		
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), getNearestPillNode(game) ,game.getPacmanLastMoveMade(), DM.PATH);
		
	}
	
	//Returns the nearest none edible ghost to MsPacMan that is within limit distance
	private GHOST getNearestChasingGhostFromMsPacMan(Game game, int limit) {
		double min =  Double.MAX_VALUE;
		GHOST awayFrom = null;
		
		for (GHOST ghostType : GHOST.values()) {
			//If the ghost is not edible
			if(!game.isGhostEdible(ghostType)) {
				double dis = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
				
				if(dis < min && dis < limit) {
					awayFrom = ghostType;
					min = dis;
				}
			}
        }
		
		return awayFrom;
	}
	
	//Returns the nearest edible ghost to MsPacMan that is within limit distance
	private GHOST getNearestEdibleGhostFromMsPacMan(Game game, int limit) {
		double min =  Double.MAX_VALUE;
		GHOST awayFrom = null;
		
		for (GHOST ghostType : GHOST.values()) {
			//If the ghost is not edible
			if(game.isGhostEdible(ghostType)) {
				double dis = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), DM.PATH);
				
				if(dis < min && dis < limit) {
					awayFrom = ghostType;
					min = dis;
				}
			}
        }
		
		return awayFrom;
	}
	
	//Returns the node of the nearest pill
	private int getNearestPillNode(Game game) {
		int node = -1;
		double minDist = Double.MAX_VALUE, dist;
		
		//Search for the nearest pill to msPacMan
		for(int i : game.getActivePillsIndices()) {
			dist = game.getDistance(game.getPacmanCurrentNodeIndex(), i, DM.PATH);
			if(dist < minDist) {
				node = i;
				minDist = dist;
			}
		}
		
		//Search if there is any power pill closer than a regular pill
		for(int i : game.getActivePowerPillsIndices()) {
			dist = game.getDistance(game.getPacmanCurrentNodeIndex(), i, DM.PATH);
			if(dist < minDist) {
				node = i;
				minDist = dist;
			}
		}
		
		return node;
	}

}

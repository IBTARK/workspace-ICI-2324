package es.ucm.fdi.ici.c2324.practica0.grupoIndividual;

import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacMan  extends PacmanController {
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		int limit = 20;
		
		//Nearest non edible ghost to MsPacMan within limit distance
		GHOST nearestChasing = getNearestChasingGhostFromMsPacMan(game, limit);
		
		//If there is a none edible chasing ghost close enough from MsPacMan
		if(nearestChasing != null) {
			return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestChasing), game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		//Nearest edible ghost to MsPacMan within limit distance
		GHOST nearestEdible = getNearestEdibleGhostFromMsPacMan(game, limit);
		
		if(nearestEdible != null) {
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(nearestEdible), game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		
		
		return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),  ,game.getPacmanLastMoveMade(), DM.PATH);
		
	}
	
	//Returns the nearest none edible ghost to MsPacMan that is within limit distance
	private GHOST getNearestChasingGhostFromMsPacMan(Game game, int limit) {
		double min = 1000000;
		GHOST awayFrom = null;
		
		for (GHOST ghostType : GHOST.values()) {
			//If the ghost is not edible
			if(!game.isGhostEdible(ghostType)) {
				double dis = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), game.getGhostLastMoveMade(ghostType), DM.PATH);
				
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
		double min = 1000000;
		GHOST awayFrom = null;
		
		for (GHOST ghostType : GHOST.values()) {
			//If the ghost is not edible
			if(game.isGhostEdible(ghostType)) {
				double dis = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), game.getGhostLastMoveMade(ghostType), DM.PATH);
				
				if(dis < min && dis < limit) {
					awayFrom = ghostType;
					min = dis;
				}
			}
        }
		
		return awayFrom;
	}

}

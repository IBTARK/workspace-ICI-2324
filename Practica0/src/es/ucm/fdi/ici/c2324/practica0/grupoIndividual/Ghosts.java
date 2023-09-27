package es.ucm.fdi.ici.c2324.practica0.grupoIndividual;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class Ghosts extends GhostController{
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private Random rnd = new Random();
	private MOVE[] allMoves = MOVE.values();
	 
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		moves.clear();
		int limitNearPill = 15;
		
        for (GHOST ghostType : GHOST.values()) {
            if (game.doesGhostRequireAction(ghostType)) {
            	if(game.isGhostEdible(ghostType) || getNearestPillNode(game,limitNearPill)) {
            		moves.put(ghostType, game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType),DM.PATH));
            	}
            	else {
            		if(rnd.nextDouble(1) < 0.9) {
            			moves.put(ghostType, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(ghostType), DM.PATH));
            		}
            		else {
            			moves.put(ghostType, allMoves[rnd.nextInt(allMoves.length)]);
            		}
            	}
            }
        }
        return moves;
	}
	
	//Returns the node of the nearest pill
	private boolean getNearestPillNode(Game game, int limit) {
		int node = -1;
		double minDist = Double.MAX_VALUE, dist;
		
		//Search if there is any power pill closer than a regular pill
		for(int i : game.getActivePowerPillsIndices()) {
			dist = game.getDistance(game.getPacmanCurrentNodeIndex(), i, DM.PATH);
			if(dist < minDist) {
				node = i;
				minDist = dist;
			}
		}
		
		return minDist <= limit;
	}

}

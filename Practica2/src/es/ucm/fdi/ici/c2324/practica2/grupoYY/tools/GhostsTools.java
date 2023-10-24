package es.ucm.fdi.ici.c2324.practica2.grupoYY.tools;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsTools {

	//Get the closest PPill to the given ghost
	public static int getClosestPPill(Game game, GHOST ghost) {
		int minDist = Integer.MAX_VALUE, closestPPill = -1;
		//Index of the ghost
		int pos = game.getGhostCurrentNodeIndex(ghost);
		//Last movement made by the ghost
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//Search for the closest PPill to the ghost
		for(int i : game.getActivePowerPillsIndices()) {
			int dist = game.getShortestPathDistance(pos, i, lastMove);
			if(dist < minDist) {
				minDist = dist;
				closestPPill = i;
			}
		}
		
		return closestPPill;
	}
	
	
	public static GHOST getNearestChasing(Game game, GHOST ghost) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		for (GHOST g : GHOST.values()) {
			int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), 
													game.getGhostCurrentNodeIndex(g), 
													game.getGhostLastMoveMade(ghost));
			if (!g.equals(ghost) && !game.isGhostEdible(g) && dist >= 0 && minDist > dist) {
				minDist = dist;
				nearest = g;
			}
		}
		return nearest;
	}
	
	public static GHOST getNearestEdible(Game game, GHOST ghost) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		for (GHOST g : GHOST.values()) {
			int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), 
													game.getGhostCurrentNodeIndex(g), 
													game.getGhostLastMoveMade(ghost));
			if (!g.equals(ghost) && game.isGhostEdible(g) && dist >= 0 && minDist > dist) {
				minDist = dist;
				nearest = g;
			}
		}
		return nearest;
	}
	
	public static int nextJunction(Game game, int pos, MOVE lastMove) {
		MOVE move = lastMove;
		while (!game.isJunction(pos)) {
			move = game.getPossibleMoves(pos, move)[0];
			pos = game.getNeighbour(pos, move);
		}
		return pos;
	}
	
	public static boolean blocked(Game game, GHOST orig, GHOST dest) {
		int posOrig = game.getGhostCurrentNodeIndex(orig),
			posDest = game.getGhostCurrentNodeIndex(dest);
		for (int node : game.getShortestPath(posOrig, posDest, game.getGhostLastMoveMade(orig))) 
			if (game.getPacmanCurrentNodeIndex() == node)
				return true;
		return false;
	}
}

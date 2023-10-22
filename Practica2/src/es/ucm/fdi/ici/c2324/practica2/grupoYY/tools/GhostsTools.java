package es.ucm.fdi.ici.c2324.practica2.grupoYY.tools;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsTools {

	public static GHOST getNearestChasing(Game game, GHOST ghost) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		for (GHOST g : GHOST.values()) {
			int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), 
													game.getGhostCurrentNodeIndex(g), 
													game.getGhostLastMoveMade(ghost));
			if (!g.equals(ghost) && dist >= 0 && minDist > dist) {
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

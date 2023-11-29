package es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsTools {
	
	// FOR DEBUG -----------------------------------------------------------
	public static boolean debug() {
		return false;
	}
	// ---------------------------------------------------------------------

	/**
	 * Get the closest PPill to the given ghost
	 * @param game
	 * @param ghost
	 * @return
	 */
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
	
	/**
	 * Nearest chasing ghost to the given one
	 * @param game
	 * @param ghost
	 * @return
	 */
	public static GHOST getNearestChasing(Game game, GHOST ghost) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		
		//Find the nearest chasing ghost 
		for (GHOST g : GHOST.values()) {
			//If the ghost is not on the lair
			if(game.getGhostLairTime(g) <= 0 && !game.isGhostEdible(g) && !g.equals(ghost)) {
				int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), 
						game.getGhostCurrentNodeIndex(g), 
						game.getGhostLastMoveMade(ghost));
				if (dist >= 0 && minDist > dist) {
					minDist = dist;
					nearest = g;
				}
			}
		}
		return nearest;
	}
	
	/**
	 * Nearest edible ghost to the given one
	 * @param game
	 * @param ghost
	 * @return
	 */
	public static GHOST getNearestEdible(Game game, GHOST ghost) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		
		//Find the nearest edible ghost
		for (GHOST g : GHOST.values()) {
			//If the ghost is not on the lair
			if(game.getGhostLairTime(g) <= 0 && !g.equals(ghost) && game.isGhostEdible(g)) {
				int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), 
						game.getGhostCurrentNodeIndex(g), 
						game.getGhostLastMoveMade(ghost));
				if (dist >= 0 && minDist > dist) {
					minDist = dist;
					nearest = g;
				}
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
	
	/**
	 * Nearest chasing not blocked ghost to the given one (CAREFUL: it can return null)
	 * @param game
	 * @param ghost
	 * @return
	 */
	public static GHOST getNearestChasingNotBlocked(Game game, GHOST ghost) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		
		//Find the nearest chasing ghost 
		for (GHOST g : GHOST.values()) {
			//If the ghost is not on the lair
			if(game.getGhostLairTime(g) <= 0) {
				//Index of the given ghost
				int pos = game.getGhostCurrentNodeIndex(ghost);
				//Index of the other ghost
				int pos2 = game.getGhostCurrentNodeIndex(g);
				//Last movement of the given ghost
				MOVE lastMove = game.getGhostLastMoveMade(ghost);
				
				int dist = game.getShortestPathDistance(pos, pos2, lastMove);
				
				if (!g.equals(ghost) && !game.isGhostEdible(g) && !blocked(game, ghost, g) && minDist > dist) {
					minDist = dist;
					nearest = g;
				}
			}
		}
		return nearest;
	}
	
	public static MOVE goTo(Game game, int pos, int dest, MOVE lastMove) {
		int distMin = Integer.MAX_VALUE;
		MOVE bestMove = null;
		for (MOVE m : game.getPossibleMoves(pos, lastMove)) {
			int dist = game.getShortestPathDistance(game.getNeighbour(pos, m), dest, m);
			if (dist < distMin) {
				distMin = dist;
				bestMove = m;
			}
		}
		return bestMove;
	}
}

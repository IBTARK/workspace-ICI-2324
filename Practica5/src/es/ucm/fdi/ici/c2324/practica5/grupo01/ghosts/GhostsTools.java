package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts;


import java.util.ArrayList;
import java.util.Map;

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
	 * Nearest chasing ghost to the given one.
	 * Ignores the maxDistance
	 * @param game
	 * @param ghost
	 * @return
	 */
	public static GHOST getNearestChasing(Game game, GHOST ghost) {
		return getNearestChasing(game, ghost, Integer.MAX_VALUE);
	}
	
	/**
	 * Nearest chasing ghost to the given one that's not farther than maxDistance
	 * @param game
	 * @param ghost
	 * @return
	 */
	public static GHOST getNearestChasing(Game game, GHOST ghost, int maxDistance) {
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
	 * Nearest edible ghost to the given one.
	 * Ignores the maxDistance
	 * @param game
	 * @param ghost
	 * @return
	 */
	public static GHOST getNearestEdible(Game game, GHOST ghost) {
		return getNearestEdible(game, ghost, Integer.MAX_VALUE);
	}
	
	/**
	 * Nearest edible ghost to the given one that does have a distance greater than maxDistance
	 * @param game
	 * @param ghost
	 * @param maxDistance
	 * @return
	 */
	public static GHOST getNearestEdible(Game game, GHOST ghost, int maxDistance) {
		/*
		En vez de hacer sobrecarga de metodos se podria hacer por Varargs:
		GHOST getNearestEdible(Game game, GHOST ghost, Integer... max)
		Integer maxDistance = max.length == 1 ?  max[0] : Integer.MAX_VALUE;
		*/
		
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		
		//Find the nearest edible ghost
		for (GHOST g : GHOST.values()) {
			//If the ghost is not on the lair
			if(game.getGhostLairTime(g) <= 0 && !g.equals(ghost) && game.isGhostEdible(g)) {
				int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), 
						game.getGhostCurrentNodeIndex(g), 
						game.getGhostLastMoveMade(ghost));
				if (dist >= 0 && dist < maxDistance && minDist > dist ) {
					minDist = dist;
					nearest = g;
				}
			}
		}
		return nearest;
	}
	
	public static Integer[] nextJunctionPath(Game game, int pos, MOVE lastMove) {
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(pos);
		MOVE move = lastMove;
		while (!game.isJunction(pos)) {
			move = game.getPossibleMoves(pos, move)[0];
			pos = game.getNeighbour(pos, move);
			path.add(pos);
		}
		return path.toArray(new Integer[path.size()]);
	}
	
	public static int nextJunction(Game game, int pos, MOVE lastMove) {
		Integer path[] = nextJunctionPath(game, pos, lastMove);
		return path[path.length-1];
	}
	
	/**
	 * Nos devuelve nextJunction en el primer indice, 
	 * y el resto de indices (min 2 max 3 más) tienen los level2Junctions
	 * 
	 * @param game
	 * @param pos
	 * @param lastMove
	 * @return
	 */
	public static Integer[] nextJunctions(Game game, int pos, MOVE lastMove, Map<Integer, Integer[]> lvl3Junctions) {
		// Contiene en el primer spot el firstJunction y en los siguientes los junctions de nivel 2
		ArrayList<Integer> nextJunctions = new ArrayList<Integer>();
		
		// Junction de primer nivel (puede coincidir con mspacman)
		Integer njPath[] = nextJunctionPath(game, pos, lastMove);
		pos = njPath[njPath.length-1];
		MOVE move = njPath.length == 1 ? lastMove 
				: game.getMoveToMakeToReachDirectNeighbour(njPath[njPath.length-2], njPath[njPath.length-1]);
		
		int firstJunction = pos;
		nextJunctions.add(firstJunction);
		
		// lvl2 y lvl3 junctions
		ArrayList<Integer> lvl3 = new ArrayList<>();
		// Una vez tenemos el primer junction (puede coincidir con la posicion de mspacman) calculamos los junctions de nivel 2
		// Junctions de nivel 2
		int lvl2Junction;
		for(MOVE m: game.getPossibleMoves(firstJunction, move)) {
			pos = game.getNeighbour(firstJunction, m);
			
			njPath =nextJunctionPath(game, pos, m);
			pos = njPath[njPath.length-1];
			m = game.getMoveToMakeToReachDirectNeighbour(njPath[njPath.length-2], njPath[njPath.length-1]);
			
			lvl2Junction = pos;
			nextJunctions.add(lvl2Junction);
			
			// Calulamos los junctions de nivel 3
			lvl3.clear();
			for(MOVE mtolvl3: game.getPossibleMoves(lvl2Junction, m)) {
				pos = game.getNeighbour(lvl2Junction, mtolvl3);
				
				lvl3.add(nextJunction(game, pos, mtolvl3));
			}
			lvl3Junctions.put(lvl2Junction, lvl3.toArray(new Integer[lvl3.size()]));
		}
		
		return nextJunctions.toArray(new Integer[nextJunctions.size()]);
	}
	
	public static boolean blocked(Game game, GHOST orig, GHOST dest) {
		int posOrig = game.getGhostCurrentNodeIndex(orig),
			posDest = game.getGhostCurrentNodeIndex(dest),
			pacman = game.getPacmanCurrentNodeIndex();
		for (int node : game.getShortestPath(posOrig, posDest, game.getGhostLastMoveMade(orig))) 
			if (pacman == node)
				return true;
		return false;
	}
	
	
	
	/**
	 * Nearest chasing not blocked ghost to the given one (CAREFUL: it can return null)
	 * It ignores the maxDistance
	 * @param game
	 * @param ghost
	 * @return
	 */
	public static GHOST getNearestChasingNotBlocked(Game game, GHOST ghost) {
		return getNearestChasingNotBlocked(game, ghost, Integer.MAX_VALUE);
	}
	
	/**
	 * Nearest chasing not blocked ghost to the given one that's not farther than maxDistance(CAREFUL: it can return null)
	 * @param game
	 * @param ghost
	 * @param maxDistance
	 * @return
	 */
	public static GHOST getNearestChasingNotBlocked(Game game, GHOST ghost, int maxDistance) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		
		//Find the nearest chasing ghost 
		for (GHOST g : GHOST.values()) {
			//If the ghost is not on the lair, g is not ghost, and the target ghost is not edible
			if(game.getGhostLairTime(g) <= 0 && !g.equals(ghost) && !game.isGhostEdible(g)) {
				//Index of the given ghost
				int pos = game.getGhostCurrentNodeIndex(ghost);
				//Index of the other ghost
				int pos2 = game.getGhostCurrentNodeIndex(g);
				//Last movement of the given ghost
				MOVE lastMove = game.getGhostLastMoveMade(ghost);
				
				int dist = game.getShortestPathDistance(pos, pos2, lastMove); 
				
				// If its not farther than the imposed maxDistance, 
				// it is not blocked, 
				// and its nearer than the minimum distance.
				if (dist < maxDistance && !blocked(game, ghost, g) && minDist > dist) {
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

	public static Integer distanceToMspacmanFromFront(Game game, int gIndex, MOVE gLastMove, int mspacman,
			MOVE msLastMove) {
		
		

		
		return null;
	}
}


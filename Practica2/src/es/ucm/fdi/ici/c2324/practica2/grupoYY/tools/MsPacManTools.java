package es.ucm.fdi.ici.c2324.practica2.grupoYY.tools;

import java.util.ArrayList;
import java.util.List;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManTools {

	public static List<int[]> possiblePaths(Game game, int orig, int dest) {
		List<int[]> paths = new ArrayList<>();
		for (MOVE m : MOVE.values())
			if (game.getNeighbour(dest, m.opposite()) > -1)
				paths.add(game.getShortestPath(dest, orig, m));
		return paths;
	}
	
	public static int closestPPill(Game game) {
		int node = -1, dist = Integer.MAX_VALUE;
		for (int p : game.getPowerPillIndices()) {
			int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), 
			 									   p, game.getPacmanLastMoveMade());
			if (dist > aux) {
				node = p;
				dist = aux;
			}
		}
		return node;
	}
	
	public static GHOST getNearestEdible(Game game, int pos, MOVE lastMove) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		for (GHOST g : GHOST.values()) {
			int dist = game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(g), lastMove);
			if (dist >= 0 && minDist > dist) {
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
}

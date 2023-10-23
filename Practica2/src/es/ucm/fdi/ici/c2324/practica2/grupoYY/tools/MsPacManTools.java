package es.ucm.fdi.ici.c2324.practica2.grupoYY.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManTools {

	public static List<Integer[]> possiblePaths(Game game, int orig, int dest, MOVE lastMove) {
		List<Integer[]> paths = new ArrayList<>();
		int maxDist = game.getShortestPathDistance(orig, dest, lastMove) * 2;
		
		//Búsqueda de caminos en anchura con poda
		Queue<ArrayList<Integer>> queue = new LinkedList<>();
		for (int n : game.getNeighbouringNodes(orig, lastMove))
			if (n == dest) paths.add(new Integer[] {orig, n});
			else queue.add(new ArrayList<Integer> (Arrays.asList(orig, n)));
		
		while (!queue.isEmpty()) {
			ArrayList<Integer> act = queue.remove();
			for (int n : game.getNeighbouringNodes(orig, lastMove)) {
				act.add(n);
				if (n == dest) paths.add((Integer[]) act.toArray());
				else if (maxDist > act.size() + game.getShortestPathDistance(act.get(act.size()-1), dest, 
								   				game.getMoveToMakeToReachDirectNeighbour(act.get(act.size()-2), act.get(act.size()-1)))) 
					queue.add(new ArrayList<Integer>(act));
				act.remove(act.size()-1);
			}
		}
		return paths;
	}
	
	public static int closestPPill(Game game) {
		int node = -1, dist = Integer.MAX_VALUE;
		for (int p : game.getActivePowerPillsIndices()) {
			int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), 
			 									   p, game.getPacmanLastMoveMade());
			if (dist > aux) {
				node = p;
				dist = aux;
			}
		}
		return node;
	}
	
	public static int closestPill(Game game) {
		int node = -1, dist = Integer.MAX_VALUE;
		for (int p : game.getActivePillsIndices()) {
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
	
	public static List<MOVE> movesInPath(Game game, Integer[] path) {
		List<MOVE> moves = new ArrayList<>();
		for (int i = 0; i < path.length-1; ++i)
			if (game.isJunction(path[i]))
				moves.add(game.getMoveToMakeToReachDirectNeighbour(path[i], path[i+1]));
		return moves;
	}
	
	//Checks if there is a ghost in the given path coming 
	public static boolean blocked(Game game, Integer[] path) {
		for(int node : path) {
			for(GHOST g :  GHOST.values())
				if(game.getGhostCurrentNodeIndex(g) == node) return true;
		}
		return false;
	}
	
	//Checks if there is a PPill in the given path coming 
	public static boolean blockedByClosestPPill(Game game, int[] path) {
		int ppillIndex = closestPPill(game);
		for(int node : path) {
			if(ppillIndex == node) return true;
		}
		return false;
	}

	public static GHOST getNearestChasing(Game game, int pos, MOVE lastMove) {
		GHOST nearest = null;
		int aux = Integer.MAX_VALUE, dist = Integer.MAX_VALUE;
		
		for (GHOST g : GHOST.values()) {
			if(!game.isGhostEdible(g))
				aux = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
				 							   	   pos, game.getGhostLastMoveMade(g));
			if (dist > aux) {
				dist = aux;
				nearest = g;
			}
		}
		return nearest;
	}
}

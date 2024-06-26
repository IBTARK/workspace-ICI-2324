package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

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
		int maxDist = game.getShortestPathDistance(orig, dest, lastMove) * 3 / 2;
		
		//Búsqueda de caminos en anchura con poda
		Queue<ArrayList<Integer>> queue = new LinkedList<>();
		for (int n : game.getNeighbouringNodes(orig, lastMove))
			if (n == dest) paths.add(new Integer[] {orig, n});
			else queue.add(new ArrayList<Integer> (Arrays.asList(orig, n)));
		
		while (!queue.isEmpty()) {
			ArrayList<Integer> act = queue.remove();
			for (int n : game.getNeighbouringNodes(act.get(act.size()-1), game.getMoveToMakeToReachDirectNeighbour(act.get(act.size()-2), act.get(act.size()-1)))) {
				act.add(n);
				if (n == dest) paths.add(act.toArray(new Integer[act.size()]));
				else if (act.size() <= 2 || maxDist > act.size() + game.getShortestPathDistance(act.get(act.size()-1), dest, game.getMoveToMakeToReachDirectNeighbour(act.get(act.size()-2), act.get(act.size()-1)))) 
					queue.add(new ArrayList<Integer>(act));
				act.remove(act.size()-1);
			}
		}
		return paths;
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
		boolean firstJuncitonChecked = false;
		int numNodes = 0;
		
		for(int node : path) {
			for(GHOST g :  GHOST.values()) {
				int ghostPos = game.getGhostCurrentNodeIndex(g);
				MOVE ghostLastMove = game.getGhostLastMoveMade(g);
				
				//The ghost is in the path
				if(ghostPos == node) return true;
				//The ghost can reach faster the first junction in the path
				if(ghostPos >= 0 && !firstJuncitonChecked && game.isJunction(node) 
						&& game.getShortestPathDistance(ghostPos, node, ghostLastMove) < numNodes) return true;
			}
			if(!firstJuncitonChecked && game.isJunction(node)) firstJuncitonChecked = true;
			numNodes++;
		}
		return false;
	}
	
	//Checks if there is a PPill in the given path coming 
	public static boolean blockedByClosestPPill(Game game, int[] path, int ppill) {
		for(int node : path) {
			if(ppill == node) return true;
		}
		return false;
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

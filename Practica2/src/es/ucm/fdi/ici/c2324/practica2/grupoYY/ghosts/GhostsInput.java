package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends Input {
	
	//Thresholds
	private static final int TH_PACMAN_PPILL = 30;
	private static final int TH_CHASING = 30;
	private static final int TH_EDIBLE = 30;

	private Map<GHOST, Boolean> alive = new HashMap<>(); //Map indicating if the ghosts are alive or not
	private Map<GHOST, Boolean> edible = new HashMap<>(); //Map indicating if the ghosts are edible or not
	private Map<GHOST, GHOST> nearestChasing = new HashMap<>(); //Map indicating for every ghost the nearest chasing ghost to him
	private Map<GHOST, GHOST> nearestEdible = new HashMap<>(); //Map indicating for every  ghost the nearest edible ghost to him
	private Map<GHOST, Integer> pacmanDist = new HashMap<>(); //Map indicating for every ghost the distance from him to MsPacMan
	private Map<GHOST, Integer> pacmanJunctDist = new HashMap<>(); //Map indicating for every ghost the distance from him to MsPacMans next junction
	private Map<GHOST, Integer> ppillDist = new HashMap<>(); //Map indicating for every ghost the distance from him to his closest PPill
	private Map<GHOST, Boolean> nearestChasingBlocked = new HashMap<>(); //Map indicating for every ghost if the nearest ghost to him is blocked 
	private Map<GHOST, Map<GHOST, Integer>> distanceBetweenGhosts = new HashMap<>(); //Map of distances between ghosts
	private double minPacmanDistancePPill; //Distance from MsPacMan to the nearest PPill to her
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		int pacman = game.getPacmanCurrentNodeIndex();
		int pacmanNextJunction = GhostsTools.nextJunction(game, pacman, game.getPacmanLastMoveMade());
		
		//If possible, the index of the nearest PPill to MsPacMan is compute
		int closestPPill = -1;
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getPowerPillIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
			if (distance < minPacmanDistancePPill) {
				minPacmanDistancePPill = distance;
				closestPPill = ppill;
			}
		}
		
		for (GHOST g : GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			MOVE lastMove = game.getGhostLastMoveMade(g);
			
			alive.put(g, game.getGhostLairTime(g) <= 0);
			edible.put(g, game.isGhostEdible(g));
			nearestChasing.put(g, GhostsTools.getNearestChasing(game, g));
			pacmanDist.put(g, game.getShortestPathDistance(pos, pacman, lastMove));
			pacmanJunctDist.put(g, game.getShortestPathDistance(pos, pacmanNextJunction, lastMove));
			ppillDist.put(g, (closestPPill < 0 ? Integer.MAX_VALUE : game.getShortestPathDistance(pos, closestPPill, lastMove)));
			nearestChasingBlocked.put(g, GhostsTools.blocked(game, g, nearestChasing.get(g)));
			nearestEdible.put(g, GhostsTools.getNearestEdible(game, g));
			
			HashMap<GHOST, Integer> distances = new HashMap<GHOST, Integer>();
			//Compute the distances between ghosts
			for(GHOST g2 : GHOST.values()) {
				if(g2 != g) {
					//Distance from g to g2 taking into consideration the last move made by g
					if(game.getGhostLairTime(g) <= 0)distances.put(g2, game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(g2), game.getGhostLastMoveMade(g)));
					else distances.put(g2, Integer.MAX_VALUE); //If g2 is in the lair, the distance is the maximum possible
				}
			}
			distanceBetweenGhosts.put(g, distances);
		}
	}

	//Indicates if the given ghost is alive
	public boolean isAlive(GHOST g) {
		return alive.get(g);
	}
	
	//Indicates if the given ghost is edible
	public boolean edible(GHOST g) {
		return edible.get(g);
	}
	
	//Gets the distance from MsPacMan to the closest PPill to her
	public double getMinPacmanDistancePPill() {
		return minPacmanDistancePPill;
	}

	//Indicates if MsPacMan is far from the closest PPill to her
	public boolean msPacManFarFromPPill() {
		return minPacmanDistancePPill > TH_PACMAN_PPILL;
	}
	
	//Returns the nearest chasing ghost to the given one
	public GHOST getNearestChasing(GHOST g) {
		return nearestChasing.get(g);
	}
	
	//Checks if there are chasing ghost near the given ghost
	public boolean chasingClose(GHOST g) {
		
		//Check if any of the distances between g and the chasing ghosts is smaller than the threshold
		for(GHOST g2 : GHOST.values()) {
			if(g2 != g && !edible.get(g2) && distanceBetweenGhosts.get(g).get(g2) <= TH_CHASING) return true;
		}
		
		return false;
	}
	
	//Indicates if there are any edible ghosts near to the given ghost
	public boolean ediblesClose(GHOST g) {
		//Check if any of the distances between g and the other ghost is smaller than the threshold
		return distanceBetweenGhosts.get(g).get(nearestEdible.get(g)) <= TH_EDIBLE;
		
	}
	
	//Gets the length of the shortest path from the given ghost to MsPacMan
	public int getDistToMsPacMan(GHOST g) {
		return pacmanDist.get(g);
	}
	
	//Gets the length of the shortest path from the given ghost to MsPacMans next junction
	public int getDistToMsPacManNextJunction(GHOST g) {
		return pacmanJunctDist.get(g);
	}
	
	//Indicates if the given PPill is covered by a ghost
	public boolean ppillCovered(int PPill) {
		return true; //TODO ibon
	}
	
	//Gets the distance from the given ghost to the nearest PPill to him
	public int ppillDistance(GHOST g) {
		return ppillDist.get(g);
	}
	
	//Checks if the given ghost is the nearest one to MsPacMan
	public boolean isNearestEdible(GHOST g) {
		//If the given ghost is not edible, it can't be the nearest edible one to MsPacMan
		if(!edible.get(g)) return false;
		
		//Check if there is another edible ghost closer to MsPacMan
		for(GHOST ghost : GHOST.values()) {
			if(ghost != g && edible.get(ghost) && pacmanDist.get(ghost) < pacmanDist.get(g)) return false;
		}
		return true;
	}
	
	//Indicates if the nearest chasing ghost to the given ghost is blocked
	public boolean nearestChasingBlocked(GHOST g) {
		return nearestChasingBlocked.get(g);
	}
	
	//Indicates if there are any edible none covered ghost near the given ghost
	public boolean ediblesNotCoveredClose(GHOST g) {
		return true; //TODO yikang
	}
}

package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends Input {
	
	//Thresholds
	private static final int TH_PACMAN_PPILL = 120;
	private static final int TH_CHASING = 50;
	private static final int TH_EDIBLE = 120;
	private static final int TH_DANGER = 120;

	private Map<GHOST, Boolean> alive; //Map indicating if the ghosts are alive or not
	private Map<GHOST, Boolean> edible; //Map indicating if the ghosts are edible or not
	private Map<GHOST, GHOST> nearestChasing; //Map indicating for every ghost the nearest chasing ghost to him
	private Map<GHOST, GHOST> nearestEdible; //Map indicating for every  ghost the nearest edible ghost to him
	private Map<GHOST, Integer> pacmanDist; //Map indicating for every ghost the distance from him to MsPacMan
	private Map<GHOST, Integer> pacmanJunctDist; //Map indicating for every ghost the distance from him to MsPacMans next junction
	private Map<GHOST, Integer> ppillDist; //Map indicating for every ghost the distance from him to MsPacMans closest PPill
	private Map<GHOST, GHOST> nearestChasingNotBlocked; //Map indicating for every ghost the nearest chasing not blocked ghost to him
	private Map<GHOST, Boolean> nearestChasingBlocked; //Map indicating for every ghost if the nearest ghost to him is blocked 
	private Map<GHOST, Map<GHOST, Integer>> distanceBetweenGhosts; //Map of distances between ghosts
	private Map<GHOST, Map<Integer, Integer>> distanceGhostsPPills; //Map of the distances of the ghost to every PPill remaining
	private GHOST closestEdibleGhostMsPacMan; //Ghost MsPacMan is closest to
	private int minPacmanDistancePPill; //Distance from MsPacMan to the nearest PPill to her
	private int closestPPillMsPacMan; //PPill MsPacMan is closest to
	private int numPPills; //Number of remaining PPills
	private GhostsCoordination coord;
	
	public GhostsInput(Game game, GhostsCoordination coord) {
		super(game);
		this.coord = coord;
	}

	@Override
	public void parseInput() {
		alive = new HashMap<>(); //Map indicating if the ghosts are alive or not
		edible = new HashMap<>(); //Map indicating if the ghosts are edible or not
		nearestChasing = new HashMap<>(); //Map indicating for every ghost the nearest chasing ghost to him
		nearestEdible = new HashMap<>(); //Map indicating for every  ghost the nearest edible ghost to him
		pacmanDist = new HashMap<>(); //Map indicating for every ghost the distance from him to MsPacMan
		pacmanJunctDist = new HashMap<>(); //Map indicating for every ghost the distance from him to MsPacMans next junction
		ppillDist = new HashMap<>(); //Map indicating for every ghost the distance from him to MsPacMans closest PPill
		nearestChasingNotBlocked = new HashMap<>(); //Map indicating for every ghost the nearest chasing not blocked ghost to him
		nearestChasingBlocked = new HashMap<>(); //Map indicating for every ghost if the nearest ghost to him is blocked 
		distanceBetweenGhosts = new HashMap<>(); //Map of distances between ghosts
		distanceGhostsPPills = new HashMap<>(); //Map of the distances of the ghost to every PPill remaining
		
		
		int pacman = game.getPacmanCurrentNodeIndex();
		int pacmanNextJunction = GhostsTools.nextJunction(game, pacman, game.getPacmanLastMoveMade());
		
		//If possible, the index and the distance of the nearest PPill to MsPacMan is compute
		int closestPPill = -1;
		this.minPacmanDistancePPill = Integer.MAX_VALUE;
		for(int ppill: game.getActivePowerPillsIndices()) {
			int distance = game.getShortestPathDistance(pacman, ppill, game.getPacmanLastMoveMade());
			if (distance < minPacmanDistancePPill) {
				minPacmanDistancePPill = distance;
				closestPPill = ppill;
			}
		}
		closestPPillMsPacMan = closestPPill; //CAREFUL it can be -1
		
		numPPills = game.getNumberOfActivePowerPills();
		
		int distClosestGhost = Integer.MAX_VALUE; closestEdibleGhostMsPacMan = null;
		for (GHOST g : GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			MOVE lastMove = game.getGhostLastMoveMade(g);
			
			alive.put(g, game.getGhostLairTime(g) <= 0);
			edible.put(g, game.isGhostEdible(g));
			nearestChasing.put(g, GhostsTools.getNearestChasing(game, g));
			pacmanDist.put(g, game.getShortestPathDistance(pos, pacman, lastMove));
			pacmanJunctDist.put(g, game.getShortestPathDistance(pos, pacmanNextJunction, lastMove));
			ppillDist.put(g, (closestPPill < 0 ? Integer.MAX_VALUE : game.getShortestPathDistance(pos, closestPPill, lastMove)));
			nearestChasingNotBlocked.put(g, GhostsTools.getNearestChasingNotBlocked(game, g)); //CAREFUL, the value for a ghost can be NULL
			nearestChasingBlocked.put(g, nearestChasing.get(g) != null ? GhostsTools.blocked(game, g, nearestChasing.get(g)) : null);
			nearestEdible.put(g, GhostsTools.getNearestEdible(game, g));
			
			HashMap<Integer, Integer> distancePPills = new HashMap<Integer, Integer>();
			//Compute the distances between the ghost g to every remaining PPill
			for(int ppill : game.getActivePowerPillsIndices()) {
				if(alive.get(g)) distancePPills.put(ppill, game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), ppill, game.getGhostLastMoveMade(g)));
				else distancePPills.put(ppill, Integer.MAX_VALUE);
			}
			distanceGhostsPPills.put(g, distancePPills);
			
			HashMap<GHOST, Integer> distances = new HashMap<GHOST, Integer>();
			//Compute the distances between ghosts
			for(GHOST g2 : GHOST.values()) {
				if(g2 != g) {
					//Distance from g to g2 taking into consideration the last move made by g
					if(alive.get(g) && game.getGhostLairTime(g2) <= 0)distances.put(g2, game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getGhostCurrentNodeIndex(g2), game.getGhostLastMoveMade(g)));
					else distances.put(g2, Integer.MAX_VALUE); //If g2 is in the lair, the distance is the maximum possible
				}
			}
			distanceBetweenGhosts.put(g, distances);
			
			if(alive.get(g)) {
				int closestGhostAux = game.getShortestPathDistance(pacman, pos, game.getPacmanLastMoveMade());
				if (game.isGhostEdible(g) && closestGhostAux < distClosestGhost) {
					distClosestGhost = closestGhostAux;
					closestEdibleGhostMsPacMan = g;
				}
			}
		}
	}

	//Indicate if the given ghost is alive
	public boolean isAlive(GHOST g) {
		return alive.get(g);
	}
	
	//Indicate if the given ghost is edible
	public boolean edible(GHOST g) {
		return edible.get(g);
	}
	
	//Indicate the closest PPill to MsPacMan (CAREFUL it can be -1)
	public int getClosestPPillMsPacMan() {
		return closestPPillMsPacMan;	
	}
	
	//Indicate the closest edible ghost to MsPacMan (CAREFUL it can be null)
	public GHOST getClosestEdibleGhostMsPacMan() {
		return closestEdibleGhostMsPacMan;	
	}
	
	//Get the distance from MsPacMan to the closest PPill to her
	public int getMinPacmanDistancePPill() {
		return minPacmanDistancePPill;
	}

	//Indicate if MsPacMan is far from the closest PPill to her
	public boolean msPacManFarFromPPill() {
		return minPacmanDistancePPill > TH_PACMAN_PPILL;
	}
	
	//Return the nearest chasing ghost to the given one
	public GHOST getNearestChasing(GHOST g) {
		return nearestChasing.get(g);
	}
	
	//Get the distance between two ghosts
	public int getDistanceBetweenGhosts(GHOST g1, GHOST g2) {
		return distanceBetweenGhosts.get(g1).get(g2);
	}
	
	//Check if there are chasing ghost near the given ghost
	public boolean chasingClose(GHOST g) {
		
		//Check if any of the distances between g and the chasing ghosts is smaller than the threshold
		for(GHOST g2 : GHOST.values()) {
			if(g2 != g && !edible.get(g2) && distanceBetweenGhosts.get(g).get(g2) <= TH_CHASING) return true;
		}
		
		return false;
	}
	
	//Indicate if there are any edible ghosts near to the given ghost
	public boolean ediblesClose(GHOST g) {
		//Check if any of the distances between g and the other ghost is smaller than the threshold
		return nearestEdible.get(g) != null && distanceBetweenGhosts.get(g).get(nearestEdible.get(g)) <= TH_EDIBLE;
	}
	
	//Get the length of the shortest path from the given ghost to MsPacMan
	public int getDistToMsPacMan(GHOST g) {
		return pacmanDist.get(g);
	}
	
	//Get the length of the shortest path from the given ghost to MsPacMans next junction
	public int getDistToMsPacManNextJunction(GHOST g) {
		return pacmanJunctDist.get(g);
	}
	
	//Indicate if the given PPill is covered by a ghost
	public boolean ppillCovered() {
		return coord.whoCoversPPill(closestPPillMsPacMan) != null;
	}

	//Get the distance from the given ghost to the given PPill
	public int ppillDistance(GHOST g, int ppill) {
		return distanceGhostsPPills.get(g).get(ppill);
	}
	
	//Get the distance from the given ghost to the nearest PPill to him
	public int closestPPillDistance(GHOST g) {
		return ppillDist.get(g);
	}
	
	//Check if the given ghost is the nearest one to MsPacMan
	public boolean isNearestEdible(GHOST g) {
		//If the given ghost is not edible, it can't be the nearest edible one to MsPacMan
		if(!edible.get(g)) return false;
		
		//Check if there is another edible ghost closer to MsPacMan
		for(GHOST ghost : GHOST.values()) {
			if(ghost != g && edible.get(ghost) && pacmanDist.get(ghost) < pacmanDist.get(g)) return false;
		}
		return true;
	}
	
	//Get the nearest chasing not blocked ghost
	public GHOST getNearestChasingNotBlocked(GHOST g) {
		return nearestChasingNotBlocked.get(g);
	}
	
	//Indicate if the nearest chasing not blocked ghost is close to the given ghost 
	public boolean isNearestchasingNotBlockedClose (GHOST g) {
		GHOST ghostNCNB = nearestChasingNotBlocked.get(g);
		return ghostNCNB != null ? distanceBetweenGhosts.get(g).get(ghostNCNB) <= TH_CHASING : false;
	}
	
	//Indicate if the nearest chasing ghost to the given ghost is blocked
	public boolean isNearestChasingBlocked(GHOST g) {
		return nearestChasingBlocked.get(g);
	}
	
	//Indicate if there are any edible none covered ghost near the given ghost
	public boolean ediblesNotCoveredClose(GHOST g) {
		
		for(GHOST g2 : GHOST.values()) {
			if(g != g2 && edible.get(g2) && distanceBetweenGhosts.get(g).get(g2) <= TH_EDIBLE && coord.whoCoversEdible(g2) == null) return true;
		}
		
		return false;
	}
	
	//Indicate if a ghost is in danger (close to MsPacMan)
	public boolean danger(GHOST g) {
		return pacmanDist.get(g) <= TH_DANGER;
	}
	
	//Indicate the number of remaining PPills
	public int numPPills() {
		return numPPills;
	}
}

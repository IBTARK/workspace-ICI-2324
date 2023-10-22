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

	private Map<GHOST, Boolean> edible = new HashMap<>();
	private Map<GHOST, GHOST> nearestChasing = new HashMap<>();
	private Map<GHOST, Integer> pacmanDist = new HashMap<>();
	private Map<GHOST, Integer> pacmanJunctDist = new HashMap<>();
	private Map<GHOST, Integer> ppillDist = new HashMap<>();
	private Map<GHOST, Boolean> nearestChasingBlocked = new HashMap<>();
	private double minPacmanDistancePPill;
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		int pacman = game.getPacmanCurrentNodeIndex();
		int pacmanNextJunction = GhostsTools.nextJunction(game, pacman, game.getPacmanLastMoveMade());
		
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
			
			edible.put(g, game.isGhostEdible(g));
			nearestChasing.put(g, GhostsTools.getNearestChasing(game, g));
			pacmanDist.put(g, game.getShortestPathDistance(pos, pacman, lastMove));
			pacmanJunctDist.put(g, game.getShortestPathDistance(pos, pacmanNextJunction, lastMove));
			ppillDist.put(g, (closestPPill < 0 ? Integer.MAX_VALUE : game.getShortestPathDistance(pos, closestPPill, lastMove)));
			nearestChasingBlocked.put(g, GhostsTools.blocked(game, g, nearestChasing.get(g)));
		}
	}

	public boolean edible(GHOST g) {
		return edible.get(g);
	}
	
	public double getMinPacmanDistancePPill() {
		return minPacmanDistancePPill;
	}

	public boolean msPacManFarFromPPill() {
		return minPacmanDistancePPill > TH_PACMAN_PPILL;
	}
	
	public GHOST getNearestChasing(GHOST g) {
		return nearestChasing.get(g);
	}
	
	public boolean chasingClose(GHOST g) {
		return true; //TODO ibon
	}
	
	public boolean ediblesClose(GHOST g) {
		return true; //TODO yikang
	}
	
	public int getDistToMsPacMan(GHOST g) {
		return pacmanDist.get(g);
	}
	
	public int getDistToMsPacManNextJunction(GHOST g) {
		return pacmanJunctDist.get(g);
	}
	
	public boolean ppillCovered() {
		return true; //TODO ibon
	}
	
	public int ppillDistance(GHOST g) {
		return ppillDist.get(g);
	}
	
	public boolean isNearestEdible(GHOST g) {
		return true; //TODO ibon
	}
	
	public boolean nearestChasingBlocked(GHOST g) {
		return nearestChasingBlocked.get(g);
	}
	
	public boolean ediblesNotCoveredClose(GHOST g) {
		return true; //TODO yikang
	}
}

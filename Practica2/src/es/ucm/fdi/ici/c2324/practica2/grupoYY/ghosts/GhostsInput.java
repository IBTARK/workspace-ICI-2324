package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends Input {
	
	//Thresholds
	private static final int TH_PACMAN_PPILL = 30;

	private Map<GHOST, Boolean> edible = new HashMap<>();
	private Map<GHOST, GHOST> nearestChasing = new HashMap<>();
	private double minPacmanDistancePPill;
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		for (GHOST g : GHOST.values()) {
			edible.put(g, game.isGhostEdible(g));
			nearestChasing.put(g, GhostsTools.getNearestChasing(game, g));
		}
	
		int pacman = game.getPacmanCurrentNodeIndex();
		
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getPowerPillIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
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
}

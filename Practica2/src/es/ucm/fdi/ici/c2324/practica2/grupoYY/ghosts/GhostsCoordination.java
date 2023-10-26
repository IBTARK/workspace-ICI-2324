package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsCoordination {

	private Map<GHOST, GHOST> coveringEdible = new HashMap<>();	//For every ghost, contains the one protecting it (if exists)
	private Map<Integer, GHOST> coveringPPill = new HashMap<>();//For every ppill, contains the ghost protecting it (if exists)
	private List<Entry<GHOST, Integer>> flankAttacks = new LinkedList<>();
	private GHOST directAttacker = null;
	
	public void update(Game game) {
		Map<Integer, GHOST> newCoveringPPill = new HashMap<>();
		for (int pp : game.getActivePowerPillsIndices())
			newCoveringPPill.put(pp, coveringPPill.get(pp));
		coveringPPill = newCoveringPPill;
	}
	
	//Returns which ghost covers the edible one, if none null
	public GHOST whoCoversEdible(GHOST edible) {
		return coveringEdible.get(edible);
	}
	
	public void coverEdible(GHOST edible, GHOST chasing) {
		if (coveringEdible.containsKey(edible))
			throw new RuntimeException("Te pasaste cubriendo weon");
			
		coveringEdible.put(edible, chasing);
	}
	
	public void uncoverEdible(GHOST chasing) {
		for (GHOST g : GHOST.values())
			if (chasing == coveringEdible.get(g))
				coveringEdible.remove(g);
	}
	
	//Returns which ghost covers the ppill, if none null
	public GHOST whoCoversPPill(Integer ppill) {
		return coveringPPill.get(ppill);
	}
	
	public void coverPPill(Integer ppill, GHOST chasing) {
		if (coveringPPill.containsKey(ppill))
			throw new RuntimeException("Te pasaste cubriendo weon");
			
		coveringPPill.put(ppill, chasing);
	}
	
	public void uncoverPPill(GHOST ghost) {
		for (Integer pp : coveringPPill.keySet())
			if (ghost == coveringPPill.get(pp))
				coveringPPill.remove(pp);
	}
	
	public GHOST whoIsDirectAttacker() {
		return directAttacker;
	}
	
	public void directAttack(GHOST g) {
		if (directAttacker != null)
			throw new RuntimeException("Te pasaste atacando weon");
		directAttacker = g;
	}
}

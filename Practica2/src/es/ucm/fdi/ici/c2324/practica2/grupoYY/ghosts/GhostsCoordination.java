package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts;

import java.util.HashMap;
import java.util.Map;

import pacman.game.Constants.GHOST;

public class GhostsCoordination {

	private volatile Map<GHOST, GHOST> coveringEdible = new HashMap<>();	//For every ghost, contains the one protecting it (if exists)
	private GHOST coveringPPill = null;
	private Integer ppillCovered = -1;
	
	//Returns which ghost covers the edible one, if none null
	public GHOST whoCoversEdible(GHOST edible) {
		GHOST aux;
		synchronized (coveringEdible) {
			aux = coveringEdible.get(edible);
		}
		return aux;
	}
	
	public void coverEdible(GHOST edible, GHOST chasing) {
		synchronized (coveringEdible) {
			if (coveringEdible.containsKey(edible))
				throw new RuntimeException("Te pasaste cubriendo weon");
				
			coveringEdible.put(edible, chasing);
		}
	}
	
	public void uncoverEdible(GHOST chasing) {
		synchronized (coveringEdible) {
			for (GHOST g : GHOST.values())
				if (chasing == coveringEdible.get(g))
					coveringEdible.remove(g);
		}
	}
	
	//Returns which ghost covers the ppill, if none null
	synchronized public GHOST whoCoversPPill(Integer ppill) {
		return coveringPPill;
	}
	
	synchronized public void coverPPill(Integer ppill, GHOST chasing) {
		if (ppill == ppillCovered && coveringPPill != null)
			throw new RuntimeException("Te pasaste cubriendo weon");
		ppillCovered = ppill;
		coveringPPill = chasing;
	}
	
	synchronized public void uncoverPPill(GHOST ghost) {
		ppillCovered = -1;
		coveringPPill = null;
	}
}

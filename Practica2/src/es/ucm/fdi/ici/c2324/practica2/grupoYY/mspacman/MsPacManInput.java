package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class MsPacManInput extends Input {
	
	// Thresholds
	private static final int TH_CHASING_GHOST = 50; 
	
	private int dangerLevel = 0;
	private boolean levelUp;
	private boolean ppillAccessible;

	public MsPacManInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		for (GHOST g : GHOST.values()) {
			if (TH_CHASING_GHOST > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
											 					game.getPacmanCurrentNodeIndex(), 
											 					game.getGhostLastMoveMade(g)))
				dangerLevel++;
		}
		
		levelUp = game.getCurrentLevelTime() == 0;
		
		int pos = game.getPacmanCurrentNodeIndex(), ppill = MsPacManTools.closestPPill(game);
		ppillAccessible = 0 < MsPacManTools.possiblePaths(game, pos, ppill).size();
	}
	
	public int dangerLevel() {
		return dangerLevel;
	}
	
	public boolean levelUp() {
		return levelUp;
	}
	
	public boolean ppillAccessible() {
		return ppillAccessible;
	}
}

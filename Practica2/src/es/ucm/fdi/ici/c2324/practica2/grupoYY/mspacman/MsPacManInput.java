package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends Input {
	
	// Thresholds
	private static final int TH_CHASING_GHOST = 50; 
	private static final int TH_EDIBLE_GHOST = 60;
	private static final int TH_PPILL = 50;
	
	private int dangerLevel = 0;
	private int closestPPill;
	private boolean levelUp;
	private boolean ppillAccessible;
	private boolean ppillClose;
	private boolean attack;
	private boolean combo;
	private int nearestEdibleDist;
	private int nearestEdibleNextJunctionDist;

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
			if (TH_EDIBLE_GHOST > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
											 				   game.getPacmanCurrentNodeIndex(), 
											 				   game.getGhostLastMoveMade(g)))
				attack = true;
				
		}
		
		closestPPill = MsPacManTools.closestPPill(game);
		// El numero que devuelde getNumGhostEaten() no esta definido ...
		combo = game.getNumGhostsEaten() >= 2; 
		levelUp = game.getCurrentLevelTime() == 0;
		
		int pos = game.getPacmanCurrentNodeIndex(), ppill = MsPacManTools.closestPPill(game);
		MOVE lastMove = game.getPacmanLastMoveMade();
		ppillAccessible = 0 < MsPacManTools.possiblePaths(game, pos, ppill).size();
		ppillClose = TH_PPILL > game.getShortestPathDistance(pos, ppill, lastMove);
		
		GHOST nearest = MsPacManTools.getNearestEdible(game, pos, lastMove);
		nearestEdibleDist = game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearest), lastMove);
		
		int edibleJunction = MsPacManTools.nextJunction(game, game.getGhostCurrentNodeIndex(nearest), game.getGhostLastMoveMade(nearest));
		nearestEdibleNextJunctionDist = game.getShortestPathDistance(pos, edibleJunction, lastMove);
	}
	
	public int dangerLevel() {
		return dangerLevel;
	}
	
	public boolean danger() {
		return dangerLevel > 0;
	}
	
	public boolean levelUp() {
		return levelUp;
	}
	
	public boolean ppillAccessible() {
		return ppillAccessible;
	}
	
	public boolean attack() {
		return attack;
	}
	
	public int getClosestPPill() {
		return closestPPill;
	}
	
	public boolean isPPillClose() {
		return ppillClose;
	}
	
	public boolean combo() {
		return combo;
	}
	
	public int getNearestEdibleDistance() {
		return nearestEdibleDist;
	}
	
	public int nearestEdibleNextJunctionDistance() {
		return nearestEdibleNextJunctionDist;
	}
}

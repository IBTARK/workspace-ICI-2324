package es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman;

import java.util.Collection;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends RulesInput {
	
	// Thresholds
	private static final int TH_CHASING_GHOST = 80; 
	private static final int TH_EDIBLE_GHOST = 370;
	private static final int TH_EDIBLE_GHOST_CLOSE = 170;
	private static final int TH_PPILL = 100;
	private static final int TH_FEWPILLS = 20;
	private static final int TH_COMBO = 500;
	
	private int dangerLevel;
	private int closestPPill;
	private boolean levelUp;
	private boolean ppillAccessible;
	private boolean ppillClose;
	private boolean attack;
	private boolean attackClose;
	private boolean combo;
	private boolean fewPills;
	private boolean nearestPPillBlocked;
	private int nearestEdibleDist; //Distance from MsPacMan to the nearest edible ghost to her
	private int nearestEdibleNextJunctionDist; //Distance from MsPacMan to the next junction of the nearest edible ghosts to her
	private int distOfNearestEdibleToHisNextJunction; //Distance of MsPacMans nearest edible ghost to his next junction
	
	public MsPacManInput(Game game) {
		super(game);
	}
	
	
	@Override
	public void parseInput() {
		dangerLevel = 0;
		for (GHOST g : GHOST.values()) {
			if (game.getGhostLairTime(g) <= 0) {
				if (!game.isGhostEdible(g)
					&& TH_CHASING_GHOST > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
												 					game.getPacmanCurrentNodeIndex(), 
												 					game.getGhostLastMoveMade(g)))
					dangerLevel++;
				if (game.isGhostEdible(g)
					&& TH_EDIBLE_GHOST > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
												 				   game.getPacmanCurrentNodeIndex(), 
												 				   game.getGhostLastMoveMade(g)))
					attack = true;
				if(game.isGhostEdible(g)
					&& TH_EDIBLE_GHOST_CLOSE > game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
												 				   game.getPacmanCurrentNodeIndex(), 
												 				   game.getGhostLastMoveMade(g)))
					attackClose = true;
			}
		}
		
		closestPPill = MsPacManTools.closestPPill(game);
		// El numero que devuelde getNumGhostEaten() no esta definido ...
		combo = game.getGhostCurrentEdibleScore() >= TH_COMBO; 
		levelUp = game.getCurrentLevelTime() == 0;
		
		int pos = game.getPacmanCurrentNodeIndex(), ppill = MsPacManTools.closestPPill(game);
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		
		ppillAccessible = false;
		ppillClose = false;
		nearestPPillBlocked = false;
		if (ppill >= 0) {
			if (!MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, ppill, lastMove)))) ppillAccessible = true;
			else for (Integer[] path : MsPacManTools.possiblePaths(game, pos, ppill, lastMove))
					ppillAccessible |= !MsPacManTools.blocked(game, path);
			
			ppillClose = TH_PPILL > game.getShortestPathDistance(pos, ppill, lastMove);
			nearestPPillBlocked = MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, ppill, lastMove)));
		}
		
		//Nearest edible ghost to MsPacMan
		GHOST nearest = MsPacManTools.getNearestEdible(game, pos, lastMove); //CAREFUL, can return null
		nearestEdibleDist = nearest == null ? Integer.MAX_VALUE : game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearest), lastMove);
		
		//Next junction of the edible ghost, it can be null if there is no edible ghost
		Integer edibleJunction = nearest == null ? null : MsPacManTools.nextJunction(game, game.getGhostCurrentNodeIndex(nearest), game.getGhostLastMoveMade(nearest));
		nearestEdibleNextJunctionDist = nearest == null ? Integer.MAX_VALUE : game.getShortestPathDistance(pos, edibleJunction, lastMove);
		
		distOfNearestEdibleToHisNextJunction = nearest == null ? Integer.MAX_VALUE : game.getShortestPathDistance(game.getGhostCurrentNodeIndex(nearest), edibleJunction, game.getGhostLastMoveMade(nearest));
		
		fewPills = game.getNumberOfActivePills() <= TH_FEWPILLS;
		
	}


	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
//		facts.add(String.format("(MSPACMAN (levelUp %b))", this.levelUp));
		facts.add(String.format("(MSPACMAN (combo %b))", this.combo));
//		facts.add(String.format("(PPILL (closest %d))", this.closestPPill));
		facts.add(String.format("(PPILL (accessible %b))", this.ppillAccessible));
		facts.add(String.format("(PPILL (close %b))", this.ppillClose));
		facts.add(String.format("(PPILL (blocked %b))", this.nearestPPillBlocked));
		facts.add(String.format("(EDIBLE (nearestDist %d))", this.nearestEdibleDist));
		facts.add(String.format("(EDIBLE (attack %b))", this.attack));
		facts.add(String.format("(EDIBLE (nearestNextJunctDist %b))", this.nearestEdibleNextJunctionDist));
//		facts.add(String.format("(EDIBLE (nextJuntDist %b))", this.distOfNearestEdibleToHisNextJunction));
		facts.add(String.format("(EDIBLE (attackClose %b))", this.attackClose));
		facts.add(String.format("(CHASING (dangerLevel %d))", this.dangerLevel));
		facts.add(String.format("(PILLS (few %b))", this.fewPills));
		
		return facts;
	}
}

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
	private static final int MAX_DIST = 5000;
	
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
		levelUp = false;
		ppillAccessible = false;
		ppillClose = false;
		attack = false;
		attackClose = false;
		combo = false;
		fewPills = false;
		nearestPPillBlocked = false;
		nearestEdibleDist = -1;
		nearestEdibleNextJunctionDist = -1;
		distOfNearestEdibleToHisNextJunction = -1;
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
		nearestEdibleDist = nearest == null ? MAX_DIST : game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearest), lastMove);
		
		//Next junction of the edible ghost, it can be null if there is no edible ghost
		Integer edibleJunction = nearest == null ? null : MsPacManTools.nextJunction(game, game.getGhostCurrentNodeIndex(nearest), game.getGhostLastMoveMade(nearest));
		nearestEdibleNextJunctionDist = nearest == null ? MAX_DIST : game.getShortestPathDistance(pos, edibleJunction, lastMove);
		
		
		fewPills = game.getNumberOfActivePills() <= TH_FEWPILLS;
		
	}


	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
//		facts.add(String.format("(MSPACMAN (levelUp %b))", this.levelUp));
		facts.add(String.format("(MSPACMAN (combo %b))", this.combo));
//		facts.add(String.format("(PPILL (closest %d))", this.closestPPill));
		facts.add(String.format("(PPILL (accessible %b) (close %b) (blocked %b))", 
				this.ppillAccessible, this.ppillClose, this.nearestPPillBlocked));
		facts.add(String.format("(EDIBLE (nearestDist %d) (attack %b) (attackClose %b) (nearestNextJunctDist %b))", 
				this.nearestEdibleDist, this.attack, this.attackClose, this.nearestEdibleNextJunctionDist));
//		facts.add(String.format("(EDIBLE (nextJunctDist %b))", this.distOfNearestEdibleToHisNextJunction));
		facts.add(String.format("(CHASING (dangerLevel %d) (danger %b))", this.dangerLevel, this.dangerLevel > 0));
		facts.add(String.format("(PILLS (few %b))", this.fewPills));
		
		return facts;
	}
}

package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends FuzzyInput {
	
	private static final Double INVISIBLE = null;

	private Double ppillDistance;
	private Double combo;
	private Double numPills;
	private Double nearestPPillBlocked;	//Treat as boolean
	private Double nearestChasingDist; //Distance from MsPacMan to the nearest edible ghost to her
	private Double nearestChasingDist2; //Distance from MsPacMan to the nearest edible ghost to her
	private Double nearestEdibleDist; //Distance from MsPacMan to the nearest edible ghost to her
	private Double nearestEdibleNextJunctionDist; //Distance from MsPacMan to the next junction of the nearest edible ghosts to her
	private Double distOfNearestEdibleToHisNextJunction; //Distance of MsPacMans nearest edible ghost to his next junction
	
	public MsPacManInput(Game game) {
		super(game);
	}
	
	@Override
	public void parseInput() {
		combo = 0.0;
		numPills = INVISIBLE;
		ppillDistance = INVISIBLE;
		nearestEdibleDist = INVISIBLE;
		nearestChasingDist = INVISIBLE;
		nearestChasingDist2 = INVISIBLE;
		nearestEdibleNextJunctionDist = INVISIBLE;
		distOfNearestEdibleToHisNextJunction = INVISIBLE;
		nearestPPillBlocked = 0.0;//Treat as boolean
		
		for (GHOST g : GHOST.values()) {
			if (game.getGhostCurrentNodeIndex(g) >= 0) {
				double dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
						   game.getPacmanCurrentNodeIndex(), 
						   game.getGhostLastMoveMade(g));
				
				if (game.getGhostLairTime(g) <= 0) {
					if (!game.isGhostEdible(g)) {
						if (nearestChasingDist == INVISIBLE || dist < nearestChasingDist) {
							nearestChasingDist2 = nearestChasingDist;
							nearestChasingDist = dist;
						}
						else if (nearestChasingDist == INVISIBLE || dist < nearestChasingDist2)
							nearestChasingDist2 = dist;
					}
					else {
						if (nearestChasingDist == INVISIBLE || dist < nearestEdibleDist)
							nearestEdibleDist = dist;
					}
				}
			}
		}
		
		combo = (double) game.getGhostCurrentEdibleScore();
		
		int pos = game.getPacmanCurrentNodeIndex(), ppill = MsPacManTools.closestPPill(game);
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		
		boolean ppillAccessible = false;
		if (ppill >= 0) {
			if (!MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, ppill, lastMove)))) ppillAccessible = true;
			else for (Integer[] path : MsPacManTools.possiblePaths(game, pos, ppill, lastMove))
					ppillAccessible |= !MsPacManTools.blocked(game, path);
			
			if (ppillAccessible) {
				ppillDistance = (double) game.getShortestPathDistance(pos, ppill, lastMove);
				nearestPPillBlocked = (double) (MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, ppill, lastMove))) ? 1 : 0);
			}   //Treat as boolean
		}
		
		//Nearest edible ghost to MsPacMan
		GHOST nearest = MsPacManTools.getNearestEdible(game, pos, lastMove); //CAREFUL, can return null
		nearestEdibleDist = (double) (nearest == null ? INVISIBLE : game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearest), lastMove));
		
		//Next junction of the edible ghost, it can be null if there is no edible ghost
		Integer edibleJunction = nearest == null ? null : MsPacManTools.nextJunction(game, game.getGhostCurrentNodeIndex(nearest), game.getGhostLastMoveMade(nearest));
		nearestEdibleNextJunctionDist = (double) (nearest == null ? INVISIBLE : game.getShortestPathDistance(pos, edibleJunction, lastMove));
		
		
		numPills = (double) game.getNumberOfActivePills();
	}

	@Override
	public HashMap<String, Double> getFuzzyValues() {
		HashMap<String,Double> vars = new HashMap<String,Double>();
		vars.put("ppillDistance", ppillDistance);
		vars.put("combo", combo);
		vars.put("numPills", numPills);
		vars.put("nearestPPillBlocked", nearestPPillBlocked); //Treat as boolean
		vars.put("nearestEdibleDist", nearestEdibleDist);
		vars.put("nearestChasingDist", nearestChasingDist);
		vars.put("nearestChasingDist2", nearestChasingDist2);
		vars.put("nearestEdibleNextJunctionDist", nearestEdibleNextJunctionDist);
		vars.put("distOfNearestEdibleToHisNextJunction", distOfNearestEdibleToHisNextJunction);
		return vars;
	}
}

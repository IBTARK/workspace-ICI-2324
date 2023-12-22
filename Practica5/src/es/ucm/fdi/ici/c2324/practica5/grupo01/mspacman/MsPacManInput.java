package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends FuzzyInput {
	
	private static final Double INVISIBLE = Double.MAX_VALUE;

	private int nearestPPill;
	private int nearestEdibleNextJunction;
	private int nearestChasing;
	private int nearestEdible;
	private MOVE nearestEdibleLastMove;
	private MOVE nearestChasingLastMove;
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
		nearestChasing = -1;
		nearestEdible = -1;
		nearestEdibleLastMove = null;
		nearestChasingLastMove = null;
		ppillDistance = INVISIBLE;
		nearestEdibleDist = INVISIBLE;
		nearestChasingDist = INVISIBLE;
		nearestChasingDist2 = INVISIBLE;
		distOfNearestEdibleToHisNextJunction = INVISIBLE;
		nearestPPillBlocked = -1.0;//Treat as boolean
		
		for (GHOST g : GHOST.values()) {
			int node = game.getGhostCurrentNodeIndex(g);
			if (node >= 0) {
				double dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
						   game.getPacmanCurrentNodeIndex(), 
						   game.getGhostLastMoveMade(g));
				
				if (game.getGhostLairTime(g) <= 0) {
					if (!game.isGhostEdible(g)) {
						if (nearestChasingDist == INVISIBLE || dist < nearestChasingDist) {
							nearestChasing = node;
							nearestChasingDist2 = nearestChasingDist;
							nearestChasingDist = dist;
							nearestChasingLastMove = game.getGhostLastMoveMade(g);
						}
						else if (nearestChasingDist == INVISIBLE || dist < nearestChasingDist2)
							nearestChasingDist2 = dist;
					}
					else {
						if (nearestChasingDist == INVISIBLE || dist < nearestEdibleDist) {
							nearestEdible = node;
							nearestEdibleDist = dist;
							nearestEdibleLastMove = game.getGhostLastMoveMade(g);
						}
					}
				}
			}
		}
		
		combo = (double) game.getGhostCurrentEdibleScore();
		
		int pos = game.getPacmanCurrentNodeIndex();
		nearestPPill = closestPPill(game); //-1 if there is no PPill visible
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		
		boolean ppillAccessible = false;
		if (nearestPPill >= 0) {
			if (!MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, nearestPPill, lastMove)))) ppillAccessible = true;
			else for (Integer[] path : MsPacManTools.possiblePaths(game, pos, nearestPPill, lastMove))
					ppillAccessible |= !MsPacManTools.blocked(game, path);
			
			if (ppillAccessible) {
				ppillDistance = (double) game.getShortestPathDistance(pos, nearestPPill, lastMove);
				nearestPPillBlocked = (double) (MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, nearestPPill, lastMove))) ? 1 : 0);
			}   //Treat as boolean
		}
		
		
		//Next junction of the edible ghost, it can be null if there is no edible ghost
		nearestEdibleNextJunction = nearestEdible < 0 ? null : MsPacManTools.nextJunction(game, nearestEdible, nearestEdibleLastMove);
		nearestEdibleNextJunctionDist = (double) (nearestEdible < 0 ? INVISIBLE : game.getShortestPathDistance(pos, nearestEdibleNextJunction, lastMove));
		
		//INVISIBLE if no pills are visible
		numPills = (game.getNumberOfActivePills() <= 0 ? INVISIBLE : (double) game.getNumberOfActivePills());
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
	
	public Integer getNearestPPill() {
		return (nearestPPill < 0 ? null : nearestPPill);
	}
	
	public Integer getNearestEdibleNextJunction() {
		return (nearestEdibleNextJunction < 0 ? null : nearestEdibleNextJunction);
	}
	
	public Integer getNearestChasing() {
		return (nearestChasing < 0 ? null : nearestChasing);
	}
	
	public Integer getNearestEdible() {
		return (nearestEdible < 0 ? null : nearestEdible);
	}
	
	public MOVE getNearestChasingLastMove() {
		return nearestEdibleLastMove;
	}
	
	private int closestPPill(Game game) {
		int node = -1, dist = Integer.MAX_VALUE;
		for (int p : game.getActivePowerPillsIndices()) {
			int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), 
			 									   p, game.getPacmanLastMoveMade());
			if (dist > aux) {
				node = p;
				dist = aux;
			}
		}
		return node;
	}
}

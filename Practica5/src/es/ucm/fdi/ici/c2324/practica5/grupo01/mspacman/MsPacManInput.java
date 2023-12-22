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
	private boolean wasPillEaten;
	private boolean wasPPillEaten;
	private MOVE nearestChasingLastMove;
	private Double ppillDistance;
	private Double combo;
	private Double numPills;
	private Double nearestPPillBlocked;	//Treat as boolean
	private Double nearestChasingDist; //Distance from MsPacMan to the nearest edible ghost to her
	private Double nearestChasingDist2; //Distance from MsPacMan to the nearest edible ghost to her
	private Double nearestEdibleDist; //Distance from MsPacMan to the nearest edible ghost to her
	private Double nearestEdibleNextJunctionDist; //Distance from MsPacMan to the next junction of the nearest edible ghosts to her
	private int pos; //MsPacMan current position
	private MOVE lastMove; //MsPacMan last move made
	
	
	public MsPacManInput(Game game) {
		super(game);
	}
	
	@Override
	public void parseInput() {
		nearestChasing = -1;
		nearestEdible = -1;
		nearestChasingLastMove = null;
		ppillDistance = INVISIBLE;
		nearestEdibleDist = INVISIBLE;
		nearestChasingDist = INVISIBLE;
		nearestChasingDist2 = INVISIBLE;
		nearestPPillBlocked = -1.0;//Treat as boolean
		
		MOVE nearestEdibleLastMove = null;
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
							nearestChasingLastMove = game.getGhostLastMoveMade(g);
							nearestChasingDist = dist;
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
		
		pos = game.getPacmanCurrentNodeIndex();
		nearestPPill = closestPPill(game); //-1 if there is no PPill visible
		lastMove = game.getPacmanLastMoveMade();
		
		
		boolean ppillAccessible = false;
		if (nearestPPill >= 0) {
			if (!MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, nearestPPill, lastMove)))) ppillAccessible = true;
			else for (Integer[] path : MsPacManTools.possiblePaths(game, pos, nearestPPill, lastMove))
					ppillAccessible |= !MsPacManTools.blocked(game, path);
			
			ppillDistance = INVISIBLE - 1;
			if (ppillAccessible) {
				ppillDistance = (double) game.getShortestPathDistance(pos, nearestPPill, lastMove);
				nearestPPillBlocked = (double) (MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, nearestPPill, lastMove))) ? 1 : 0);
			}   //Treat as boolean
		}
		
		
		//Next junction of the edible ghost, it can be null if there is no edible ghost
		nearestEdibleNextJunction = nearestEdible < 0 ? -1 : MsPacManTools.nextJunction(game, nearestEdible, nearestEdibleLastMove);
		nearestEdibleNextJunctionDist = (double) (nearestEdible < 0 ? INVISIBLE : game.getShortestPathDistance(pos, nearestEdibleNextJunction, lastMove));
		
		//INVISIBLE if no pills are visible
		numPills = (double) game.getNumberOfPills();
		
		wasPillEaten = game.wasPillEaten();
		wasPPillEaten = game.wasPowerPillEaten();
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
		return vars;
	}
	
	public Boolean wasPillEaten(){
		return wasPillEaten;
	}
	
	public Boolean wasPPillEaten(){
		return wasPPillEaten;
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
		return nearestChasingLastMove;
	}
	
	public int shortestPathDistance(int orig, int dest, MOVE lastMove) {
		return game.getShortestPathDistance(orig, dest, lastMove);
	}
	
	public int getMsPacManCurrPos(){
		return pos;
	}
	
	public MOVE getMsPacManLastMove(){
		return lastMove;
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

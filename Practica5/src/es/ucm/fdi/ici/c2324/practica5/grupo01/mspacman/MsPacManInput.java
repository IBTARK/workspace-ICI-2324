package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends FuzzyInput {

	private double distance[];
	private double ppillDistance;
	private double combo;
	private double numPills;
	private double nearestPPillBlocked;	//Treat as boolean
	private double nearestChasingDist; //Distance from MsPacMan to the nearest edible ghost to her
	private double nearestChasingDist2; //Distance from MsPacMan to the nearest edible ghost to her
	private double nearestEdibleDist; //Distance from MsPacMan to the nearest edible ghost to her
	private double nearestEdibleNextJunctionDist; //Distance from MsPacMan to the next junction of the nearest edible ghosts to her
	private double distOfNearestEdibleToHisNextJunction; //Distance of MsPacMans nearest edible ghost to his next junction
	
	public MsPacManInput(Game game) {
		super(game);
	}
	
	@Override
	public void parseInput() {
		distance = new double[4];
		combo = 0;
		numPills = Integer.MAX_VALUE;
		ppillDistance = Integer.MAX_VALUE;
		nearestEdibleDist = Integer.MAX_VALUE;
		nearestChasingDist = Integer.MAX_VALUE;
		nearestChasingDist2 = Integer.MAX_VALUE;
		nearestEdibleNextJunctionDist = -1;
		distOfNearestEdibleToHisNextJunction = -1;
		nearestPPillBlocked = 0;//Treat as boolean
		
		for (GHOST g : GHOST.values()) {
			int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
					   game.getPacmanCurrentNodeIndex(), 
					   game.getGhostLastMoveMade(g));
			distance[g.ordinal()] = dist;
			
			if (game.getGhostLairTime(g) <= 0) {
				if (!game.isGhostEdible(g)) {
					if (dist < nearestChasingDist) {
						nearestChasingDist2 = nearestChasingDist;
						nearestChasingDist = dist;
					}
					else if (dist < nearestChasingDist2)
						nearestChasingDist2 = dist;
				}
				else {
					if (dist < nearestEdibleDist)
						nearestEdibleDist = dist;
				}
			}
		}
		
		combo = game.getGhostCurrentEdibleScore();
		
		int pos = game.getPacmanCurrentNodeIndex(), ppill = MsPacManTools.closestPPill(game);
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		
		boolean ppillAccessible = false;
		if (ppill >= 0) {
			if (!MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, ppill, lastMove)))) ppillAccessible = true;
			else for (Integer[] path : MsPacManTools.possiblePaths(game, pos, ppill, lastMove))
					ppillAccessible |= !MsPacManTools.blocked(game, path);
			
			if (ppillAccessible) {
				ppillDistance = game.getShortestPathDistance(pos, ppill, lastMove);
				nearestPPillBlocked = MsPacManTools.blocked(game, ArrayUtils.toObject(game.getShortestPath(pos, ppill, lastMove))) ? 1 : 0;
			}   //Treat as boolean
		}
		
		//Nearest edible ghost to MsPacMan
		GHOST nearest = MsPacManTools.getNearestEdible(game, pos, lastMove); //CAREFUL, can return null
		nearestEdibleDist = nearest == null ? Integer.MAX_VALUE : game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearest), lastMove);
		
		//Next junction of the edible ghost, it can be null if there is no edible ghost
		Integer edibleJunction = nearest == null ? null : MsPacManTools.nextJunction(game, game.getGhostCurrentNodeIndex(nearest), game.getGhostLastMoveMade(nearest));
		nearestEdibleNextJunctionDist = nearest == null ? Integer.MAX_VALUE : game.getShortestPathDistance(pos, edibleJunction, lastMove);
		
		
		numPills = game.getNumberOfActivePills();
	}
	
	public boolean isVisible(GHOST ghost)
	{
		return distance[ghost.ordinal()]!=-1;
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

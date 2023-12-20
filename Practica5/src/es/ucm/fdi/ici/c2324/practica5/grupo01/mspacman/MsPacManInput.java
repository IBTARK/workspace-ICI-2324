package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.HashMap;

import org.apache.commons.lang.ArrayUtils;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends FuzzyInput {
	
	private static final Double MAX_DIST = Double.MAX_VALUE;
	
	private boolean isChasing1Seen;
	private boolean isChasing2Seen;
	private boolean isEdibleSeen;

	private Double distance[];
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
	
	public MsPacManInput(Game game, HashMap<String, Double> lastValues) {
		super(game);
		updateNonVisible(lastValues);
	}
	
	@Override
	public void parseInput() {
		distance = new Double[4];
		combo = 0.0;
		numPills = MAX_DIST;
		ppillDistance = MAX_DIST;
		nearestEdibleDist = MAX_DIST;
		nearestChasingDist = MAX_DIST;
		nearestChasingDist2 = MAX_DIST;
		nearestEdibleNextJunctionDist = MAX_DIST;
		distOfNearestEdibleToHisNextJunction = MAX_DIST;
		nearestPPillBlocked = 0.0;//Treat as boolean
		isEdibleSeen = false;
		isChasing1Seen = false;
		isChasing2Seen = false;
		
		for (GHOST g : GHOST.values()) {
			double dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), 
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
		nearestEdibleDist = (double) (nearest == null ? Integer.MAX_VALUE : game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearest), lastMove));
		
		//Next junction of the edible ghost, it can be null if there is no edible ghost
		Integer edibleJunction = nearest == null ? null : MsPacManTools.nextJunction(game, game.getGhostCurrentNodeIndex(nearest), game.getGhostLastMoveMade(nearest));
		nearestEdibleNextJunctionDist = (double) (nearest == null ? Integer.MAX_VALUE : game.getShortestPathDistance(pos, edibleJunction, lastMove));
		
		
		numPills = (double) game.getNumberOfActivePills();
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
	
	private void updateNonVisible(HashMap<String, Double> lastValues) {
		if (nearestEdibleDist >= MAX_DIST) {
			nearestEdibleDist = lastValues.get("nearestEdibleDist");
		}
		if (nearestChasingDist >= MAX_DIST) {
			nearestChasingDist = lastValues.get("nearestChasingDist");
			nearestChasingDist2 = lastValues.get("nearestChasingDist2");
		}
	}
	
	public int numChasingSeen() {
		return numChasingSeen;
	}
	
	public boolean isEdibleSeen() {
		return isEdibleSeen;
	}
}

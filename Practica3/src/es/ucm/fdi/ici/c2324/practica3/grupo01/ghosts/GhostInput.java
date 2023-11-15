package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostInput extends CBRInput {

	GHOST type;
	Integer mspacmanLives;
	Integer score;
	Integer time;
	Boolean edible;
	Integer edibleTime;
	Integer mspacmanToPPill;		// MsPacMan's distance to its nearest PPill.
	// 4 arrays, one for each move, each containing in order:
	/* Integer mspacman; 			Distance to mspacman.
	 * Integer nearestEdible;		Distance to nearest edible.
	 * Integer nearestEdibleTime;	Remaining edible time of the nearest edible.
	 * Integer nearestChasing;		Distance to nearest chasing ghost.
	 */
	private GhostDistanceVector up = new GhostDistanceVector(MOVE.UP);
	private GhostDistanceVector right = new GhostDistanceVector(MOVE.RIGHT);
	private GhostDistanceVector down = new GhostDistanceVector(MOVE.DOWN);
	private GhostDistanceVector left = new GhostDistanceVector(MOVE.LEFT);
	
	public GhostInput(Game game, GHOST g) {
		super(game);
		type = g;
	}

	@Override
	public void parseInput() {
		if(type==null) return;
		int ghost = game.getGhostCurrentNodeIndex(type);
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		this.mspacmanLives = game.getPacmanNumberOfLivesRemaining();
		this.score = game.getScore();
		this.time = game.getTotalTime();
		this.edible = game.isGhostEdible(type);
		this.edibleTime = edible ? game.getGhostEdibleTime(type) : Integer.MAX_VALUE;
		computeMspacmanToPPill(game);
		
		ArrayList<Integer> auxList;
		for(MOVE move: game.getPossibleMoves(ghost, game.getGhostLastMoveMade(type))) {
			auxList = new ArrayList<Integer>(4);
			// mspacman's distance from ghost
			auxList.add(game.getShortestPathDistance(ghost, mspacman, move));
			// this method adds to the move list nearestEdible, nearestEdibleTime, nearestChasing
			computeNearestGhosts(game, move, auxList);
			copyList(auxList, move);
		}
	}

	private void computeMspacmanToPPill(Game game) {
		this.mspacmanToPPill = Integer.MAX_VALUE;
		for(int pos: game.getActivePowerPillsIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < mspacmanToPPill)
				mspacmanToPPill = distance;
		}
		
	}

	private void copyList(ArrayList<Integer> auxList, MOVE move) {
		switch(move) {
		case UP:
			this.up.setDistancias(new ArrayList<Integer>(auxList));
			break;
		case RIGHT:
			this.right.setDistancias(new ArrayList<Integer>(auxList));
			break;
		case DOWN:
			this.down.setDistancias(new ArrayList<Integer>(auxList));
			break;
		case LEFT:
			this.left.setDistancias(new ArrayList<Integer>(auxList));
			break;
		case NEUTRAL:
		default:
		}
	}

	@Override
	public CBRQuery getQuery() {
		GhostDescription description = new GhostDescription();
		
		description.setMspacmanLives(mspacmanLives);
		description.setScore(score);
		description.setTime(time);
		description.setType(type);
		description.setEdible(edible);
		description.setEdibleTime(edibleTime);
		description.setMspacmanToPPill(mspacmanToPPill);
		description.setUp(up);
		description.setRight(right);
		description.setDown(down);
		description.setLeft(left);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeNearestGhosts(Game game, MOVE move, ArrayList<Integer> list) {
		Integer nearestChasing = Integer.MAX_VALUE;
		Integer nearestEdible = Integer.MAX_VALUE;
		Integer nearestEdibleTime = Integer.MAX_VALUE;
		
		for(GHOST g: GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			boolean edible = game.isGhostEdible(g);
			
			int distance;
			if(pos != -1 && this.type!=g) 
				distance = (int)game.getDistance(game.getGhostCurrentNodeIndex(type), pos, DM.PATH);
			else
				distance = Integer.MAX_VALUE;
			
			// nearestEdible y nearestEdibleTime
			if(edible && distance < nearestEdible)
			{
				nearestEdible = distance;
				nearestEdibleTime = game.getGhostEdibleTime(g);
			}
			// nearestChasing
			else if(!edible && distance<nearestChasing) {
				nearestChasing = distance;
			}
		}
		list.add(1, nearestEdible);
		list.add(2, nearestEdibleTime);
		list.add(3, nearestChasing);
	}
}

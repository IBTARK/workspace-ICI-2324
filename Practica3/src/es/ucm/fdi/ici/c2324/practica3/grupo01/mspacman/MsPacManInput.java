package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}

	Integer time;
	Integer lives; //Remaining lives of MsPacMan
	/*
	 List for each possible movement with the next information:
	 0: distance to the nearest chasing ghost
	 1: distance to the nearest edible ghost
	 2: remaining edible time of the nearest edible ghost
	 3: distance to the nearest PPill
	*/
	ArrayList<Integer> up;  
	ArrayList<Integer> down;
	ArrayList<Integer> right;
	ArrayList<Integer> left;
	
	
	@Override
	public void parseInput() {
		time = game.getTotalTime();
		lives = game.getPacmanNumberOfLivesRemaining();
		
		//Actual position of MsPacMan
		int actPos = game.getPacmanCurrentNodeIndex();
		//Last move made by MsPacMan
		MOVE lastMove = game.getPacmanLastMoveMade();
		//Possible moves of MsPacMan
		ArrayList<MOVE> possibleMoves = new ArrayList<MOVE>(Arrays.asList(game.getPossibleMoves(actPos, lastMove)));
		
		//UP move is computed
		if(possibleMoves.contains(MOVE.UP)) up = computeMovement(game, MOVE.UP);
		else up = null;
		
		//DOWN move is computed
		if(possibleMoves.contains(MOVE.DOWN)) down = computeMovement(game, MOVE.DOWN);
		else down = null;
		
		//RIGHT move is computed
		if(possibleMoves.contains(MOVE.RIGHT)) right = computeMovement(game, MOVE.RIGHT);
		else right = null;
		
		//LEFT move is computed
		if(possibleMoves.contains(MOVE.LEFT)) left = computeMovement(game, MOVE.LEFT);
		else left = null;
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		description.setTime(time);
		description.setLives(lives);
		description.setUpVector(up);
		description.setDownVector(down);
		description.setRightVector(right);
		description.setLeftVector(left);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private ArrayList<Integer> computeMovement(Game game, MOVE m) {
		//Actual position of MsPacMan
		int actPos = game.getPacmanCurrentNodeIndex();
		//Position of MsPacMan after making the move m
		int nextPos = game.getNeighbour(actPos, m);
		//ArrayList to be returned
		ArrayList<Integer> vector = new ArrayList<Integer>();
		
		//The distance to the nearest chasing ghost is computed
		vector.add(computeDistanceNearestChasingGhostToPos(game, nextPos, m));
		//The distance to the nearest edible ghost and his remaining edidle time is computed
		ArrayList<Integer> distTimeNearestEdible = computeDistanceTimeNearestEdibleGhostToPos(game, nextPos, m);
		vector.add(distTimeNearestEdible.get(0)); //Distance
		vector.add(distTimeNearestEdible.get(1)); //Time
		//The distance to the nearest PPill is computed
		vector.add(computeDistanceNearestPPillToPos(game, nextPos, m));
		//The distance to the nearest Pill is computed
		vector.add(computeDistanceNearestPillToPos(game, nextPos, m));
		
		return vector;
	}
	
	/**
	 * Gets the nearest ghost (edible or chasing) to the index pos
	 * 
	 * @param game
	 * @param pos
	 * @param edible Indicates if the nearest ghost to pos has to be edible or chasing (true = edible, false = chasing)
	 * @return the nearest ghost (edible or chasing) to pos. null if no ghost is close
	 */
	private GHOST getNearestGhostToPos(Game game, int pos, MOVE m, boolean edible) {
		int nearestGhostDistance = Integer.MAX_VALUE, distance;
		GHOST nearest = null;
		
		//Check all the ghosts
		for(GHOST g: GHOST.values()) {
			//Only the ones determined by "edible"
			if(game.isGhostEdible(g) == edible) {
				//Position of the ghost g
				int ghostPos = game.getGhostCurrentNodeIndex(g);
				//Last movement of the ghost g
				MOVE ghostLastMove = game.getGhostLastMoveMade(g);
				
				if(ghostPos != -1) 
					if(edible) distance = game.getShortestPathDistance(pos, ghostPos, m);
					else distance = game.getShortestPathDistance(ghostPos, pos, ghostLastMove);
				else
					distance = Integer.MAX_VALUE;
				
				if(distance < nearestGhostDistance){
					nearestGhostDistance = distance;
					nearest = g;
				}
			}
		}
		
		return nearest;
	}
	
	/**
	 * Gets the nearest PPill to the index pos
	 * 
	 * @param game
	 * @param pos
	 * @param m movement to be made
	 * @return the nearest PPill to pos. null if no PPill is close
	 */
	private Integer getNearestPPillToPos(Game game, int pos, MOVE m) {
		int nearestPPillDistance = Integer.MAX_VALUE, distance;
		Integer closestPPill = null;
		
		//Check all the remaining power pills
		for(int pPill: game.getActivePowerPillsIndices()) {
			distance = game.getShortestPathDistance(pos, pPill, m);
			if(distance < nearestPPillDistance) {
				nearestPPillDistance = distance;
				closestPPill = pPill;
			}	
		}
		
		return closestPPill;
	}
	
	/**
	 * Gets the nearest Pill to the index pos
	 * 
	 * @param game
	 * @param pos
	 * @param m movement to be made
	 * @return the nearest Pill to pos. null if no Pill is close
	 */
	private Integer getNearestPillToPos(Game game, int pos, MOVE m) {
		int nearestPillDistance = Integer.MAX_VALUE, distance;
		Integer closestPill = null;
		
		//Check all the remaining power pills
		for(int pill: game.getActivePillsIndices()) {
			distance = game.getShortestPathDistance(pos, pill, m);
			if(distance < nearestPillDistance) {
				nearestPillDistance = distance;
				closestPill = pill;
			}	
		}
		
		return closestPill;
	}
	
	/**
	 * Gets the distance to the nearest chasing ghost to MsPacMan supposing she is located in pos
	 * 
	 * @param g
	 * @param pos
	 * @param m movement to be made
	 * @return distance to the nearest chasing ghost to pos. null if no chasing ghost is close
	 */
	private Integer computeDistanceNearestChasingGhostToPos(Game g, int pos, MOVE m) {
		GHOST nearestChasing = getNearestGhostToPos(game, pos, m, false);
		
		return nearestChasing != null ? game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearestChasing), m) : null;
	}
	
	/**
	 * Gets the distance to the nearest edible ghost to MsPacMan supposing she is located in pos. And also
	 * gets the remaining edible time of that ghost
	 * 
	 * @param g
	 * @param pos
	 * @param m movement to be made
	 * @return distance to the nearest edible ghost to pos and remaining edible time of that ghost. both null if no edible ghost is close
	 */
	private ArrayList<Integer> computeDistanceTimeNearestEdibleGhostToPos(Game g, int pos, MOVE m) {
		ArrayList<Integer> resul = new ArrayList<Integer>();
		GHOST nearestEdible = getNearestGhostToPos(game, pos, m, true);
		
		if(nearestEdible != null) {
			resul.add(game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearestEdible), m));
			resul.add(game.getGhostEdibleTime(nearestEdible));
		}
		else {
			resul.add(null);
			resul.add(null);
		}
		
		return resul;
	}
	
	/**
	 * Gets the distance to the nearest PPill to MsPacMan supposing she is located in pos
	 * 
	 * @param g
	 * @param pos
	 * @param m movement to be made
	 * @return distance to the nearest PPill to pos. null if no PPill is close
	 */
	private Integer computeDistanceNearestPPillToPos(Game g, int pos, MOVE m) {
		Integer nearestPPill = getNearestPPillToPos(game, pos, m);
		
		return nearestPPill != null ? game.getShortestPathDistance(pos, nearestPPill, m) : null;
	}
	
	/**
	 * Gets the distance to the nearest Pill to MsPacMan supposing she is located in pos
	 * 
	 * @param g
	 * @param pos
	 * @param m movement to be made
	 * @return distance to the nearest Pill to pos. null if no Pill is close
	 */
	private Integer computeDistanceNearestPillToPos(Game g, int pos, MOVE m) {
		Integer nearestPill = getNearestPillToPos(game, pos, m);
		
		return nearestPill != null ? game.getShortestPathDistance(pos, nearestPill, m) : null;
	}
}

package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManInput extends CBRInput {

	public MsPacManInput(Game game) {
		super(game);
		
	}
	
	Integer score;
	Integer mspacman;
	MOVE lastMove;
	Integer nearestPPill;
	Integer nearestPill;
	Integer nearestChasingGhost1;
	Integer nearestChasingGhost2;
	Integer nearestEdibleGhost1;
	Integer nearestEdibleGhost2;

	
	@Override
	public void parseInput() {
		computeNearestGhosts(game);
		computeNearestPPill(game);
		computeNearestPill(game);
		lastMove = game.getPacmanLastMoveMade();
		mspacman = game.getPacmanCurrentNodeIndex();
		score = game.getScore();
	}

	@Override
	public CBRQuery getQuery() {
		MsPacManDescription description = new MsPacManDescription();
		description.setMspacman(mspacman);
		description.setLastMove(lastMove);
		description.setNearestEdibleGhost1(nearestEdibleGhost1);
		description.setNearestEdibleGhost2(nearestEdibleGhost2);
		description.setNearestChasingGhost1(nearestChasingGhost1);
		description.setNearestChasingGhost2(nearestChasingGhost2);
		description.setNearestPPill(nearestPPill);
		description.setNearestPill(nearestPill);
		description.setScore(score);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}
	
	private void computeNearestGhosts(Game game) {
		nearestChasingGhost1 = Integer.MAX_VALUE;
		nearestChasingGhost2 = Integer.MAX_VALUE;
		nearestEdibleGhost1 = Integer.MAX_VALUE;
		nearestEdibleGhost2 = Integer.MAX_VALUE;
		
		for(GHOST g: GHOST.values()) {
			int pos = game.getGhostCurrentNodeIndex(g);
			int distance; 
			if(pos != -1) 
				distance = (int)game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pos, game.getPacmanLastMoveMade());
			else
				distance = Integer.MAX_VALUE;
			
			// For the edible ghosts
			if(game.isGhostEdible(g)) {
				if(distance < nearestEdibleGhost1)
				{
					nearestEdibleGhost2 = nearestEdibleGhost1;
					nearestEdibleGhost1 = distance;
				}
				else if (distance < nearestEdibleGhost2)
				{
					nearestEdibleGhost2 = distance;
				}
			}
			// For the chasing ghosts
			else {
				if(distance < nearestChasingGhost1)
				{
					nearestChasingGhost2 = nearestChasingGhost1;
					nearestChasingGhost1 = distance;
				}
				else if (distance < nearestChasingGhost2)
				{
					nearestChasingGhost2 = distance;
				}
			}
		}
	}
	
	
	
	private void computeNearestPPill(Game game) {
		nearestPPill = Integer.MAX_VALUE;
		for(int pos: game.getActivePowerPillsIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < nearestPPill)
				nearestPPill = distance;
		}
	}
	
	private void computeNearestPill(Game game) {
		nearestPill = Integer.MAX_VALUE;
		for(int pos: game.getActivePillsIndices()) {
			int distance = (int)game.getDistance(game.getPacmanCurrentNodeIndex(), pos, DM.PATH);
			if(distance < nearestPill)
				nearestPill = distance;
		}
	}
}

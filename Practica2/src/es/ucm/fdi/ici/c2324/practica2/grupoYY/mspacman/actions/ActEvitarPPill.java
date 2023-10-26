package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action to avoid PPills
 */
public class ActEvitarPPill implements Action {


	/**
	 * Gets the best movement to avoid the closest PPill to MsPacMan. Does it by analyzing every possible movement and 
	 * choosing the one that maximizes the distance from MsPacMan to her closest PPill
	 * 
	 * @param game 
	 * @return the best movement to avoid the closest PPill
	 */
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		MOVE nextMove = null;
		int closestPill = MsPacManTools.closestPill(game);
		int distance = Integer.MAX_VALUE;
		//Choose the move that makes MsPacMan stay as far as possible from his closest PPill
		for(MOVE move: game.getPossibleMoves(pos, lastMove)) {
			int nextJunc = MsPacManTools.nextJunction(game, game.getNeighbour(pos, move), move);
			//Path from the current position of MsPacMan to MsPacMans next junction
			int[] p = game.getShortestPath(pos, nextJunc, move);
			//Check if there isn't a PPill in the path p
			if(!MsPacManTools.blockedByClosestPPill(game, p)) {
				//Choose the movement that minimizes the distance to the Pill
				if(nextMove == null || game.getShortestPathDistance(pos, closestPill, move) < distance) {
					nextMove = move;
					distance = game.getShortestPathDistance(pos, closestPill, move);
				}
			}
		}
		
		return nextMove;
	}
	
	@Override
	public String getActionId() {
		return "Evitar Power Pill";
	}
	
}

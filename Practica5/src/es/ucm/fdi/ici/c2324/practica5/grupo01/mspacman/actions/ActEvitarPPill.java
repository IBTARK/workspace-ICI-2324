package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action to avoid PPills
 */
public class ActEvitarPPill implements Action {

	private MsPacManFuzzyData data;
	
	public ActEvitarPPill(MsPacManFuzzyData data) {
		this.data = data;
	}
	
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
		
		MOVE nextMove = MOVE.NEUTRAL;
		int closestPPill = data.getNearestPPill();
		int distance = Integer.MAX_VALUE;
		//Choose the move that makes MsPacMan stay as far as possible from his closest PPill
		for(MOVE move: game.getPossibleMoves(pos, lastMove)) {
			int nextJunc = MsPacManTools.nextJunction(game, game.getNeighbour(pos, move), move);
			//Path from the current position of MsPacMan to MsPacMans next junction
			int[] p = game.getShortestPath(pos, nextJunc, move);
			//Check if there isn't a PPill in the path p
			if(!MsPacManTools.blockedByClosestPPill(game, p, closestPPill)) {
				//Choose the movement that maximizes the distance to the Pill
				if(nextMove == MOVE.NEUTRAL || game.getShortestPathDistance(pos, closestPPill, move) > distance) {
					nextMove = move;
					distance = game.getShortestPathDistance(pos, closestPPill, move);
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

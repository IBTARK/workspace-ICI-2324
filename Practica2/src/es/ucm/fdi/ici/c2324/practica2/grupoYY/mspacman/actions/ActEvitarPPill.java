package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActEvitarPPill implements Action {

	@Override
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		MOVE nextMove = null;
		int closestPill = MsPacManTools.closestPill(game);
		int distance = Integer.MAX_VALUE;
		for(MOVE move: game.getPossibleMoves(pos, lastMove)) {
			int nextJunc = MsPacManTools.nextJunction(game, pos, move);
			int[] p = game.getShortestPath(pos, nextJunc, move);
			if(!MsPacManTools.blockedByClosestPPill(game, p)) {
				if(nextMove != null) {
					if(game.getShortestPathDistance(pos, closestPill, move) < distance) {
						nextMove = move;
					}
				}
				else {
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

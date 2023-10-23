package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActHuirVariosFantasmas implements Action {
	
	@Override
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		MOVE nextMove = lastMove;
		GHOST nearestGhost = MsPacManTools.getNearestChasing(game, pos, lastMove);
		int ghostIndex = game.getGhostCurrentNodeIndex(nearestGhost);
		int distance2pcm = game.getShortestPathDistance(ghostIndex, pos, game.getGhostLastMoveMade(nearestGhost));
		// get next move away from ghosts in this junction
		for(MOVE move: game.getPossibleMoves(pos, lastMove)) {
			int nextJunc = MsPacManTools.nextJunction(game, pos, move);
			nearestGhost = MsPacManTools.getNearestChasing(game, nextJunc, move);
			ghostIndex = game.getGhostCurrentNodeIndex(nearestGhost);
			int aux = game.getShortestPathDistance(ghostIndex, nextJunc, game.getGhostLastMoveMade(nearestGhost));
			if(aux > distance2pcm) {
				nextMove = move;
				distance2pcm = aux;
			}
		}
		return nextMove;
	}
	
	@Override
	public String getActionId() {
		return "Huir de varios fantasmas";
	}
}

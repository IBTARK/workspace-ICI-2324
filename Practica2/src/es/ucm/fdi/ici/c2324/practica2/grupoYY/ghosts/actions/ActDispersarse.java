package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActDispersarse implements Action {
	
	private GHOST ghost;
	
	public ActDispersarse(GHOST g) {
		ghost = g;
	}
	
	@Override
	public MOVE execute(Game game) {
		int pos = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		MOVE toPacman = game.getNextMoveTowardsTarget(pos, game.getPacmanCurrentNodeIndex(), DM.PATH);
		MOVE move = null; int separation = 0;
		for (MOVE m : game.getPossibleMoves(pos, lastMove)) {
			int dist = game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(GhostsTools.getNearestEdible(game, ghost)));
			if (m != toPacman && dist > separation) {
				separation = dist;
				move = m;
			}
		}
		return move;
	}

	@Override
	public String getActionId() {
		return "Dispersarse";
	}
}

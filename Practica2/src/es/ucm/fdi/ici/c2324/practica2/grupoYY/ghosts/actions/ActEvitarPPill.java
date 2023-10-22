package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActEvitarPPill implements Action {
	
	private GHOST ghost;
	
	public ActEvitarPPill(GHOST g) {
		ghost = g;
	}
	
	@Override
	public MOVE execute(Game game) {
		int pos = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		int ppill = MsPacManTools.closestPPill(game);
		return game.getNextMoveAwayFromTarget(pos, ppill, lastMove, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Evitar PPill";
	}
}

package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActKamikazeFantasma implements Action {

	@Override
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		GHOST ghost = MsPacManTools.getNearestEdible(game, pos, lastMove);
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		
		return game.getApproximateNextMoveTowardsTarget(pos, ghostIndex, lastMove, DM.PATH);
	}
	
	@Override
	public String getActionId() {
		return "Kamikaze a fantasma";
	}
	
}
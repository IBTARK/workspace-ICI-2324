package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActFlanquearFantasma implements Action {
	
	@Override
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		GHOST ghost = MsPacManTools.getNearestEdible(game, pos, lastMove);
		int junction = MsPacManTools.nextJunction(game, game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
		
		return game.getApproximateNextMoveTowardsTarget(pos, junction, lastMove, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Flanquear fantasma";
	}
}

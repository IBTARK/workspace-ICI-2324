package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActKamikazeAPill implements Action {

	@Override
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		int pill = MsPacManTools.closestPPill(game);
		
		return game.getApproximateNextMoveTowardsTarget(pos, pill, lastMove, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Kamikaze a pill";
	}

}

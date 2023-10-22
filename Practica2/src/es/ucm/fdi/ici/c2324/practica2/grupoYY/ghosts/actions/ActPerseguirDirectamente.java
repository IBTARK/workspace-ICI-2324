package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActPerseguirDirectamente implements Action {
	
	private GHOST ghost;
	
	public ActPerseguirDirectamente(GHOST g) {
		ghost = g;
	}
	
	@Override
	public MOVE execute(Game game) {
		int pos = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		
		return game.getNextMoveTowardsTarget(pos, game.getPacmanCurrentNodeIndex(), lastMove, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Perseguir directamente";
	}
}

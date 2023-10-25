package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActHuirDirectamente implements Action {
	//Owner of the FMS
	private GHOST ghost;
	
	public ActHuirDirectamente(GHOST g) {
		ghost = g;
	}
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//If the ghost does not require an action
		if (!game.doesGhostRequireAction(ghost))
			return  MOVE.NEUTRAL;
		
		int pos = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		// FOR DEBUG ---------------------------------------------------------------
		if(GhostsTools.debug() && ghost == GHOST.SUE) {
			System.out.println("SUE: " + getActionId());
		}
		// -------------------------------------------------------------------------
		//Get the movement that makes the ghost move towards MsPacMan
		return game.getNextMoveAwayFromTarget(pos, game.getPacmanCurrentNodeIndex(), lastMove, DM.PATH);
	}
	
	@Override
	public String getActionId() {
		return "Huir directamente";
	}
}
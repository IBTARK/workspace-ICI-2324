package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to avoid the closest PPill to the ghost
public class ActEvitarPPill implements Action {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public ActEvitarPPill(GHOST g) {
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
		
		//Get the closest PPill
		int ppill = MsPacManTools.closestPPill(game);
		// FOR DEBUG ---------------------------------------------------------------
		if(GhostsTools.debug() && ghost == GHOST.SUE) {
			System.out.println("SUE: " + getActionId());
		}
		// -------------------------------------------------------------------------
		//Get the movement that makes the ghost move away from the closest PPill to him
		return game.getNextMoveAwayFromTarget(pos, ppill, lastMove, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Evitar PPill";
	}
}

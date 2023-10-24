package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

//Action of covering the closest PPill (always has to remain at least 1 PPill to enter this action, is ensured in the transition)
public class ActCubrirPPill implements Action {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public ActCubrirPPill(GHOST g) {
		ghost = g;
	}
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//If the ghost does not require an action
		if (!game.doesGhostRequireAction(ghost))
			return  MOVE.NEUTRAL;
		
		//Position of the ghost and closest PPill to the ghost
		int pos = game.getGhostCurrentNodeIndex(ghost), closestPPill = GhostsTools.getClosestPPill(game, ghost);
		//Last movement made by the ghost
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		
		//If there are PPills left move towards the closest one, if not TODO
		return game.getNextMoveTowardsTarget(pos, closestPPill, lastMove, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Dispersarse";
	}
}
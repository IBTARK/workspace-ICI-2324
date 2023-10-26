package es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupo01.tools.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to go towards the nearest chasing not blocked ghost
public class ActIrAChasing implements Action {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public ActIrAChasing(GHOST g) {
		ghost = g;
	}

	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//If the ghost does not require an action
		if (!game.doesGhostRequireAction(ghost))
			return  MOVE.NEUTRAL;
		//Position of the ghost 
		int pos = game.getGhostCurrentNodeIndex(ghost);
		//Closest chasing not blocked ghost to "ghost"
		GHOST nearestChasingNotBlocked = GhostsTools.getNearestChasingNotBlocked(game, ghost);
		//Last movement made by the ghost
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		// FOR DEBUG ---------------------------------------------------------------
		if(GhostsTools.debug() && ghost == GHOST.SUE) {
			System.out.println("SUE: " + getActionId());
		}
		// -------------------------------------------------------------------------
		
		return GhostsTools.goTo(game, pos, game.getGhostCurrentNodeIndex(nearestChasingNotBlocked), lastMove);
	}
	
	@Override
	public String getActionId() {
		return "Ir a Chasing";
	}
}
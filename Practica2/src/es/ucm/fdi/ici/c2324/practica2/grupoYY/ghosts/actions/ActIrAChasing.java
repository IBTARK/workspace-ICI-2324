package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

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
				
		
		return game.getNextMoveTowardsTarget(pos, game.getGhostCurrentNodeIndex(nearestChasingNotBlocked), lastMove, DM.PATH);
	}
	
	@Override
	public String getActionId() {
		return "Evitar Power Pill";
	}
}
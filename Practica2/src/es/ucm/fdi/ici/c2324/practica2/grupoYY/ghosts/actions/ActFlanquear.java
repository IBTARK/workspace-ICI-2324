package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActFlanquear implements Action {
	//Owner of the FMS
	private GHOST ghost;
	
	public ActFlanquear(GHOST g) {
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
		
		//Get the movement that makes the ghost move towards MsPacMans Next Junction
		return game.getNextMoveTowardsTarget(pos, 
				GhostsTools.nextJunction(game, game.getPacmanCurrentNodeIndex(),game.getPacmanLastMoveMade()), 
				lastMove, DM.PATH);
	}
	
	@Override
	public String getActionId() {
		return "Flanquear ";
	}
}
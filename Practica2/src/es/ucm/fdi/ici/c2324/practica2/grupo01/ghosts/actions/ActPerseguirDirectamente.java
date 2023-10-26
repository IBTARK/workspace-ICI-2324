package es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupo01.tools.GhostsTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to chase MsPacMan
public class ActPerseguirDirectamente implements Action {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public ActPerseguirDirectamente(GHOST g) {
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
		return GhostsTools.goTo(game, pos, game.getPacmanCurrentNodeIndex(), lastMove);
	}

	@Override
	public String getActionId() {
		return "Perseguir directamente";
	}
}

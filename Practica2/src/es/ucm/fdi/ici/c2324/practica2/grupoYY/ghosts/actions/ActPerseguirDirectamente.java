package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
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
		
		//Get the movement that makes the ghost move towards MsPacMan
		return game.getNextMoveTowardsTarget(pos, game.getPacmanCurrentNodeIndex(), lastMove, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Perseguir directamente";
	}
}

package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class LookForMsPacman implements Action {


	private GHOST ghost;
	
	public LookForMsPacman(GHOST g) {
		ghost = g;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "Look for MsPacman";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			nextMove = game.getApproximateNextMoveTowardsTarget(
					game.getGhostCurrentNodeIndex(ghost), 
					game.getPacmanCurrentNodeIndex(), 
					game.getGhostLastMoveMade(ghost), 
					DM.PATH);
        }
            
        return nextMove;	
	}
}

package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayFromMs implements Action{


	private GHOST ghost;
	
	public RunAwayFromMs(GHOST g) {
		ghost = g;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "Go To First Junction";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			nextMove = game.getApproximateNextMoveAwayFromTarget(
					game.getGhostCurrentNodeIndex(ghost), 
					game.getPacmanCurrentNodeIndex(), 
					game.getGhostLastMoveMade(ghost), 
					DM.PATH);
        }
            
        return nextMove;	
	}
}

package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToFirstJunction implements Action{

	private GHOST ghost;
	
	public GoToFirstJunction(GHOST g) {
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
			int nextJunction;
			int gIndex = game.getGhostCurrentNodeIndex(ghost);
			int msPacman = game.getPacmanCurrentNodeIndex();
			MOVE gLastMove = game.getGhostLastMoveMade(ghost);
			
			nextJunction = GhostsTools.nextJunction(game, msPacman, game.getPacmanLastMoveMade());
			
			nextMove = game.getApproximateNextMoveTowardsTarget(
					gIndex, 
					nextJunction, 
					gLastMove, 
					DM.PATH);
        }
            
        return nextMove;	
	}
}

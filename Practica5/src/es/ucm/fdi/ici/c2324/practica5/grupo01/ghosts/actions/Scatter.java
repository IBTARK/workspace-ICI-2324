package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Scatter implements Action {


	private GHOST ghost;
	private GhostFuzzyData data;
	
	public Scatter(GHOST g, GhostFuzzyData data) {
		this.ghost = g;
		this.data = data;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "Scatter";
	}

	/**
	 * Returns a direct move away from our nearest chasing ghost.
	 */
	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			int gIndex = game.getGhostCurrentNodeIndex(ghost);
			MOVE gLastMove = game.getGhostLastMoveMade(ghost);
			
			GHOST nearestChasing = GhostsTools.getNearestChasing(game, ghost);
			
			if(nearestChasing != null)
				nextMove = game.getApproximateNextMoveAwayFromTarget(
					gIndex, 
					game.getGhostCurrentNodeIndex(nearestChasing), 
					gLastMove, 
					DM.PATH);
        }
            
        return nextMove;	
	}
}

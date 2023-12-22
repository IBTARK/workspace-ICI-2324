package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayToChasing implements Action {

	private GHOST ghost;
	private GhostFuzzyData data;
	
	public RunAwayToChasing(GHOST g, GhostFuzzyData data) {
		this.ghost = g;
		this.data = data;
	}
	
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "RunAwayToChasing";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			GHOST nearestChasing = null;
			nearestChasing = GhostsTools.getNearestChasing(game, ghost);
			if(nearestChasing != null) {
				nextMove = game.getApproximateNextMoveTowardsTarget(
						game.getGhostCurrentNodeIndex(ghost), 
						game.getGhostCurrentNodeIndex(nearestChasing), 
						game.getGhostLastMoveMade(ghost), 
						DM.PATH);
			}
        }
		return nextMove;	
	}

}

package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ScatterEdible implements Action {

	private GHOST ghost;
	private GhostFuzzyData data;
	
	public ScatterEdible(GHOST g, GhostFuzzyData data) {
		this.ghost = g;
		this.data = data;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "ScatterEdible";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			int gIndex = game.getGhostCurrentNodeIndex(ghost);
			MOVE gLastMove = game.getGhostLastMoveMade(ghost);
			
			GHOST nearestEdible = GhostsTools.getNearestEdible(game, ghost);
			
			if(nearestEdible != null)
				nextMove = game.getApproximateNextMoveTowardsTarget(
					gIndex, 
					game.getGhostCurrentNodeIndex(nearestEdible), 
					gLastMove, 
					DM.PATH);
        }
            
        return nextMove;	
	}
}

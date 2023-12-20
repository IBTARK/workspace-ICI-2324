package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Scatter implements Action {


	private GHOST ghost;
	
	public Scatter(GHOST g) {
		ghost = g;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "Scatter";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			int gIndex = game.getGhostCurrentNodeIndex(ghost);
			int msPacman = game.getPacmanCurrentNodeIndex();
			MOVE gLastMove = game.getGhostLastMoveMade(ghost);
			
			int minDistGhost = Integer.MAX_VALUE, curDist;
			GHOST closestGhost = null;
			for(GHOST g: GHOST.values()) {
				if(game.getGhostLairTime(g) <= 0) {
					curDist = game.getShortestPathDistance(gIndex, game.getGhostCurrentNodeIndex(g), gLastMove);
					if(curDist < minDistGhost) {
						closestGhost = g;
						minDistGhost = curDist;
					}
				}
			}
			
			if(closestGhost != null)
				nextMove = game.getApproximateNextMoveTowardsTarget(
					gIndex, 
					game.getGhostCurrentNodeIndex(closestGhost), 
					gLastMove, 
					DM.PATH);
        }
            
        return nextMove;	
	}
}

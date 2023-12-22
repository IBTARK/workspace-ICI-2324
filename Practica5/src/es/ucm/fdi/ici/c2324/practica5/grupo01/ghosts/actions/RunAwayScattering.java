package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayScattering implements Action {

	private GHOST ghost;
	private GhostFuzzyData data;
	
	public RunAwayScattering(GHOST g, GhostFuzzyData data) {
		this.ghost = g;
		this.data = data;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "Run Away Scattering";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL, scatterMove, escapeMove;
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			int gIndex = game.getGhostCurrentNodeIndex(ghost);
			int msPacman = game.getPacmanCurrentNodeIndex();
			MOVE gLastMove = game.getGhostLastMoveMade(ghost);
			MOVE possibleMoves[] = game.getPossibleMoves(gIndex, gLastMove);
			
			
			int minDistGhost = Integer.MAX_VALUE, curDist;
			GHOST closestGhost = null;
			for(GHOST g: GHOST.values()) {
				if(game.getGhostLairTime(g) <= 0 && game.isGhostEdible(g)) {
					curDist = game.getShortestPathDistance(gIndex, game.getGhostCurrentNodeIndex(g), gLastMove);
					if(curDist < minDistGhost) {
						closestGhost = g;
						minDistGhost = curDist;
					}
				}
			}
			
			escapeMove = game.getApproximateNextMoveAwayFromTarget(gIndex, msPacman, gLastMove, DM.PATH);
			nextMove = escapeMove;
			if(closestGhost != null) {
				scatterMove = game.getApproximateNextMoveAwayFromTarget(
						gIndex, 
						game.getGhostCurrentNodeIndex(closestGhost), 
						gLastMove, 
						DM.PATH);
				
				if(scatterMove!=escapeMove) {
					for(MOVE m: possibleMoves) {
						if(m != scatterMove && m != escapeMove)
							nextMove = m;
					}
				}
			}
			
        }
            
        return nextMove;	
	}
}

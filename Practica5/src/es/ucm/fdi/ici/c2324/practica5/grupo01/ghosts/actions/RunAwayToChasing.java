package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
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
		return "Run Away to Chasing";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			GHOST nearestChasing = null;
			int pos = game.getGhostCurrentNodeIndex(ghost);
			int dist = Integer.MAX_VALUE;
			int aux;
			for(GHOST g: GHOST.values()) {
				if(game.getGhostLairTime(g) <= 0 && !game.isGhostEdible(g)) {
					aux = game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(g), game.getGhostLastMoveMade(ghost));
					if(aux < dist) {
						dist = aux;
						nearestChasing = g;
					}
				}
			}
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

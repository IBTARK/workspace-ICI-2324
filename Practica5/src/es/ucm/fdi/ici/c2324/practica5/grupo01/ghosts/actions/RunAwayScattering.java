package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsTools;
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
		return "RunAwayScattering";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL, scatterMove, escapeMove;
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			int gIndex = game.getGhostCurrentNodeIndex(ghost);
			int mspacman = data.getMspacman();
			if(mspacman == -1) return MOVE.NEUTRAL;

			
			MOVE gLastMove = game.getGhostLastMoveMade(ghost);
			MOVE possibleMoves[] = game.getPossibleMoves(gIndex, gLastMove);
			
			GHOST nearestEdible = GhostsTools.getNearestEdible(game, ghost);
			
			escapeMove = game.getApproximateNextMoveAwayFromTarget(gIndex, mspacman, gLastMove, DM.PATH);
			nextMove = escapeMove;
			if(nearestEdible != null) {
				scatterMove = game.getApproximateNextMoveAwayFromTarget(
						gIndex, 
						game.getGhostCurrentNodeIndex(nearestEdible), 
						gLastMove, 
						DM.PATH);
				
				// Si el movimiento de escapar y separarse coinciden, usamos uno de los dos
				if(scatterMove!=escapeMove) {
					// En caso de no ser el mismo, intentamos encontrar un intermedio, si no lo hay usamos el de escapar
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

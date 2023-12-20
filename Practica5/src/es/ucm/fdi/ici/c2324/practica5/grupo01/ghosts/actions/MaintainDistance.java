package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MaintainDistance implements Action {


	private GHOST ghost;
	
	public MaintainDistance(GHOST g) {
		ghost = g;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "Maintain Distance";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		MOVE escapeMove, chaseMove;
		MOVE possibleMoves[];
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			int gIndex = game.getGhostCurrentNodeIndex(ghost);
			int msPacman = game.getPacmanCurrentNodeIndex();
			MOVE gLastMove = game.getGhostLastMoveMade(ghost);
			
			possibleMoves = game.getPossibleMoves(gIndex, gLastMove);
			
			escapeMove = game.getApproximateNextMoveAwayFromTarget(gIndex, msPacman, gLastMove, DM.PATH);
			chaseMove = game.getApproximateNextMoveTowardsTarget(gIndex, msPacman, gLastMove, DM.PATH);
			
			nextMove = chaseMove; // por si no encontramos otro movimiento posible, asignamos el chaseMove por predeterminado
			for(MOVE m: possibleMoves) {
				if(m!=escapeMove && m!=chaseMove) {
					nextMove = m;
				}
			}
        }
            
        return nextMove;	
	}
}

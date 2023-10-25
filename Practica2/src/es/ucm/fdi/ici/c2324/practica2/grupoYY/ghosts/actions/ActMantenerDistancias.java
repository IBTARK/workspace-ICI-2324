package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to maintain distance to MsPacMan (the movement is made randomly, to introduce a random factor)
public class ActMantenerDistancias implements Action {
	//Owner of the FMS
	private GHOST ghost;
	
	public ActMantenerDistancias(GHOST g) {
		ghost = g;
	}

	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//If the ghost does not require an action
		if (!game.doesGhostRequireAction(ghost))
			return  MOVE.NEUTRAL;
		
		//Get a random movement
		MOVE[] moves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
		// FOR DEBUG ---------------------------------------------------------------
		if(GhostsTools.debug() && ghost == GHOST.SUE) {
			System.out.println("SUE: " + getActionId());
		}
		// -------------------------------------------------------------------------
		return moves[(new Random()).nextInt(moves.length)];
	}

	@Override
	public String getActionId() {
		return "Mantener Distancias";
	}
}

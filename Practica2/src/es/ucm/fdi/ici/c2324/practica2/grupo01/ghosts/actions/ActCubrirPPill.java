package es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupo01.tools.GhostsTools;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

//Action of covering the closest PPill (always has to remain at least 1 PPill to enter this action, is ensured in the transition)
public class ActCubrirPPill implements Action {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public ActCubrirPPill(GHOST g) {
		ghost = g;
	}
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//If the ghost does not require an action
		if (!game.doesGhostRequireAction(ghost))
			return  MOVE.NEUTRAL;
		
		//Position of the ghost and closest PPill to the ghost
		int pos = game.getGhostCurrentNodeIndex(ghost), closestPPill = GhostsTools.getClosestPPill(game, ghost);
		//Last movement made by the ghost
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		
		// FOR DEBUG ---------------------------------------------------------------
		if(GhostsTools.debug() && ghost == GHOST.SUE) {
			System.out.println("SUE: " + getActionId());
		}
		// -------------------------------------------------------------------------
		if(closestPPill < 0) {
			return game.getPossibleMoves(pos, lastMove)[new Random().nextInt(game.getPossibleMoves(pos, lastMove).length)];
		}
		//If there are PPills left move towards the closest one
		return GhostsTools.goTo(game, pos, closestPPill, lastMove);
	}

	@Override
	public String getActionId() {
		return "Cubrir PPill";
	}
}
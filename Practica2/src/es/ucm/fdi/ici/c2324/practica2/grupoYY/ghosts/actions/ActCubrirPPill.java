package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsCoordination;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action of covering the closest PPill (always has to remain at least 1 PPill to enter this action, is ensured in the transition)
public class ActCubrirPPill implements Action {
	
	//Owner of the FMS
	private GHOST ghost;
	private GhostsCoordination coord;
	
	public ActCubrirPPill(GHOST g, GhostsCoordination coord) {
		ghost = g;
		this.coord = coord;
	}
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//If the ghost does not require an action
		if (!game.doesGhostRequireAction(ghost))
			return  MOVE.NEUTRAL;
		
		//Position of the ghost
		int pos = game.getGhostCurrentNodeIndex(ghost);
		//Last movement made by the ghost
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		
		int ppill = -1;
		for (Integer pp : game.getActivePowerPillsIndices())
			if (coord.whoCoversPPill(pp) == ghost)
				ppill = pp;
		
		// FOR DEBUG ---------------------------------------------------------------
		if(GhostsTools.debug() && ghost == GHOST.SUE) {
			System.out.println("SUE: " + getActionId());
		}
		// -------------------------------------------------------------------------
		
		//If there are PPills left move towards the closest one
		return GhostsTools.goTo(game, pos, ppill, lastMove);
	}

	@Override
	public String getActionId() {
		return "Cubrir PPill";
	}
}
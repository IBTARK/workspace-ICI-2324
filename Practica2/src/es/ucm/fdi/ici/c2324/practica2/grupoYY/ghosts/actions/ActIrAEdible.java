package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsCoordination;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to go towards an edible ghost
public class ActIrAEdible implements Action {
	//Owner of the FMS
	private GHOST ghost;
	private GhostsCoordination coord;
	
	public ActIrAEdible(GHOST g, GhostsCoordination coord) {
		ghost = g;
		this.coord = coord;
	}

	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//If the ghost does not require an action
		if (!game.doesGhostRequireAction(ghost))
			return MOVE.NEUTRAL;
		
		int pos = game.getGhostCurrentNodeIndex(ghost);
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		
		int nearest = -1;
		for (GHOST g : GHOST.values())
			if (coord.whoCoversEdible(g) == ghost)
				nearest = game.getGhostCurrentNodeIndex(g);

		// FOR DEBUG ---------------------------------------------------------------
		if(GhostsTools.debug() && ghost == GHOST.SUE) {
			System.out.println("SUE: " + getActionId());
		}
		// -------------------------------------------------------------------------
		//Get the movement that makes the ghost move towards the one to cover
		return GhostsTools.goTo(game, pos, nearest, lastMove);
	}

	@Override
	public String getActionId() {
		return "Ir a compañero edible";
	}
}

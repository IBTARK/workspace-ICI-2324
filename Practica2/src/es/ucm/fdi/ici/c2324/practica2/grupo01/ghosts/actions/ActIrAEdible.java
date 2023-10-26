package es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.GhostsCoordination;
import es.ucm.fdi.ici.c2324.practica2.grupo01.tools.GhostsTools;
import pacman.game.Constants.DM;
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
		
		int nearest = -1,  minDist = Integer.MAX_VALUE;
		for (GHOST g : GHOST.values())
			if (game.isGhostEdible(g) && coord.whoCoversEdible(g) == null) {
				int posAux = game.getGhostCurrentNodeIndex(g);
				int dist = game.getShortestPathDistance(pos, posAux, lastMove);
				if (dist < minDist) {
					minDist = dist;
					nearest = posAux;
				}
			}
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

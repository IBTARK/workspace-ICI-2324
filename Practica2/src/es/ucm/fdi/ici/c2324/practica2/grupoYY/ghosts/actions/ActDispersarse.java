package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to scatter
public class ActDispersarse implements Action {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public ActDispersarse(GHOST g) {
		ghost = g;
	}
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//If the ghost does not require an action
		if (!game.doesGhostRequireAction(ghost))
			return  MOVE.NEUTRAL;
		
		//Get the position of the ghost
		int pos = game.getGhostCurrentNodeIndex(ghost);
		//Get the last move made by the ghost
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		//Get the next movement necessary to move towards MsPacMan 
		MOVE toPacman = game.getNextMoveTowardsTarget(pos, game.getPacmanCurrentNodeIndex(), DM.PATH);
		
		MOVE move = null; int separation = 0;
		
		//Choose the movement that 
		for (MOVE m : game.getPossibleMoves(pos, lastMove)) {
			//Distance from the ghost to the nearest edible ghost
			int dist = game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(GhostsTools.getNearestEdible(game, ghost)));
			//Try to find a movement to cover an edible ghost 
			if (m != toPacman && dist > separation) {
				separation = dist;
				move = m;
			}
		}
		// FOR DEBUG ---------------------------------------------------------------
		if(GhostsTools.debug() && ghost == GHOST.SUE) {
			System.out.println("SUE: " + getActionId());
		}
		// -------------------------------------------------------------------------
		//It there is no edible ghost to cover, the ghost moves towards MsPacMan
		return move == null ? toPacman : move;
	}

	@Override
	public String getActionId() {
		return "Dispersarse";
	}
}

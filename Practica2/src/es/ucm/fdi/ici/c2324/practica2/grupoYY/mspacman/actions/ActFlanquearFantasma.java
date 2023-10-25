package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to flank the closest edible ghost
public class ActFlanquearFantasma implements Action {
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the nearest edible ghost to MsPacMan
		GHOST ghost = MsPacManTools.getNearestEdible(game, pos, lastMove);
		//Get the junction that the nearest edible ghost is moving towards 
		int junction = MsPacManTools.nextJunction(game, game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
		// FOR DEBUG --------------------------------------------------------
		if(MsPacManTools.debug()) {
			System.out.println(getActionId());
		}
		// ------------------------------------------------------------------
		return game.getApproximateNextMoveTowardsTarget(pos, junction, lastMove, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Flanquear fantasma";
	}
}

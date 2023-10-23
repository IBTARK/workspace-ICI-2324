package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to run away from the ghosts
public class ActHuirDeFantasma implements Action {

	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//MsPacMans current position
		int pos = game.getPacmanCurrentNodeIndex();
		//MsPacMans last move
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//TODO
		
		//Nearest (and only) chasing ghost to pacman
		GHOST nearestChasing = MsPacManTools.getNearestChasing(game, pos, lastMove);
		
		//Choose the best available move.
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Huir de Fantasma";
	}
}

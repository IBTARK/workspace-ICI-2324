package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action to run away from the ghost moving towards the closest PPill
 */
public class ActHuirHaciaPPill implements Action{

	@Override
	/**
	 * Gets the best movement for MsPacMan to run away from the chasing ghosts towards the nearest PPill. 
	 * Does it by choosing the movement that makes her get closer to the PPill.
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to run away from the ghosts and moving toward the PPill
	 */
	public MOVE execute(Game game) {
		//MsPacMans current position
		int pos = game.getPacmanCurrentNodeIndex();
		//MsPacMans last move
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one move is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Nearest Power Pill to MsPacMan
		int nearestPPill = MsPacManTools.closestPPill(game);
		
		//Move to reduce the distance to the nearest Power Pill
		
		return MsPacManTools.goTo(game, pos, nearestPPill, lastMove);
	}

	@Override
	public String getActionId() {
		return "Huir Hacia Power Pill";
	}
}

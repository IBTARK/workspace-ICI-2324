package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action that always makes the movement that makes MsPacMan move towards the closest pill
 */
public class ActKamikazeAPill implements Action {

	/**
	 * Gets the movement that gets MsPacMan closer to the closest pill to her.
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to get closer to the closest pill to her.
	 */
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the closest pill to MsPacMan
		int pill = MsPacManTools.closestPill(game); // TODO quizás sacar de data (no debería hacer falta)
		
		//Returns the movement that makes MsPacMan move toward the pill
		return MsPacManTools.goTo(game, pos, pill, lastMove);
	}

	@Override
	public String getActionId() {
		return "Kamikaze a pill";
	}

}

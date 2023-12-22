package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


/**
 * Action to move towards the pill
 */
public class ActBuscarPills implements Action {

	@Override
	/**
	 * Returns the necessary movement to move towards the pills
	 *
	 * Gets the best movement to move towards the closest pill to MsPacMan. Does it by analyzing every possible movement and 
	 * choosing the one that minimizes the distance from MsPacMan to her closest pill
	 * 
	 * @param game 
	 * @return the best movement to move towards the closest pill
	 */
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the closest pill to MsPacMan
		int pill = MsPacManTools.closestPill(game);
		
		//If there are not visible pills a random move is made
		if (pill < 0) {
			MOVE[] moves = game.getPossibleMoves(pos, lastMove);
			return moves[new Random(moves.length).nextInt()];
		}
		return MsPacManTools.goTo(game, pos, pill, lastMove);
	}

	@Override
	public String getActionId() {
		return "Buscar Pills";
	}

}

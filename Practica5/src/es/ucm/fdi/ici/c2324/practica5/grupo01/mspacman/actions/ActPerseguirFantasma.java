package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action to chase the closest ghost
 */
public class ActPerseguirFantasma implements Action{
	
	private MsPacManFuzzyData data;
	
	public ActPerseguirFantasma(MsPacManFuzzyData data) {
		this.data = data;
	}
	
	/**
	 * Gets the movement that gets MsPacMan closer to the closest pill to her.
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to get closer to the closest pill to her.
	 */
	public MOVE execute(Game game) {
		//MsPacMans current position
		int pos = game.getPacmanCurrentNodeIndex();
		//MsPacMans last move
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one move is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Move to chase the nearest edible ghost reducing the path distance
		return MsPacManTools.goTo(game, pos, data.getNearestEdible(), lastMove);
	}

	@Override
	public String getActionId() {
		return "Perseguir Fantasma";
	}
}

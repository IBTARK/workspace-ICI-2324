package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action to flank the closest edible ghost
 */
public class ActFlanquearFantasma implements Action {
	
	private MsPacManFuzzyData data;
	
	public ActFlanquearFantasma(MsPacManFuzzyData data) {
		this.data = data;
	}

	/**
	 * Gets the best movement that MsPacMan has to execute to move towards the junction that the closest edible ghost
	 * to her is moving to. This is done by making the first move of the shortest path from MsPacMan to her closest edible ghost.
	 * 
	 * @param game 
	 * @return the best movement to move towards the next junction of the closest edible ghost to MsPacMan
	 */
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the junction that the nearest edible ghost is moving towards 
		int junction = data.getNearestEdibleNextJunction();
		
		return MsPacManTools.goTo(game, pos, junction, lastMove);
	}

	@Override
	public String getActionId() {
		return "ActFlanquearFantasma";
	}
}

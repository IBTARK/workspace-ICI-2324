package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action that executes a random move
 */
public class RandomAction implements Action {

    private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	
	/**
	 * Gets a random move
	 * 
	 * @param game
	 */
	public MOVE execute(Game game) {
	
		return allMoves[rnd.nextInt(allMoves.length)];
	}

	@Override
	public String getActionId() {
		return "Random Action";
	}

}

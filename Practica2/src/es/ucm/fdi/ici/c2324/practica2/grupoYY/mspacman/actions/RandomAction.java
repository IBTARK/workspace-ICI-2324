package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RandomAction implements Action {

	public RandomAction() {
		
	}

    private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	
	@Override
	public MOVE execute(Game game) {
		// FOR DEBUG --------------------------------------------------------
		if(MsPacManTools.debug()) {
			System.out.println(getActionId());
		}
		// ------------------------------------------------------------------
		return allMoves[rnd.nextInt(allMoves.length)];
	}

	@Override
	public String getActionId() {
		return "Random Action";
	}

}

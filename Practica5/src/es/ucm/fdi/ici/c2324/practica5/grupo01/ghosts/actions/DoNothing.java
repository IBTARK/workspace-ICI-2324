package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DoNothing implements Action {
	
	@Override
	public String getActionId() {
		return "DoNothing";
	}

	@Override
	public MOVE execute(Game game) {
		return MOVE.NEUTRAL;
	}


}

package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

//Action of staying dead
public class ActMuerto implements Action {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public ActMuerto(GHOST g) {
		ghost = g;
	}
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		return MOVE.NEUTRAL;
	}

	@Override
	public String getActionId() {
		return "Dispersarse";
	}
}
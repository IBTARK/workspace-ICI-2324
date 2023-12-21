package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayToChasing implements Action {

	private GHOST ghost;
	private GhostFuzzyData data;
	
	public RunAwayToChasing(GHOST g, GhostFuzzyData data) {
		this.ghost = g;
		this.data = data;
	}
	
	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MOVE execute(Game game) {
		// TODO Auto-generated method stub
		return null;
	}

}

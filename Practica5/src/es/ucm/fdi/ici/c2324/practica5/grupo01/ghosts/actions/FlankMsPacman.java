package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FlankMsPacman implements Action {

	private GHOST ghost;
	private GhostFuzzyData data;
	
	public FlankMsPacman(GHOST g, GhostFuzzyData data) {
		this.ghost = g;
		this.data = data;
	}
	
	@Override
	public String getActionId() {
		return "Flank MsPacman";
	}

	@Override
	public MOVE execute(Game game) {
		int mspacman = data.getMspacman();
		if(mspacman == -1) return MOVE.NEUTRAL;
		
		return MOVE.NEUTRAL;
	}

}

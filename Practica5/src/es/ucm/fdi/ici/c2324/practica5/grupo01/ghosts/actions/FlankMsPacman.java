package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import java.util.HashMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsTools;
import pacman.game.Constants.DM;
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
		return "FlankMsPacman";
	}

	/**
	 * Returns an "optimal" next move towards one of the next junctions of pacman.
	 */
	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		int mspacman = data.getMspacman();
		MOVE msLastMove = data.getMsLastMove();
		if(mspacman == -1) return MOVE.NEUTRAL;
		
		if(game.doesGhostRequireAction(ghost)) {
			nextMove = GhostsTools.getOptimalFlankingMove(game, ghost, mspacman, msLastMove);
		}
		
		
		return nextMove;
	}

}

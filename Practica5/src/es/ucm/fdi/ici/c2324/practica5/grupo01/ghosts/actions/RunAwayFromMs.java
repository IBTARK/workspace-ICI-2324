package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayFromMs implements Action{


	private GHOST ghost;
	private GhostFuzzyData data;
	
	public RunAwayFromMs(GHOST g, GhostFuzzyData data) {
		this.ghost = g;
		this.data = data;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "Run Away from MsPacman";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		int mspacman = data.getMspacman();
		if(mspacman == -1) return MOVE.NEUTRAL;

		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			nextMove = game.getApproximateNextMoveAwayFromTarget(
					game.getGhostCurrentNodeIndex(ghost),
					mspacman,
					game.getGhostLastMoveMade(ghost), 
					DM.PATH);
        }
            
        return nextMove;	
	}
}

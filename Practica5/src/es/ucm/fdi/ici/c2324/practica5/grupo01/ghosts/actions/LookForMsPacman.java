package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import java.util.Random;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class LookForMsPacman implements Action {


	private GHOST ghost;
	private GhostFuzzyData data;
	
	public LookForMsPacman(GHOST g, GhostFuzzyData data) {
		this.ghost = g;
		this.data = data;
	}

	@Override
	public String getActionId() {
		// TODO Auto-generated method stub
		return "LookForMsPacman";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		int mspacman = data.getMspacman();
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			int gIndex = game.getGhostCurrentNodeIndex(ghost);
			MOVE gLastMove = game.getGhostLastMoveMade(ghost);
			
			MOVE possibleMoves[] = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
			Random rnd = new Random(game.getTotalTime());
			nextMove = possibleMoves[rnd.nextInt(possibleMoves.length)];
					
			GHOST nearestChasing = GhostsTools.getNearestChasing(game, ghost, 50);;
			if(nearestChasing != null) {
				MOVE nearestChasingMove = game.getApproximateNextMoveTowardsTarget(gIndex, game.getGhostCurrentNodeIndex(nearestChasing), gLastMove, DM.PATH);
				if(nextMove == nearestChasingMove)
					nextMove = game.getApproximateNextMoveAwayFromTarget(gIndex, game.getGhostCurrentNodeIndex(nearestChasing), gLastMove, DM.PATH);
			}
			
        }
            
        return nextMove;	
	}
}

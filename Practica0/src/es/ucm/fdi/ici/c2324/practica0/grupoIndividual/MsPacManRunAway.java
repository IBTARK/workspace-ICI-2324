package es.ucm.fdi.ici.c2324.practica0.grupoIndividual;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacManRunAway extends PacmanController {

	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(getNearestGhostFromMsPacMan(game)), game.getPacmanLastMoveMade(), DM.PATH);
	}
	
	
	private GHOST getNearestGhostFromMsPacMan(Game game) {
		double min = 1000000;
		GHOST awayFrom = null;
		
		for (GHOST ghostType : GHOST.values()) {
			double dis = game.getDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType), game.getGhostLastMoveMade(ghostType), DM.PATH);
			
			if(dis < min) {
				awayFrom = ghostType;
				min = dis;
			}
        }
		
		return awayFrom;
	}

}

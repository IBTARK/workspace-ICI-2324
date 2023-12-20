package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action to run away from the chasing ghosts
 */
public class ActHuirDeFantasma implements Action {

	/**
	 * Gets the best movement for MsPacMan to run away from the chasing ghosts. Does it by analyzing every possible movement and 
	 * assigning them an score. The best movement is the one with highest score
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to run away from the ghosts
	 */
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//Nearest chasing ghost to MsPacMan
		GHOST nearest = MsPacManTools.getNearestChasing(game, pos, lastMove);
		int posNearest = game.getGhostCurrentNodeIndex(nearest);
		MOVE lastMoveNearest = game.getGhostLastMoveMade(nearest);
		
		
		int maxDist = Integer.MIN_VALUE;
		MOVE bestMove = MOVE.NEUTRAL;
		
		//Get the movement that maximizes the distance to the nearest chasing ghost
		for(MOVE m : game.getPossibleMoves(pos, lastMove)) {
			int nextDist = game.getShortestPathDistance(posNearest, game.getNeighbour(pos, m), lastMoveNearest);
			
			if(nextDist > maxDist) {
				maxDist = nextDist;
				bestMove = m;
			}
		}
		
		return bestMove;
	}

	@Override
	public String getActionId() {
		return "Huir de Fantasma";
	}
}

package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to move away from several ghosts
public class ActHuirVariosFantasmas implements Action {
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Closest ghost to MsPacMan
		GHOST closestGhost =  MsPacManTools.getNearestChasing(game, pos, lastMove);
		//Index and last movement of the closest ghost
		int closestGhostIndex = game.getGhostCurrentNodeIndex(closestGhost);
		MOVE closestGhostLastMove = game.getGhostLastMoveMade(closestGhost);
		
		
		int maxDists = -1;
		MOVE bestMove = null;
		
		for(MOVE m : game.getPossibleMoves(pos, lastMove)) {
			int nextPos = game.getNeighbour(pos, m), dists = 0;
			for(GHOST g : GHOST.values()) {
				if(!game.isGhostEdible(g)) {
					if(g == closestGhost) {
						dists += 10 * game.getShortestPathDistance(closestGhostIndex, nextPos, closestGhostLastMove);
					}
					else {
						dists += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), nextPos, game.getGhostLastMoveMade(g));
					}
				}
			}
			if(dists > maxDists) {
				maxDists = dists;
				bestMove = m;
			}
		}
		
		return bestMove;
	}
	
	@Override
	public String getActionId() {
		return "Huir de varios fantasmas";
	}
}

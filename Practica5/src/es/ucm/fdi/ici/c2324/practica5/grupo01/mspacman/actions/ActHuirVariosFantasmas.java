package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action to move away from several ghosts
 */
public class ActHuirVariosFantasmas implements Action {
	
	private MsPacManFuzzyData data;
	
	public ActHuirVariosFantasmas(MsPacManFuzzyData data) {
		this.data = data;
	}
	
	/**
	 * Gets the best movement for MsPacMan to run away from several chasing ghosts. This is done by analyzing every
	 * possible movement and choosing the one that increments the most the distance to the ghost (prioritizing the closest 
	 * one to MsPacMan).
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to run away from several close ghosts.
	 */
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Index and last movement of the closest ghost
		int closestGhostIndex = data.getNearestChasing();
		MOVE closestGhostLastMove = data.getNearestChasingLastMove();
		
		
		int maxDists = -1;
		MOVE bestMove = null;
		
		for(MOVE m : game.getPossibleMoves(pos, lastMove)) {
			int nextPos = game.getNeighbour(pos, m), dists = 0;
//			int nextJunction = MsPacManTools.nextJunction(game, nextPos, m);
			//TODO revisar lo comentado
//			GHOST closestGhostJunction = MsPacManTools.getNearestChasing(game, nextJunction, m); // TODO sacar del data (usar nodo)
			
			//int distBtwNextJuntNextPos = game.getShortestPathDistance(nextPos, nextJunction);
			
			for(GHOST g : GHOST.values()) {	
				int node = game.getGhostCurrentNodeIndex(g);
				if(node >= 0 && !game.isGhostEdible(g)) {
					//Actual distances
					if(node == closestGhostIndex) {
						dists += 5 * game.getShortestPathDistance(closestGhostIndex, nextPos, closestGhostLastMove); 
					}
					else {
						dists += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), nextPos, game.getGhostLastMoveMade(g));
					}
//					//Future distances
//					if(g == closestGhostJunction) { //TODO usar nodo
//						dists += 10 * game.getShortestPathDistance(game.getGhostCurrentNodeIndex(closestGhostJunction), nextJunction);
//					}
//					else {
//						dists += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), nextPos);
//					}
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
		return "ActHuirVariosFantasmas";
	}
}

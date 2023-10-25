package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to separate from another ghost (maximizes the distances from "ghost" to the other chasing ghosts)
public class ActSepararseFantasma implements Action {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public ActSepararseFantasma(GHOST g) {
		ghost = g;
	}
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//If the ghost does not require an action
		if (!game.doesGhostRequireAction(ghost))
			return  MOVE.NEUTRAL;
		
		//Position of the ghost
		int pos = game.getGhostCurrentNodeIndex(ghost);
		//Last movement made by the ghost
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		
		int  maxDist = 0;
		MOVE bestMove = null;
		
		//Analyze every move and select the one that maximizes the distances from "ghost" to the other chasing ghosts 
		for(MOVE m : game.getPossibleMoves(pos, lastMove)) {
			//Position the ghost will move to if he made the movement m
			int nextPos = game.getNeighbour(pos, m);
			//Sum of the distances between ghost and the other chasing ghost after making the movement m
			int dist = 0;
			
			for(GHOST g : GHOST.values()) {
				if(g != ghost && game.getGhostLairTime(g) <= 0 && !game.isGhostEdible(g))
					dist += game.getShortestPathDistance(nextPos, game.getGhostCurrentNodeIndex(g), m);
			}
			
			if(dist > maxDist) {
				maxDist = dist;
				bestMove = m;
			}
		}
		
		return bestMove;
	}

	@Override
	public String getActionId() {
		return "Separarse de otro fantasma";
	}
}

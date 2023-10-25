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
		
		MOVE nextMove = lastMove;
		GHOST nearestGhost = MsPacManTools.getNearestChasing(game, pos, lastMove);
		//Get the index of the nearest ghost to MsPacMan
		int ghostIndex = game.getGhostCurrentNodeIndex(nearestGhost);
		//Get the distance form the nearest ghost to MsPacMan
		int distance2pcm = game.getShortestPathDistance(ghostIndex, pos, game.getGhostLastMoveMade(nearestGhost));
		
		//Get next move away from ghosts in this junction
		for(MOVE move: game.getPossibleMoves(pos, lastMove)) {
			
			int nextJunc = MsPacManTools.nextJunction(game, game.getNeighbour(pos, move), move);
			//Nearest chasing ghost to the next junction
			nearestGhost = MsPacManTools.getNearestChasing(game, nextJunc, move);
			//Position of the ghost
			ghostIndex = game.getGhostCurrentNodeIndex(nearestGhost);
			int aux = game.getShortestPathDistance(ghostIndex, nextJunc, game.getGhostLastMoveMade(nearestGhost));
			//If the distance from the ghost to the next junction is greater than the distance to MsPacMan, the first movement is saved
			if(aux > distance2pcm) {
				nextMove = move;
				distance2pcm = aux;
			}
		}
		// FOR DEBUG --------------------------------------------------------
		if(MsPacManTools.debug()) {
			System.out.println(getActionId());
		}
		// ------------------------------------------------------------------
		//Returns the movement that increments the distance to the ghosts
		return nextMove;
	}
	
	@Override
	public String getActionId() {
		return "Huir de varios fantasmas";
	}
}

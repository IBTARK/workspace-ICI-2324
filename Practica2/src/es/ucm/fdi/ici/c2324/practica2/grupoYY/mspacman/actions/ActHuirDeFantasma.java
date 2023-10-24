package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to run away from the ghosts
public class ActHuirDeFantasma implements Action {

	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		//MsPacMans current position
		int pos = game.getPacmanCurrentNodeIndex();
		//MsPacMans last move
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Nearest (and only) chasing ghost to pacman
		GHOST nearestChasing = MsPacManTools.getNearestChasing(game, pos, lastMove);
		int ghostIndex = game.getGhostCurrentNodeIndex(nearestChasing);
		MOVE ghostMove = game.getGhostLastMoveMade(nearestChasing);
		
		// Nearest edible ghost index
		int nearestEdibleGhostIndex = game.getGhostCurrentNodeIndex(MsPacManTools.getNearestEdible(game, pos, lastMove));
		
		// Nearest PPill index
		// TODO annadir que el bucle ignore el camino con PPill a no ser que sea estrictamente necesario
		int nearestPPill = MsPacManTools.closestPPill(game);
		
		/*
		 * BFS (only one junction)
		 * Analyzing the score we would get with each move (prioritizing moves with longer distance)
		 * and if that move is available to be made (we would 100% not die performing that move).
		 */
		int[] ap = game.getActivePillsIndices();
		List<Integer> activePills = new ArrayList<Integer>(ap.length);
		for (int i : ap)
		    activePills.add(i);
		
		MOVE nextMove = lastMove, onlyMove;
		int auxScore, maxScore = 0, curNode, distance;
		boolean availableMove;
		
		for(MOVE move: game.getPossibleMoves(pos, lastMove)) {
			// Readying the variables for each possible move
			onlyMove = move;
			curNode = game.getNeighbour(pos, onlyMove);
			availableMove = true;
			auxScore = 0; distance = 0;
			
			// Following the path until it reaches a junction or it becomes unavailable.
			while(!game.isJunction(curNode) && availableMove) {
				//Check if the ghost is in the index and going on a different direction as mspacman
				if((curNode == ghostIndex) && (onlyMove != ghostMove)) 
					availableMove = false;
				//Check if we can eat an edibleGhost
				if(nearestEdibleGhostIndex == curNode)
					auxScore += 15;
				//Check if the current node is a pill (add score)
				if(activePills.contains(curNode)) 
					auxScore++;
				//Increasing the distance
				distance++;
				//Moving to the next node
				onlyMove = game.getPossibleMoves(curNode, onlyMove)[0];
				curNode = game.getNeighbour(curNode, onlyMove);
			}
			// In case of having reached a junction, check if the ghost can reach that junction before us
			if(availableMove && (game.getShortestPathDistance(ghostIndex, curNode, ghostMove) <= distance)) {
				// We have to calculate the move the ghost does arriving at the junction.
				int[] ghostPath = game.getShortestPath(ghostIndex, curNode, ghostMove);
				// The last ghost move (ghostPath[length-1] to ghostPath[length-2]) or ghostMove arriving at the junction.
				MOVE ghostArrivingMove = ghostPath.length > 1 ? 
								game.getMoveToMakeToReachDirectNeighbour(ghostPath[ghostPath.length-1], ghostPath[ghostPath.length-2]) : 
								ghostMove;
				// If ghostArrivingMove is different from our onlyMove (arriving move at the junction) then the ghost can get us.
				if(ghostArrivingMove != onlyMove) {
					availableMove = false;
				}
			}
			
			// Update de maxScore and nextMove
			if(availableMove && auxScore > maxScore) {
				maxScore = auxScore;
				nextMove = move;
			}
		}
		//Choose the best available move.
		return nextMove;
	}

	@Override
	public String getActionId() {
		return "Huir de Fantasma";
	}
}

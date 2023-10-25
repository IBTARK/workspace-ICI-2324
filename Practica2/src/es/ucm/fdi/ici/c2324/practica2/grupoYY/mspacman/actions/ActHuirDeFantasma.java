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
		int nearestPPill = MsPacManTools.closestPPill(game);
		boolean caminoPPill = false, auxPPill;
		
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
		int auxScore, maxScore = -1, curNode, distance;
		boolean availableMove;
		
		for(MOVE move: game.getPossibleMoves(pos, lastMove)) {
			// Readying the variables for each possible move
			onlyMove = move;
			curNode = game.getNeighbour(pos, onlyMove);
			availableMove = true; auxPPill = false;
			auxScore = 0; distance = 0;
			
			// Following the path until it reaches a junction (or a PPill) or it becomes unavailable.
			while(!game.isJunction(curNode) && availableMove && (curNode != nearestPPill)) {
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
			// Version 2: in case we have encountered a PPill in the path, we will also execute this
			if(availableMove && (game.getShortestPathDistance(ghostIndex, curNode, ghostMove) <= distance)) {
				// We have to calculate the move the ghost does arriving at the junction.
				int[] ghostPath = game.getShortestPath(ghostIndex, curNode, ghostMove);
				// The last ghost move (ghostPath[length-1] to ghostPath[length-2]) or ghostMove arriving at the junction.
				MOVE ghostArrivingMove = ghostPath.length > 1 ? 
								game.getMoveToMakeToReachDirectNeighbour(ghostPath[ghostPath.length-2], ghostPath[ghostPath.length-1]) : 
								ghostMove;
				// If ghostArrivingMove is different from our onlyMove (arriving move at the junction) then the ghost can get us.
				if(ghostArrivingMove != onlyMove) {
					availableMove = false;
				}
			}
			// Check if the path is available and mspacman will go to the PPill.
			if(availableMove && curNode == nearestPPill)
				auxPPill = true;
			
			// Update de maxScore and nextMove
			// Conditions for the if:
			// We need to have an availableMove
			// AND we need:
			// 				EITHER a maximum score and not PPill
			//				OR we have encountered a PPill and we have not yet found another availableMove
			//				OR nextMove is a move that will take a PPill (and we have found another availableMove)	
			if(availableMove && 
					(	(auxScore > maxScore && !auxPPill)
						|| (auxPPill && maxScore==-1)
						|| (caminoPPill)
					)) {
				maxScore = auxScore;
				nextMove = move;
				// caminoPPill will store if we have chosen a move that will eat a PPill.
				caminoPPill = auxPPill;
			}
		}
		
		// Once we have finished this loop we will have the best next junction move (in case there's any)
		// If every single move previously had a score of 0 (that means there were no pills or ghosts to eat)...
		// ...  we need to chose the move that will take us closer to the pills.
		// nextMove = auxScore == 0 ? nextMoveTowardsPills : nextMove;
		// If we were to take the move towards the nearest pill, we could chose the path where we take a PPill.
		
		//Choose the best available move.
		// FOR DEBUG --------------------------------------------------------
		if(MsPacManTools.debug()) {
			System.out.println(getActionId());
		}
		// ------------------------------------------------------------------
		return nextMove;
	}

	@Override
	public String getActionId() {
		return "Huir de Fantasma";
	}
}

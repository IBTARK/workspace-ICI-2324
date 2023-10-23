package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

//Action to chase the closest ghost
public class ActPerseguirFantasma implements Action{
	
	@Override
	//Executes the action, returning the necessary movement
	public MOVE execute(Game game) {
		//MsPacMans current position
		int pos = game.getPacmanCurrentNodeIndex();
		//MsPacMans last move
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one move is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Nearest edible ghost to MsPacMan
		GHOST nearestEdible = MsPacManTools.getNearestEdible(game, pos, lastMove);
		
		//Move to chase the nearest edible ghost reducing the path distance
		return game.getNextMoveTowardsTarget(pos, game.getGhostCurrentNodeIndex(nearestEdible), lastMove, DM.PATH);
	}

	@Override
	public String getActionId() {
		return "Perseguir Fantasma";
	}
}

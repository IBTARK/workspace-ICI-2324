package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action that always makes the movement that makes MsPacMan move towards the closest edible ghost
public class ActKamikazeFantasma implements Action {

	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the nearest edible ghost
		GHOST ghost = MsPacManTools.getNearestEdible(game, pos, lastMove);
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		// FOR DEBUG --------------------------------------------------------
		if(MsPacManTools.debug()) {
			System.out.println(getActionId());
		}
		// ------------------------------------------------------------------
		//Movement that makes MsPacMan move towards the closest edible ghost
		return MsPacManTools.goTo(game, pos, ghostIndex, lastMove);
	}
	
	@Override
	public String getActionId() {
		return "Kamikaze a fantasma";
	}
	
}

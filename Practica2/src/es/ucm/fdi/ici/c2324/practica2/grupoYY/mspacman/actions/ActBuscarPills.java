package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to move towards pills
public class ActBuscarPills implements Action {

	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the closest pill to MsPacMan
		int pill = MsPacManTools.closestPill(game);
		
		// FOR DEBUG --------------------------------------------------------
		if(MsPacManTools.debug()) {
			System.out.println(getActionId());
		}
		// ------------------------------------------------------------------
		
		return MsPacManTools.goTo(game, pos, pill, lastMove);
	}

	@Override
	public String getActionId() {
		return "Buscar Pills";
	}

}

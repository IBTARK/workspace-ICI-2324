package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

//Action to chase the closest ghost
public class ActPerseguirFantasma implements Action{
	@Override
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		
		
		
		return ;
	}

	@Override
	public String getActionId() {
		return "Perseguir Fantasma";
	}
}

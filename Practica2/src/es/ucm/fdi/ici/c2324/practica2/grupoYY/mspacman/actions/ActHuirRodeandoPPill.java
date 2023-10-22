package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ActHuirRodeandoPPill implements Action {
	
	@Override
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		int ppill = MsPacManTools.closestPPill(game);
		Integer[] path = null;
		for (Integer [] p : MsPacManTools.possiblePaths(game, pos, ppill, lastMove))
			if (!MsPacManTools.blocked(game, p) && (path == null || path.length > p.length))
				path = p;
		
		return MsPacManTools.movesInPath(game, path).get(0);
	}

	@Override
	public String getActionId() {
		return "Huir rodeando hacia PPill";
	}
}

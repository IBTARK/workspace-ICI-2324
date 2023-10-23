package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.tools.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//Action to run away from the ghost circling the closest PPill
public class ActHuirRodeandoPPill implements Action {
	
	@Override
	//Execute the action, returning the necessary movement
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the closest PPill to MsPacMan
		int ppill = MsPacManTools.closestPPill(game);
		Integer[] path = null;
		
		//Get the path that circles the PPill
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

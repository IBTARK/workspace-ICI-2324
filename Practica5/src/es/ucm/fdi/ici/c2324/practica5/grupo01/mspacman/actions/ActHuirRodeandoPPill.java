package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManTools;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Action to run away from the ghost circling the closest PPill
 */
public class ActHuirRodeandoPPill implements Action {
	
	private MsPacManFuzzyData data;
	
	public ActHuirRodeandoPPill(MsPacManFuzzyData data) {
		this.data = data;
	}
	
	/**
	 * Gets the best movement for MsPacMan to get to the closest PPill to her by an indirect path (not the shortest path).
	 * Does this by analyzing every possible path (except the shortest one), and choosing the one that is not blocked.
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to run away from the ghosts and circle toward the PPill
	 */
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the closest PPill to MsPacMan
		int ppill = data.getNearestPPill();
		Integer[] path = null;
		
		//Get the path that circles the PPill
		for (Integer [] p : MsPacManTools.possiblePaths(game, pos, ppill, lastMove))
			if (!MsPacManTools.blocked(game, p) && (path == null || path.length > p.length)) //TODO comprobar que funcione bien
				path = p;
		
		return MsPacManTools.movesInPath(game, path).get(0);
	}

	@Override
	public String getActionId() {
		return "Huir rodeando hacia PPill";
	}
}

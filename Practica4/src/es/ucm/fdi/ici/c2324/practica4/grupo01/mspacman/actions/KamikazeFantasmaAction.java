package es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.actions;

import es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.MsPacManTools;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class KamikazeFantasmaAction implements RulesAction {
	
	@Override
	public String getActionId() {
		return "kamikazeFantasma";
	}

	@Override
	public void parseFact(Fact actionFact) {
		return;
	}

	/**
	 * Gets the movement that gets MsPacMan closer to the closest edible ghost to her.
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to get closer to the closest edible ghost to her.
	 */
	@Override
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the nearest edible ghost
		GHOST ghost = MsPacManTools.getNearestEdible(game, pos, lastMove);
		int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
		
		//Movement that makes MsPacMan move towards the closest edible ghost
		return MsPacManTools.goTo(game, pos, ghostIndex, lastMove);
	}
}
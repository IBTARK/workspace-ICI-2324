package es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.actions;

import es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.MsPacManTools;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class KamikazePillAction implements RulesAction {
	
	@Override
	public String getActionId() {
		return "KamikazeAPill";
	}

	@Override
	public void parseFact(Fact actionFact) {
		return;
	}

	/**
	 * Gets the movement that gets MsPacMan closer to the closest pill to her.
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to get closer to the closest pill to her.
	 */
	@Override
	public MOVE execute(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the closest pill to MsPacMan
		int pill = MsPacManTools.closestPill(game);
		
		//Returns the movement that makes MsPacMan move toward the pill
		return MsPacManTools.goTo(game, pos, pill, lastMove);
	}
}

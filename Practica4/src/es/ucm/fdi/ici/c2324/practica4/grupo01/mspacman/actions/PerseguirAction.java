package es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.actions;

import es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.MsPacManTools;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PerseguirAction implements RulesAction {

    enum STRATEGY { PERSEGUIR, FLANQUEAR };
    STRATEGY chaseStrategy; 

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("strategy");
			if(value == null)
				return;
			String strategyValue = value.stringValue(null);
			chaseStrategy = STRATEGY.valueOf(strategyValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getActionId() {
		return "Perseguir";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = null;
		switch (chaseStrategy) {
		case PERSEGUIR:
			nextMove = actPerseguirFantasma(game);
			break;
		case FLANQUEAR:
			nextMove = actFlanquearFantasma(game);
			break;
		}
		return nextMove;
	}
	
	/**
	 * Gets the movement that gets MsPacMan closer to the closest pill to her.
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to get closer to the closest pill to her.
	 */
	private MOVE actPerseguirFantasma(Game game) {
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
		return MsPacManTools.goTo(game, pos, game.getGhostCurrentNodeIndex(nearestEdible), lastMove);
	}
	
	/**
	 * Gets the best movement that MsPacMan has to execute to move towards the junction that the closest edible ghost
	 * to her is moving to. This is done by making the first move of the shortest path from MsPacMan to her closest edible ghost.
	 * 
	 * @param game 
	 * @return the best movement to move towards the next junction of the closest edible ghost to MsPacMan
	 */
	private MOVE actFlanquearFantasma(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the nearest edible ghost to MsPacMan
		GHOST ghost = MsPacManTools.getNearestEdible(game, pos, lastMove);
		//Get the junction that the nearest edible ghost is moving towards 
		int junction = MsPacManTools.nextJunction(game, game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost));
		
		return MsPacManTools.goTo(game, pos, junction, lastMove);
	}
}

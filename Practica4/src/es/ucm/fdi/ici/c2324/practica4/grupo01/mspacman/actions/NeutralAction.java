package es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.actions;

import es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.MsPacManTools;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class NeutralAction implements RulesAction {
	
	enum STRATEGY { BUSCAR_PILLS, EVITAR_PPILL };
	STRATEGY neutralStrategy;

	@Override
	public String getActionId() {
		return "Neutral";
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("neutralStrategy");
			if(value == null) return;
			String strategyValue = value.stringValue(null);
			neutralStrategy = STRATEGY.valueOf(strategyValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = null;
		switch (neutralStrategy) {
		case BUSCAR_PILLS:
			nextMove = actBuscarPills(game);
			break;
		case EVITAR_PPILL:
			nextMove = actEvitarPPill(game);
			break;
		}
		return nextMove;
	}

	/**
	 * Returns the necessary movement to move towards the pills
	 *
	 * Gets the best movement to move towards the closest pill to MsPacMan. Does it by analyzing every possible movement and 
	 * choosing the one that minimizes the distance from MsPacMan to her closest pill
	 * 
	 * @param game 
	 * @return the best movement to move towards the closest pill
	 */
	private MOVE actBuscarPills(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Get the closest pill to MsPacMan
		int pill = MsPacManTools.closestPill(game);
		
		return MsPacManTools.goTo(game, pos, pill, lastMove);
	}
	
	/**
	 * Gets the best movement to avoid the closest PPill to MsPacMan. Does it by analyzing every possible movement and 
	 * choosing the one that maximizes the distance from MsPacMan to her closest PPill
	 * 
	 * @param game 
	 * @return the best movement to avoid the closest PPill
	 */
	private MOVE actEvitarPPill(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		MOVE nextMove = null;
		int closestPill = MsPacManTools.closestPill(game);
		int distance = Integer.MAX_VALUE;
		//Choose the move that makes MsPacMan stay as far as possible from his closest PPill
		for(MOVE move: game.getPossibleMoves(pos, lastMove)) {
			int nextJunc = MsPacManTools.nextJunction(game, game.getNeighbour(pos, move), move);
			//Path from the current position of MsPacMan to MsPacMans next junction
			int[] p = game.getShortestPath(pos, nextJunc, move);
			//Check if there isn't a PPill in the path p
			if(!MsPacManTools.blockedByClosestPPill(game, p)) {
				//Choose the movement that minimizes the distance to the Pill
				if(nextMove == null || game.getShortestPathDistance(pos, closestPill, move) < distance) {
					nextMove = move;
					distance = game.getShortestPathDistance(pos, closestPill, move);
				}
			}
		}
		return nextMove;
	}
}

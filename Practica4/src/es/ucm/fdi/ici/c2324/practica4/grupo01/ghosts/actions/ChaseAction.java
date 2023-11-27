package es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions;

import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.GhostsTools;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class ChaseAction implements RulesAction {
	
	enum STRATEGY {DIRECT, FLANK, PROTECT_EDIBLE};
	STRATEGY chaseStrategy;
    GHOST ghost;
	public ChaseAction( GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = null;
		switch (chaseStrategy) {
		case DIRECT:
			nextMove = actPerseguirMsPacman(game);
			break;
		case FLANK:
			nextMove = actFlanquearMsPacman(game);
			break;
		case PROTECT_EDIBLE:
			nextMove = actProtectEdible(game);
			break;
		}
		return nextMove;
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("chasestrategy");
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
		return ghost + "chases";
	}

	/**
	 * Movement to get approximate to mspacman
	 * @param game
	 * @return move
	 */
	private MOVE actPerseguirMsPacman (Game game) {
		int ghostPos = game.getGhostCurrentNodeIndex(ghost);
		int pacmanPos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		return game.getApproximateNextMoveTowardsTarget(ghostPos, pacmanPos, lastMove, DM.PATH);
	}
	
	/**
	 * Next move towards to mspacmans nextJunction
	 * @param game
	 * @return
	 */
	private MOVE actFlanquearMsPacman (Game game) {
		int ghostPos = game.getGhostCurrentNodeIndex(ghost);
		int pacmanPos = game.getPacmanCurrentNodeIndex();
		int pacmanNextJunction = GhostsTools.nextJunction(game, pacmanPos, game.getPacmanLastMoveMade());
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		return game.getApproximateNextMoveTowardsTarget(ghostPos, pacmanNextJunction, lastMove, DM.PATH);
	}
	
	/**
	 * Get next move towards to the nearest edible ghost
	 * (NO TIENE EN CUENTA EL CAMINO HACIA MSPACMAN)
	 * @param game
	 * @return
	 */
	private MOVE actProtectEdible (Game game) {
		int ghostPos = game.getGhostCurrentNodeIndex(ghost);
		GHOST nearestEdible = GhostsTools.getNearestEdible(game,ghost);
		int nearestEdibleIndex = game.getGhostCurrentNodeIndex(nearestEdible);
		//int pacmanPos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		
		return game.getApproximateNextMoveTowardsTarget(ghostPos, nearestEdibleIndex, lastMove, DM.PATH);
	}
	

}

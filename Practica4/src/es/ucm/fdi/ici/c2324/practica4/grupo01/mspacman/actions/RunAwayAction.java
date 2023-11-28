package es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.actions;

import es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.MsPacManTools;
import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayAction implements RulesAction{
	enum STRATEGY { HUIR_DE_FANTASMA, HUIR_HACIA_PPILL, HUIR_RODEANDO_PPILL, HUIR_VARIOS_FANTASMAS };
	STRATEGY runAwayStrategy;
	
	@Override
	public String getActionId() {
		return "Huir";
	}
	
	@Override
	public void parseFact(Fact actionFact) {
		try {
			Value value = actionFact.getSlotValue("runAwayStrategy");
			if(value == null) return;
			String strategyValue = value.stringValue(null);
			runAwayStrategy = STRATEGY.valueOf(strategyValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = null;
		switch (runAwayStrategy) {
		case HUIR_DE_FANTASMA:
			nextMove = actHuirDeFantasma(game);
			break;
		case HUIR_HACIA_PPILL:
			nextMove = actHuirHaciaPPill(game);
			break;
		case HUIR_RODEANDO_PPILL:
			nextMove = actHuirRodeandoPPill(game);
			break;
		case HUIR_VARIOS_FANTASMAS:
			nextMove = actHuirVariosFantasmas(game);
			break;
		}
		return nextMove;
	}

	private MOVE actHuirDeFantasma(Game game) {
		
	}
	
	/**
	 * Gets the best movement for MsPacMan to run away from the chasing ghosts towards the nearest PPill. 
	 * Does it by choosing the movement that makes her get closer to the PPill.
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to run away from the ghosts and moving toward the PPill
	 */
	private MOVE actHuirHaciaPPill(Game game) {
		//MsPacMans current position
		int pos = game.getPacmanCurrentNodeIndex();
		//MsPacMans last move
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one move is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Nearest Power Pill to MsPacMan
		int nearestPPill = MsPacManTools.closestPPill(game);
		
		//Move to reduce the distance to the nearest Power Pill
		
		return MsPacManTools.goTo(game, pos, nearestPPill, lastMove);
	}
	
	/**
	 * Gets the best movement for MsPacMan to get to the closest PPill to her by an indirect path (not the shortest path).
	 * Does this by analyzing every possible path (except the shortest one), and choosing the one that is not blocked.
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to run away from the ghosts and circle toward the PPill
	 */
	private MOVE actHuirRodeandoPPill(Game game) {
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
	
	/**
	 * Gets the best movement for MsPacMan to run away from several chasing ghosts. This is done by analyzing every
	 * possible movement and choosing the one that increments the most the distance to the ghost (prioritizing the closest 
	 * one to MsPacMan).
	 * 
	 * @param game 
	 * @return the best movement for MsPacMan to run away from several close ghosts.
	 */
	private MOVE actHuirVariosFantasmas(Game game) {
		int pos = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getPacmanLastMoveMade();
		
		//If MsPacMan is not in a junction only one movement is possible
		if (!game.isJunction(pos))
			return game.getPossibleMoves(pos, lastMove)[0];
		
		//Closest ghost to MsPacMan
		GHOST closestGhost =  MsPacManTools.getNearestChasing(game, pos, lastMove);
		//Index and last movement of the closest ghost
		int closestGhostIndex = game.getGhostCurrentNodeIndex(closestGhost);
		MOVE closestGhostLastMove = game.getGhostLastMoveMade(closestGhost);
		
		
		int maxDists = -1;
		MOVE bestMove = null;
		
		for(MOVE m : game.getPossibleMoves(pos, lastMove)) {
			int nextPos = game.getNeighbour(pos, m), dists = 0;
			int nextJunction = MsPacManTools.nextJunction(game, nextPos, m);
			
			GHOST closestGhostJunction = MsPacManTools.getNearestChasing(game, nextJunction, m);
			
			//int distBtwNextJuntNextPos = game.getShortestPathDistance(nextPos, nextJunction);
			
			for(GHOST g : GHOST.values()) {
				if(!game.isGhostEdible(g) && MsPacManTools.isGhostClose(game, g)) {
					//Actual distances
					if(g == closestGhost) {
						dists += 10 * game.getShortestPathDistance(closestGhostIndex, nextPos, closestGhostLastMove); 
					}
					else {
						dists += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), nextPos, game.getGhostLastMoveMade(g));
					}
					//Future distances
					if(g == closestGhostJunction) {
						dists += 10 * game.getShortestPathDistance(game.getGhostCurrentNodeIndex(closestGhostJunction), nextJunction);
					}
					else {
						dists += game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), nextPos);
					}
				}
			}
			if(dists > maxDists) {
				maxDists = dists;
				bestMove = m;
			}
		}
		
		return bestMove;
	}
}

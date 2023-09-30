package es.ucm.fdi.ici.c2324.practica1.grupoYY;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.GhostController;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	
	private Game game;
	private Random rand;
	private static final int SECURITY_DIST = 30;
    private static final MOVE[] allMoves = MOVE.values();

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		this.game = game;
		this.rand = new Random();
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		for (GHOST g : GHOST.values()) 
			if (game.doesGhostRequireAction(g))
				moves.put(g, getGhostMove(g));
		return moves;
	}

	private MOVE getGhostMove(GHOST g) {
		int pacman = game.getPacmanCurrentNodeIndex();
		if (game.isGhostEdible(g) || isCloseToPPill(pacman))
			return nextMoveAwayFrom(pacman, g);
		
		if (rand.nextFloat() < 0.9)
			return nextMoveTowards(pacman, g);
		return nextRandomMove();
	}
	
	private boolean isCloseToPPill(int pacman) {
		for (int pp : game.getActivePowerPillsIndices())
			if (SECURITY_DIST >= game.getDistance(pacman, pp, Constants.DM.PATH))
				return true;
		return false;
	}
	
	private MOVE nextMoveAwayFrom(int pacman, GHOST g) {
		return game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(g),
														 pacman, 
														 game.getGhostLastMoveMade(g),
														 Constants.DM.PATH);
	}
	
	private MOVE nextMoveTowards(int pacman, GHOST g) {
		return game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(g),
														pacman, 
														game.getGhostLastMoveMade(g),
														Constants.DM.PATH);
	}
	
	private MOVE nextRandomMove() {
		return allMoves[rand.nextInt(allMoves.length)];
	}
}

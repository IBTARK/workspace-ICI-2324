package es.ucm.fdi.ici.c2324.practica2.grupoYY.tools;

import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsTools {

	public static GHOST getNearestChasing(Game game, GHOST ghost) {
		GHOST nearest = null;
		int minDist = Integer.MAX_VALUE;
		for (GHOST g : GHOST.values()) {
			int dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), 
													game.getGhostCurrentNodeIndex(g), 
													game.getGhostLastMoveMade(ghost));
			if (!g.equals(ghost) && minDist > dist) {
				minDist = dist;
				nearest = g;
			}
		}
		return nearest;
	}
}

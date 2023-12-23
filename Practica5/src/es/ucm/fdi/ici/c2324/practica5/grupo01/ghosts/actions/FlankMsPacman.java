package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions;

import java.util.HashMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsTools;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FlankMsPacman implements Action {

	private GHOST ghost;
	private GhostFuzzyData data;
	
	public FlankMsPacman(GHOST g, GhostFuzzyData data) {
		this.ghost = g;
		this.data = data;
	}
	
	@Override
	public String getActionId() {
		return "FlankMsPacman";
	}

	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		int mspacman = data.getMspacman();
		MOVE msLastMove = data.getMsLastMove();
		if(mspacman == -1) return MOVE.NEUTRAL;
		
		if(game.doesGhostRequireAction(ghost)) {
			int g = game.getGhostCurrentNodeIndex(ghost);
			MOVE gLastMove = game.getGhostLastMoveMade(ghost);
			HashMap<Integer, Integer[]> level3JunctionsMap = new HashMap<Integer, Integer[]>();
			Integer level2Junctions[] = GhostsTools.nextJunctions(game, mspacman, msLastMove, level3JunctionsMap);
			
			int nearestJunction=-1, curLvl2Junction, nearestJunctionDistance = Integer.MAX_VALUE, curJunctionDistance;
			int curJunctionPath[];
			int nextJunction = level2Junctions[0];
			for(int i = 1; i < level2Junctions.length; i++) {
				curLvl2Junction = level2Junctions[i];
				curJunctionPath = game.getShortestPath(g, curLvl2Junction, gLastMove);
				curJunctionDistance = curJunctionPath.length;
				if(nearestJunctionDistance > curJunctionDistance) {
					if(curJunctionDistance == 0) 
						nearestJunction = nextJunction;
					else 
						nearestJunction = curLvl2Junction;
					nearestJunctionDistance = curJunctionDistance;
				}
				// Integer level3Junctions[] = level3JunctionsMap.get(curLvl2Junction);
				// Hacer un for
			}
			
			if(nearestJunction!=-1) {
				nextMove = game.getApproximateNextMoveTowardsTarget(g, nearestJunction, gLastMove, DM.PATH);
			}
		}
		
		
		return nextMove;
	}

}

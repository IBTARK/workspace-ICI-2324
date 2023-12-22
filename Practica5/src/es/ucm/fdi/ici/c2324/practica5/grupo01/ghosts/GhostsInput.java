package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostsInput extends FuzzyInput {
	
	private double msToPPill;
	private double[] msDistance;
	private double[] msFirstJunctionDistance;
	private double[] nearestChasingDistance;
	private double[] nearestEdibleDistance;
	private MOVE msLastMove;
	

	public GhostsInput(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void parseInput() {
		int mspacman;
		msDistance = new double[] {-1,-1,-1,-1};
		mspacman = game.getPacmanCurrentNodeIndex();
		if(mspacman != -1) {
			msLastMove = game.getPacmanLastMoveMade();
			for(GHOST g: GHOST.values()) {
				int index = g.ordinal();
				int pos = game.getGhostCurrentNodeIndex(g);
				if(game.getGhostLairTime(g) <= 0) {
					
					msDistance[index] = game.getShortestPathDistance(pos, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
					
					int msNextJunction = GhostsTools.nextJunction(game, mspacman, msLastMove);
					msFirstJunctionDistance[index] = game.getShortestPathDistance(pos, msNextJunction, game.getGhostLastMoveMade(g));
					
					int nearestChasing = game.getGhostCurrentNodeIndex(GhostsTools.getNearestChasing(game, g));
					nearestChasingDistance[index] = game.getShortestPathDistance(pos, nearestChasing, game.getGhostLastMoveMade(g));
					
					int nearestEdible = game.getGhostCurrentNodeIndex(GhostsTools.getNearestEdible(game, g));
					nearestEdibleDistance[index] = game.getShortestPathDistance(pos, nearestEdible, game.getGhostLastMoveMade(g));
				}
				else {
					msDistance[index] = Double.MAX_VALUE;
					msFirstJunctionDistance[index] = Double.MAX_VALUE;
					nearestChasingDistance[index] = Double.MAX_VALUE;
					nearestEdibleDistance[index] = Double.MAX_VALUE;
				}
				
			}
			int dist = Integer.MAX_VALUE;
			for (int ppill : game.getActivePowerPillsIndices()) {
				int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), ppill, game.getPacmanLastMoveMade());
				if (aux < dist) {
					msToPPill = aux;
				}
			}
			
		}

	}

	public boolean isMsPacManVisible() {
		return msDistance[0]!=-1;
	}
	
	@Override
	public HashMap<String, Double> getFuzzyValues() {
		HashMap<String,Double> vars = new HashMap<String,Double>();
		vars.put("msToPPill",   msToPPill);
		for(GHOST g: GHOST.values()) {
			vars.put(g.name()+"msDistance",   msDistance[g.ordinal()]);
			vars.put(g.name()+"msFirstJunctionDistance",   msFirstJunctionDistance[g.ordinal()]);
			vars.put(g.name()+"nearestChasingDistance",   nearestChasingDistance[g.ordinal()]);
			vars.put(g.name()+"nearestEdibleDistance",   nearestEdibleDistance[g.ordinal()]);
		}
		return vars;
	}

}

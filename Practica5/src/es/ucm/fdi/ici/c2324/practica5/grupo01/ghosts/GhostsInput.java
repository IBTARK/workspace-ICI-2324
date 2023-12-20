package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

public class GhostsInput extends FuzzyInput {
	
	private double msToPPill;
	private double[] msDistance;
	private double[] msFirstJunctionDistance;
	private double[] nearestChasingDistance;
	private double[] nearestEdibleDistance;
	

	public GhostsInput(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void parseInput() {
		distance = new double[] {-1,-1,-1,-1};

		
		for(GHOST g: GHOST.values()) {
			int index = g.ordinal();
			int pos = game.getGhostCurrentNodeIndex(g);
			if(pos != -1) {
				distance[index] = game.getDistance(pos, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g), DM.PATH);
			}
			else
				distance[index] = -1;
		}

	}

	public boolean isMsPacManVisible() {
		return distance[0]!=-1;
	}
	
	@Override
	public HashMap<String, Double> getFuzzyValues() {
		HashMap<String,Double> vars = new HashMap<String,Double>();
		for(GHOST g: GHOST.values()) {
			vars.put(g.name()+"distance",   distance[g.ordinal()]);
		}
		return vars;
	}

}

package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts;

import java.util.HashMap;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends FuzzyInput {
	
	private static final double MAX_DISTANCE = 250.0;
	
	private boolean msVisible;
	private int currentLevel;
	private boolean ppillEaten;
	
	private double msToPPill;
	private double[] alives;
	private double[] edibles;
	private double[] msDistance;
	private double[] msFirstJunctionDistance;
	private double[] nearestChasingDistance;
	private double[] nearestEdibleDistance;
	
	public GhostsInput(Game game) {
		super(game);
	}


	@Override
	public void parseInput() {
		
		msToPPill = MAX_DISTANCE;
		alives = new double[] {0,0,0,0};
		edibles = new double[] {0,0,0,0};
		msDistance = new double[] {MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE};
		msFirstJunctionDistance = new double[] {MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE};
		nearestChasingDistance = new double[] {MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE};
		nearestEdibleDistance = new double[] {MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE};
		
		// Se tiene que ver si está permitido, si no, chequeamos que algun fantasma 
		ppillEaten = game.wasPowerPillEaten();		
		
		currentLevel = game.getCurrentLevel();
		
		int mspacman = game.getPacmanCurrentNodeIndex();	
		
		msVisible = false;
		MOVE msLastMove = MOVE.NEUTRAL;
		if(mspacman != -1) {
			msVisible = true;
			msLastMove = msVisible ? game.getPacmanLastMoveMade() : MOVE.NEUTRAL;
		}
		
		int index, pos;
		for(GHOST g: GHOST.values()) {
			index = g.ordinal();
			pos = game.getGhostCurrentNodeIndex(g);
			if(game.getGhostLairTime(g) <= 0) {
				alives[index] = 1;
				
				if(mspacman != -1) {
					msDistance[index] = game.getShortestPathDistance(pos, game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(g));
					
					int msNextJunction = GhostsTools.nextJunction(game, mspacman, msLastMove);
					msFirstJunctionDistance[index] = game.getShortestPathDistance(pos, msNextJunction, game.getGhostLastMoveMade(g));
				}
				
				edibles[index] = game.isGhostEdible(g) ? 1 : 0;
				
				GHOST nearestChasing = GhostsTools.getNearestChasing(game, g);
				if(nearestChasing != null) {
					int nearestChasingIdx = game.getGhostCurrentNodeIndex(nearestChasing);
					nearestChasingDistance[index] = game.getShortestPathDistance(pos, nearestChasingIdx, game.getGhostLastMoveMade(g));
				}
				
				GHOST nearestEdible = GhostsTools.getNearestEdible(game, g);
				if(nearestEdible != null) {
					int nearestEdibleIdx = game.getGhostCurrentNodeIndex(nearestEdible);
					nearestEdibleDistance[index] = game.getShortestPathDistance(pos, nearestEdibleIdx, game.getGhostLastMoveMade(g));
				}
			}
			
		}
		
		// Calculamos la supuesta distancia
		if(mspacman!=-1) {
			int minDist = Integer.MAX_VALUE;
			for (int ppill : game.getPowerPillIndices()) {
				int aux = game.getShortestPathDistance(mspacman, ppill, game.getPacmanLastMoveMade());
				if (aux < minDist) {
					minDist = aux;
					msToPPill = aux;
				}
			}
		}
		

	}

	public boolean isMsPacManVisible() {
		return msVisible;
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}
	
	public boolean wasPPillEaten() {
		return ppillEaten;
	}
	
	@Override
	public HashMap<String, Double> getFuzzyValues() {
		HashMap<String,Double> vars = new HashMap<String,Double>();
		vars.put("MSPACMANtoPPILL", msToPPill);
		String ghostName;
		for(GHOST g: GHOST.values()) {
			ghostName = g.name().toUpperCase();
			vars.put(ghostName+"edible", edibles[g.ordinal()]);
			vars.put(ghostName+"toMS",   msDistance[g.ordinal()]);
			vars.put(ghostName+"toFIRSTJUNCTION",   msFirstJunctionDistance[g.ordinal()]);
			vars.put(ghostName+"toCHASING",   nearestChasingDistance[g.ordinal()]);
			vars.put(ghostName+"toEDIBLE",   nearestEdibleDistance[g.ordinal()]);
		}
		return vars;
	}

}

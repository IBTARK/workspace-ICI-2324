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
	private boolean mspacmanEaten;
	
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
		
		// Parseamos los valores que utilizaremos despues en el fuzzyMemory
		ppillEaten = game.wasPowerPillEaten();		
		mspacmanEaten = game.wasPacManEaten();
		currentLevel = game.getCurrentLevel();
		
		int mspacman = game.getPacmanCurrentNodeIndex();	
		
		msVisible = false;
		MOVE msLastMove = MOVE.NEUTRAL;
		if(mspacman != -1) {
			msVisible = true;
			msLastMove = msVisible ? game.getPacmanLastMoveMade() : MOVE.NEUTRAL;
		}
		
		int index, pos;
		// Añadimos para cada fantasma, sus distintos valores a parsear para el input.
		for(GHOST g: GHOST.values()) {
			index = g.ordinal();
			pos = game.getGhostCurrentNodeIndex(g);
			MOVE lastMove = game.getGhostLastMoveMade(g);
			if(game.getGhostLairTime(g) <= 0) {
				alives[index] = 1;
				
				if(mspacman != -1) {
					msDistance[index] = game.getShortestPathDistance(pos, mspacman, lastMove);
					
					int msNextJunction = GhostsTools.nextJunction(game, mspacman, msLastMove);
					int[] shortestGhostPath = game.getShortestPath(pos, msNextJunction, msLastMove);
					
					// Lo siguiente sirve para detectar si perseguiríamos por detras a mspacman
					// En caso de que la distancia al first junction sea corto detectamos si el camino iria por detras de mspacman
					boolean caminoCorrecto = true;
					if(shortestGhostPath.length<20) {
						int i = 0;
						while(i<shortestGhostPath.length && caminoCorrecto) {
							if(shortestGhostPath[i] == mspacman)
								caminoCorrecto = false;
							
							++i;
						}
					}
					
					if(caminoCorrecto)
						msFirstJunctionDistance[index] = shortestGhostPath.length;
				}
				
				edibles[index] = game.isGhostEdible(g) ? 1 : 0;
				
				GHOST nearestChasing = GhostsTools.getNearestChasing(game, g);
				if(nearestChasing != null) {
					int nearestChasingIdx = game.getGhostCurrentNodeIndex(nearestChasing);
					nearestChasingDistance[index] = game.getShortestPathDistance(pos, nearestChasingIdx, lastMove);
				}
				
				GHOST nearestEdible = GhostsTools.getNearestEdible(game, g);
				if(nearestEdible != null) {
					int nearestEdibleIdx = game.getGhostCurrentNodeIndex(nearestEdible);
					nearestEdibleDistance[index] = game.getShortestPathDistance(pos, nearestEdibleIdx, lastMove);
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
	
	public boolean wasMspacmanEaten() {
		return mspacmanEaten;
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

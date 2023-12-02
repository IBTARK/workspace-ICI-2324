package es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends RulesInput {

	
	private static int MAX_DISTANCE_EDIBLE = 20;
	private static int MAX_DISTANCE_CHASING = 40;
	
	private int msNextJunction;				// El indice del siguiente junction del pacman (puede coincidir con el indice de pacman
	private int level2Junctions[];			// Los siguientes junctions despues del siguiente junction de mspacman (nivel 2)
	private Map<Integer, Integer[]> level3Junctions;
	
	// Datos a asertar sobre CADA fantasma:
	private Map<GHOST, Boolean> alive;						// Si estamos vivos o no
	private Map<GHOST, Boolean> edibles;					// Si somos comestibles o no
	private Map<GHOST, GHOST> nearestChasing;				// El fantasma chasing mas cercano (dentro del rango de MAX_DISTANCE_CHASING)
	private Map<GHOST, GHOST> nearestEdible;				// El fantasma edible mas cercano (dentro del rango de MAX_DISTANCE_EDIBLE)
	private Map<GHOST, Integer> distanceToMspacman;			// La distancia a mspacman
	private Map<GHOST, Integer> distanceToMsNextJunction; 	// La distancia al siguiente junction de mspacman
	private Map<GHOST, Integer[]> distanceToLevel2Junctions;// La distancia a los junctions de nivel 2 de mspacman
	private Map<GHOST, Map<Integer, Integer[]>> distanceToLevel3Junctions;// La distancia a los junctions de nivel 3 de mspacman
	
	// Datos a asertar sobre el pacman:
	private int minPacmanDistancePPill;	// Su distancia al powerpill
	private int msNextJunctionDistance;		// La distancia de pacman a su siguiente junction (puede ser 0 indicando que está ya ahi)
	private int msToLvl2Junctions[];		// La distancia de mspacman a cada uno de los junctions
	private Map<Integer, Integer[]> msToLvl3Junctions;
	
	public GhostsInput(Game game) {
		super(game);
	}

	@Override
	public void parseInput() {
		clearAndInitializeMaps();
		
		computeAliveAndEdibles();
		
		computeNearestChasing();
		
		computeNearestEdible();
		
		computeDistanceToMspacman();	
		
		computeMinPacmanDistancePPill();
		
		computeMsNextJunctionIndexesAndDistances();
		
		computeGhostsDistancesToNextJunctions();
	}
	
	private void clearAndInitializeMaps() {
		alive = new HashMap<>();
		edibles = new HashMap<>();
		nearestChasing = new HashMap<>();
		nearestEdible = new HashMap<>();
		distanceToMspacman = new HashMap<>();
		distanceToMsNextJunction = new HashMap<>();
		distanceToLevel2Junctions = new HashMap<>();
		distanceToLevel3Junctions = new HashMap<>();
		level3Junctions = new HashMap<>();
		msToLvl3Junctions = new HashMap<>();
	}


	private void computeGhostsDistancesToNextJunctions() {
		for(GHOST g: GHOST.values()) {
			if(game.getGhostLairTime(g) <= 0) {
				int ghost = game.getGhostCurrentNodeIndex(g);
				MOVE move = game.getGhostLastMoveMade(g);
				Integer distancesToLvl2[] = new Integer[level2Junctions.length];
				
				distanceToMsNextJunction.put(g, game.getShortestPathDistance(ghost, msNextJunction, move));
				for(int i = 0; i < distancesToLvl2.length; i++) {
					distancesToLvl2[i] = game.getShortestPathDistance(ghost, level2Junctions[i], move);
				}
				distanceToLevel2Junctions.put(g, distancesToLvl2);
				
				// level 3 junctions
				Map<Integer, Integer[]> ghostToLvl3 = new HashMap<>();
				Integer[] distancesToLvl3;
				for(int lvl2: level2Junctions) {
					Integer[] msLvl3 = level3Junctions.get(lvl2);
					// Rellenamos los junctions de nivel 3 por cada uno de nivel 2.
					distancesToLvl3 = new Integer[msLvl3.length];
					for(int i = 0; i < distancesToLvl3.length; i++) {
						distancesToLvl3[i] = game.getShortestPathDistance(ghost, msLvl3[i], move);
					}
					ghostToLvl3.put(lvl2, distancesToLvl3);
					distanceToLevel3Junctions.put(g, ghostToLvl3);
				}
			}
		}
	}

	private void computeMsNextJunctionIndexesAndDistances() {
		int mspacman = game.getPacmanCurrentNodeIndex();
		MOVE msLastMove = game.getPacmanLastMoveMade();
		
		// Calculamos los indices de los siguientes junctions de mspacman
		Integer nextAndLvl2Junctions[] = GhostsTools.nextJunctions(game, mspacman, msLastMove, level3Junctions);
		msNextJunction = nextAndLvl2Junctions[0];
		level2Junctions = new int[nextAndLvl2Junctions.length-1];
		for(int i = 1; i < nextAndLvl2Junctions.length; i++)  {
			level2Junctions[i-1] = nextAndLvl2Junctions[i];
		}
		
		// Una vez tenemos los indices, calculamos las distancias a esos indices.
		// JUNCTIONS DE NIVEL 2 y 3
		msToLvl2Junctions = new int[level2Junctions.length];
		msNextJunctionDistance = game.getShortestPathDistance(mspacman, msNextJunction, msLastMove);
		Integer[] distancesToLvl3;
		for(int i = 0; i < level2Junctions.length; i++) {
			int lvl2Junction = level2Junctions[i];
			msToLvl2Junctions[i] = game.getShortestPathDistance(mspacman, lvl2Junction, msLastMove);
			
			Integer[] msLvl3 = level3Junctions.get(lvl2Junction);
			// Rellenamos los junctions de nivel 3 por cada uno de nivel 2.
			distancesToLvl3 = new Integer[msLvl3.length];
			for(int j = 0; j < msLvl3.length; j++) {
				distancesToLvl3[j] = game.getShortestPathDistance(mspacman, msLvl3[j], msLastMove);
			}
			msToLvl3Junctions.put(level2Junctions[i], distancesToLvl3);
		}
		
	}

	private void computeDistanceToMspacman() {
		int mspacman = game.getPacmanCurrentNodeIndex();
		MOVE msLastMove = game.getPacmanLastMoveMade();
		for(GHOST g : GHOST.values()) {
			int gIndex = game.getGhostCurrentNodeIndex(g);
			// MOVE gLastMove = game.getGhostLastMoveMade(g);
			if(game.getGhostLairTime(g) <= 0) {
				// Primera opcion
				distanceToMspacman.put(g, game.getShortestPathDistance(
						mspacman,
						gIndex,
						msLastMove));
				// Segunda opcion
				// Calular la distancia nuestra hacia ms pacman pero teniendo en cuenta si vamos detras suyo.
				// distanceToMspacman.put(g, GhostsTools.distanceToMspacmanFromFront(game, gIndex, gLastMove, mspacman, msLastMove));
			}	
		}
	}

	private void computeNearestEdible() {
		for(GHOST g : GHOST.values()) {
			
			if(game.getGhostLairTime(g) <= 0)
				nearestEdible.put(g, GhostsTools.getNearestEdible(game, g, MAX_DISTANCE_EDIBLE));
		}
	}

	private void computeNearestChasing() {
		for(GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g) <= 0)
				nearestChasing.put(g, GhostsTools.getNearestChasingNotBlocked(game, g, MAX_DISTANCE_CHASING));
		}
	}

	private void computeAliveAndEdibles() {
		for(GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g) <= 0) {
				alive.put(g, true);
				edibles.put(g, game.isGhostEdible(g));
			}
			else {
				alive.put(g, false);
				// La siguiente linea no hace falta, el <map>.get(<key>) nos devolverï¿½ null.
				//edibles.put(g, null);
			}
			edibles.put(g, game.isGhostEdible(g));
		}
		
	}

	private void computeMinPacmanDistancePPill() {
		int pacman = game.getPacmanCurrentNodeIndex();
		this.minPacmanDistancePPill = Integer.MAX_VALUE;
		for(int ppill: game.getActivePowerPillsIndices()) {
			int distance = game.getShortestPathDistance(pacman, ppill, game.getPacmanLastMoveMade());
			this.minPacmanDistancePPill = Math.min(distance, this.minPacmanDistancePPill);
		}
	}

	/**
	 * Returns a list of facts in CLIPS syntax to be asserted every game tick.
	 * These facts must be defined by the corresponding deftemplate rules in the clp file loaded into the RuleEngine
	 * @see es.ucm.fdi.ici.rules.RuleEngine
	 */
	@Override
	public Collection<String> getFacts() {
		Vector<String> facts = new Vector<String>();
		facts.add("(CURRENTGHOST (tipo nil))");
		facts.add("(NEARESTGHOST (tipo nil))");
		
		// Hacer un for para aï¿½adir estilo:
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		StringBuilder str;
		for(GHOST g: GHOST.values()) {
			str = new StringBuilder();
			
			str.append("(GHOST ");
			str.append(String.format("(tipo %s)", g.toString()));
			str.append(String.format("(alive %s)", alive.get(g)));
			// eliminar el siguiente if si es neccesario aï¿½adir los slots para las condiciones de las reglas
			if(alive.get(g)) {
				str.append(String.format("(edible %s)", edibles.get(g)));
				str.append(String.format("(nearestChasing %s)", nearestChasing.get(g)));
				str.append(String.format("(nearestEdible %s)", nearestEdible.get(g)));
				
				// Distancia a mspacman
				str.append(String.format("(mspacman %d))", distanceToMspacman.get(g)));
				
				// VAMOS A ASERTAR LOS INDICES DE CADA GHOST
				// El unico junction de nivel 1 que tiene pacman (puede ser igual que su posicion en el caso de que esté justo en el junction)
				facts.add(String.format("(INDEX (owner %s) (lvl 1) (index %d) (previousIndex %d) (distance %d))", g.toString(), msNextJunction, mspacman, distanceToMsNextJunction.get(g)));
				// Junctions de nivel 2
				Integer distanceToLvl2[] = distanceToLevel2Junctions.get(g);
				for(int i = 0; i < distanceToLvl2.length; i++) {
					facts.add(String.format("(INDEX (owner %s) (lvl 2) (index %d) (previousIndex %d) (distance %d))", g.toString(), level2Junctions[i], msNextJunction, distanceToLvl2[i]));
				}
				// Junctions de nivel 3
			}
			str.append(")");
			
			facts.add(str.toString());
		}
		
		// VAMOS A ASERTAR LOS INDEX DE MSPACMAN
		facts.add(String.format("(INDEX (owner MSPACMAN) (lvl 1) (index %d) (previousIndex %d) (distance %d)))", msNextJunction, mspacman, msNextJunctionDistance));
		Integer[] lvl3, msDistToLvl3;
		for(int i = 0; i < level2Junctions.length; i++ ) {
			int lvl2Junction = level2Junctions[i];
			facts.add(String.format("(INDEX (owner MSPACMAN) (lvl 2) (index %d) (previousIndex %d) (distance %d)))", lvl2Junction, msNextJunction, msToLvl2Junctions[i]));
			// Junctions de nivel 3
			lvl3 = level3Junctions.get(lvl2Junction);
			msDistToLvl3 = msToLvl3Junctions.get(lvl2Junction);
			for(int j = 0; j < lvl3.length; j++) {
				facts.add(String.format("(INDEX (owner MSPACMAN) (lvl 3) (index %d) (previousIndex %d) (distance %d)))", lvl3[j], lvl2Junction, msDistToLvl3[j]));
			}
		}
		facts.add(String.format("(MSPACMAN (ppill %d))", minPacmanDistancePPill));
	
		
		return facts;
	}

	
	
	
}

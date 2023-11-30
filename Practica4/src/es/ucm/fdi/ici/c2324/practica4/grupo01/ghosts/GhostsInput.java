package es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostsInput extends RulesInput {

	
	private static int MAX_DISTANCE_EDIBLE = 20;
	private static int MAX_DISTANCE_CHASING = 40;
	
	// Datos a asertar sobre CADA fantasma:
	private HashMap<GHOST, Boolean> alive;						// Si estamos vivos o no
	private HashMap<GHOST, Boolean> edibles;					// Si somos comestibles o no
	private HashMap<GHOST, GHOST> nearestChasing;				// El fantasma chasing mas cercano (dentro del rango de MAX_DISTANCE_CHASING)
	private HashMap<GHOST, GHOST> nearestEdible;				// El fantasma edible mas cercano (dentro del rango de MAX_DISTANCE_EDIBLE)
	private HashMap<GHOST, Integer> distanceToMspacman;			// La distancia a mspacman
	private HashMap<GHOST, Integer> distanceToMsNextJunction; 	// La distancia al siguiente junction de mspacman
	private HashMap<GHOST, Integer[]> distanceToLevel2Junctions;// La distancia a los siguientes junctions de mspacman
	
	// Datos a asertar sobre el pacman:
	private double minPacmanDistancePPill;	// Su distancia al powerpill
	private int msNextJunction;				// El indice del siguiente junction del pacman (puede coincidir con el indice de pacman
	private int msNextJunctionDistance;		// La distancia de pacman a su siguiente junction (puede ser 0 indicando que está ya ahi)
	private static String level2JunctionNames[] = {"firstJunction", "secondJunction", "thirdJunction"};
	private int level2Junctions[];			// Los siguientes junctions despues del siguiente junction de mspacman (nivel 2)
	private int msToLvl2Junctions[];		// La distancia de mspacman a cada uno de los junctions
	
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


	private void computeGhostsDistancesToNextJunctions() {
		
		
	}

	private void computeMsNextJunctionIndexesAndDistances() {
		int mspacman = game.getPacmanCurrentNodeIndex();
		MOVE msLastMove = game.getPacmanLastMoveMade();
		
		// Calculamos los indices de los siguientes junctions de mspacman
		Integer nextAndLvl2Junctions[] = GhostsTools.nextJunctions(game, mspacman, msLastMove);
		msNextJunction = nextAndLvl2Junctions[0];
		level2Junctions = new int[nextAndLvl2Junctions.length-1];
		for(int i = 1; i < nextAndLvl2Junctions.length; i++)  {
			level2Junctions[i-1] = nextAndLvl2Junctions[i];
		}
		
		// Una vez tenemos los indices, calculamos las distancias a esos indices.
		msToLvl2Junctions = new int[level2Junctions.length];
		msNextJunctionDistance = game.getShortestPathDistance(mspacman, msNextJunction, msLastMove);
		for(int i = 0; i < level2Junctions.length; i++) {
			msToLvl2Junctions[i] = game.getShortestPathDistance(mspacman, level2Junctions[i], msLastMove);
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

	private void clearAndInitializeMaps() {
		alive = new HashMap<>();
		edibles = new HashMap<>();
		nearestChasing = new HashMap<>();
		nearestEdible = new HashMap<>();
		distanceToMspacman = new HashMap<>();
		// si las funciones devuelven datos default entonces no hace falta lo siguiente:
		/*
		for(GHOST g : GHOST.values()) {
			alive.put(g, null);
			edibles.put(g, null);
			distanceToMspacman.put(g, Integer.MAX_VALUE);
			//...
		}
		*/
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
		this.minPacmanDistancePPill = Double.MAX_VALUE;
		for(int ppill: game.getPowerPillIndices()) {
			double distance = game.getDistance(pacman, ppill, DM.PATH);
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
		// Hacer un for para aï¿½adir estilo:
		int mspacman = game.getPacmanCurrentNodeIndex();
		
		for(GHOST g: GHOST.values()) {
			StringBuilder str = new StringBuilder();
			
			str.append("(GHOST ");
			str.append(String.format("(tipo %s)", g.toString()));
			str.append(String.format("(alive %s)", alive.get(g)));
			// eliminar el siguiente if si es neccesario aï¿½adir los slots para las condiciones de las reglas
			if(alive.get(g)) {
				str.append(String.format("(edible %s)", edibles.get(g)));
				str.append(String.format("(nearestChasing %s)", nearestChasing.get(g)));
				str.append(String.format("(nearestEdible %s)", nearestEdible.get(g)));
				str.append(String.format("(INDEX (assert (INDEX (name %s) (index %d) (distance %d))))", "mspacman", mspacman, distanceToMspacman.get(g)));
			}
			str.append(")");
			
			// Cadenas resultante de ejemplo:
			// (BLINKY (alive TRUE)(edible FALSE)(nearestChasing INKY)(nearestEdible null)(distanceToMspacman 42))
			// (SUE (alive FALSE))
			facts.add(str.toString());
		}
		
		/*
		for(GHOST g: GHOST.values()) {
			StringBuilder str = new StringBuilder();
			str.append("(GHOST ");
			str.append(String.format("(tipo %s) ", g.toString()));
			str.append(String.format("(edible %s))", edibles.get(g).toString()));
			
			facts.add(str.toString());
		}
		*/
		
		return facts;
	}

	
	
	
}

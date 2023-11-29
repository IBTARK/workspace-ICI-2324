package es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import es.ucm.fdi.ici.rules.RulesInput;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostsInput extends RulesInput {

	
	private static int MAX_DISTANCE_EDIBLE = 20;
	private static int MAX_DISTANCE_CHASING = 40;
	
	// Map que nos facilitar� las condiciones de las reglas
	private HashMap<GHOST, Boolean> alive;
	// Maps con informacion util para elegir las actions
	private HashMap<GHOST, Boolean> edibles;
	private HashMap<GHOST, GHOST> nearestChasing;
	private HashMap<GHOST, GHOST> nearestEdible;
	private HashMap<GHOST, Integer> distanceToMspacman;
	private double minPacmanDistancePPill;
	
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
	}


	private void computeDistanceToMspacman() {
		for(GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g) <= 0)
				distanceToMspacman.put(g, -1);
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
		alive.clear();
		edibles.clear();
		nearestChasing.clear();
		nearestEdible.clear();
		distanceToMspacman.clear();
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
				// La siguiente linea no hace falta, el <map>.get(<key>) nos devolver� null.
				//edibles.put(g, null);
			}
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
		/*
		facts.add(String.format("(BLINKY (edible %s))", this.BLINKYedible));
		facts.add(String.format("(INKY (edible %s))", this.INKYedible));
		facts.add(String.format("(PINKY (edible %s))", this.PINKYedible));
		facts.add(String.format("(SUE (edible %s))", this.SUEedible));
		facts.add(String.format("(MSPACMAN (mindistancePPill %d))", 
								(int)this.minPacmanDistancePPill));
		*/
		// Hacer un for para a�adir estilo:
		for(GHOST g: GHOST.values()) {
			StringBuilder str = new StringBuilder();
			
			str.append(String.format("(%s ", g.toString()));
			str.append(String.format("(alive %s)", alive.get(g)));
			// eliminar el siguiente if si es neccesario a�adir los slots para las condiciones de las reglas
			if(alive.get(g)) {
				str.append(String.format("(edible %s)", edibles.get(g)));
				str.append(String.format("(nearestChasing %s)", nearestChasing.get(g).toString()));
				str.append(String.format("(nearestEdible %s)", nearestEdible.get(g).toString()));
				str.append(String.format("(distanceToMspacman %i)", distanceToMspacman.get(g)));
			}
			str.append(")");
			
			// Cadenas resultante de ejemplo:
			// (BLINKY (alive TRUE)(edible FALSE)(nearestChasing INKY)(nearestEdible null)(distanceToMspacman 42))
			// (SUE (alive FALSE))
			facts.add(str.toString());
		}
		
		facts.add(String.format("(MSPACMAN (mindistancePPill %d))", 
				(int)this.minPacmanDistancePPill));

		return facts;
	}

	
	
	
}

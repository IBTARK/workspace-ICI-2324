package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Higher state "Perseguir"
 * Transition from "Flanquear fantasma" to "Perseguir fantasma"
 */
public class PTFlanquearPerseguir implements Transition {

	/**
	 * Evaluates when to change from the state "Flanquear fantasma" to "Perseguir fantasma".
	 * This happens when the distance to the next junction of the nearest edible ghost to MsPacMan is greater than
	 * the distance from MsPacMan to the nearest edible ghost.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.nearestEdibleNextJunctionDistance() >= pcin.distOfNearestEdibleToHisNextJunction();
	}

	@Override
	public String toString() {
		return String.format("Perseguir: FlanquearFantasma -> PerseguirFantasma \n");
	}
}

package es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Higher state "Perseguir"
 * Transition from "Perseguir fantasma" to "Flanquear fantasma"
 * */
public class PTPerseguirFlanquear implements Transition {
	
	/**
	 * Evaluates when to change from the state "Perseguir fantasma" to "Flanquear fantasma".
	 * This happens when the distance to the next junction of the nearest edible ghost to MsPacMan is smaller than
	 * the distance from MsPacMan to the nearest edible ghost.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.nearestEdibleNextJunctionDistance() < pcin.distOfNearestEdibleToHisNextJunction();
	}

	@Override
	public String toString() {
		return String.format("Perseguir: PerseguirFantasma -> FlanquearFantasma \n");
	}
}

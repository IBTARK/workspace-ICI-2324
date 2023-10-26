package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Kamikaze a fantasma" to "Huir"
 */
public class TKamikazeFantasmaHuir implements Transition {

	/**
	 * Evaluates when to change from the state "Kamikaze a fantasma" to "Huir".
	 * This happens when are not edible ghosts close to MsPacMan.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return !pcin.attack();
	}

	@Override
	public String toString() {
		return String.format("Kamikaze -> Huir \n");
	}
}

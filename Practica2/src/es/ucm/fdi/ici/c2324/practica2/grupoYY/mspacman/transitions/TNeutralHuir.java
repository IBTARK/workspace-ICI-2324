package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Neutral" to "Huir"
 */
public class TNeutralHuir implements Transition {

	/**
	 * Evaluates when to change from the state "Neutral" to "Huir".
	 * This happens when there are chasing ghosts close to MsPacMan.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.danger();
	}

	@Override
	public String toString() {
		return String.format("Neutral -> Huir \n");
	}
}

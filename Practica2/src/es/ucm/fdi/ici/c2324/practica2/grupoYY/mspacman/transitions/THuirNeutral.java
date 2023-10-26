package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Huir" to "Neutral"
 */
public class THuirNeutral implements Transition {

	/**
	 * Evaluates when to change from the state "Huir" to "Neutral".
	 * This happens when the are not ghost close to MsPacMan and when there are not few pills left.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return !pcin.danger() && !pcin.attack() && !pcin.fewPills();
	}

	@Override
	public String toString() {
		return String.format("Huir -> Neutral \n");
	}
}

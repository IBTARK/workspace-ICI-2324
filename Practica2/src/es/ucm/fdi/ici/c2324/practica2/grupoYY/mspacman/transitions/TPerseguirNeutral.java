package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Perseguir" to "Neutral"
 */
public class TPerseguirNeutral implements Transition{
	
	@Override
	/**
	 * Evaluates when to change from the state "Perseguir" to "Neutral".
	 * This happens when the there are not ghost close to MsPacMan and when MsPacMan is not on a combo.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return !pcin.danger() && !pcin.attack() && !pcin.combo();
	}

	@Override
	public String toString() {
		return String.format("Perseguir -> Neutral \n");
	}
}

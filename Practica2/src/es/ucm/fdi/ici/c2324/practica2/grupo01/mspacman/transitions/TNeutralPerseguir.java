package es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Neutral" to "Perseguir"
 */
public class TNeutralPerseguir implements Transition {

	/**
	 * Evaluates when to change from the state "Neutral" to "Perseguir".
	 * This happens when there are edible ghost close to MsPacMan and when there are not chasing ghost close to MsPacMan.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin= (MsPacManInput) in;
		
		return pcin.attack() && !pcin.danger();
	}
	
	@Override
	public String toString() {
		return String.format("Neutral -> Perseguir \n");
	}

}

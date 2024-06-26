package es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Huir" to "Perseguir"
 */
public class THuirPerseguir implements Transition{

	/**
	 * Evaluates when to change from the state "Huir" to "Perseguir".
	 * This happens when there are edible ghosts close and when there are not chasing ghosts close and when there are not
	 * few pills remaining.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.attack() && !pcin.danger() && !pcin.fewPills();
	}

	@Override
	public String toString() {
		return String.format("Huir -> Perseguir \n");
	}
}

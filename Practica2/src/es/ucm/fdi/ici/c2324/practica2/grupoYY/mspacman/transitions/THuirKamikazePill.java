package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Huir" to "Kamikaze a pills"
 */
public class THuirKamikazePill implements Transition{

	/**
	 * Evaluates when to change from the state "Huir" to "Kamikaze".
	 * This happens when there are few pills remaining.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.fewPills();
	}

	@Override
	public String toString() {
		return String.format("Huir -> KamikazePill \n");
	}
}

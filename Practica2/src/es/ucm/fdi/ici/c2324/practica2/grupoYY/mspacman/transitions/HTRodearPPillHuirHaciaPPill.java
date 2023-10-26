package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Higher state "Huir"
 * Transition from "Huir rodeando hacia PPill" to "Huir hacia PPill"
 **/
public class HTRodearPPillHuirHaciaPPill implements Transition {

	@Override
	/**
	 * Evaluates when to change from the state "Huir rodeando hacia PPill" to "Huir hacia PPill".
	 * This happens when there is a PPill close to MsPacMan or when the closest PPill to MsPacMan is not blocked.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.isPPillClose() && !pcin.isNearestPPillBlocked();
	}

	@Override
	public String toString() {
		return String.format("Huir: HuirRodeandoHaciaPPill -> Huir hacia PPill \n");
	}

}

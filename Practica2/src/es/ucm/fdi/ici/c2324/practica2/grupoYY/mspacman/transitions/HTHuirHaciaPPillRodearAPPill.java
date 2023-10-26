package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Higher state "Huir"
 * Transition from "Huir hacia PPill" to "Huir rodeando hacia PPill"
 */
public class HTHuirHaciaPPillRodearAPPill implements Transition {

	@Override
	/**
	 * Evaluates when to change from the state "Huir hacia PPill" to "Huir rodeando hacia PPill".
	 * This happens when there is  a PPill close to MsPacMan and when the closest PPill to MsPacMan is blocked and when the 
	 * closest PPill to MsPacMan is accessible.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.isPPillClose() && pcin.isNearestPPillBlocked() && pcin.ppillAccessible();
	}

	@Override
	public String toString() {
		return String.format("Huir: HuirHaciaPPill -> RodearAPPill\n");
	}

}

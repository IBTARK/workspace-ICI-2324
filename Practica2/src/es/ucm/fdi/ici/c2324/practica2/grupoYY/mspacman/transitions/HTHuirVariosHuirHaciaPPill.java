package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Higher state "Huir"
 * Transition from "Huir de varios fantasmas" to "Huir hacia PPill"
 */
public class HTHuirVariosHuirHaciaPPill implements Transition {

	@Override
	/**
	 * Evaluates when to change from the state "Huir de varios fantasmas" to "Huir hacia PPill.
	 * This happens when there is a PPill close to MsPacMan and when the nearest PPill to MsPacMan is not blocked and when
	 * there is more than one chasing ghost close to MsPacMan.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.isPPillClose() && !pcin.isNearestPPillBlocked() && pcin.dangerLevel() > 1;
	}

	@Override
	public String toString() {
		return String.format("Huir: HuirDeVariosFantasmas -> HuirHaciaPPill \n");
	}

}

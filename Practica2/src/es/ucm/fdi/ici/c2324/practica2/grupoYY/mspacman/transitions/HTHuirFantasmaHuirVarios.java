package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Higher state "Huir"
 * Transition from "Huir de un fantasma" to "Huir de varios fantasmas"
 */
public class HTHuirFantasmaHuirVarios implements Transition {

	@Override
	/**
	 * Evaluates when to change from the state "Huir de un fantasma" to "Huir de varios fantasmas".
	 * This happens when there are several ghosts close to MsPacMan and when there is no PPill close or the nearest PPill is blocked.
	 *
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.dangerLevel() > 1 && !(pcin.isPPillClose() && !pcin.isNearestPPillBlocked());
	}

	@Override
	public String toString() {
		return String.format("Huir: HuirDeUnFantasma -> HuirDeVariosFantasmas \n");
	}
}

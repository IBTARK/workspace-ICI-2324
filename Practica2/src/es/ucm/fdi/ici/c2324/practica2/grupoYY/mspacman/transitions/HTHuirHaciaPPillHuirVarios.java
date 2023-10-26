package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Higher state "Huir"
 * Transition from "Huir hacia PPill" to "Huir de varios fantasmas"
 *
 */
public class HTHuirHaciaPPillHuirVarios implements Transition{
	
	/**
	 * Evaluates when to change from the state "Huir hacia PPill" to "Huir de varios fantasmas".
	 * This happens when there is not a PPill close to MsPacMan or when the closest PPill to MsPacMan is not accessible.
	 *
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return !pcin.isPPillClose() || !pcin.ppillAccessible();
	}

	@Override
	public String toString() {
		return String.format("Huir: HuirHaciaPPill -> HuirDeVariosFantasmas \n");
	}
}

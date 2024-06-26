package es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Higher state "Neutral"
 * Transition from "Buscar pills" to "Evitar PPill"
 */
public class NTBuscarPillsEvitarPPill implements Transition {

	/**
	 * Evaluates when to change from the state "Buscar pills" to "Evitar PPill".
	 * This happens when there is a PPill close to MsPacMan.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.isPPillClose();
	}

	@Override
	public String toString() {
		return String.format("Neutral: BuscarPills -> EvitarPPill \n");
	}

}

package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Perseguir" to "Kamikaze a fantasma"
 */
public class TPerseguirKamikazeFantasma implements Transition {

	/**
	 * Evaluates when to change from the state "Perseguir" to "Kamikaze a fantasma".
	 * This happens when MsPacMan is on a combo or when there is an edible ghost close.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.combo() && pcin.attackClose();
	}

	@Override
	public String toString() {
		return String.format("Perseguir -> KamikazeFantasma \n");
	}
}

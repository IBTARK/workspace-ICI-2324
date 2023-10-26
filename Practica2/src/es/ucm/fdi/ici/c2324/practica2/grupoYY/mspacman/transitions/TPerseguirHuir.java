package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Perseguir" to "Huir"
 */
public class TPerseguirHuir implements Transition {

	@Override
	/**
	 * Evaluates when to change from the state "Perseguir" to "Huir".
	 * This happens when the there are chasing ghosts close to MsPacMan or when there MsPacMan is not on a combo and there are 
	 * not edible ghost close to MsPacMan.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin= (MsPacManInput) in;
		
		return pcin.danger() && !(pcin.combo() && pcin.attack());
	}

	@Override
	public String toString() {
		return String.format("Perseguir -> Huir \n");
	}
}

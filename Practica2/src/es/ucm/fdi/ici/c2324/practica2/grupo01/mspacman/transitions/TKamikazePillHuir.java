package es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

/**
 * Transition from "Kamikaze a pills" to "Huir"
 */
public class TKamikazePillHuir implements Transition {

	/**
	 * Evaluates when to change from the state "Kamikaze a pills" to "Huir".
	 * This happens when MsPacMan changes of level.
	 * 
	 * @param in MsPacMans input
	 */
	public boolean evaluate(Input in) {
		MsPacManInput pcin= (MsPacManInput) in;
		
		return pcin.levelUp();
	}
	
	@Override
	public String toString() {
		return String.format("KamikazePill -> Huir \n");
	}

}

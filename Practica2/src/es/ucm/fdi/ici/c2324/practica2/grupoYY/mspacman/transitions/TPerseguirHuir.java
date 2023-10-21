package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class TPerseguirHuir implements Transition {

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput msPcinput= (MsPacManInput) in;
		
		return msPcinput.dangerLevel() > 0 && !(msPcinput.getCombo() && msPcinput.getAttack());
	}

	@Override
	public String toString() {
		return String.format("PerseguirHuir Transition \n");
	}
}

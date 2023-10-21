package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class TNeutralPerseguir implements Transition {

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput msPcinput= (MsPacManInput) in;
		
		return msPcinput.getAttack() && msPcinput.dangerLevel() == 0;
	}
	
	@Override
	public String toString() {
		return String.format("NeutralPerseguir Transition \n");
	}

}

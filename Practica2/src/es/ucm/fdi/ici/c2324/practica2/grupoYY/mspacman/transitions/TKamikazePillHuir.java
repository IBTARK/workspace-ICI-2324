package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class TKamikazePillHuir implements Transition {

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput msPcinput= (MsPacManInput) in;
		
		return msPcinput.levelUp();
	}
	
	@Override
	public String toString() {
		return String.format("KamikazePillHuir Transition \n");
	}

}

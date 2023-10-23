package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class TPerseguirNeutral implements Transition{
	
	@Override
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return !pcin.danger() && !pcin.attack() && !pcin.combo();
	}

	@Override
	public String toString() {
		return String.format("Huir -> Perseguir \n");
	}
}

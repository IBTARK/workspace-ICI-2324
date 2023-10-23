package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

//Higher state "Perseguir"

//Transition from "Perseguir fantasma" to "Flanquear fantasma"
public class PTPerseguirFlanquear implements Transition {
	
	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.getNearestEdibleDistance() > pcin.nearestEdibleNextJunctionDistance();
	}

	@Override
	public String toString() {
		return String.format("Perseguir: PerseguirFantasma->FlanquearFantasma \n");
	}
}

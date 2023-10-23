package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

//Higher state "Neutral"

//Transition from "Evitar PPill" to "Buscar Pills"
public class NTEvitarPPillBuscarPills implements Transition{
	
	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return !pcin.isPPillClose();
	}

	@Override
	public String toString() {
		return String.format("Neutral: EvitarPPill-> BuscarPills \n");
	}
}

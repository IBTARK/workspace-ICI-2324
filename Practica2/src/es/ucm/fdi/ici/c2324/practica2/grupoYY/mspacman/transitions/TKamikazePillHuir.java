package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

//Transition from "Kamikaze a pills" to "Huir"
public class TKamikazePillHuir implements Transition {

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		MsPacManInput pcin= (MsPacManInput) in;
		
		return pcin.levelUp();
	}
	
	@Override
	public String toString() {
		return String.format("KamikazePill -> Huir \n");
	}

}

package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class HTHuirVariosHuirHaciaPPill implements Transition {

	@Override
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return pcin.isPPillClose() && !pcin.isNearestPPillBlocked() && pcin.dangerLevel() > 1;
	}

	@Override
	public String toString() {
		return String.format("Huir: HuirDeVariosFantasmas-> HuirHaciaPPill \n");
	}

}

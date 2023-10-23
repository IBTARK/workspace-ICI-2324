package es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.mspacman.MsPacManInput;
import es.ucm.fdi.ici.fsm.Transition;

public class HTHuirHaciaPPillHuirVarios implements Transition{
	@Override
	public boolean evaluate(Input in) {
		MsPacManInput pcin = (MsPacManInput) in;
		
		return !pcin.isPPillClose() || !pcin.ppillAccessible();
	}

	@Override
	public String toString() {
		return String.format("Huir: HuirHaciaPPill-> HuirDeVariosFantasmas \n");
	}
}
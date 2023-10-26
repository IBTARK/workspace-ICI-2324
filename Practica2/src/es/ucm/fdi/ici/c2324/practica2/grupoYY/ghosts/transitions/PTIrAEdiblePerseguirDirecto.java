package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Perseguir"

//Transition from "Ir a compañero edible" to "Perseguir directamente"
public class PTIrAEdiblePerseguirDirecto implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public PTIrAEdiblePerseguirDirecto(GHOST g) {
		ghost = g;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
	
		return !gin.ediblesClose(ghost);
	}

	@Override
	public String toString() {
		return String.format("Perseguir: IrACompañeroEdible -> PerseguirDirectamente\n");
	}
}

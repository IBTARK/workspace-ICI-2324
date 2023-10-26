package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsCoordination;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Perseguir"

//Transition from "Cubrir PPill" to "Perseguir directamente"
public class PTCubrirPPillPerseguirDirecto  implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	private GhostsCoordination coord;
	
	public PTCubrirPPillPerseguirDirecto(GHOST g, GhostsCoordination coord) {
		ghost = g;
		this.coord = coord;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
	
		boolean eval = gin.closestPPillDistance(ghost) == 0 || !gin.isPPillClose(ghost);
		
		if (eval) coordinate();
		return eval;
	}

	@Override
	public String toString() {
		return String.format("Perseguir: CubrirPPill -> PerseguirDirectamente \n");
	}
	
	private void coordinate() {
		coord.uncoverPPill(ghost);
	}
}

package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Huir"

//Transition from "Huir directamente" to "Ir a compañero chasing"
public class HTHuirDirectoIrAChasing implements Transition {
	
	//Owner of the FSM
	private GHOST ghost;
	
	public HTHuirDirectoIrAChasing(GHOST g) {
		ghost = g;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		return gin.chasingClose(ghost) && !gin.nearestChasingBlocked(ghost);
	}

	@Override
	public String toString() {
		return String.format("Huir: HuirDirectamente -> IrACompañeroChasing \n");
	}
}

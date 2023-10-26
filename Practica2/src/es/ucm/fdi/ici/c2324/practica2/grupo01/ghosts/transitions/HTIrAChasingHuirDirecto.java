package es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Huir"

//Transition from "Ir a compañero chasing" to "Huir directamente"
public class HTIrAChasingHuirDirecto implements Transition {
	
	//Owner of the FSM
	private GHOST ghost;
	
	public HTIrAChasingHuirDirecto(GHOST g) {
		ghost = g;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		GHOST nearest = gin.getNearestChasingNotBlocked(ghost);
		
		return nearest == null || gin.getDistanceBetweenGhosts(ghost, nearest) == 0;
	}

	@Override
	public String toString() {
		return String.format("Huir: IrACompañeroChasing -> HuirDirectamente \n");
	}
}

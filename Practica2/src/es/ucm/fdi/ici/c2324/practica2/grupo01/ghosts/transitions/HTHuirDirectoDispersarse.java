package es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Huir"

//Transition from "Huir directamente" to "Dispersarse"
public class HTHuirDirectoDispersarse implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public HTHuirDirectoDispersarse(GHOST g) {
		ghost = g;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		return !gin.isNearestEdible(ghost) && gin.ediblesClose(ghost) && !gin.isNearestchasingNotBlockedClose(ghost);
	}

	@Override
	public String toString() {
		return String.format("Huir: HuirDirectamente -> Dispersarse \n");
	}
}

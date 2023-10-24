package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Perseguir"

//Transition from "Perseguir directamente" to "Evitar PPill"
public class PTPerseguirDirectoEvitarPPill implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public PTPerseguirDirectoEvitarPPill(GHOST g) {
		ghost = g;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
	
		return !gin.chasingClose(ghost) && gin.msPacManFarFromPPill() && gin.getClosestPPillMsPacMan() != -1 && gin.ppillDistance(ghost, gin.getClosestPPillMsPacMan()) > gin.getMinPacmanDistancePPill();
	}

	@Override
	public String toString() {
		return String.format("Perseguir: PerseguirDirectamente -> EvitarPPill \n");
	}
}
package es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;
/*
 * Higher state "Perseguir"
 * Transition from "Perseguir Directamente" to "Flanquear"
 */
public class PTPerseguirDirectoFlanquear implements Transition {
	//Owner of the FMS
	private GHOST ghost;
	
	public PTPerseguirDirectoFlanquear(GHOST g) {
		ghost = g;
	} 
	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		return !gin.chasingClose(ghost) && gin.getDistToMsPacMan(ghost) > gin.getDistToMsPacManNextJunction(ghost);
	}

	@Override
	public String toString() {
		return String.format("Perseguir: PerseguirDirectamente -> Flanquear \n");
	}
}

package es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Transition from "Huir" to "Perseguir"
public class THuirPerseguir implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public THuirPerseguir(GHOST g) {
		ghost = g;
	}

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		return !gin.edible(ghost) && gin.isAlive(ghost);
	}

	@Override
	public String toString() {
		return String.format("Huir -> Perseguir \n");
	}
}

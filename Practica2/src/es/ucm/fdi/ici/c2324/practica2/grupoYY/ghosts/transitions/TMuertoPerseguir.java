package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Transition from "Muerto" to "Perseguir"
public class TMuertoPerseguir implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public TMuertoPerseguir(GHOST g) {
		ghost = g;
	}

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		return gin.isAlive(ghost);
	}

	@Override
	public String toString() {
		return String.format("Muerto -> Perseguir \n");
	}
}
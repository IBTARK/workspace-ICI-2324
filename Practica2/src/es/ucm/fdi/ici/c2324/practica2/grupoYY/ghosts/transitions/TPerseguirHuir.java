package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsCoordination;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;


//Transition from "Perseguir" to "Huir"
public class TPerseguirHuir implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	private GhostsCoordination coord;
	
	public TPerseguirHuir(GHOST g, GhostsCoordination coord) {
		ghost = g;
		this.coord = coord;
	}

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		boolean eval = gin.edible(ghost);
		
		if (eval) coordinate();
		return eval;
	}

	@Override
	public String toString() {
		return String.format("Perseguir -> Huir \n");
	}
	
	private void coordinate() {
		coord.uncoverEdible(ghost);
		coord.uncoverPPill(ghost);
	}
}

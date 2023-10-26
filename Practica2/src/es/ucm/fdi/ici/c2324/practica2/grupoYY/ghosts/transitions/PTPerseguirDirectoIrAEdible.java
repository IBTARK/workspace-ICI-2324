package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsCoordination;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Perseguir"

//Transition from "Perseguir directamente" to "Ir a compañero edible"
public class PTPerseguirDirectoIrAEdible implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	private GhostsCoordination coord;
	
	public PTPerseguirDirectoIrAEdible(GHOST g, GhostsCoordination coord) {
		ghost = g;
		this.coord = coord;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		boolean eval = gin.ediblesNotCoveredClose(ghost);
		
		if (eval) coordinate(gin);
		return eval;
	}

	@Override
	public String toString() {
		return String.format("Perseguir: PerseguirDirectamente -> IrACompañeroEdible \n");
	}
	
	private void coordinate(GhostsInput gin) {
		coord.coverEdible(gin.getClosestEdibleNotCovered(ghost), ghost);
	}
}

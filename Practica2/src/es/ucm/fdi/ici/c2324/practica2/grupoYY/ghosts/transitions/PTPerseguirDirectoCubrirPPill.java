package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsCoordination;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Perseguir"

//Transition from "Perseguir directamente" to "Cubrir PPill"
public class PTPerseguirDirectoCubrirPPill implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	private GhostsCoordination coord;
	
	public PTPerseguirDirectoCubrirPPill(GHOST g, GhostsCoordination coord) {
		ghost = g;
		this.coord = coord;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		boolean eval =  gin.numPPills() > 0 && !gin.chasingClose(ghost) && !gin.ppillCovered() && !gin.msPacManFarFromPPill() 
							&& gin.closestPPillDistance(ghost) < gin.getMinPacmanDistancePPill();
		
		if (eval) coordinate(gin);
		return eval;
	}

	@Override
	public String toString() {
		return String.format("Perseguir: PerseguirDirectamente -> CubrirPPill \n");
	}
	
	private void coordinate(GhostsInput in) {
		coord.coverPPill(in.getClosestPPillMsPacMan(), ghost);
	}
}

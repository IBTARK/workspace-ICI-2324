package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Perseguir"

//Transition from "Flanquear" to "Perseguir directamente"
public class PTFlanquearPerseguirDireccto implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public PTFlanquearPerseguirDireccto(GHOST g) {
		ghost = g;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		return !gin.chasingClose(ghost) && gin.getDistToMsPacMan(ghost) < gin.getDistToMsPacManNextJunction(ghost);
	}

	@Override
	public String toString() {
		return String.format("Perseguir: Flanquear -> PerseguirDirectamente \n");
	}
}
package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Perseguir"

//Transition from "Evitar PPill" to "Perseguir directamente"
public class PTEvitarPPillPerseguirDirecto implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public PTEvitarPPillPerseguirDirecto(GHOST g) {
		ghost = g;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
	
		return gin.msPacManFarFromPPill() ||  gin.getMinPacmanDistancePPill() > gin.ppillDistance(ghost, gin.getClosestPPillMsPacMan());
	}

	@Override
	public String toString() {
		return String.format("Perseguir: EvitarPPill -> PerseguirDirectamente \n");
	}
}

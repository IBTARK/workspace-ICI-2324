package es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

//Higher state "Perseguir"

//Transition from "Perseguir directamente" to "Separarse de otro fantasma"
public class PTPerseguirDirectoSepararseFantasma implements Transition {
	
	//Owner of the FMS
	private GHOST ghost;
	
	public PTPerseguirDirectoSepararseFantasma(GHOST g) {
		ghost = g;
	} 

	@Override
	//Evaluate if the transition can be made
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
	
		return gin.chasingClose(ghost) && !gin.ediblesNotCoveredClose(ghost) && (gin.ppillCovered() ||gin.msPacManFarFromPPill());
	}

	@Override
	public String toString() {
		return String.format("Perseguir: PerseguirDirectamente -> SepararseDeOtroFantasma\n");
	}
}
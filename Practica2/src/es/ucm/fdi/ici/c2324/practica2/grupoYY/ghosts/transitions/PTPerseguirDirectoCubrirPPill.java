package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class PTPerseguirDirectoCubrirPPill implements Transition {
	
	private GHOST ghost;
	
	public PTPerseguirDirectoCubrirPPill(GHOST g) {
		ghost = g;
	} 

	@Override
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		return !gin.chasingClose(ghost) && !gin.ppillCovered() && !gin.msPacManFarFromPPill();
	}

	@Override
	public String toString() {
		return String.format("Perseguir: SepararseDeOtroFantasma->PerseguirDirectamente \n");
	}
}

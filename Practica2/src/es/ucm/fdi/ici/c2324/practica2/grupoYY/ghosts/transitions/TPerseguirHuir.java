package es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.game.Constants.GHOST;

public class TPerseguirHuir implements Transition {
	
	private GHOST ghost;
	
	public TPerseguirHuir(GHOST g) {
		ghost = g;
	}

	@Override
	public boolean evaluate(Input in) {
		GhostsInput gin = (GhostsInput) in;
		
		return gin.edible(ghost);
	}

	@Override
	public String toString() {
		return String.format("Perseguir->Huir \n");
	}
}

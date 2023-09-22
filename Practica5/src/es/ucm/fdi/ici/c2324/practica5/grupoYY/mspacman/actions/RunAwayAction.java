package es.ucm.fdi.ici.c2324.practica5.grupoYY.mspacman.actions;


import java.util.Random;

import es.ucm.fdi.ici.Action;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayAction implements Action {
    
	private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();
	public RunAwayAction() {
	}
	
	@Override
	public MOVE execute(Game game) {
		return allMoves[rnd.nextInt(allMoves.length)];
    }
	
	@Override
	public String getActionId() {
		return "Runaway";
	}
            
}

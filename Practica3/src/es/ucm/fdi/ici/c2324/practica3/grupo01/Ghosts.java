package es.ucm.fdi.ici.c2324.practica3.grupo01;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts.GhostCBRengine;
import es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts.GhostStorageManager;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	GhostCBRengine cbrEngine;
	GhostStorageManager storageManager;
	
	public Ghosts()
	{		
		this.storageManager = new GhostStorageManager();
		cbrEngine = new GhostCBRengine(storageManager);
	}
	
	@Override
	public void preCompute(String opponent) {
		cbrEngine.setOpponent(opponent);
		try {
			cbrEngine.configure();
			cbrEngine.preCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void postCompute() {
		try {
			cbrEngine.postCycle();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		return null;
	}

}

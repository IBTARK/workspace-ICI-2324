package es.ucm.fdi.ici.c2324.practica3.grupo01;

import java.util.EnumMap;

import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts.GhostCBRengine;
import es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts.GhostInput;
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
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);
		
		try {
			for(GHOST ghost: GHOST.values())
			{
				if(game.doesGhostRequireAction(ghost)) {
					GhostInput input = new GhostInput(game, ghost);
					input.parseInput();
					storageManager.setGame(game);
					cbrEngine.cycle(input.getQuery());
					MOVE move = cbrEngine.getSolution();
					result.put(ghost, move);
				}
				else result.put(ghost, MOVE.NEUTRAL);
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public String getName() {
		return "YI-DJ";
	}
}

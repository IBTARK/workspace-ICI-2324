package es.ucm.fdi.ici.c2324.practica5.grupo01;

import java.io.File;
import java.util.EnumMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsFuzzyMemory;
import es.ucm.fdi.ici.fuzzy.ActionSelector;
import es.ucm.fdi.ici.fuzzy.FuzzyEngine;
import es.ucm.fdi.ici.fuzzy.observers.ConsoleFuzzyEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {
	
	private static final String RULES_PATH = 
			"bin"+File.separator+
			"es"+File.separator+
			"ucm"+File.separator+
			"fdi"+File.separator+
			"ici"+File.separator+
			"c2324"+File.separator+
			"practica5"+File.separator+
			"grupo01"+File.separator+
			"ghosts"+File.separator;

	FuzzyEngine fuzzyEngine;
	GhostsFuzzyMemory fuzzyMemory;
	
	public Ghosts() {
		setName("MsPacMan XX");

		fuzzyMemory = new GhostsFuzzyMemory();
		
		/*
		Action[] actions = {new GoToPPillAction(), new RunAwayAction()};
		
		
		ActionSelector actionSelector = new MaxActionSelector(actions);
		 
		fuzzyEngine = new FuzzyEngine("Ghosts",
				RULES_PATH+"mspacman.fcl",
				"FuzzyMsPacMan",
				actionSelector);

		ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver("MsPacMan","MsPacManRules");
		fuzzyEngine.addObserver(observer);
		*/
	}
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		// TODO Auto-generated method stub
		return null;
	}

}

package es.ucm.fdi.ici.c2324.practica5.grupo01;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsActionSelector;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsFuzzyMemory;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.ChaseMsPacman;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.GoToFirstJunction;
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

	Map<GHOST, FuzzyEngine> fuzzyEngines;
	GhostsFuzzyMemory fuzzyMemory;
	
	private GhostFuzzyData data;
	
	public Ghosts() {
		setName("MsPacMan XX");

		fuzzyMemory = new GhostsFuzzyMemory();
		fuzzyEngines = new EnumMap<GHOST, FuzzyEngine>(GHOST.class);
		
		
		//Action[] actions = {/*new GoToPPillAction(), new RunAwayAction()*/};
		
		
		//ActionSelector actionSelector = new GhostsActionSelector(actions);
		
		FuzzyEngine ghostFuzzyEngine;
		ActionSelector actionSelector;
		for(GHOST g: GHOST.values()) {
			
			Action[] actions = {new ChaseMsPacman(g, data), new GoToFirstJunction(g)};
			actionSelector = new GhostsActionSelector(actions);
			
			ghostFuzzyEngine = new FuzzyEngine(g.toString(),
					RULES_PATH+"ghosts.fcl",
					String.format("fuzzy%s", g.toString()),
					actionSelector);
			
			// Añadimos un observer a blinky.
			if(g == GHOST.BLINKY) {
				ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver("MsPacMan","MsPacManRules");
				ghostFuzzyEngine.addObserver(observer);
			}
			
			fuzzyEngines.put(g, ghostFuzzyEngine);	
		}
		
		
		//ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver("MsPacMan","MsPacManRules");
		//fuzzyEngine.addObserver(observer);	
	}
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		
		GhostsInput input = new GhostsInput(game);
		input.parseInput();
		fuzzyMemory.getInput(input);
		
		HashMap<String, Double> fvars = input.getFuzzyValues();
		fvars.putAll(fuzzyMemory.getFuzzyValues());
		
		FuzzyEngine ghostEngine;
		for(GHOST g: GHOST.values()) {
			ghostEngine = fuzzyEngines.get(g);
			moves.put(g, ghostEngine.run(fvars, game));
		}
		
		return moves;
	}

}

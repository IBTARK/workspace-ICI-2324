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
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.FlankMsPacman;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.GoToFirstJunction;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.LookForMsPacman;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.MaintainDistance;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.RunAwayFromMs;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.RunAwayScattering;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.RunAwayToChasing;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.Scatter;
import es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts.actions.ScatterEdible;
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
	
	GhostFuzzyData fuzzyData;
	
	public Ghosts() {
		setName("MsPacMan XX");

		fuzzyMemory = new GhostsFuzzyMemory();
		fuzzyEngines = new EnumMap<GHOST, FuzzyEngine>(GHOST.class);
		fuzzyData = new GhostFuzzyData();
		
		//Action[] actions = {/*new GoToPPillAction(), new RunAwayAction()*/};
		
		
		//ActionSelector actionSelector = new GhostsActionSelector(actions);
		
		FuzzyEngine ghostFuzzyEngine;
		ActionSelector actionSelector;
		for(GHOST g: GHOST.values()) {
			
			Action[] actions = 
			{
				new ChaseMsPacman(g, fuzzyData), 
				new GoToFirstJunction(g, fuzzyData),
				new FlankMsPacman(g, fuzzyData),
				new LookForMsPacman(g, fuzzyData),
				new MaintainDistance(g, fuzzyData),
				new RunAwayFromMs(g, fuzzyData),
				new RunAwayScattering(g, fuzzyData),
				new RunAwayToChasing(g, fuzzyData),
				new Scatter(g, fuzzyData),
				new ScatterEdible(g, fuzzyData)
			};
			actionSelector = new GhostsActionSelector(actions);
			String fuzzyBlockName = "Fuzzy"+g.toString();
			ghostFuzzyEngine = new FuzzyEngine(g.toString(),
					RULES_PATH+"ghosts.fcl",
					fuzzyBlockName,
					actionSelector);
			
			// Añadimos un observer a blinky.
			if(g == GHOST.BLINKY) {
				ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver("BLINKY","BLINKYrules");
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
		// input.parseInput(); // Ya se parsea en el constructor.
		fuzzyMemory.getInput(input);
		
		if(game.getPacmanCurrentNodeIndex()!=-1) {
			fuzzyData.setMspacman(game.getPacmanCurrentNodeIndex());
			fuzzyData.setMsLastMove(game.getPacmanLastMoveMade());
		}
		
		HashMap<String, Double> fvars = input.getFuzzyValues();
		fvars.putAll(fuzzyMemory.getFuzzyValues());
		
		FuzzyEngine ghostEngine;
		for(GHOST g: GHOST.values()) {
			if(game.getGhostLairTime(g)<=0) {
				ghostEngine = fuzzyEngines.get(g);
				moves.put(g, ghostEngine.run(fvars, game));
			}
			else {
				moves.put(g, MOVE.NEUTRAL);
			}
		}
		
		return moves;
	}

}

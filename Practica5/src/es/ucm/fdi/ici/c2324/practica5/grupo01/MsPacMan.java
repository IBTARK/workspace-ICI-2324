package es.ucm.fdi.ici.c2324.practica5.grupo01;

import java.io.File;
import java.util.HashMap;

import es.ucm.fdi.ici.Action;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MaxActionSelector;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManFuzzyData;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManFuzzyMemory;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActBuscarPills;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActEvitarPPill;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActFlanquearFantasma;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActHuirDeFantasma;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActHuirHaciaPPill;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActHuirRodeandoPPill;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActHuirVariosFantasmas;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActKamikazeAPill;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActKamikazeFantasma;
import es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman.actions.ActPerseguirFantasma;
import es.ucm.fdi.ici.fuzzy.ActionSelector;
import es.ucm.fdi.ici.fuzzy.FuzzyEngine;
import es.ucm.fdi.ici.fuzzy.observers.ConsoleFuzzyEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {

	private static final String RULES_PATH = "bin"+File.separator+"es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2324"+File.separator+"practica5"+File.separator+"grupo01"+File.separator+"mspacman"+File.separator;
	FuzzyEngine fuzzyEngine;
	MsPacManFuzzyMemory fuzzyMemory;
	MsPacManFuzzyData fuzzyData;
	
	
	public MsPacMan()
	{
		setName("MsPacMan YI-DJ");

		fuzzyData = new MsPacManFuzzyData();
		fuzzyMemory = new MsPacManFuzzyMemory(fuzzyData);
		
		Action[] actions = {new ActBuscarPills(), 
							new ActEvitarPPill(fuzzyData), 
							new ActFlanquearFantasma(fuzzyData), 
							new ActHuirDeFantasma(fuzzyData), 
							new ActHuirHaciaPPill(fuzzyData), 
							new ActHuirRodeandoPPill(fuzzyData), 
							new ActHuirVariosFantasmas(fuzzyData), 
							new ActKamikazeAPill(fuzzyData), 
							new ActKamikazeFantasma(fuzzyData), 
							new ActPerseguirFantasma(fuzzyData) };
		
		ActionSelector actionSelector = new MaxActionSelector(actions);
		 
		fuzzyEngine = new FuzzyEngine("MsPacMan",
				RULES_PATH+"mspacman.fcl",
				"FuzzyMsPacMan",
				actionSelector);

		/*ConsoleFuzzyEngineObserver observer = new ConsoleFuzzyEngineObserver("MsPacMan","MsPacManRules");
		fuzzyEngine.addObserver(observer);
		*/
		
	}
	
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		MsPacManInput input = new MsPacManInput(game);
		input.parseInput();
		fuzzyMemory.getInput(input);
		
		HashMap<String, Double> fvars = fuzzyMemory.getFuzzyValues();
		
		return fuzzyEngine.run(fvars,game);
	}

}

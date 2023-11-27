package es.ucm.fdi.ici.c2324.practica4.grupo01;

import java.io.File;
import java.util.HashMap;

import es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.actions.ChaseAction;
import es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.actions.KamikazePillAction;
import es.ucm.fdi.ici.c2324.practica4.grupo01.mspacman.actions.NeutralAction;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class MsPacMan extends PacmanController {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2324"+File.separator+"practica4"+File.separator+
											 "grupo01"+File.separator+"mspacman"+File.separator+"mspacmanrules.clp";
	HashMap<String,RulesAction> map;
	
	RuleEngine ruleEngine;
	
	
	public MsPacMan() {
		setName("PacMan YI-DJ");
		setTeam("Team 01");
		
		map = new HashMap<String,RulesAction>();
		//Fill Actions
		RulesAction chase = new ChaseAction();
		RulesAction neutral = new NeutralAction();
		RulesAction kamikazePill = new KamikazePillAction(); 
		//TODO
		
		map.put("chase", chase);
		map.put("neutral", neutral);
		map.put("kamikazePill", kamikazePill);
		
		ruleEngine = new RuleEngine("MsPacManRuleEngine", RULES_PATH, map);
		
		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver(ruleEngine.toString(), true);
		ruleEngine.addObserver(observer);
	}

	@Override
	public MOVE getMove(Game game, long timeDue) {
		//Process input
		RulesInput input = new MsPacManInput(game);
		//load facts
		//reset the rule engines
		ruleEngine.reset();
		ruleEngine.assertFacts(input.getFacts());
		
		return ruleEngine.run(game);
	}
}

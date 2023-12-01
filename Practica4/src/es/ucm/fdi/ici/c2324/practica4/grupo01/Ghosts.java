package es.ucm.fdi.ici.c2324.practica4.grupo01;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Vector;

import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions.ChaseMspacmanAction;
import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions.FlankMspacmanAction;
import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions.RunAwayAction;
import es.ucm.fdi.ici.rules.RuleEngine;
import es.ucm.fdi.ici.rules.RulesAction;
import es.ucm.fdi.ici.rules.RulesInput;
import es.ucm.fdi.ici.rules.observers.ConsoleRuleEngineObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts  extends GhostController  {
	private static final String RULES_PATH = "es"+File.separator+"ucm"+File.separator+"fdi"+File.separator+"ici"+File.separator+"c2324"+File.separator+"practica4"+File.separator+"grupo01"+File.separator+"ghosts"+File.separator;
	HashMap<String,RulesAction> map;
	
	RuleEngine ruleEngine;
	
	
	public Ghosts() {
		setName("Ghosts YI-DJ");
		setTeam("Team 01");
		
		map = new HashMap<String,RulesAction>();
		
		
		RulesAction ChaseMspacman = new ChaseMspacmanAction();
		RulesAction FlankMspacman = new FlankMspacmanAction();
		RulesAction RunAway = new RunAwayAction();
		// Se annadiran mas tarde (cuando todo funcione bien)
		//RulesAction GoToChasing = new GoToChasingAction();
		//RulesAction ProtectEdible = new ProtectEdibleAction();
		
		map.put("ChaseMspacman", ChaseMspacman);
		map.put("FlankMspacman", FlankMspacman);
		map.put("RunAway", RunAway);
		//map.put("GoToChasing", GoToChasing);
		//map.put("ProtectEdible", ProtectEdible);
		
		
		String rulesFile = String.format("%sghostrules.clp", RULES_PATH);
		ruleEngine = new RuleEngine("ghostsEngine",rulesFile, map);
		
		// Add an observer to the current ruleEngine (one for all ghosts)
		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver("ghostsEngine", true);
		ruleEngine.addObserver(observer);
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		//Process input
		RulesInput input = new GhostsInput(game);
		
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);	

		// We reset the rule engine once per getMove() is called
		ruleEngine.reset();
		
		// Assert all facts
		Vector<String> facts = new Vector<String>();
		// It doesn't matter the type of the currentghost we put here
		facts.add("(CURRENTGHOST (tipo BLINKY))");
		ruleEngine.assertFacts(facts);
		ruleEngine.assertFacts(input.getFacts());
		
		// Once all facts are asserted, we then will ask for the action of each ghost
		for(GHOST ghost: GHOST.values())
		{			
			facts = new Vector<String>();
			// We add the "NEWGHOST", where a set of rules will:
			//  	- Delete the previous asserted ACTION
			// 		- Change the CURRENTGHOST to the type of the NEWGHOST
			// 		- Assing the ACTION to the newly modified CURRENTGHOST
			facts.add(String.format("(NEWGHOST (tipo %s))", ghost.toString()));
			ruleEngine.assertFacts(facts);
			MOVE move = ruleEngine.run(game);
			result.put(ghost, move);
		}
		
		return result;
	}

}

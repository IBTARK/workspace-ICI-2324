package es.ucm.fdi.ici.c2324.practica4.grupo01;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Vector;

import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions.ChaseMspacmanAction;
import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions.FlankMspacmanAction;
import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions.GoToChasingAction;
import es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions.ProtectEdibleAction;
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
		//Fill Actions
		/*RulesAction BLINKYchases = new ChaseAction(GHOST.BLINKY);
		RulesAction INKYchases = new ChaseAction(GHOST.INKY);
		RulesAction PINKYchases = new ChaseAction(GHOST.PINKY);
		RulesAction SUEchases = new ChaseAction(GHOST.SUE);
		RulesAction BLINKYrunsAway = new RunAwayAction(GHOST.BLINKY);
		RulesAction INKYrunsAway = new RunAwayAction(GHOST.INKY);
		RulesAction PINKYrunsAway = new RunAwayAction(GHOST.PINKY);
		RulesAction SUErunsAway = new RunAwayAction(GHOST.SUE);*/
		
		RulesAction ChaseMspacman = new ChaseMspacmanAction();
		RulesAction FlankMspacman = new FlankMspacmanAction();
		//RulesAction GoToChasing = new GoToChasingAction();
		// Se añadira mas tarde (cuando todo funcione bien)
		//RulesAction ProtectEdible = new ProtectEdibleAction();
		RulesAction RunAway = new RunAwayAction();
		
		map.put("ChaseMspacman", ChaseMspacman);
		map.put("FlankMspacman", FlankMspacman);
		//map.put("GoToChasing", GoToChasing);
		//map.put("ProtectEdible", ProtectEdible);
		map.put("RunAway", RunAway);
		
		
		/**
		 * HACER UN SOLO CLP CON LAS ACCIONES "GENERICAS" DONDE LE PASEMOS EL FANTASMA 
		 * DONDE EN EL CLP TENDREMOS:
		 * 	PARA PACMAN:
		 * 		SU POSICION,
		 * 		SUS TRES (MAX) SIGUIENTES JUNCTIONS
		 *	PARA CADA FANTASMA:
		 *		DISTANCIA A MSPACMAN
		 *		DISTANCIA A CADA UNO DE LOS JUNCTIONS
		 *		+ INFORMACION QUE TENEMOS YA
		 *
		 * VER EXPLICACION EN EL SIGUIENTE METODO
		 * PARA ELEGIR EL MOVIMIENTO DE CADA FANTASMA TENDREMOS QUE ASERTAR EN EL INPUT.GETFACTS EL CURRENT GHOST
		 * Y TENER EN LAS REGLAS CONDICIONES "GENERICAS" Y ACCIONES GENERICAS DONDE LE PASEMOS POR PARAMETRO (A LAS ACCIONES)
		 * EL FANTASMA QUE TIENE QUE REALIZARLAS
		 */
		
		
		
		String rulesFile = String.format("%sghostrules.clp", RULES_PATH);
		ruleEngine = new RuleEngine("ghostsEngine",rulesFile, map);
		
		
		//add observer only to BLINKY
		ConsoleRuleEngineObserver observer = new ConsoleRuleEngineObserver("ghostsEngine", true);
		ruleEngine.addObserver(observer);
		
	}

	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		
		//Process input
		RulesInput input = new GhostsInput(game);
		//load facts
		//reset the rule engines
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);	
		/**
		 * chequear si somos el fantasma mas cercano a mspacman
		 * si no lo somos (somos pinky)
		 * 
		 * BLINKY (es el mas cercano)
		 * INKY (va al segundo junction)
		 * PINKY <- 
		 * SUE
		 */
		ruleEngine.reset();
		Vector<String> facts = new Vector<String>();
		facts.add("(CURRENTGHOST (tipo BLINKY))");
		ruleEngine.assertFacts(facts);
		ruleEngine.assertFacts(input.getFacts());
		for(GHOST ghost: GHOST.values())
		{			
			facts = new Vector<String>();
			facts.add(String.format("(NEWGHOST (tipo %s))", ghost.toString()));
			ruleEngine.assertFacts(facts);
			MOVE move = ruleEngine.run(game);
			result.put(ghost, move);
		}
		
		return result;
	}

}

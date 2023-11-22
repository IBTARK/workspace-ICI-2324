package ghosts_simplified;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.Attribute;
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.CustomPlainTextConnector;
import pacman.game.Constants.MOVE;

public class GhostCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private CBRCase oldCase;
	
	/*
	 * Explicacion de por que tenemos que tener 1 solo storage manager que lleve ambas CaseBase:
	 * Queremos tener un buffer por cada fantasma que almacene sus movimientos cada junction, aunque despues
	 * almacene los movimientos como edible o chasing en sus distintas CaseBase.
	 * De esta manera, estrictamente cada X(=3) movimientos sean edible o chasing haremos el revise and retain.
	 * 
	 * Si tuviesemos 2 storage managers uno siendo edible y chasing, tendrian que ocurrir 3 movimientos de ese tipo especifico
	 * para que se hiciese el revise and retain. (Habria casos en los que nunca se hiciese revise and retain).
	 */
	private GhostStorageManager storageManager;

	// These connectors and case bases are the ones connecting to the generic case base.
	CustomPlainTextConnector connectorEdible;
	CustomPlainTextConnector connectorChasing;
	CachedLinearCaseBase caseBaseEdible;
	CachedLinearCaseBase caseBaseChasing;
	
	CachedLinearCaseBase generalCaseBaseEdible;
	CachedLinearCaseBase generalCaseBaseChasing;
	CustomPlainTextConnector generalConnectorEdible;
	CustomPlainTextConnector generalConnectorChasing;

	NNConfig simConfigEdible;
	NNConfig simConfigChasing;
	
	
	final static String TEAM = "grupo01";
	
	
	final static String CONNECTOR_EDIBLE_FILE_PATH = "/ghosts_simplified/plaintextconfig_edible.xml";
	final static String CONNECTOR_CHASING_FILE_PATH = "/ghosts_simplified/plaintextconfig_chasing.xml";
	final static String  CASE_BASE_EDIBLE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"ghosts_simplified"+File.separator+"edible"+File.separator;
	final static String  CASE_BASE_CHASING_PATH = "cbrdata"+File.separator+TEAM+File.separator+"ghosts_simplified"+File.separator+"chasing"+File.separator;
	final static String GENERIC_CASE_BASE_NAME = "generic.csv";
	
	final static int NUM_NEIGHBORS = 5; //number of neighbors of the KNN
	// La similitud de los casos va de 1.2 (max) a 0
	final static double MINIMUM_SIM_VAL = 0.8;
	final static double RETAIN_SIM_VAL = 1.05;
	
	public GhostCBRengine(GhostStorageManager storageManager) {
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		// Connectors and case bases for each opponent
		connectorEdible = new CustomPlainTextConnector();
		connectorChasing = new CustomPlainTextConnector();
		caseBaseEdible = new CachedLinearCaseBase();
		caseBaseChasing = new CachedLinearCaseBase();
		
		// Connectors and case bases to connect to the general data (when we have a low similarity in opponent cases)
		generalConnectorEdible = new CustomPlainTextConnector();
		generalConnectorChasing = new CustomPlainTextConnector();
		generalCaseBaseEdible = new CachedLinearCaseBase();
		generalCaseBaseChasing = new CachedLinearCaseBase();
		
		connectorEdible.initFromXMLfile(FileIO.findFile(CONNECTOR_EDIBLE_FILE_PATH));
		connectorChasing.initFromXMLfile(FileIO.findFile(CONNECTOR_CHASING_FILE_PATH));
		generalConnectorEdible.initFromXMLfile(FileIO.findFile(CONNECTOR_EDIBLE_FILE_PATH));
		generalConnectorChasing.initFromXMLfile(FileIO.findFile(CONNECTOR_CHASING_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connectorEdible.setCaseBaseFile(CASE_BASE_EDIBLE_PATH, opponent+".csv");
		connectorChasing.setCaseBaseFile(CASE_BASE_CHASING_PATH, opponent+".csv");
		
		generalConnectorEdible.setCaseBaseFile(CASE_BASE_EDIBLE_PATH, GENERIC_CASE_BASE_NAME);
		generalConnectorChasing.setCaseBaseFile(CASE_BASE_CHASING_PATH, GENERIC_CASE_BASE_NAME);
		
		this.storageManager.setCaseBaseEdible(caseBaseEdible);
		this.storageManager.setCaseBaseChasing(caseBaseChasing);
		
		/*
		 * HACER 2 SIMCONFIG , UNA EDIBLE OTRA NO Y A LA HORA DE EVALUAR (EN cycle) SE ELIGE LA SIMCONFIg
		 */
		// Se puede hacer un CaseComponent por cada vector a la hora de almacenar (se tendria que cambiar el mapeo tambi�n en los config.xml
		simConfigEdible =  new NNConfig();
		simConfigEdible.setDescriptionSimFunction(new SimGhost());
		simConfigEdible.addMapping(new Attribute("mspacmanLives", GhostDescription.class), new DistanceInterval(3));
		simConfigEdible.addMapping(new Attribute("mspacmanDistance", GhostDescription.class), new DistanceInterval(250));
		simConfigEdible.addMapping(new Attribute("score", GhostDescription.class), new Interval(15000));
		simConfigEdible.addMapping(new Attribute("time", GhostDescription.class),new Interval(4000));
		simConfigEdible.addMapping(new Attribute("edibleTime", GhostDescription.class),new DistanceInterval(200));
		simConfigEdible.addMapping(new Attribute("mspacmanToPPill", GhostDescription.class),new DistanceInterval(300));
		simConfigEdible.addMapping(new Attribute("upVector",GhostDescription.class), new SimGhostVector());
		simConfigEdible.addMapping(new Attribute("downVector",GhostDescription.class), new SimGhostVector());
		simConfigEdible.addMapping(new Attribute("leftVector",GhostDescription.class), new SimGhostVector());
		simConfigEdible.addMapping(new Attribute("rightVector",GhostDescription.class), new SimGhostVector());
		
		simConfigChasing = new NNConfig();
		simConfigChasing.setDescriptionSimFunction(new SimGhost());
		simConfigChasing.addMapping(new Attribute("mspacmanLives", GhostDescription.class), new DistanceInterval(3));
		simConfigChasing.addMapping(new Attribute("mspacmanDistance", GhostDescription.class), new DistanceInterval(250));
		simConfigChasing.addMapping(new Attribute("score", GhostDescription.class), new Interval(15000));
		simConfigChasing.addMapping(new Attribute("time", GhostDescription.class),new Interval(4000));
		simConfigChasing.addMapping(new Attribute("mspacmanToPPill", GhostDescription.class),new DistanceInterval(300));
		simConfigChasing.addMapping(new Attribute("upVector",GhostDescription.class), new SimGhostVector());
		simConfigChasing.addMapping(new Attribute("downVector",GhostDescription.class), new SimGhostVector());
		simConfigChasing.addMapping(new Attribute("leftVector",GhostDescription.class), new SimGhostVector());
		simConfigChasing.addMapping(new Attribute("rightVector",GhostDescription.class), new SimGhostVector());
	}


	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBaseEdible.init(connectorEdible);
		caseBaseChasing.init(connectorChasing);
		generalCaseBaseEdible.init(generalConnectorEdible);
		generalCaseBaseChasing.init(generalConnectorChasing);
		
		return caseBaseEdible;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		
		boolean edible = ((GhostDescription) query.getDescription()).getEdible();
		this.oldCase = null;
		// HACER UN IF DONDE VEAMOS SI ES EDIBLE.
		// ELEGIR DE LA BASE DE CASOS Y ASIGNAR EL SIMCONFIG CORRESPONDIENTE
		
		computeRetrieveAndReuse(caseBaseEdible, caseBaseChasing, query, edible);
		
		if(this.action == MOVE.NEUTRAL) {
			computeRetrieveAndReuse(generalCaseBaseEdible, generalCaseBaseChasing, query, edible);
			this.oldCase = null;
			
			if(this.action != MOVE.NEUTRAL) {
				System.out.print("GENERICA ");
			}
		}
		else
			System.out.print("OPONENTE ");
		
		if(this.action == MOVE.NEUTRAL) {
			this.action = getRandomPossibleMove(query);
			System.out.print("MOVIMIENTO ALEATORIO ");
		}
		
			//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase, oldCase);
		
	}

	private void computeRetrieveAndReuse(CBRCaseBase CBedible, CBRCaseBase CBchasing, CBRQuery query, boolean edible) {
		
		Collection<RetrievalResult> eval;
		ArrayList<RetrievalResult> neighbors = null;
		if((edible && CBedible.getCases().isEmpty())
				|| (!edible && CBchasing.getCases().isEmpty())) {
			this.action = MOVE.NEUTRAL;
		}
		else {
			// Comprobamos la evaluacion en la base de casos del oponente
			if(edible)
				eval = NNScoringMethod.evaluateSimilarity(CBedible.getCases(), query, simConfigEdible);
			else
				eval = NNScoringMethod.evaluateSimilarity(CBchasing.getCases(), query, simConfigChasing);
			
			neighbors = new ArrayList<>(SelectCases.selectTopKRR(eval, NUM_NEIGHBORS));

			//RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
			//double similarity = first.getEval();
			
			
			if(neighbors.get(0).getEval() < MINIMUM_SIM_VAL) 
				this.action = MOVE.NEUTRAL;
			else {
				if(edible) 	this.action = reuseEdible(eval, query);
				else this.action = reuseChasing(eval, query);
			}
		}
		
	}

	private MOVE reuseChasing(Collection<RetrievalResult> eval, CBRQuery query)
	{
		// Queremos maximizar la reutilizacion (minimizar la distanciaa entre nosotros y pacman)
		
		
		Iterator<RetrievalResult> topCases = SelectCases.selectTopKRR(eval, NUM_NEIGHBORS).iterator();
		/*
		RetrievalResult topRetrieval = topCases.next();
		CBRCase retrievedCase = topRetrieval.get_case();
		double similarity = topRetrieval.getEval();
		GhostResult result = (GhostResult) retrievedCase.getResult();
		*/
		
		RetrievalResult topRetrieval = null;
		GhostResult result = null;
		
		double maxReut = 0;
		double curReut;
		MOVE action = MOVE.NEUTRAL;

		// Con este bucle calculamos el caso con MAYOR "reut" y lo seleccionamos como topRetrieval para luego elegir su movimiento.
		for(RetrievalResult currentRetrieval : SelectCases.selectTopKRR(eval, NUM_NEIGHBORS)) {
			GhostSolution solution = (GhostSolution) currentRetrieval.get_case().getSolution();
			if(moveAvailable(query, solution.getAction())) {
				result = (GhostResult) currentRetrieval.get_case().getResult();
				curReut = currentRetrieval.getEval() * (result.getScore());

				if(curReut > maxReut) {
					action = solution.getAction();
					maxReut = curReut;
					topRetrieval = currentRetrieval;
				}
			}
		}
		
		if(topRetrieval != null) {
			System.out.print("CHASING Eval ["+topRetrieval.getEval() + "] ");
			if(topRetrieval.getEval() >= RETAIN_SIM_VAL) {
				this.oldCase =  topRetrieval.get_case();
			}
			else if (topRetrieval.getEval() <= MINIMUM_SIM_VAL) {
				action = MOVE.NEUTRAL;
			}
		}

		return action;
	}
	
	private MOVE reuseEdible(Collection<RetrievalResult> eval, CBRQuery query)
	{
		// Queremos minimizar la reutilizacion (maximizar la distanciaa entre nosotros y pacman
		
		Iterator<RetrievalResult> topCases = SelectCases.selectTopKRR(eval, NUM_NEIGHBORS).iterator();
		/*
		RetrievalResult topRetrieval = topCases.next();
		CBRCase retrievedCase = topRetrieval.get_case();
		double similarity = topRetrieval.getEval();
		GhostResult result = (GhostResult) retrievedCase.getResult();
		*/
		
		RetrievalResult topRetrieval = null;
		GhostResult result = null;
		
		double minReut = 0;
		double curReut;
		MOVE action = MOVE.NEUTRAL;

		// Con este bucle calculamos el caso con MENOR "reut" y lo seleccionamos como topRetrieval para luego elegir su movimiento.
		for(RetrievalResult currentRetrieval : SelectCases.selectTopKRR(eval, NUM_NEIGHBORS)) {
			GhostSolution solution = (GhostSolution) currentRetrieval.get_case().getSolution();
			if(moveAvailable(query, solution.getAction())) {
				result = (GhostResult) currentRetrieval.get_case().getResult();
				curReut = currentRetrieval.getEval() * (result.getScore());

				if(curReut < minReut) {
					action = solution.getAction();
					minReut = curReut;
					topRetrieval = currentRetrieval;
				}
			}
		}
		
		if(topRetrieval != null) {
			System.out.print("CHASING Eval ["+topRetrieval.getEval() + "] ");
			if(topRetrieval.getEval() >= RETAIN_SIM_VAL) {
				this.oldCase =  topRetrieval.get_case();
				}
			else if (topRetrieval.getEval() <= MINIMUM_SIM_VAL) {
				action = MOVE.NEUTRAL;
			}
			
		}

		return action;
	}
	
	private boolean moveAvailable(CBRQuery query, MOVE move) {
		boolean res;
		
		res = (( GhostDescription )query.getDescription()).getPossibleMoves().contains(move);
		
		return res;
	}
	
	private MOVE getRandomPossibleMove(CBRQuery query) {
		ArrayList<MOVE> possibleMoves = (( GhostDescription )query.getDescription()).getPossibleMoves();
		int index = (int)Math.floor(Math.random()*4);
		index= (index+1)%possibleMoves.size();
		MOVE res = possibleMoves.get(index);
		
		return res;
	}
	
	

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		GhostDescription newDescription = (GhostDescription) query.getDescription();
		GhostResult newResult = new GhostResult();
		GhostSolution newSolution = new GhostSolution();
		int newId;

		if(newDescription.getEdible()) {
			newId = this.caseBaseEdible.getNextId();
			newId+= storageManager.getPendingEdibleCases();
		}
		else {
			newId = this.caseBaseChasing.getNextId();
			newId+= storageManager.getPendingChasingCases();
		}
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
	
		
		newSolution.setAction(this.action);
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
	
		System.out.println(newSolution.toString());
		
		return newCase;
	}
	
	public MOVE getSolution() {
		return this.action;
	}

	@Override
	public void postCycle() throws ExecutionException {
		this.storageManager.close();
		this.caseBaseChasing.close();
		this.caseBaseEdible.close();
		this.generalCaseBaseEdible.close();
		this.generalCaseBaseChasing.close();
	}


}

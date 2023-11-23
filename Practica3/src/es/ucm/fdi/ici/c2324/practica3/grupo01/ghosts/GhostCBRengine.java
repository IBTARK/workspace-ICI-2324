package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.exception.ExecutionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.RetrievalResult;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.selection.SelectCases;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
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
	
	final static boolean DEBUG_EVALS = false;
	
	final static String CONNECTOR_EDIBLE_FILE_PATH = "es/ucm/fdi/ici/c2324/practica3/"+TEAM+"/ghosts/plaintextconfig_edible.xml";
	final static String CONNECTOR_CHASING_FILE_PATH = "es/ucm/fdi/ici/c2324/practica3/"+TEAM+"/ghosts/plaintextconfig_chasing.xml";
	final static String GENERIC_CASE_BASE_EDIBLE_PATH = "src/es/ucm/fdi/ici/c2324/practica3/"+TEAM+File.separator+"cbrdata"+File.separator+TEAM+File.separator+"ghosts"+File.separator+"edible"+File.separator;
	final static String GENERIC_CASE_BASE_CHASING_PATH = "src/es/ucm/fdi/ici/c2324/practica3/"+TEAM+File.separator+"cbrdata"+File.separator+TEAM+File.separator+"ghosts"+File.separator+"chasing"+File.separator;

	final static String OPPONENT_CASE_BASE_EDIBLE_PATH = "/data/grupo01/ghosts"+File.separator+"edible"+File.separator;
	final static String OPPONENT_CASE_BASE_CHASING_PATH = "/data/grupo01/ghosts"+File.separator+"chasing"+File.separator;
	final static String GENERIC_CASE_BASE_NAME = "generic.csv";
	
	final static int NUM_NEIGHBORS = 5; //number of neighbors of the KNN
	// La similitud de los casos va de 1 a 0
	final static double MINIMUM_SIM_VAL = 0.6;
	final static double RETAIN_SIM_VAL = 0.80;
	
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
		connectorEdible.setCaseBaseFile(OPPONENT_CASE_BASE_EDIBLE_PATH, opponent+".csv");
		connectorChasing.setCaseBaseFile(OPPONENT_CASE_BASE_CHASING_PATH, opponent+".csv");
		
		generalConnectorEdible.setCaseBaseFile(GENERIC_CASE_BASE_EDIBLE_PATH, GENERIC_CASE_BASE_NAME);
		generalConnectorChasing.setCaseBaseFile(GENERIC_CASE_BASE_CHASING_PATH, GENERIC_CASE_BASE_NAME);
		
		this.storageManager.setCaseBaseEdible(caseBaseEdible);
		this.storageManager.setCaseBaseChasing(caseBaseChasing);
		
		/*
		 * We are doing 2 simConfigs
		 */
		simConfigEdible =  new NNConfig();
		simConfigEdible.setDescriptionSimFunction(new SimGhost());
		
		simConfigChasing = new NNConfig();
		simConfigChasing.setDescriptionSimFunction(new SimGhost());
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
		
		// Retrieves and reuses the opponent's specific cases
		computeRetrieveAndReuse(caseBaseEdible, caseBaseChasing, query, edible);
		
		if(this.action == MOVE.NEUTRAL) {
			// In case we have not found a fitting case we retrieve and reuse the generic cases
			computeRetrieveAndReuse(generalCaseBaseEdible, generalCaseBaseChasing, query, edible);
			this.oldCase = null;
		}
		
		if(this.action == MOVE.NEUTRAL) {
			// In case 
			this.action = getRandomPossibleMove(query);
		}
		
			//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase, oldCase);
		
	}

	/**
	 * This function checks if the given case bases are empty. Evaluates the similarity if they aren't 
	 * and, if the top neighbour's similarity surpases MINIMUM_SIM_VAL then it calls the reuse() method.
	 * 
	 * @param CBedible case base for edible cases
	 * @param CBchasing case base for chasing cases
	 * @param query the query to check
	 * @param edible if the current query's ghost is edible
	 */
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

	/**
	 * Method that given an eval, gets the top NUM_NEIGHBORS and selects the MAXIMUM reut given by the next equation
	 * reut = "Evaluation of the case" * "result score of the case"
	 * @param eval
	 * @param query
	 * @return (MOVE) the action that the reuse method has chosen
	 */
	private MOVE reuseChasing(Collection<RetrievalResult> eval, CBRQuery query)
	{
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
				curReut = currentRetrieval.getEval() * result.getScore();

				if(curReut > maxReut) {
					action = solution.getAction();
					maxReut = curReut;
					topRetrieval = currentRetrieval;
				}
			}
		}
		
		if(topRetrieval != null) {
			if(topRetrieval.getEval() >= RETAIN_SIM_VAL) {
				this.oldCase =  topRetrieval.get_case();
			}
			else if (topRetrieval.getEval() <= MINIMUM_SIM_VAL) {
				action = MOVE.NEUTRAL;
			}
		}

		return action;
	}
	
	/**
	 * Method that given an eval, gets the top NUM_NEIGHBORS and selects the MINIMUM reut given by the next equation
	 * reut = "Evaluation of the case" * "result score of the case"
	 * 
	 * @param eval
	 * @param query
	 * @return
	 */
	private MOVE reuseEdible(Collection<RetrievalResult> eval, CBRQuery query)
	{
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
				curReut = currentRetrieval.getEval() * result.getScore();

				if(curReut < minReut) {
					action = solution.getAction();
					minReut = curReut;
					topRetrieval = currentRetrieval;
				}
			}
		}
		
		if(topRetrieval != null) {
			if(topRetrieval.getEval() >= RETAIN_SIM_VAL) {
				this.oldCase =  topRetrieval.get_case();
			}
			else if (topRetrieval.getEval() <= MINIMUM_SIM_VAL) {
				action = MOVE.NEUTRAL;
			}
			
		}

		return action;
	}
	
	/**
	 * Checks if the given query contains the given MOVE in its "possibleMoves"
	 * 
	 * @param query
	 * @param move
	 * @return
	 */
	private boolean moveAvailable(CBRQuery query, MOVE move) {
		boolean res;
		
		res = (( GhostDescription )query.getDescription()).getPossibleMoves().contains(move);
		
		return res;
	}
	
	/**
	 * Selects a random move from the given query's "possibleMove" attribute
	 * @param query
	 * @return
	 */
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

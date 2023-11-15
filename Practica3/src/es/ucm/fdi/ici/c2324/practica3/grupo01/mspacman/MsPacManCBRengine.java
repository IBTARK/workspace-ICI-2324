package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import java.io.File;
import java.util.Collection;

import es.ucm.fdi.gaia.jcolibri.cbraplications.StandardCBRApplication;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
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
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.Average;
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.CustomPlainTextConnector;
import pacman.game.Constants.MOVE;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CustomPlainTextConnector connectorGeneric;
	CBRCaseBase caseBase;
	CBRCaseBase genericCaseBase; //TODO como inicializarla
	NNConfig simConfig;
	
	private Map<CBRCase, CBRCase> chosenReusedCaseMap; //Map of case to the case chosen to be reused
	
	
	final static String TEAM = "grupo01"; 
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2324/practica3/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;
	
	final static int NUM_NEIGHBORS = 5; //number of neighbors of the KNN
	final static double MOST_SIM_VAL = 0.5; 
	
	public MsPacManCBRengine(MsPacManStorageManager storageManager)
	{
		this.storageManager = storageManager;
		chosenReusedCaseMap = new HashMap<>();
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		connectorGeneric = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();
		genericCaseBase = new CachedLinearCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		connectorGeneric.setCaseBaseFile(CASE_BASE_PATH, "generic.csv");
		
		this.storageManager.setCaseBase(caseBase);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new SimPacman());
		simConfig.addMapping(new Attribute("score",MsPacManDescription.class), new Interval(15000));
		simConfig.addMapping(new Attribute("lives", MsPacManDescription.class), new Interval(2));
		simConfig.addMapping(new Attribute("time",MsPacManDescription.class), new Interval(4000));
		simConfig.addMapping(new Attribute("upVector",MsPacManDescription.class), new SimPacmanVector());
		simConfig.addMapping(new Attribute("downVector",MsPacManDescription.class), new SimPacmanVector());
		simConfig.addMapping(new Attribute("leftVector",MsPacManDescription.class), new SimPacmanVector());
		simConfig.addMapping(new Attribute("rightVector",MsPacManDescription.class), new SimPacmanVector());
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		Collection<RetrievalResult> eval;
		ArrayList<RetrievalResult> neighbors = null;
		
		if(!caseBase.getCases().isEmpty()) {
			//Compute retrieve from the case base 
			eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			//NUM_NEIGHBORS-NN
			neighbors = new ArrayList<>(SelectCases.selectTopKRR(eval, NUM_NEIGHBORS));
			
		}
		
		boolean fromGeneric = false;
		
		//The generic case base has to be used
		if(caseBase.getCases().isEmpty() || neighbors.get(0).getEval() < MOST_SIM_VAL) {
			//Compute retrieve
			eval = NNScoringMethod.evaluateSimilarity(genericCaseBase.getCases(), query, simConfig);
			//NUM_NEIGHBORS-NN
			neighbors = new ArrayList<>(SelectCases.selectTopKRR(eval, NUM_NEIGHBORS));
			
			fromGeneric = true;
		}
	
		
		CBRCase newCase = createNewCase(query);
		
		//Compute reuse
		this.action = reuse(neighbors, newCase, fromGeneric);
	
		
		//Compute revise & retain
		this.storageManager.reviseAndRetain(newCase, chosenReusedCaseMap);
	}

	
	private MOVE reuse(ArrayList<RetrievalResult> neighbors, CBRCase reuseCase, boolean fromGeneric)
	{
		double maxReutVal = -1;
		int reutCase = -1;
		
		for(int i = 0; i < NUM_NEIGHBORS; i++) {
			RetrievalResult ret = neighbors.get(i);
			MsPacManResult result = (MsPacManResult) ret.get_case().getResult();
			double reutVal = ret.getEval() * Math.sqrt(result.getNumReps()) * result.getScore();
			if(reutVal > maxReutVal) {
				maxReutVal = reutVal;
				reutCase = i;
			}
		}
		
		if(!fromGeneric)chosenReusedCaseMap.put(reuseCase,neighbors.get(reutCase).get_case());
		else chosenReusedCaseMap.put(reuseCase,null);
		
		CBRCase chosenCase = neighbors.get(reutCase).get_case();
		MsPacManSolution solution = (MsPacManSolution) chosenCase.getSolution();
		
		return solution.getAction();
		
	}

	/**
	 * Creates a new case using the query as description, 
	 * storing the action into the solution and 
	 * setting the proper id number
	 */
	private CBRCase createNewCase(CBRQuery query) {
		CBRCase newCase = new CBRCase();
		MsPacManDescription newDescription = (MsPacManDescription) query.getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
		int newId = this.caseBase.getCases().size();
		newId+= storageManager.getPendingCases();
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
		this.caseBase.close();
	}

}

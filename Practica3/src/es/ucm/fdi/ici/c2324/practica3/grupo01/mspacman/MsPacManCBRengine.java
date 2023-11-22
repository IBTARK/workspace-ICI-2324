package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.CustomPlainTextConnector;
import pacman.game.Constants.MOVE;

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CustomPlainTextConnector connectorGeneric;
	CachedLinearCaseBase caseBase;
	CBRCaseBase genericCaseBase;
	NNConfig simConfig;
	
	private Map<CBRCase, CBRCase> chosenReusedCaseMap; //Map of case to the case chosen to be reused
	
	
	final static String TEAM = "grupo01"; 
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2324/practica3/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;
	
	final static int NUM_NEIGHBORS = 10; //number of neighbors of the KNN
	final static double SIM_TH = 0.5; 
	public static final double SCORE_TH = 10 * 500; //Threshold for the product score * finalScore
	
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
		connectorGeneric.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
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
		genericCaseBase.init(connectorGeneric);
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
		if(caseBase.getCases().isEmpty() || neighbors.get(0).getEval() < SIM_TH) {
			//Compute retrieve
			eval = NNScoringMethod.evaluateSimilarity(genericCaseBase.getCases(), query, simConfig);
			//NUM_NEIGHBORS-NN
			neighbors = new ArrayList<>(SelectCases.selectTopKRR(eval, NUM_NEIGHBORS));
			
			fromGeneric = true;
		}
	
		
		CBRCase newCase = createNewCase(query);
		
		//Compute reuse
		this.action = reuse(neighbors, newCase, fromGeneric);
		//Asign the final action
		((MsPacManSolution)newCase.getSolution()).setAction(action);
		
		//Compute revise & retain
		this.storageManager.reviseAndRetain(newCase, chosenReusedCaseMap);
	}

	
	private MOVE reuse(ArrayList<RetrievalResult> neighbors, CBRCase reuseCase, boolean fromGeneric) {
		double maxReutVal = -1;
		int reutCase = new Random().nextInt(NUM_NEIGHBORS);
		chosenReusedCaseMap.put(reuseCase,null);
		//System.out.println(neighbors.get(0).getEval());
		if(neighbors.get(0).getEval() < SIM_TH)
			return randomMove();
		
		for(int i = 0; i < neighbors.size(); i++) {
			RetrievalResult ret = neighbors.get(i);
			MsPacManResult result = (MsPacManResult) ret.get_case().getResult();
			
			//The case with best short-term and final score will be reused
			double reutVal = result.getScore() * result.getFinalScore();
			if(reutVal > maxReutVal) {
				maxReutVal = reutVal;
				reutCase = i;
			}
		}
		
		if (maxReutVal < SCORE_TH)
			return randomMove();
		
		//if(!fromGeneric) chosenReusedCaseMap.put(reuseCase,neighbors.get(reutCase).get_case());
		
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
		int newId = this.caseBase.getNextId();
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
		this.genericCaseBase.close();
	}

	public void setReward(int score) {
		this.caseBase.setReward(score);
	}
	
	private MOVE randomMove() {
		int index = (int)Math.floor(Math.random()*4);
		if(MOVE.values()[index]==action.opposite()) 
			index= (index+1)%4;
		action = MOVE.values()[index];
		return action;
	}
}

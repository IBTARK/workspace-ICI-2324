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

public class MsPacManCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	private MsPacManStorageManager storageManager;

	CustomPlainTextConnector connector;
	CBRCaseBase caseBase;
	NNConfig simConfig;
	
	
	final static String TEAM = "grupo01"; 
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2324/practica3/"+TEAM+"/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"mspacman"+File.separator;
	
	final static int NUM_NEIGHBORS = 5; //number of neighbors of the KNN
	final static double MOST_SIM_VAL = 0.5; 
	
	public MsPacManCBRengine(MsPacManStorageManager storageManager)
	{
		this.storageManager = storageManager;
	}
	
	public void setOpponent(String opponent) {
		this.opponent = opponent;
	}
	
	@Override
	public void configure() throws ExecutionException {
		connector = new CustomPlainTextConnector();
		caseBase = new CachedLinearCaseBase();
		
		connector.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connector.setCaseBaseFile(CASE_BASE_PATH, opponent+".csv");
		
		this.storageManager.setCaseBase(caseBase);
		
		simConfig = new NNConfig();
		simConfig.setDescriptionSimFunction(new Average());
		simConfig.addMapping(new Attribute("score",MsPacManDescription.class), new Interval(15000));
		simConfig.addMapping(new Attribute("time",MsPacManDescription.class), new Interval(4000));
		simConfig.addMapping(new Attribute("upVector",MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("downVector",MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("leftVector",MsPacManDescription.class), new Interval(650));
		simConfig.addMapping(new Attribute("rightVector",MsPacManDescription.class), new Interval(650));
	}

	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBase.init(connector);
		return caseBase;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		if(caseBase.getCases().isEmpty()) {
			this.action = MOVE.NEUTRAL;
		}
		else {
			//Compute retrieve
			Collection<RetrievalResult> eval = NNScoringMethod.evaluateSimilarity(caseBase.getCases(), query, simConfig);
			//Compute reuse
			this.action = reuse(eval);
		}
		
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);
		
	}

	
	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		//NUM_NEIGHBORS-NN
		ArrayList<RetrievalResult> neighbors = new ArrayList<>(SelectCases.selectTopKRR(eval, NUM_NEIGHBORS));
	
		//If the similarity of the most similar case is lower then MOST_SIM_VAL the solution is taken from the generic storage
		if(neighbors.get(0).getEval() < MOST_SIM_VAL) return MOVE.NEUTRAL;//TODO coger de la genérica
		else {
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
			
			CBRCase chosenCase = neighbors.get(reutCase).get_case();
			MsPacManSolution solution = (MsPacManSolution) chosenCase.getSolution();
			
			return solution.getAction();
		}
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

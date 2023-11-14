package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

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
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.DistanceInterval;
import pacman.game.Constants.MOVE;

public class GhostCBRengine implements StandardCBRApplication {

	private String opponent;
	private MOVE action;
	
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
	CBRCaseBase caseBaseEdible;
	CBRCaseBase caseBaseChasing;
	
	CBRCaseBase generalCaseBaseEdible;
	CBRCaseBase generalCaseBaseChasing;
	CustomPlainTextConnector generalConnectorEdible;
	CustomPlainTextConnector generalConnectorChasing;

	NNConfig simConfigEdible;
	NNConfig simConfigChasing;
	
	
	final static String TEAM = "grupo01";
	
	
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2324/practica3/"+TEAM+"/ghosts/plaintextconfig.xml";
	final static String  CASE_BASE_EDIBLE_PATH = "cbrdata"+File.separator+TEAM+File.separator+"ghosts"+File.separator+"edible"+File.separator;
	final static String  CASE_BASE_CHASING_PATH = "cbrdata"+File.separator+TEAM+File.separator+"ghosts"+File.separator+"chasing"+File.separator;
	
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
		
		connectorEdible.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		connectorChasing.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connectorEdible.setCaseBaseFile(CASE_BASE_EDIBLE_PATH, opponent+".csv");
		connectorChasing.setCaseBaseFile(CASE_BASE_CHASING_PATH, opponent+".csv");
		
		generalConnectorEdible.setCaseBaseFile(CASE_BASE_EDIBLE_PATH, "general_edible.csv");
		generalConnectorChasing.setCaseBaseFile(CASE_BASE_CHASING_PATH, "general_chasing.csv");
		
		this.storageManager.setCaseBaseEdible(caseBaseEdible);
		this.storageManager.setCaseBaseChasing(caseBaseChasing);
		
		/*
		 * HACER 2 SIMCONFIG , UNA EDIBLE OTRA NO Y A LA HORA DE EVALUAR (EN cycle) SE ELIGE LA SIMCONFIg
		 */
		// TODO investigar como hacer mapping para vectores. En caso de no poder hacer cada uno de los elementos del vector new Attribute por separado
		// Estilo: UP_mspacman, UP_nearestEdible, etc.
		// Solucionado: hacemos atributos para cada uno de los elementos, como indicado en la linea previa.
		
		// lives, time, edibleTime (SOLO EDIBLE WEIGTHS), mspacmanToPPill y vectors (total de vectores, se divide para 4).
		double edibleWeigths[] = { 0.05, 0.05, 0.05, 0.05, 0.8/4};
		double chasingWeigths[] = { 0.05, 0.05, 0, 0.05, 0.85/4 };
		// mspacman, nearestEdible, nearestEdibleTime, nearestChasing
		double vectorWeigths[] = { 0.40, 0.25, 0.1, 0.25 };
		
		// Atributos que van a ser utilizados en ambas simConfigs (menos edibleTime)
		Attribute mspacmanLives = 			new Attribute("mspacmanLives",GhostDescription.class);
		Attribute time = 					new Attribute("time",GhostDescription.class);
		Attribute edibleTime = 				new Attribute("edibleTime",GhostDescription.class);
		Attribute mspacmanToPPill = 		new Attribute("mspacmanToPPill",GhostDescription.class);
		Attribute UP_mspacman = 			new Attribute("UP_mspacman",GhostDescription.class);
		Attribute UP_nearestEdible = 		new Attribute("UP_nearestEdible",GhostDescription.class);
		Attribute UP_nearestEdibleTime = 	new Attribute("UP_nearestEdibleTime",GhostDescription.class);
		Attribute UP_nearestChasing = 		new Attribute("UP_nearestChasing",GhostDescription.class);
		Attribute RIGHT_mspacman = 			new Attribute("RIGHT_mspacman",GhostDescription.class);
		Attribute RIGHT_nearestEdible = 	new Attribute("RIGHT_nearestEdible",GhostDescription.class);
		Attribute RIGHT_nearestEdibleTime = new Attribute("RIGHT_nearestEdibleTime",GhostDescription.class);
		Attribute RIGHT_nearestChasing = 	new Attribute("RIGHT_nearestChasing",GhostDescription.class);
		Attribute DOWN_mspacman = 			new Attribute("DOWN_mspacman",GhostDescription.class);
		Attribute DOWN_nearestEdible = 		new Attribute("DOWN_nearestEdible",GhostDescription.class);
		Attribute DOWN_nearestEdibleTime = 	new Attribute("DOWN_nearestEdibleTime",GhostDescription.class);
		Attribute DOWN_nearestChasing = 	new Attribute("DOWN_nearestChasing",GhostDescription.class);
		Attribute LEFT_mspacman = 			new Attribute("LEFT_mspacman",GhostDescription.class);
		Attribute LEFT_nearestEdible = 		new Attribute("LEFT_nearestEdible",GhostDescription.class);
		Attribute LEFT_nearestEdibleTime = 	new Attribute("LEFT_nearestEdibleTime",GhostDescription.class);
		Attribute LEFT_nearestChasing = 	new Attribute("LEFT_nearestChasing",GhostDescription.class);
		
		simConfigEdible = new NNConfig();
		simConfigEdible.setDescriptionSimFunction(new Average());
		simConfigEdible.addMapping(mspacmanLives, new Interval(4));
			simConfigEdible.setWeight(mspacmanLives, edibleWeigths[0]);
		simConfigEdible.addMapping(time, new Interval(4000));
			simConfigEdible.setWeight(time, edibleWeigths[1]);
		simConfigEdible.addMapping(edibleTime,new Interval(200));
			simConfigEdible.setWeight(edibleTime, edibleWeigths[2]);		
		simConfigEdible.addMapping(mspacmanToPPill, new DistanceInterval(300));
			simConfigEdible.setWeight(mspacmanToPPill, edibleWeigths[3]);
		
		simConfigEdible.addMapping(UP_mspacman, new DistanceInterval(300));
			simConfigEdible.setWeight(UP_mspacman, edibleWeigths[4]*vectorWeigths[0]);
		simConfigEdible.addMapping(UP_nearestEdible, new DistanceInterval(300));
			simConfigEdible.setWeight(UP_nearestEdible, edibleWeigths[4]*vectorWeigths[1]);
		simConfigEdible.addMapping(UP_nearestEdibleTime, new DistanceInterval(200));
			simConfigEdible.setWeight(UP_nearestEdibleTime, edibleWeigths[4]*vectorWeigths[2]);
		simConfigEdible.addMapping(UP_nearestChasing, new DistanceInterval(300));
			simConfigEdible.setWeight(UP_nearestChasing, edibleWeigths[4]*vectorWeigths[3]);
		
		simConfigEdible.addMapping(RIGHT_mspacman, new DistanceInterval(300));
			simConfigEdible.setWeight(RIGHT_mspacman, edibleWeigths[4]*vectorWeigths[0]);
		simConfigEdible.addMapping(RIGHT_nearestEdible, new DistanceInterval(300));
			simConfigEdible.setWeight(RIGHT_nearestEdible, edibleWeigths[4]*vectorWeigths[1]);
		simConfigEdible.addMapping(RIGHT_nearestEdibleTime, new DistanceInterval(200));
			simConfigEdible.setWeight(RIGHT_nearestEdibleTime, edibleWeigths[4]*vectorWeigths[2]);
		simConfigEdible.addMapping(RIGHT_nearestChasing, new DistanceInterval(300));
			simConfigEdible.setWeight(RIGHT_nearestChasing, edibleWeigths[4]*vectorWeigths[3]);
		
		simConfigEdible.addMapping(DOWN_mspacman, new DistanceInterval(300));
			simConfigEdible.setWeight(DOWN_mspacman, edibleWeigths[4]*vectorWeigths[0]);
		simConfigEdible.addMapping(DOWN_nearestEdible, new DistanceInterval(300));
			simConfigEdible.setWeight(DOWN_nearestEdible, edibleWeigths[4]*vectorWeigths[1]);
		simConfigEdible.addMapping(DOWN_nearestEdibleTime, new DistanceInterval(200));
			simConfigEdible.setWeight(DOWN_nearestEdibleTime, edibleWeigths[4]*vectorWeigths[2]);
		simConfigEdible.addMapping(DOWN_nearestChasing, new DistanceInterval(300));
			simConfigEdible.setWeight(DOWN_nearestChasing, edibleWeigths[4]*vectorWeigths[3]);
		
		simConfigEdible.addMapping(LEFT_mspacman, new DistanceInterval(300));
			simConfigEdible.setWeight(LEFT_mspacman, edibleWeigths[4]*vectorWeigths[0]);
		simConfigEdible.addMapping(LEFT_nearestEdible, new DistanceInterval(300));
			simConfigEdible.setWeight(LEFT_nearestEdible, edibleWeigths[4]*vectorWeigths[1]);
		simConfigEdible.addMapping(LEFT_nearestEdibleTime, new DistanceInterval(200));
			simConfigEdible.setWeight(LEFT_nearestEdibleTime, edibleWeigths[4]*vectorWeigths[2]);
		simConfigEdible.addMapping(LEFT_nearestChasing, new DistanceInterval(300));
			simConfigEdible.setWeight(LEFT_nearestChasing, edibleWeigths[4]*vectorWeigths[3]);
		
		
		simConfigChasing = new NNConfig();
		simConfigChasing.setDescriptionSimFunction(new Average());
		simConfigChasing.addMapping(mspacmanLives, new Interval(4));
			simConfigChasing.setWeight(mspacmanLives, chasingWeigths[0]);
		simConfigChasing.addMapping(time, new Interval(4000));
			simConfigChasing.setWeight(time, chasingWeigths[1]);
		//simConfigChasing.addMapping(edibleTime,new Interval(200));
			//simConfigChasing.setWeight(edibleTime, chasingWeigths[2]);		
		simConfigChasing.addMapping(mspacmanToPPill, new DistanceInterval(300));
			simConfigChasing.setWeight(mspacmanToPPill, chasingWeigths[3]);
		
		simConfigChasing.addMapping(UP_mspacman, new DistanceInterval(300));
			simConfigChasing.setWeight(UP_mspacman, chasingWeigths[4]*vectorWeigths[0]);
		simConfigChasing.addMapping(UP_nearestEdible, new DistanceInterval(300));
			simConfigChasing.setWeight(UP_nearestEdible, chasingWeigths[4]*vectorWeigths[1]);
		simConfigChasing.addMapping(UP_nearestEdibleTime, new DistanceInterval(200));
			simConfigChasing.setWeight(UP_nearestEdibleTime, chasingWeigths[4]*vectorWeigths[2]);
		simConfigChasing.addMapping(UP_nearestChasing, new DistanceInterval(300));
			simConfigChasing.setWeight(UP_nearestChasing, chasingWeigths[4]*vectorWeigths[3]);
		
		simConfigChasing.addMapping(RIGHT_mspacman, new DistanceInterval(300));
			simConfigChasing.setWeight(RIGHT_mspacman, chasingWeigths[4]*vectorWeigths[0]);
		simConfigChasing.addMapping(RIGHT_nearestEdible, new DistanceInterval(300));
			simConfigChasing.setWeight(RIGHT_nearestEdible, chasingWeigths[4]*vectorWeigths[1]);
		simConfigChasing.addMapping(RIGHT_nearestEdibleTime, new DistanceInterval(200));
			simConfigChasing.setWeight(RIGHT_nearestEdibleTime, chasingWeigths[4]*vectorWeigths[2]);
		simConfigChasing.addMapping(RIGHT_nearestChasing, new DistanceInterval(300));
			simConfigChasing.setWeight(RIGHT_nearestChasing, chasingWeigths[4]*vectorWeigths[3]);
		
		simConfigChasing.addMapping(DOWN_mspacman, new DistanceInterval(300));
			simConfigChasing.setWeight(DOWN_mspacman, chasingWeigths[4]*vectorWeigths[0]);
		simConfigChasing.addMapping(DOWN_nearestEdible, new DistanceInterval(300));
			simConfigChasing.setWeight(DOWN_nearestEdible, chasingWeigths[4]*vectorWeigths[1]);
		simConfigChasing.addMapping(DOWN_nearestEdibleTime, new DistanceInterval(200));
			simConfigChasing.setWeight(DOWN_nearestEdibleTime, chasingWeigths[4]*vectorWeigths[2]);
		simConfigChasing.addMapping(DOWN_nearestChasing, new DistanceInterval(300));
			simConfigChasing.setWeight(DOWN_nearestChasing, chasingWeigths[4]*vectorWeigths[3]);
		
		simConfigChasing.addMapping(LEFT_mspacman, new DistanceInterval(300));
			simConfigChasing.setWeight(LEFT_mspacman, chasingWeigths[4]*vectorWeigths[0]);
		simConfigChasing.addMapping(LEFT_nearestEdible, new DistanceInterval(300));
			simConfigChasing.setWeight(LEFT_nearestEdible, chasingWeigths[4]*vectorWeigths[1]);
		simConfigChasing.addMapping(LEFT_nearestEdibleTime, new DistanceInterval(200));
			simConfigChasing.setWeight(LEFT_nearestEdibleTime, chasingWeigths[4]*vectorWeigths[2]);
		simConfigChasing.addMapping(LEFT_nearestChasing, new DistanceInterval(300));
			simConfigChasing.setWeight(LEFT_nearestChasing, chasingWeigths[4]*vectorWeigths[3]);
	}


	@Override
	public CBRCaseBase preCycle() throws ExecutionException {
		caseBaseEdible.init(connectorEdible);
		caseBaseChasing.init(connectorChasing);
		generalCaseBaseEdible.init(generalConnectorEdible);
		generalCaseBaseChasing.init(generalConnectorChasing);
		// Por que se retorna si desde Ghosts no se hace nada con el CaseBase???
		return caseBaseEdible;
	}

	@Override
	public void cycle(CBRQuery query) throws ExecutionException {
		
		boolean edible = ((GhostDescription) query.getDescription()).getEdible();
		// HACER UN IF DONDE VEAMOS SI ES EDIBLE.
		// ELEGIR DE LA BASE DE CASOS Y ASIGNAR EL SIMCONFIG CORRESPONDIENTE
		
		computeRetrieveAndReuseOpponent(query, edible);
		
		if(this.action == MOVE.NEUTRAL)
			computeRetrieveAndReuseGeneral(query, edible);
		
		if(this.action == MOVE.NEUTRAL)
			this.action = getRandomMove();
		//Compute revise & retain
		CBRCase newCase = createNewCase(query);
		this.storageManager.reviseAndRetain(newCase);
		
	}

	private void computeRetrieveAndReuseGeneral(CBRQuery query, boolean edible) {
		
		Collection<RetrievalResult> eval;
		if(edible && generalCaseBaseEdible.getCases().isEmpty()
				|| !edible && generalCaseBaseChasing.getCases().isEmpty()) {
			this.action = MOVE.NEUTRAL;
		}
		else {
			// Comprobamos la evaluacion en la base de casos del oponente
			eval = getGeneralCaseBaseEval(query, edible);
			RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
			double similarity = first.getEval();
			
			
			if(similarity < 0.7) 
				this.action = MOVE.NEUTRAL;
			else
				this.action = reuse(eval);
		}
	}

	private void computeRetrieveAndReuseOpponent(CBRQuery query, boolean edible) {
		Collection<RetrievalResult> eval;
		if(edible && caseBaseEdible.getCases().isEmpty()
				|| !edible && caseBaseChasing.getCases().isEmpty()) {
			this.action = MOVE.NEUTRAL;
		}
		else {
			// Comprobamos la evaluacion en la base de casos del oponente
			eval = getOpponentCaseBaseEval(query, edible);
			RetrievalResult first = SelectCases.selectTopKRR(eval, 1).iterator().next();
			double similarity = first.getEval();
			
			if(similarity < 0.5) 
				this.action = MOVE.NEUTRAL;
			else
				this.action = reuse(eval);
		}
		
	}

	private Collection<RetrievalResult> getOpponentCaseBaseEval(CBRQuery query, boolean edible) {
		Collection<RetrievalResult> eval;
		
		if(edible)
			eval = NNScoringMethod.evaluateSimilarity(caseBaseEdible.getCases(), query, simConfigEdible);
		else
			eval = NNScoringMethod.evaluateSimilarity(caseBaseChasing.getCases(), query, simConfigChasing);
		
		return eval;
	}

	private Collection<RetrievalResult> getGeneralCaseBaseEval(CBRQuery query, boolean edible) {
		Collection<RetrievalResult> eval;
		
		if(edible)
			eval = NNScoringMethod.evaluateSimilarity(generalCaseBaseEdible.getCases(), query, simConfigEdible);
		else
			eval = NNScoringMethod.evaluateSimilarity(generalCaseBaseChasing.getCases(), query, simConfigChasing);
		
		return eval;
	}

	private MOVE reuse(Collection<RetrievalResult> eval)
	{
		// TODO aplicar el documento de dise�o al reuse.
		
		// This simple implementation only uses 1NN
		// Consider using kNNs with majority voting
		Iterator<RetrievalResult> topCases = SelectCases.selectTopKRR(eval, 5).iterator();
		RetrievalResult topRetrieval = topCases.next();
		CBRCase currentCase = topRetrieval.get_case();
		double similarity = topRetrieval.getEval();
		if(similarity < 0.5) 
			return MOVE.NEUTRAL;
		
		
		GhostResult result = (GhostResult) currentCase.getResult();
		GhostSolution solution = (GhostSolution) currentCase.getSolution();
		double minReut = similarity * Math.sqrt(result.getNumReps()) / result.getScore();
		double curReut;
		RetrievalResult currentRetrieval;
				
		// Con este bucle calculamos el caso con MENOR "reut" y lo seleccionamos como topRetrieval para luego elegir su movimiento.
		while(topCases.hasNext()) {
			currentRetrieval = topCases.next();
			result = (GhostResult) currentRetrieval.get_case().getResult();
			curReut = currentRetrieval.getEval() * Math.sqrt(result.getNumReps()) / result.getScore();
			
			if(curReut < minReut) {
				minReut = curReut;
				topRetrieval = currentRetrieval;
			}
		}
		
		// Al salir, en topRetrieval tenemos el caso con MENOR "reut", por lo que vemos su similitud y su solution.
		solution = (GhostSolution) topRetrieval.get_case().getSolution();
		similarity = topRetrieval.getEval();
		
		if(similarity >= 0.9) {
			// EN VEZ DE GUARDAR EL CASO HAY QUE AUMENTAR EL CONTADOR.
			// Lo podriamos (?) poner antes del bucle anterior, como "else if" del "if(similarity < 0.5)".
			
			// Proximamente: en vez de devolver el action, asignar a this.action el movimiento de la solucion, y devolver un boolean ... -
			// ... diciendo true si queremos mandarlo a reviseAndRetain o false si queremos modificar el topRetrieval, aumentando su contador
		}

		MOVE action = solution.getAction();
		return action;
	}
	
	private MOVE getRandomMove() {
		int index = (int)Math.floor(Math.random()*4);
		if(MOVE.values()[index]==action) 
			index= (index+1)%4;
		return MOVE.values()[index];
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
			newId = this.caseBaseEdible.getCases().size();
			newId+= storageManager.getPendingEdibleCases();
		}
		else {
			newId = this.caseBaseChasing.getCases().size();
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

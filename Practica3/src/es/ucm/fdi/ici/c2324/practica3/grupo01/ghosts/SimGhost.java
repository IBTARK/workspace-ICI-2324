package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import pacman.game.Constants.MOVE;

public class SimGhost implements GlobalSimilarityFunction {

	private static final double EDIBLE_VECTORSWEIGHT = 0.85;
	private static final double CHASING_VECTORSWEIGHT = 0.9;
	private static final double LIVESWEIGHT = 0.05;
	private static final double TIMEWEIGHT = 0.05;
	private static final double EDIBLETIMEWEIGHT = 0.05;

	private static final int MAX_TIME = 4000;
	private static final int MAX_EDIBLE_TIME = 200;
	private static final int MAX_LIVES = 2;
	
	@Override
	public double compute(CaseComponent componentOfCase, CaseComponent componentOfQuery, CBRCase _case, CBRQuery _query,
			NNConfig numSimConfig) {
		
		GhostDescription ghostCase = (GhostDescription) componentOfCase;
		GhostDescription ghostQuery = (GhostDescription) componentOfQuery;
		
		boolean edible = ghostQuery.getEdible();
		
		//Similarity for the vectors
		SimGhostVector simVector = new SimGhostVector();
	
		double simVectors = 0;
		
		//Local similarity of the vectors   
		for(MOVE m : MOVE.values()) {
			simVectors +=  0.25 * simVector.compute(getVector(ghostCase, m), getVector(ghostQuery, m), _case, _query, numSimConfig);
		}
		
		//Local similarity of the remaining time
		double simTime = 0;
		try {
			simTime = new Interval(MAX_TIME).compute(ghostCase.getTime(), ghostQuery.getTime());
		} catch (NoApplicableSimilarityFunctionException e) {
			e.printStackTrace();
		}
		
		//Local similarity of the lives
		double simLives = 0;
		try {
			simLives = new Interval(MAX_LIVES).compute(ghostCase.getMspacmanLives(), ghostQuery.getMspacmanLives());
		} catch (NoApplicableSimilarityFunctionException e) {
			e.printStackTrace();
		}
		
		double simEdibleTime = 0;
		if(edible) {
			try {
				simEdibleTime = new Interval(MAX_EDIBLE_TIME).compute(ghostCase.getEdibleTime(), ghostQuery.getEdibleTime());
			} catch (NoApplicableSimilarityFunctionException e) {
				e.printStackTrace();
			}
		}
	
		//Global similarity
		double globalSim = 0;
		if(edible)
			globalSim = EDIBLE_VECTORSWEIGHT * simVectors + LIVESWEIGHT * simLives + TIMEWEIGHT * simTime + EDIBLETIMEWEIGHT * simEdibleTime;
		else
			globalSim = CHASING_VECTORSWEIGHT * simVectors + LIVESWEIGHT * simLives + TIMEWEIGHT * simTime;
		return globalSim;
	}

	private CaseComponent getVector(GhostDescription ghostCase, MOVE m) {
		if(m == MOVE.UP) return ghostCase.getUp();
		else if(m == MOVE.DOWN) return ghostCase.getDown();
		else if(m == MOVE.LEFT) return ghostCase.getLeft();
		else return ghostCase.getRight();
	}
}

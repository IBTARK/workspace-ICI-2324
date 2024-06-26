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


	private static final double EDIBLE_VECTORSWEIGHT = 0.70;
	private static final double CHASING_VECTORSWEIGHT = 0.75;
	private static final double TIMEWEIGHT = 0.10;
	private static final double EDIBLETIMEWEIGHT = 0.05;
	private static final double MSPACMANTOPPILL = 0.10;

	private static final int MAX_TIME = 200;
	private static final int MAX_DIST = 300;
	private static final int MAX_EDIBLE_TIME = 200;
	
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
			if(m != MOVE.NEUTRAL)
				simVectors +=  0.25 * simVector.compute(getVector(ghostCase, m), getVector(ghostQuery, m), _case, _query, numSimConfig);
		}
		
		//Local similarity of the remaining time
		double simTime = 0;
		try {
			simTime = new DistanceInterval(MAX_TIME).compute(ghostCase.getTime(), ghostQuery.getTime());
		} catch (NoApplicableSimilarityFunctionException e) {
			e.printStackTrace();
		}
		
		// Local similarity of the edible time (if we are edible)
		double simEdibleTime = 0;
		if(edible) {
			try {
				simEdibleTime = new DistanceInterval(MAX_EDIBLE_TIME).compute(ghostCase.getEdibleTime(), ghostQuery.getEdibleTime());
			} catch (NoApplicableSimilarityFunctionException e) {
				e.printStackTrace();
			}
		}
		
		// Local similarity of mspacman's distance to its nearest PPill
		double simMspacmanToPPill = 0;
		try {
			simMspacmanToPPill = new DistanceInterval(MAX_DIST).compute(ghostCase.getMspacmanToPPill(), ghostQuery.getMspacmanToPPill());
		} catch (NoApplicableSimilarityFunctionException e) {
			e.printStackTrace();
		}
	
		//Global similarity
		double globalSim = 0;
		if(edible)
			globalSim = MSPACMANTOPPILL * simMspacmanToPPill 
			+ EDIBLE_VECTORSWEIGHT * simVectors 
			+ TIMEWEIGHT * simTime 
			+ EDIBLETIMEWEIGHT * simEdibleTime;
		else
			globalSim = MSPACMANTOPPILL * simMspacmanToPPill 
			+ CHASING_VECTORSWEIGHT * simVectors 
			+ TIMEWEIGHT * simTime;
		return globalSim;
	}

	/**
	 * Returns the vector of the given GhostDescription and given the move
	 * 
	 * @param ghostCase
	 * @param m
	 * @return
	 */
	private CaseComponent getVector(GhostDescription ghostCase, MOVE m) {
		if(m == MOVE.UP) return ghostCase.getUpVector();
		else if(m == MOVE.DOWN) return ghostCase.getDownVector();
		else if(m == MOVE.LEFT) return ghostCase.getLeftVector();
		else return ghostCase.getRightVector();
	}
}

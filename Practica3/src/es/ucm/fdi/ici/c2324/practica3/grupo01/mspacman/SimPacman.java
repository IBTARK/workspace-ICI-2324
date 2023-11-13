package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import pacman.game.Constants.MOVE;

public class SimPacman implements GlobalSimilarityFunction {
	
	private static final double VECTORSWEIGHT = 0.9;
	private static final double LIVESWEIGHT = 0.05;
	private static final double TIMEWEIGHT = 0.05;

	private static final int MAX_TIME = 4000;
	private static final int MAX_LIVES = 2;
	
	@Override
	public double compute(CaseComponent componentOfCase, CaseComponent componentOfQuery, CBRCase _case, CBRQuery _query,
			NNConfig numSimConfig) {
		
		MsPacManDescription msPacManCase = (MsPacManDescription) componentOfCase;
		MsPacManDescription msPacManQuery = (MsPacManDescription) componentOfQuery;
		
		//Similarity for the vectors
		SimPacmanVector simVector = new SimPacmanVector();
	
		double simVectors = 0;
		
		//Local similarity of the vectors   
		for(MOVE m : MOVE.values()) {
			simVectors +=  0.25 * simVector.compute(msPacManCase.getVector(m), msPacManQuery.getVector(m), _case, _query, numSimConfig);
		}
		
		//Local similarity of the remaining time
		double simTime = 0;
		try {
			simTime = new Interval(MAX_TIME).compute(msPacManCase.getTime(), msPacManQuery.getTime());
		} catch (NoApplicableSimilarityFunctionException e) {
			e.printStackTrace();
		}
		
		//Local similarity of the lives
		double simLives = 0;
		try {
			simLives = new Interval(MAX_LIVES).compute(msPacManCase.getLives(), msPacManQuery.getLives());
		} catch (NoApplicableSimilarityFunctionException e) {
			e.printStackTrace();
		}
		
	
		//Global similarity
		return VECTORSWEIGHT * simVectors + LIVESWEIGHT * simLives + TIMEWEIGHT * simTime;
	}

}

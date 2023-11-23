package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import pacman.game.Constants.MOVE;

public class SimGhostVector implements GlobalSimilarityFunction {

	// The individual weights of the vector
	// (in order of the next indices)
	private static final double[] WEIGHTS = { 0.7 , 0.05, 0.05, 0.2 };
	
	// Indices of the parameters of the vector
	private static final int MSPACMAN_IDX = 0;
	private static final int EDIBLE_IDX = 1;
	private static final int EDIBLE_TIME_IDX = 2;
	private static final int CHASING_IDX = 3;

	private static final int MAX_DIST = 250;
	private static final int MAX_TIME_EDIBLE = 200;

	@Override
	public double compute(CaseComponent componentOfCase, CaseComponent componentOfQuery, CBRCase _case, CBRQuery _query,
			NNConfig numSimConfig) {
		GhostDistanceVector vecCase = (GhostDistanceVector) componentOfCase;
		GhostDistanceVector vecQuery = (GhostDistanceVector) componentOfQuery;
		
		//Check if the movement analyzed is valid for the case and the query
		boolean vecInCase = vecCase.getDistancias() != null,
				vecInQuery = vecQuery.getDistancias() != null;
		
		if (vecCase.getMove() == vecQuery.getMove() && !vecInCase && !vecInQuery)
			return 1;
		if (vecCase.getMove() != vecQuery.getMove() || !vecInCase || !vecInQuery)
			return 0;
		
		ArrayList<Integer> distCase = vecCase.getDistancias();
		ArrayList<Integer> distQuery= vecQuery.getDistancias();
		
		// Computes the similarity of each individual Attribute of the array given the weigths and the interval.
		double sim = 0;
		try {
			sim += WEIGHTS[MSPACMAN_IDX] * new DistanceInterval(MAX_DIST).compute(distCase.get(MSPACMAN_IDX), distQuery.get(MSPACMAN_IDX));
			sim += WEIGHTS[EDIBLE_IDX] * new DistanceInterval(MAX_DIST).compute(distCase.get(EDIBLE_IDX), distQuery.get(EDIBLE_IDX));
			sim += WEIGHTS[EDIBLE_TIME_IDX] * new DistanceInterval(MAX_TIME_EDIBLE).compute(distCase.get(EDIBLE_TIME_IDX), distQuery.get(EDIBLE_TIME_IDX));
			sim += WEIGHTS[CHASING_IDX] * new DistanceInterval(MAX_DIST).compute(distCase.get(CHASING_IDX), distQuery.get(CHASING_IDX));
		}
		catch (NoApplicableSimilarityFunctionException e) {
			e.printStackTrace();
		}
		return sim;
	}
}

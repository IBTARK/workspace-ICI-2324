package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;

/**
 * The similarity of a vector is a weighted mean of the 5 elements that compose the vector.
 */
public class SimPacmanVector implements GlobalSimilarityFunction {
	
	private static final double[] WEIGHTS = { 0.35, 0.35, 0.05, 0.2, 0.05 }; //Weight of each attribute
	
	private static final int CHASING_IDX = 0; //Index of the distance to the closest chasing ghost
	private static final int EDIBLE_IDX = 1; //Index of the distance to the closest edible ghost
	private static final int EDIBLE_TIME_IDX = 2; //Index of the remaining edible time of the closest ghost 
	private static final int PPILL_IDX = 3; //Index of the distance to the closest PPill
	private static final int PILL_IDX = 4; //Index of the distance to the closest Pill

	private static final int MAX_DIST = MsPacManInput.MAX_DIST;
	public static final int MAX_TIME_EDIBLE = 200;

	/**
	 * Computes the similarity of two vectors
	 */
	@Override
	public double compute(CaseComponent componentOfCase, CaseComponent componentOfQuery, CBRCase _case, CBRQuery _query,
			NNConfig numSimConfig) {
		MsPacManDescription.DistanceVector vecCase = (MsPacManDescription.DistanceVector) componentOfCase;
		MsPacManDescription.DistanceVector vecQuery = (MsPacManDescription.DistanceVector) componentOfQuery;
		
		//Check if the movement analyzed is valid for the case and the query
		boolean vecInCase = vecCase.getVector() != null,
				vecInQuery = vecQuery.getVector() != null;
		
		if (vecCase.getMove() == vecQuery.getMove() && !vecInCase && !vecInQuery)
			return 1;
		if (vecCase.getMove() != vecQuery.getMove() || !vecInCase || !vecInQuery)
			return 0;

		ArrayList<Integer> distCase = vecCase.getVector();
		ArrayList<Integer> distQuery= vecQuery.getVector();
		double sim = 0;
		try {
			sim += WEIGHTS[CHASING_IDX] * new Interval(MAX_DIST).compute(distCase.get(CHASING_IDX), distQuery.get(CHASING_IDX));
			sim += WEIGHTS[EDIBLE_IDX] * new Interval(MAX_DIST).compute(distCase.get(EDIBLE_IDX), distQuery.get(EDIBLE_IDX));
			sim += WEIGHTS[EDIBLE_TIME_IDX] * new Interval(MAX_TIME_EDIBLE).compute(distCase.get(EDIBLE_TIME_IDX), distQuery.get(EDIBLE_TIME_IDX));
			sim += WEIGHTS[PPILL_IDX] * new Interval(MAX_DIST).compute(distCase.get(PPILL_IDX), distQuery.get(PPILL_IDX));
			sim += WEIGHTS[PILL_IDX] * new Interval(MAX_DIST).compute(distCase.get(PILL_IDX), distQuery.get(PILL_IDX));
		}
		catch (NoApplicableSimilarityFunctionException e) {
			e.printStackTrace();
		}
		return sim;
	}
}

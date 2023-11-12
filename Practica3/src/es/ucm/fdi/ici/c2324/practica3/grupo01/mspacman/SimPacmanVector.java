package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

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

public class SimPacmanVector implements GlobalSimilarityFunction {
	
	private static final double[] WEIGHTS = { 0.35, 0.2, 0.15, 0.25, 0.05 };
	
	private static final int CHASING_IDX = 0;
	private static final int EDIBLE_IDX = 1;
	private static final int EDIBLE_TIME_IDX = 2;
	private static final int PPILL_IDX = 3;
	private static final int PILL_IDX = 4;

	private static final int MAX_DIST = 650;
	private static final int MAX_TIME_EDIBLE = 200;

	@Override
	public double compute(CaseComponent componentOfCase, CaseComponent componentOfQuery, CBRCase _case, CBRQuery _query,
			NNConfig numSimConfig) {
		MsPacManDescription.DistanceVector vecCase = (MsPacManDescription.DistanceVector) componentOfCase;
		MsPacManDescription.DistanceVector vecQuery = (MsPacManDescription.DistanceVector) componentOfQuery;
		
		boolean vecInCase = new ArrayList<MOVE>(Arrays.asList(((MsPacManDescription)_case.getDescription()).getPossibleMoves())).contains(vecCase.getMove()),
				vecInQuery = new ArrayList<MOVE>(Arrays.asList(((MsPacManDescription)_query.getDescription()).getPossibleMoves())).contains(vecCase.getMove());
		
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

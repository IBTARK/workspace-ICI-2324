package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * This function returns the similarity of two number inside an interval.
 * 
 * Returns 0 if one of the values is equal to -1 or greater than the interval and the other isn't.
 * 
 * sim(x,y)=1-(|x-y|/interval)
 * 
 * Now it works with Number values.
 */
public class DistanceInterval implements LocalSimilarityFunction {

	/** Interval */
	double _interval;

	/**
	 * Constructor.
	 */
	public DistanceInterval(double interval) {
		_interval = interval;
	}

	/**
	 * Applies the similarity function.
	 * 
	 * @param o1
	 *            Number
	 * @param o2
	 *            Number
	 * @return result of apply the similarity function.
	 */
	public double compute(Object o1, Object o2) throws NoApplicableSimilarityFunctionException {
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if (!(o2 instanceof Number))
			throw new NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());


		Number i1 = (Number) o1;
		Number i2 = (Number) o2;
		
		double v1 = i1.doubleValue();
		double v2 = i2.doubleValue();
		
		// If ONLY ONE of the values is out of the range (interval, -1) returns 0, otherwise computes the equation.
		if(((v1 >= _interval || v1 <= -1) && !(v2 >= _interval || v2 <= -1))
				|| ((v2 >= _interval || v2 <= -1) && !(v1 >= _interval || v1 <= -1)))
			return 0;
		
		return 1 - ((double) Math.abs(v1 - v2) / _interval);
	}
	
	/** Applicable to Integer */
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof Number;
		else if(o2==null)
			return o1 instanceof Number;
		else
			return (o1 instanceof Number)&&(o2 instanceof Number);
	}

}

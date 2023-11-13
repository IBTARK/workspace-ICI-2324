package es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine;

import es.ucm.fdi.gaia.jcolibri.exception.NoApplicableSimilarityFunctionException;
import es.ucm.fdi.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

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
		
		// In case the distance is non existant (put with a -1 or a MAX_VALUE)
		// It would be safer to put vX>= _interval to make sure we don't exceed the range, but the _interval should be precise
		// As we are working with non precise intervals, we check with MAX_VALUE
		if((v1<=-1 || v1>=Integer.MAX_VALUE) && !(v2<=-1 || v2>=Integer.MAX_VALUE))
			return 0;
		else if(!(v1<=-1 || v1>=Integer.MAX_VALUE) && (v2<=-1 || v2>=Integer.MAX_VALUE))
			return 0;
		
		double res = 1 - ((double) Math.abs(v1 - v2) / _interval);
		
		return res < 0 ? 0 : res;
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

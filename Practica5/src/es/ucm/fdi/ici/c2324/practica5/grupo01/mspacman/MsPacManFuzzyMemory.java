package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.HashMap;

public class MsPacManFuzzyMemory {
	HashMap<String,Double> mem;
	MsPacManFuzzyData data;
	double[] confidence = {100,100,100,100};

	
	public MsPacManFuzzyMemory(MsPacManFuzzyData data) {
		mem = new HashMap<String,Double>();
		this.data = data;
	}
	
	public void getInput(MsPacManInput input)
	{
		if (input.getNearestChasing() != null) data.setNearestChasing(input.getNearestChasing());
		if (input.getNearestChasingLastMove() != null) data.setNearestChasingLastMove(input.getNearestChasingLastMove());
		if (input.getNearestEdible() != null) data.setNearestEdible(input.getNearestEdible());
		if (input.getNearestPPill() != null) data.setNearestPPill(input.getNearestPPill());
		if (input.getNearestEdibleNextJunction() != null) data.setNearestEdibleNextJunction(input.getNearestEdibleNextJunction());
		
		//TODO confianzas
	}
	
	public HashMap<String, Double> getFuzzyValues() {
		return mem;
	}
	
}

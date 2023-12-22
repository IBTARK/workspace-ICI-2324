package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.HashMap;

public class MsPacManFuzzyMemory {
	private static final Double INVISIBLE = Double.MAX_VALUE;
	
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
		
		//TODO logica de ppills
		//TODO confianzas
		
		mem.putAll(input.getFuzzyValues());
		if (mem.get("ppillDistance") == null)
			mem.put("ppillDistance", data.getPpillDistance());
		if (mem.get("nearestEdibleDist") == null)
			mem.put("nearestEdibleDist", data.getNearestEdibleDist());
		if (mem.get("nearestChasingDist") == null)
			mem.put("nearestChasingDist", data.getNearestChasingDist());
		if (mem.get("nearestChasingDist2") == null)
			mem.put("nearestChasingDist2", data.getNearestChasingDist2());
		if (mem.get("nearestEdibleNextJunctionDist") == null)
			mem.put("nearestEdibleNextJunctionDist", data.getNearestEdibleNextJunctionDist());
		if (mem.get("distOfNearestEdibleToHisNextJunction") == null)
			mem.put("distOfNearestEdibleToHisNextJunction", data.getDistOfNearestEdibleToHisNextJunction());
	}
	
	public HashMap<String, Double> getFuzzyValues() {
		return mem;
	}
	
}

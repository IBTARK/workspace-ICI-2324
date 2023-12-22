package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.HashMap;

public class MsPacManFuzzyMemory {
	private static final Double INVISIBLE = Double.MAX_VALUE;
	
	HashMap<String,Double> mem;
	MsPacManFuzzyData data;
	Double nearestEdibleConfidence;
	Double nearestChasingConfidence;
	Double nearestChasing2Confidence;

	
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
		
		//logica de ppills
		if(input.getNearestPPill() != null && !data.getPpillsPos().contains(input.getNearestPPill()))
			data.getPpillsPos().add(input.getNearestPPill());
		
		if(input.wasPPillEaten()){
			data.getPpillsPos().remove((Object) input.getNearestPPill());
			int nearest = -1,minDist = Integer.MAX_VALUE;
			
			for(int ppill : data.getPpillsPos()){
				if(input.shortestPathDistance(input.getMsPacManCurrPos(), ppill, input.getMsPacManLastMove()) < minDist){
					minDist = input.shortestPathDistance(input.getMsPacManCurrPos(), ppill, input.getMsPacManLastMove());
					nearest = ppill;
				}
			}
			
			data.setNearestPPill(nearest);
			data.setPpillDistance((double) minDist);
			
		}
		
		
			
		mem.putAll(input.getFuzzyValues());
		if (mem.get("ppillDistance") == INVISIBLE)
			mem.put("ppillDistance", data.getPpillDistance());
		
		if (mem.get("nearestEdibleDist") == INVISIBLE) {
			mem.put("nearestEdibleDist", data.getNearestEdibleDist());
			nearestEdibleConfidence = Math.max(nearestEdibleConfidence-5, 0);
		} else nearestEdibleConfidence = 100.0;
		
		if (mem.get("nearestChasingDist") == INVISIBLE) {
			mem.put("nearestChasingDist", data.getNearestChasingDist());
			nearestChasingConfidence = Math.max(nearestChasingConfidence-5, 0);
		} else nearestChasingConfidence = 100.0;
		
		if (mem.get("nearestChasingDist2") == INVISIBLE) {
			mem.put("nearestChasingDist2", data.getNearestChasingDist2());
			nearestChasing2Confidence = Math.max(nearestChasing2Confidence-5, 0);
		} else nearestChasing2Confidence = 100.0;
		
		if (mem.get("nearestEdibleNextJunctionDist") == INVISIBLE)
			mem.put("nearestEdibleNextJunctionDist", data.getNearestEdibleNextJunctionDist());
		
		//Pills
			
		if(data.getNumOfPills() < 0)
			data.setNumOfPills((mem.get("numPills")));
		
		if(input.wasPillEaten())
			data.setNumOfPills(data.getNumOfPills() - 1);
		
		mem.put("numPills", data.getNumOfPills());
		
		
		mem.put("nearestEdibleConfidence", nearestEdibleConfidence);
		mem.put("nearestChasingConfidence", nearestChasingConfidence);
		mem.put("nearestChasing2Confidence", nearestChasing2Confidence);
	}
	
	public HashMap<String, Double> getFuzzyValues() {
		return mem;
	}
	
}

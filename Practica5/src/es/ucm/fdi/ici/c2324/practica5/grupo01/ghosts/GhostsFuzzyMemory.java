package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts;

import java.util.HashMap;

import pacman.game.Constants.GHOST;

public class GhostsFuzzyMemory {
	HashMap<String,Double> mem;
	
	double pacmanVisibilityConfidence = 100;

	
	public GhostsFuzzyMemory() {
		mem = new HashMap<String,Double>();
	}
	
	public void getInput(GhostsInput input)
	{
		
		if(input.isMsPacManVisible())
			pacmanVisibilityConfidence = 100;
		else
			pacmanVisibilityConfidence = Double.max(0, pacmanVisibilityConfidence-5);
		mem.put("pacmanVisibilityConfidence", pacmanVisibilityConfidence);			
	

	}
	
	public HashMap<String, Double> getFuzzyValues() {
		return mem;
	}
	
}

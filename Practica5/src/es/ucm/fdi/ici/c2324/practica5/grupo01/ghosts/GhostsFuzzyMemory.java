package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts;

import java.util.HashMap;

import pacman.game.Constants.GHOST;

public class GhostsFuzzyMemory {
	HashMap<String,Double> mem;
	
	double pacmanVisibilityConfidence = 0;	
	int ppillsRemaining = 4;
	int currentLevel = -1;
	int mspacman = -1;
	
	public GhostsFuzzyMemory() {
		mem = new HashMap<String,Double>();
	}
	
	public void getInput(GhostsInput input)
	{
		if(input.wasPPillEaten()) {
			--ppillsRemaining;
		}
		
		if(currentLevel != input.getCurrentLevel()) {
			currentLevel = input.getCurrentLevel();
			ppillsRemaining = 4;
		}
		
		if(input.isMsPacManVisible())
			pacmanVisibilityConfidence = 100;
		else
			pacmanVisibilityConfidence = Double.max(0, pacmanVisibilityConfidence-5);
		
		
		double anyPPills = ppillsRemaining > 0 ? 1 : 0;
		mem.put("PPILLS", anyPPills);
		mem.put("MSconfidence", pacmanVisibilityConfidence);			
	}
	
	public HashMap<String, Double> getFuzzyValues() {
		return mem;
	}
	
}

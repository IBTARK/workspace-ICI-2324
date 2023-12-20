package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.HashMap;

import pacman.game.Constants.GHOST;

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
		
	}
	
	public HashMap<String, Double> getFuzzyValues() {
		return mem;
	}
	
}

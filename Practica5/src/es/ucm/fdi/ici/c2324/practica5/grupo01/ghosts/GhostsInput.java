package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.ici.fuzzy.FuzzyInput;
import pacman.game.Game;

public class GhostsInput extends FuzzyInput {

	public GhostsInput(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public HashMap<String, Double> getFuzzyValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void parseInput() {
		// TODO Auto-generated method stub

	}

	public boolean isMsPacManVisible() {
		// TODO Auto-generated method stub
		return false;
	}

}

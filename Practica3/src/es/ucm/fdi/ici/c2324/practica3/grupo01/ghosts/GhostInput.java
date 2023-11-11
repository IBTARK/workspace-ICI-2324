package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRQuery;
import es.ucm.fdi.ici.cbr.CBRInput;
import pacman.game.Game;

public class GhostInput extends CBRInput {
	
	public GhostInput(Game game) {
		super(game);
		
	}
	
	Integer time;
	Integer lives; //Remaining lives of MsPacMan
	/*
	 List for each possible movement with the next information:
	 0: distance to the nearest chasing ghost
	 1: distance to the nearest edible ghost
	 2: remaining edible time of the nearest edible ghost
	 3: distance to the nearest PPill to mspacman
	 4: distance to mspacman
	*/
	ArrayList<Integer> up;  
	ArrayList<Integer> down;
	ArrayList<Integer> right;
	ArrayList<Integer> left;
	
	@Override
	public CBRQuery getQuery() {
		GhostDescription description = new GhostDescription();
		description.setTime(time);
		description.setLives(lives);
		description.setUpVector(up);
		description.setDownVector(down);
		description.setRightVector(right);
		description.setLeftVector(left);
		
		CBRQuery query = new CBRQuery();
		query.setDescription(description);
		return query;
	}

	@Override
	public void parseInput() {
		// TODO Auto-generated method stub

	}

}

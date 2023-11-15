package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;

import pacman.game.Constants.MOVE;

public class GhostDistanceVector implements es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor {

	private MOVE move;
	private ArrayList<Integer> distancias;
	
	public MOVE getMove() {
		return move;
	}

	public void setMove(MOVE move) {
		this.move = move;
	}

	public ArrayList<Integer> getDistancias() {
		return distancias;
	}

	public void setDistancias(ArrayList<Integer> distancias) {
		this.distancias = distancias;
	}

	public GhostDistanceVector(MOVE move) {
		this.move = move;
	}
	
	
	
	@Override
	public void fromString(String content) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		
		return new String();
	}
	
	@Override
	public boolean equals(Object o) {
		
		return true;
	}
}

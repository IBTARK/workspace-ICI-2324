package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;
import java.util.StringTokenizer;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class GhostDistanceVector implements es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor, CaseComponent {

	private MOVE move;
	private ArrayList<Integer> distancias;
	
	private static final String SEPARATOR = ";";
	
	public GhostDistanceVector() {
		distancias = new ArrayList<>();
	}
		
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
		StringTokenizer tknizer = new StringTokenizer(content, SEPARATOR, false);
		
		move = (MOVE) tknizer.nextElement();
		
		this.distancias = new ArrayList<>();
		for(int i = 0; i < 4; i++) {
			distancias.add((Integer)tknizer.nextElement());
		}
	}
	
	@Override
	public String toString() {
		String str =  move.toString();
		
		for(Integer elem : distancias) {
			str += SEPARATOR + elem.toString();
		}
		
		return str;
	}
	
	

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostDistanceVector.class);
	}
}

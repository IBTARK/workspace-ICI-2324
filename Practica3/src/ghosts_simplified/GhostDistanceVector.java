package ghosts_simplified;

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

	public GhostDistanceVector(GhostDistanceVector g) {
		try {
			this.fromString(g.toString());
		}
		catch (Exception e) {
			System.out.println("ERROR CREANDO GHOSTDISTANCEVECTOR");
			e.printStackTrace();
		}
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
		this.distancias = new ArrayList<Integer>(distancias);
	}

	public GhostDistanceVector(MOVE move) {
		this.move = move;
	}
	
	
	
	@Override
	public void fromString(String content) throws Exception {
		StringTokenizer tknizer = new StringTokenizer(content, SEPARATOR, false);
		
		move = MOVE.valueOf(tknizer.nextElement().toString());
		
		this.distancias = new ArrayList<>();
		if (!content.equals(move.toString() + SEPARATOR + "null"))
			for(int i = 0; i < 4; i++) {
				distancias.add(Integer.parseInt(tknizer.nextElement().toString()));
			}
		else
			this.distancias = null;
	}
	
	@Override
	public String toString() {
		String str =  move.toString();
		
		if(this.distancias==null) {
			str += SEPARATOR + "null";
		}
		else 
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

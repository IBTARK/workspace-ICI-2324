package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import java.util.ArrayList;
import java.util.StringTokenizer;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;
import es.ucm.fdi.gaia.jcolibri.connector.TypeAdaptor;

/**
 * The description used for MsPacMan is: distances vector.
 * For each movement there is a vector describing the situation if that movement is made.
 */
public class MsPacManDescription implements CaseComponent {

	private Integer id;
	
	private Integer time;
	private Integer lives; //Remaining lives of MsPacMan
	private Integer score;
	private MOVE[] possibleMoves;
	/*
	 List for each possible movement with the next information:
	 0: distance to the nearest chasing ghost
	 1: distance to the nearest edible ghost
	 2: remaining edible time of the nearest edible ghost
	 3: distance to the nearest PPill
	*/
	DistanceVector up = new DistanceVector();  
	DistanceVector down = new DistanceVector();
	DistanceVector right = new DistanceVector();
	DistanceVector left = new DistanceVector();
	
	public static final int NUM_ELEMS = 5; //Number of elements of each vector
	
	//Getters
	
	public Integer getId() {
		return id;
	}
	
	public Integer getTime() {
		return time;
	}
	
	public Integer getLives() {
		return lives;
	}
	
	public Integer getScore() {
		return score;
	}
	
	public DistanceVector getUpVector(){
		return up;
	}
	
	public DistanceVector getDownVector(){
		return down;
	}

	public DistanceVector getRightVector(){
		return right;
	}
	
	public DistanceVector getLeftVector(){
		return left;
	}
	
	public MOVE[] getPossibleMoves(){
		return possibleMoves;
	}

	//Setters
	
	public void setId(Integer id) {
		this.id = id;
		up.setId(id);
		down.setId(id);
		left.setId(id);
		right.setId(id);
	}
	
	public void setTime(Integer time) {
		this.time = time;
	}
	
	public void setLives(Integer lives) {
		this.lives = lives;
	}
	
	public Integer setScore() {
		return score;
	}
	
	public void setUpVector(DistanceVector up){
		this.up = up;
	}
	
	public void setDownVector(DistanceVector down){
		this.down = down;
	}
	
	public void setRightVector(DistanceVector right){
		this.right = right;
	}
	
	public void setLeftVector(DistanceVector left){
		this.left = left;
	}
	
	public void setPossibleMoves(MOVE[] moves){
		possibleMoves = moves;
	}


	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	@Override
	public String toString() {
		return "MsPacManDescription [id = " + id  + ", lives = " + lives + ", time= " + time + ", up = " + up.toString() + 
				", down = " + down.toString() + ", right = " + right.toString() + ", left = " + left.toString() + "]";
	}


	protected static class DistanceVector implements TypeAdaptor, CaseComponent {
		
		private ArrayList<Integer> dist;
		private MOVE move;
		private Integer id;
		
		DistanceVector() {
			dist = new ArrayList<>();
		}
		
		DistanceVector(MOVE m, ArrayList<Integer> a) {
			move = m;
			dist = a;
		}
		
		public void setId(Integer id) {
			this.id = MOVE.values().length * id + move.ordinal();
		}
		
		public Integer getId() {
			return id;
		}
		
		public ArrayList<Integer> getVector() {
			return dist;
		}
		
		public MOVE getMove() {
			return move;
		}

		@Override
		public void fromString(String content) throws Exception {
			StringTokenizer tknizer = new StringTokenizer(content, ";", false);
			
			move = (MOVE) tknizer.nextElement();
			
			for(int i = 0; i < NUM_ELEMS; i++) {
				dist.add((Integer)tknizer.nextElement());
			}
		}
		
		public String toString() {
			String str =  move.toString();
			
			for(Integer elem : dist) {
				str += ";" + elem.toString();
			}
			
			return str;
		}

		@Override
		public Attribute getIdAttribute() {
			return new Attribute("id", DistanceVector.class);
		}
	}
}

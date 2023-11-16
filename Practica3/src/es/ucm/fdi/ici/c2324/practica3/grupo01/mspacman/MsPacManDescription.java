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
	DistanceVector upVector = new DistanceVector();  
	DistanceVector downVector = new DistanceVector();
	DistanceVector rightVector = new DistanceVector();
	DistanceVector leftVector = new DistanceVector();
	
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
		return upVector;
	}
	
	public DistanceVector getDownVector(){
		return downVector;
	}

	public DistanceVector getRightVector(){
		return rightVector;
	}
	
	public DistanceVector getLeftVector(){
		return leftVector;
	}
	
	public MOVE[] getPossibleMoves(){
		return possibleMoves;
	}

	//Setters
	
	public void setId(Integer id) {
		this.id = id;
		upVector.setId(id);
		downVector.setId(id);
		leftVector.setId(id);
		rightVector.setId(id);
	}
	
	public void setTime(Integer time) {
		this.time = time;
	}
	
	public void setLives(Integer lives) {
		this.lives = lives;
	}
	
	public void setScore(Integer score) {
		this.score = score;
	}
	
	public void setUpVector(DistanceVector up){
		this.upVector = up;
	}
	
	public void setDownVector(DistanceVector down){
		this.downVector = down;
	}
	
	public void setRightVector(DistanceVector right){
		this.rightVector = right;
	}
	
	public void setLeftVector(DistanceVector left){
		this.leftVector = left;
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
		return "MsPacManDescription [id = " + id  + ", lives = " + lives + ", time= " + time + ", up = " + upVector.toString() + 
				", down = " + downVector.toString() + ", right = " + rightVector.toString() + ", left = " + leftVector.toString() + "]";
	}


	protected static class DistanceVector implements TypeAdaptor, CaseComponent {
		
		private ArrayList<Integer> dist;
		private MOVE move;
		private Integer id;
		
		private static final String SEPARATOR = ";";
		
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
			StringTokenizer tknizer = new StringTokenizer(content, SEPARATOR, false);
			
			move = (MOVE) tknizer.nextElement();
			dist = new ArrayList<>();
			if (!content.equals(move.toString() + SEPARATOR + "null"))
				for(int i = 0; i < NUM_ELEMS; i++) {
					dist.add((Integer)tknizer.nextElement());
				}
		}
		
		public String toString() {
			String str =  move.toString();
			if (dist == null)
				return str + SEPARATOR + "null";
			
			for(Integer elem : dist) {
				str += SEPARATOR + elem.toString();
			}
			
			return str;
		}

		@Override
		public Attribute getIdAttribute() {
			return new Attribute("id", DistanceVector.class);
		}
	}
}

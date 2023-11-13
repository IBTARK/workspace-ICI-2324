package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

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
	DistanceVector up = new DistanceVector(MOVE.UP);  
	DistanceVector down = new DistanceVector(MOVE.DOWN);
	DistanceVector right = new DistanceVector(MOVE.RIGHT);
	DistanceVector left = new DistanceVector(MOVE.LEFT);
	
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
	
	public CaseComponent getUpVector(){
		return up;
	}
	
	public CaseComponent getDownVector(){
		return down;
	}

	public CaseComponent getRightVector(){
		return right;
	}
	
	public CaseComponent getLeftVector(){
		return left;
	}
	
	public CaseComponent getVector(MOVE m) {
		if(m == MOVE.UP) return up;
		else if(m == MOVE.DOWN) return down;
		else if(m == MOVE.LEFT) return left;
		else return right;
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
	
	public void setUpVector(ArrayList<Integer> up){
		this.up.setVector(up);
	}
	
	public void setDownVector(ArrayList<Integer> down){
		this.down.setVector(down);
	}
	
	public void setRightVector(ArrayList<Integer> right){
		this.right.setVector(right);
	}
	
	public void setLeftVector(ArrayList<Integer> left){
		this.left.setVector(left);
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


	protected class DistanceVector implements CaseComponent {
		
		private Integer id;
		private ArrayList<Integer> dist;
		private MOVE move;
		
		DistanceVector(MOVE move) {
			this.move = move;
		}

		@Override
		public Attribute getIdAttribute() {
			return new Attribute("id", DistanceVector.class);
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
		
		public void setId(Integer id) {
			this.id = MOVE.values().length * id + move.ordinal();
		}
		
		public void setVector(ArrayList<Integer> dist) {
			this.dist = dist;
		}
	}
}

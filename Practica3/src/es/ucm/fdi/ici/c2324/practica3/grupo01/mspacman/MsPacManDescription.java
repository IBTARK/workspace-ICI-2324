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
	/*
	 List for each possible movement with the next information:
	 0: distance to the nearest chasing ghost
	 1: distance to the nearest edible ghost
	 2: remaining edible time of the nearest edible ghost
	 3: distance to the nearest PPill
	*/
	DistanceVector up;  
	DistanceVector down;
	DistanceVector right;
	DistanceVector left;
	
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

	//Setters
	
	public void setId(Integer id) {
		this.id = id;
		up.setId(MOVE.values().length * id + MOVE.UP.ordinal());
		down.setId(MOVE.values().length * id + MOVE.DOWN.ordinal());
		left.setId(MOVE.values().length * id + MOVE.LEFT.ordinal());
		right.setId(MOVE.values().length * id + MOVE.RIGHT.ordinal());
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
		
		public void setId(Integer id) {
			this.id = id;
		}
		
		public void setVector(ArrayList<Integer> dist) {
			this.dist = dist;
		}
	}
}

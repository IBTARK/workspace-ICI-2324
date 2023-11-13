package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostDescription implements CaseComponent {
	private Integer id;
	
	private Integer time;
	private Integer lives; //Remaining lives of MsPacMan
	private Boolean edible;
	private Integer edibleTime;
	private Integer score;
	/*
	 List for each possible movement with the next information:
	 0: distance to the nearest chasing ghost
	 1: distance to the nearest edible ghost
	 2: remaining edible time of the nearest edible ghost
	 3: distance to the nearest PPill to mspacman
	 4: distance to mspacman
	*/
	private ArrayList<Integer> up;  
	private ArrayList<Integer> down;
	private ArrayList<Integer> right;
	private ArrayList<Integer> left;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public Integer getLives() {
		return lives;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}

	public Boolean getEdible() {
		return edible;
	}

	public void setEdible(Boolean edible) {
		this.edible = edible;
	}

	public Integer getEdibleTime() {
		return edibleTime;
	}

	public void setEdibleTime(Integer edibleTime) {
		this.edibleTime = edibleTime;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public ArrayList<Integer> getUp() {
		return up;
	}

	public void setUp(ArrayList<Integer> up) {
		this.up = up;
	}

	public ArrayList<Integer> getDown() {
		return down;
	}

	public void setDown(ArrayList<Integer> down) {
		this.down = down;
	}

	public ArrayList<Integer> getRight() {
		return right;
	}

	public void setRight(ArrayList<Integer> right) {
		this.right = right;
	}

	public ArrayList<Integer> getLeft() {
		return left;
	}

	public void setLeft(ArrayList<Integer> left) {
		this.left = left;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostDescription.class);
	}
	
	@Override
	public String toString() {
		return "GhostDescription [id = " + id  + ", lives = " + lives + ", time= " + time + ", up = " + up.toString() + 
				", down = " + down.toString() + ", right = " + right.toString() + ", left = " + left.toString() + "]";
	}

}

package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman.MsPacManDescription;

public class GhostDescription implements CaseComponent {
	Integer id;
	
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
	
	public Integer getId() {
		return id;
	}

	public Integer getTime() {
		return time;
	}

	public Integer getLives() {
		return lives;
	}

	public ArrayList<Integer> getUp() {
		return up;
	}

	public ArrayList<Integer> getDown() {
		return down;
	}

	public ArrayList<Integer> getRight() {
		return right;
	}

	public ArrayList<Integer> getLeft() {
		return left;
	}
	
	//Setters
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setTime(Integer time) {
		this.time = time;
	}
	
	public void setLives(Integer lives) {
		this.lives = lives;
	}
	
	public void setUpVector(ArrayList<Integer> up){
		this.up = up;
	}
	
	public void setDownVector(ArrayList<Integer> down){
		this.down = down;
	}
	
	public void setRightVector(ArrayList<Integer> right){
		this.right = right;
	}
	
	public void setLeftVector(ArrayList<Integer> left){
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

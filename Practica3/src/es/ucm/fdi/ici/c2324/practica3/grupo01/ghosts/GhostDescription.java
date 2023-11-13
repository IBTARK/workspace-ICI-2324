 package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.GHOST;

public class GhostDescription implements CaseComponent {


	Integer id;

	Integer mspacmanLives;
	Integer score;
	Integer time;
	GHOST type;
	Boolean edible;
	Integer edibleTime;
	Integer mspacmanToPPill;		//MsPacMan's distance to its nearest PPill.
	
	// In order to make the mapping easier, we will make each value of the arrays into separate attributes
	Integer UP_mspacman;
	Integer UP_nearestEdible;
	Integer UP_nearestEdibleTime;
	Integer UP_nearestChasing;
	
	Integer RIGHT_mspacman;
	Integer RIGHT_nearestEdible;
	Integer RIGHT_nearestEdibleTime;
	Integer RIGHT_nearestChasing;
	
	Integer DOWN_mspacman;
	Integer DOWN_nearestEdible;
	Integer DOWN_nearestEdibleTime;
	Integer DOWN_nearestChasing;
	
	Integer LEFT_mspacman;
	Integer LEFT_nearestEdible;
	Integer LEFT_nearestEdibleTime;
	Integer LEFT_nearestChasing;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getMspacmanToPPill() {
		return mspacmanToPPill;
	}

	public void setMspacmanToPPill(Integer mspacmanToPPill) {
		this.mspacmanToPPill = mspacmanToPPill;
	}

	
	public Integer getMspacmanLives() {
		return mspacmanLives;
	}

	public void setMspacmanLives(Integer mspacmanLives) {
		this.mspacmanLives = mspacmanLives;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public GHOST getType() {
		return type;
	}

	public void setType(GHOST type) {
		this.type = type;
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

	public Integer getUP_mspacman() {
		return UP_mspacman;
	}

	public void setUP_mspacman(Integer uP_mspacman) {
		UP_mspacman = uP_mspacman;
	}

	public Integer getUP_nearestEdible() {
		return UP_nearestEdible;
	}

	public void setUP_nearestEdible(Integer uP_nearestEdible) {
		UP_nearestEdible = uP_nearestEdible;
	}

	public Integer getUP_nearestEdibleTime() {
		return UP_nearestEdibleTime;
	}

	public void setUP_nearestEdibleTime(Integer uP_nearestEdibleTime) {
		UP_nearestEdibleTime = uP_nearestEdibleTime;
	}

	public Integer getUP_nearestChasing() {
		return UP_nearestChasing;
	}

	public void setUP_nearestChasing(Integer uP_nearestChasing) {
		UP_nearestChasing = uP_nearestChasing;
	}

	public Integer getRIGHT_mspacman() {
		return RIGHT_mspacman;
	}

	public void setRIGHT_mspacman(Integer rIGHT_mspacman) {
		RIGHT_mspacman = rIGHT_mspacman;
	}

	public Integer getRIGHT_nearestEdible() {
		return RIGHT_nearestEdible;
	}

	public void setRIGHT_nearestEdible(Integer rIGHT_nearestEdible) {
		RIGHT_nearestEdible = rIGHT_nearestEdible;
	}

	public Integer getRIGHT_nearestEdibleTime() {
		return RIGHT_nearestEdibleTime;
	}

	public void setRIGHT_nearestEdibleTime(Integer rIGHT_nearestEdibleTime) {
		RIGHT_nearestEdibleTime = rIGHT_nearestEdibleTime;
	}

	public Integer getRIGHT_nearestChasing() {
		return RIGHT_nearestChasing;
	}

	public void setRIGHT_nearestChasing(Integer rIGHT_nearestChasing) {
		RIGHT_nearestChasing = rIGHT_nearestChasing;
	}

	public Integer getDOWN_mspacman() {
		return DOWN_mspacman;
	}

	public void setDOWN_mspacman(Integer dOWN_mspacman) {
		DOWN_mspacman = dOWN_mspacman;
	}

	public Integer getDOWN_nearestEdible() {
		return DOWN_nearestEdible;
	}

	public void setDOWN_nearestEdible(Integer dOWN_nearestEdible) {
		DOWN_nearestEdible = dOWN_nearestEdible;
	}

	public Integer getDOWN_nearestEdibleTime() {
		return DOWN_nearestEdibleTime;
	}

	public void setDOWN_nearestEdibleTime(Integer dOWN_nearestEdibleTime) {
		DOWN_nearestEdibleTime = dOWN_nearestEdibleTime;
	}

	public Integer getDOWN_nearestChasing() {
		return DOWN_nearestChasing;
	}

	public void setDOWN_nearestChasing(Integer dOWN_nearestChasing) {
		DOWN_nearestChasing = dOWN_nearestChasing;
	}

	public Integer getLEFT_mspacman() {
		return LEFT_mspacman;
	}

	public void setLEFT_mspacman(Integer lEFT_mspacman) {
		LEFT_mspacman = lEFT_mspacman;
	}

	public Integer getLEFT_nearestEdible() {
		return LEFT_nearestEdible;
	}

	public void setLEFT_nearestEdible(Integer lEFT_nearestEdible) {
		LEFT_nearestEdible = lEFT_nearestEdible;
	}

	public Integer getLEFT_nearestEdibleTime() {
		return LEFT_nearestEdibleTime;
	}

	public void setLEFT_nearestEdibleTime(Integer lEFT_nearestEdibleTime) {
		LEFT_nearestEdibleTime = lEFT_nearestEdibleTime;
	}

	public Integer getLEFT_nearestChasing() {
		return LEFT_nearestChasing;
	}

	public void setLEFT_nearestChasing(Integer lEFT_nearestChasing) {
		LEFT_nearestChasing = lEFT_nearestChasing;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostDescription.class);
	}
	public void setUP(ArrayList<Integer> list) {
		this.UP_mspacman = list.get(0);
		this.UP_nearestEdible = list.get(1);
		this.UP_nearestEdibleTime = list.get(2);
		this.UP_nearestChasing = list.get(3);
	}

	public void setRIGHT(ArrayList<Integer> list) {
		this.RIGHT_mspacman = list.get(0);
		this.RIGHT_nearestEdible = list.get(1);
		this.RIGHT_nearestEdibleTime = list.get(2);
		this.RIGHT_nearestChasing = list.get(3);
	}

	public void setDOWN(ArrayList<Integer> list) {
		this.DOWN_mspacman = list.get(0);
		this.DOWN_nearestEdible = list.get(1);
		this.DOWN_nearestEdibleTime = list.get(2);
		this.DOWN_nearestChasing = list.get(3);
	}
	
	public void setLEFT(ArrayList<Integer> list) {
		this.LEFT_mspacman = list.get(0);
		this.LEFT_nearestEdible = list.get(1);
		this.LEFT_nearestEdibleTime = list.get(2);
		this.LEFT_nearestChasing = list.get(3);
	}
	
	// TODO rellenar toString
	@Override
	public String toString() {
		// return "GhostDescription [id = " + id  + ", lives = " + lives + ", time= " + time + ", up = " + up.toString() + 
		//		", down = " + down.toString() + ", right = " + right.toString() + ", left = " + left.toString() + "]";
		return "GhostsDescription [id=" + id + "]";
	}
	
}
 package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostDescription implements CaseComponent {


	private Integer id;

	private Integer mspacmanLives;
	private Integer score;
	private Integer time;
	private GHOST type;
	private Boolean edible;
	private Integer edibleTime;
	private Integer mspacmanToPPill;		//MsPacMan's distance to its nearest PPill.
	private MOVE[] possibleMoves;
	
	private GhostDistanceVector up = new GhostDistanceVector(MOVE.UP);
	private GhostDistanceVector right = new GhostDistanceVector(MOVE.RIGHT);
	private GhostDistanceVector down = new GhostDistanceVector(MOVE.DOWN);
	private GhostDistanceVector left = new GhostDistanceVector(MOVE.LEFT);
	
	// In order to make the mapping easier, we will make each value of the arrays into separate attributes

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMspacmanLives() {
		return mspacmanLives;
	}

	public void setMspacmanLives(Integer mspacmanLives) {
		this.mspacmanLives = mspacmanLives;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
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

	public Integer getMspacmanToPPill() {
		return mspacmanToPPill;
	}

	public void setMspacmanToPPill(Integer mspacmanToPPill) {
		this.mspacmanToPPill = mspacmanToPPill;
	}

	public GhostDistanceVector getUp() {
		return up;
	}

	public void setUp(GhostDistanceVector up) {
		this.up = up;
	}

	public GhostDistanceVector getRight() {
		return right;
	}

	public void setRight(GhostDistanceVector right) {
		this.right = right;
	}

	public GhostDistanceVector getDown() {
		return down;
	}

	public void setDown(GhostDistanceVector down) {
		this.down = down;
	}

	public GhostDistanceVector getLeft() {
		return left;
	}

	public void setLeft(GhostDistanceVector left) {
		this.left = left;
	}

	@Override
	public Attribute getIdAttribute() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		// return "GhostDescription [id = " + id  + ", lives = " + lives + ", time= " + time + ", up = " + up.toString() + 
		//		", down = " + down.toString() + ", right = " + right.toString() + ", left = " + left.toString() + "]";
		return "GhostsDescription [id=" + id + "]";
	}
	
	
}
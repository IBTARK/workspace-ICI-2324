 package ghosts_simplified;

import java.util.ArrayList;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostDescription implements CaseComponent {


	private Integer id;

	private Integer mspacmanLives;
	private Integer mspacmanDistance;
	private Integer score;
	private Integer time;
	private GHOST type;
	private Boolean edible;
	private Integer edibleTime;
	private Integer mspacmanToPPill;		//MsPacMan's distance to its nearest PPill.
	private ArrayList<MOVE> possibleMoves;  // NO LO VAMOS A GUARDAR. ES PARA HACER CHECKEO RAPIDO
	
	private GhostDistanceVector upVector = new GhostDistanceVector(MOVE.UP);
	private GhostDistanceVector rightVector = new GhostDistanceVector(MOVE.RIGHT);
	private GhostDistanceVector downVector = new GhostDistanceVector(MOVE.DOWN);
	private GhostDistanceVector leftVector = new GhostDistanceVector(MOVE.LEFT);
	
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

	public Integer getMspacmanDistance() {
		return mspacmanDistance;
	}

	public void setMspacmanDistance(Integer mspacmanDistance) {
		this.mspacmanDistance = mspacmanDistance;
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
		if(edible==null)
			return false;
		else
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

	public ArrayList<MOVE> getPossibleMoves() {
		return possibleMoves;
	}

	public void setPossibleMoves(ArrayList<MOVE> possibleMoves) {
		this.possibleMoves = new ArrayList<MOVE>(possibleMoves);
	}

	public GhostDistanceVector getUpVector() {
		return upVector;
	}

	public void setUpVector(GhostDistanceVector upVector) {
		this.upVector = new GhostDistanceVector(upVector);
	}

	public GhostDistanceVector getRightVector() {
		return rightVector;
	}

	public void setRightVector(GhostDistanceVector rightVector) {
		this.rightVector = new GhostDistanceVector(rightVector);
	}

	public GhostDistanceVector getDownVector() {
		return downVector;
	}

	public void setDownVector(GhostDistanceVector downVector) {
		this.downVector = new GhostDistanceVector(downVector);
	}

	public GhostDistanceVector getLeftVector() {
		return leftVector;
	}

	public void setLeftVector(GhostDistanceVector leftVector) {
		this.leftVector = new GhostDistanceVector(leftVector);
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostDescription.class);
	}
	
	@Override
	public String toString() {
		// return "GhostDescription [id = " + id  + ", lives = " + lives + ", time= " + time + ", up = " + up.toString() + 
		//		", down = " + down.toString() + ", right = " + right.toString() + ", left = " + left.toString() + "]";
		return "GhostsDescription [id=" + id + "]";
	}
	
	
}
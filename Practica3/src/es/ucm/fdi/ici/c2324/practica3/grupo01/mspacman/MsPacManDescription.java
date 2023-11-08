package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class MsPacManDescription implements CaseComponent {

	Integer id;
	
	Integer score;
	Integer mspacman;	// MsPacMan's position
	MOVE lastMove; 		// MsPacMan's last move
	// Next attributes are all distances
	Integer nearestPPill;
	Integer nearestPill;
	Integer nearestChasingGhost1;
	Integer nearestChasingGhost2;
	Integer nearestEdibleGhost1;
	Integer nearestEdibleGhost2;



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


	public Integer getMspacman() {
		return mspacman;
	}

	public void setMspacman(Integer mspacman) {
		this.mspacman = mspacman;
	}

	public MOVE getLastMove() {
		return lastMove;
	}

	public void setLastMove(MOVE lastMove) {
		this.lastMove = lastMove;
	}

	public Integer getNearestPPill() {
		return nearestPPill;
	}

	public void setNearestPPill(Integer nearestPPill) {
		this.nearestPPill = nearestPPill;
	}

	public Integer getNearestPill() {
		return nearestPill;
	}

	public void setNearestPill(Integer nearestPill) {
		this.nearestPill = nearestPill;
	}

	public Integer getNearestChasingGhost1() {
		return nearestChasingGhost1;
	}

	public void setNearestChasingGhost1(Integer nearestChasingGhost1) {
		this.nearestChasingGhost1 = nearestChasingGhost1;
	}

	public Integer getNearestChasingGhost2() {
		return nearestChasingGhost2;
	}

	public void setNearestChasingGhost2(Integer nearestChasingGhost2) {
		this.nearestChasingGhost2 = nearestChasingGhost2;
	}

	public Integer getNearestEdibleGhost1() {
		return nearestEdibleGhost1;
	}

	public void setNearestEdibleGhost1(Integer nearestEdibleGhost1) {
		this.nearestEdibleGhost1 = nearestEdibleGhost1;
	}

	public Integer getNearestEdibleGhost2() {
		return nearestEdibleGhost2;
	}

	public void setNearestEdibleGhost2(Integer nearestEdibleGhost2) {
		this.nearestEdibleGhost2 = nearestEdibleGhost2;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManDescription.class);
	}

	
	// TODO modificar el toString
	@Override
	public String toString() {
		return "MsPacManDescription [id=" + id + ", score=" + score + ", time=" + time + ", nearestPPill="
				+ nearestPPill + ", nearestGhost=" + nearestGhost + ", edibleGhost=" + edibleGhost + "]";
	}


	
	

}

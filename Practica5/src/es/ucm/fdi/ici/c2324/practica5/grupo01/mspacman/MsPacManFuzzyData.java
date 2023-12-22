package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.ArrayList;

import pacman.game.Constants.MOVE;

public class MsPacManFuzzyData {
	
	private int nearestPPill; 
	private int nearestChasing;
	private MOVE nearestChasingLastMove;
	private int nearestEdible; //Distance from MsPacMan to the nearest edible ghost to her
	private int nearestEdibleNextJunction; //Distance from MsPacMan to the next junction of the nearest edible ghosts to her
	private Double numOfPills = -1.0;
	private ArrayList<Integer> ppillsPos = new ArrayList<>(); 
	private Double ppillDistance;
	private Double nearestChasingDist;
	private Double nearestChasingDist2;
	private Double nearestEdibleDist;
	private Double nearestEdibleNextJunctionDist;
	
	public int getNearestPPill() {
		return nearestPPill;
	}
	
	public int getNearestChasing() {
		return nearestChasing;
	}
	
	public MOVE getNearestChasingLastMove() {
		return nearestChasingLastMove;
	}
	
	public int getNearestEdible() {
		return nearestEdible;
	}
	
	public int getNearestEdibleNextJunction() {
		return nearestEdibleNextJunction;
	}
	
	public Double getNumOfPills() {
		return numOfPills;
	}
	
	public ArrayList<Integer> getPpillsPos() {
		return ppillsPos;
	}

	public Double getPpillDistance() {
		return ppillDistance;
	}

	public Double getNearestChasingDist() {
		return nearestChasingDist;
	}

	public Double getNearestEdibleNextJunctionDist() {
		return nearestEdibleNextJunctionDist;
	}

	public Double getNearestEdibleDist() {
		return nearestEdibleDist;
	}

	public Double getNearestChasingDist2() {
		return nearestChasingDist2;
	}
	
	public void setNearestPPill(int nearestPPill) {
		this.nearestPPill = nearestPPill;
	}
	
	public void setNearestChasing(int nearestChasing) {
		this.nearestChasing = nearestChasing;
	}
	
	public void setNearestChasingLastMove(MOVE nearestChasingLastMove) {
		this.nearestChasingLastMove = nearestChasingLastMove;
	}
	
	public void setNearestEdible(int nearestEdible) {
		this.nearestEdible = nearestEdible;
	}
	
	public void setNearestEdibleNextJunction(int nearestEdibleNextJunction) {
		this.nearestEdibleNextJunction = nearestEdibleNextJunction;
	}
	
	public void setNumOfPills(Double numOfPills) {
		this.numOfPills = numOfPills;
	}
	
	public void setPpillsPos(ArrayList<Integer> ppillsPos) {
		this.ppillsPos = ppillsPos;
	}

	public void setPpillDistance(Double ppillDistance) {
		this.ppillDistance = ppillDistance;
	}

	public void setNearestChasingDist(Double nearestChasingDist) {
		this.nearestChasingDist = nearestChasingDist;
	}

	public void setNearestChasingDist2(Double nearestChasingDist2) {
		this.nearestChasingDist2 = nearestChasingDist2;
	}

	public void setNearestEdibleDist(Double nearestEdibleDist) {
		this.nearestEdibleDist = nearestEdibleDist;
	}

	public void setNearestEdibleNextJunctionDist(Double nearestEdibleNextJunctionDist) {
		this.nearestEdibleNextJunctionDist = nearestEdibleNextJunctionDist;
	}
}

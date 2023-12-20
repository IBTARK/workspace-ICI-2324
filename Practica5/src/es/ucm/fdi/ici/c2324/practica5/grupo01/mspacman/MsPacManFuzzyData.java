package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.ArrayList;

public class MsPacManFuzzyData {
	
	private Double ppillDistance;
	private Double nearestChasingDist; //Distance from MsPacMan to the nearest edible ghost to her
	private Double nearestChasingDist2; //Distance from MsPacMan to the nearest edible ghost to her
	private Double nearestEdibleDist; //Distance from MsPacMan to the nearest edible ghost to her
	private Double nearestEdibleNextJunctionDist; //Distance from MsPacMan to the next junction of the nearest edible ghosts to her
	private Double distOfNearestEdibleToHisNextJunction; //Distance of MsPacMans nearest edible ghost to his next junction
	private int numOfPills;
	private int numOfPPills;
	private ArrayList<Integer> ppillsPos; 
	
	public Double getPpillDistance() {
		return ppillDistance;
	}
	
	public Double getNearestChasingDist() {
		return nearestChasingDist;
	}
	
	public Double getNearestChasingDist2() {
		return nearestChasingDist2;
	}
	
	public Double getNearestEdibleDist() {
		return nearestEdibleDist;
	}
	
	public Double getNearestEdibleNextJunctionDist() {
		return nearestEdibleNextJunctionDist;
	}
	
	public Double getDistOfNearestEdibleToHisNextJunction() {
		return distOfNearestEdibleToHisNextJunction;
	}
	
	public int getNumOfPills() {
		return numOfPills;
	}
	
	public int getNumOfPPills() {
		return numOfPPills;
	}
	
	public ArrayList<Integer> getPpillsPos() {
		return ppillsPos;
	}
	
	public void setPPillDistance(Double ppillDistance) {
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
	
	public void setDistOfNearestEdibleToHisNextJunction(Double distOfNearestEdibleToHisNextJunction) {
		this.distOfNearestEdibleToHisNextJunction = distOfNearestEdibleToHisNextJunction;
	}
	
	public void setNumOfPills(int numOfPills) {
		this.numOfPills = numOfPills;
	}
	
	public void setNumOfPPills(int numOfPPills) {
		this.numOfPPills = numOfPPills;
	}
	
	public void setPpillsPos(ArrayList<Integer> ppillsPos) {
		this.ppillsPos = ppillsPos;
	}
	
	
}

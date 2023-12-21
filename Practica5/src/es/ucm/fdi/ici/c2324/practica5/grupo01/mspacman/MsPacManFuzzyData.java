package es.ucm.fdi.ici.c2324.practica5.grupo01.mspacman;

import java.util.ArrayList;

import pacman.game.Constants.MOVE;

public class MsPacManFuzzyData {
	
	private int nearestPPill; 
	private int nearestChasing;
	private MOVE nearestChasingLastMove;
	private int nearestEdible; //Distance from MsPacMan to the nearest edible ghost to her
	private int nearestEdibleNextJunction; //Distance from MsPacMan to the next junction of the nearest edible ghosts to her
	private int numOfPills;
	private int numOfPPills;
	private ArrayList<Integer> ppillsPos; 
	
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
	
	public int getNumOfPills() {
		return numOfPills;
	}
	
	public int getNumOfPPills() {
		return numOfPPills;
	}
	
	public ArrayList<Integer> getPpillsPos() {
		return ppillsPos;
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

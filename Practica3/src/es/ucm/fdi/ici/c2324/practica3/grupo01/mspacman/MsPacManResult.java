package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManResult implements CaseComponent {

	private Integer id;
	private Integer score; //Points obtained between the first phase and the revision phase
	private Integer numReps = 1; //Number of times a case (or very similar ones) have appeared on the case base
	private Integer finalScore; //Score at the end of the game
	
	//Getters
	
	public Integer getId() {
		return id;
	}
	
	public Integer getScore() {
		return score;
	}
	
	public Integer getNumReps() {
		return numReps;
	}
	
	public Integer getFinalScore() {
		return finalScore;
	}

	//Setters
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setScore(Integer score) {
		this.score = score;
	}
	
	public void setNumReps(Integer numReps) {
		this.numReps = numReps;
	}
	
	public void setFinalScore(Integer score) {
		finalScore = score;
	}

	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", MsPacManResult.class);
	}
	
	@Override
	public String toString() {
		return "MsPacManResult [id = " + id + ", score = " + score + ", numReps = " + numReps + ", finalScore = " + finalScore + "]";
	} 
	
	

}

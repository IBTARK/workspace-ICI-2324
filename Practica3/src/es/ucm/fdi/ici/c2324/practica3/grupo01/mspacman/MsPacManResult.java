package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class MsPacManResult implements CaseComponent {

	private Integer id;
	private Integer score;
	private Integer numReps = 1;
	private Integer finalScore;
	
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

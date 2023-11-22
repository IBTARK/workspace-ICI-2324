package ghosts_simplified;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;

public class GhostResult implements CaseComponent {
	
	Integer id;
	Integer score;
	Integer numReps;
	
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
	
	public void initializeCounter() {
		this.numReps = 1;
	}
	
	public void incrementCounter() {
		this.numReps += 1;
	}

	public void decrementCounter() {
		this.numReps -= 1;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostResult.class);
	}
	
	@Override
	public String toString() {
		return "GhostResult [id = " + id + ", score = " + score + ", numReps = " + numReps + "]";
	} 

}

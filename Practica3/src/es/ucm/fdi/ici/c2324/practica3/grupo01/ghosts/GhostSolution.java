package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import es.ucm.fdi.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseComponent;
import pacman.game.Constants.MOVE;

public class GhostSolution implements CaseComponent {
	
	Integer id;
	MOVE action;
	
	//Getters
	
	public Integer getId() {
		return id;
	}
	
	public MOVE getAction() {
		return action;
	}
	
	//Setters
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setAction(MOVE action) {
		this.action = action;
	}
	
	@Override
	public Attribute getIdAttribute() {
		return new Attribute("id", GhostSolution.class);
	}
	
	@Override
	public String toString() {
		return "GhostSolution [id=" + id + ", action=" + action + "]";
	}  

}

package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostStorageManager {
	
	// vamos a necesitar tener 2 case base, uno para el ghosts edible y otro para el chasing
	// las case base generales no es necesario tenerlas aqui porque no almacenaremos ahi movimientos mientras jugamos contra un oponente
	Game game;
	CBRCaseBase caseBaseEdible;
	CBRCaseBase caseBaseChasing;
	
	// Tendremos un buffer por cada ghost, aunque recuperando casos no tengamos en cuenta el ghost que era.
	Vector<CBRCase> bufferBlinky;
	Vector<CBRCase> bufferPinky;
	Vector<CBRCase> bufferInky;
	Vector<CBRCase> bufferSue;
	
	int pendingEdibleCases;
	int pendingChasingCases;

	private final static int TIME_WINDOW = 3;
	
	public GhostStorageManager()
	{
		this.bufferBlinky = new Vector<CBRCase>();
		this.bufferPinky = new Vector<CBRCase>();
		this.bufferInky = new Vector<CBRCase>();
		this.bufferSue = new Vector<CBRCase>();
		pendingEdibleCases = 0;
		pendingChasingCases = 0;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBaseEdible(CBRCaseBase caseBase)
	{
		this.caseBaseEdible = caseBase;
	}
	
	public void setCaseBaseChasing(CBRCaseBase caseBase)
	{
		this.caseBaseChasing = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase)
	{
		GhostDescription desc = (GhostDescription) newCase.getDescription();
		boolean edible = desc.getEdible();
		GHOST type = desc.getType();
		
		// Ya explicado en el reuse y en el createNewCase:
		// A esta función llegarán casos con id=-1
		// Estos casos serán aquellos que tenian vecinos similares, por lo que no los tendremos en cuenta ... -
		// ... a la hora del revise y el retain.
		// No incrementamos ni decrementamos el contador de los pendingCases porque no lo vamos a guardar.
		// De todas maneras, queremos guardarlos en sus buffers correspondientes para llevar la cuenta de 3 junctions para el reviseAndRetain de los otros casos
		if(desc.getId() >= 0) {
			if(edible) pendingEdibleCases += 1;
			else pendingChasingCases += 1;
		}
		
		// Add to the buffer
		// Revise the buffer size
		// Remove from the buffer the next case
		CBRCase bCase;
		switch (type) {
		case BLINKY:
			this.bufferBlinky.add(newCase);
			if(this.bufferBlinky.size()<TIME_WINDOW)
				return;
			bCase = this.bufferBlinky.remove(0);
			break;
		case PINKY:
			this.bufferPinky.add(newCase);
			if(this.bufferPinky.size()<TIME_WINDOW)
				return;
			bCase = this.bufferPinky.remove(0);
			break;
		case INKY:
			this.bufferInky.add(newCase);
			if(this.bufferInky.size()<TIME_WINDOW)
				return;
			bCase = this.bufferInky.remove(0);
			break;
		case SUE:
		default:
			this.bufferSue.add(newCase);
			if(this.bufferSue.size()<TIME_WINDOW)
				return;
			bCase = this.bufferSue.remove(0);
			break;
		}
		
		desc = (GhostDescription) bCase.getDescription();
		edible = desc.getEdible();
		if(desc.getId() >= 0) {
			if(edible) pendingEdibleCases -= 1;
			else pendingChasingCases -= 1;
		}
		
		reviseCase(bCase);
		retainCase(bCase);
	}
	
	private void reviseCase(CBRCase bCase) {
		// En caso de que el id=-1 no queremos ni revisar ni guardar el caso.
		if(((GhostDescription) bCase.getDescription()).getId() == -1) return;
		
		GhostDescription description = (GhostDescription)bCase.getDescription();
		int oldScore = description.getScore();
		int oldLives = description.getMspacmanLives();
		int currentScore = game.getScore();
		int currentLives = game.getPacmanNumberOfLivesRemaining();
		int resultScore = (currentScore - oldScore) / (oldLives - currentLives + 1);
		
		GhostResult result = (GhostResult)bCase.getResult();
		result.setScore(resultScore);	
		result.initializeCounter();
	}
	
	public void removeOldCase(CBRCase bCase) {
		boolean edible = ((GhostDescription) bCase.getDescription()).getEdible();
		
		Collection<CBRCase> caseToModify = new ArrayList<>(1);
		caseToModify.add(bCase);
		
		if(edible)	
			this.caseBaseEdible.forgetCases(caseToModify);
		else
			this.caseBaseEdible.forgetCases(caseToModify);
	}
	
	public void retainOldCase(CBRCase oldCase) {
		// Por no cambiar "retainCase" 
		retainCase(oldCase);
	}
	
	private void retainCase(CBRCase bCase)
	{
		// En caso de que el id=-1 no queremos ni revisar ni guardar el caso.
		if(((GhostDescription) bCase.getDescription()).getId() == -1) return;
		
		boolean edible = ((GhostDescription) bCase.getDescription()).getEdible();
		
		if(edible)	
			StoreCasesMethod.storeCase(this.caseBaseEdible, bCase);
		else
			StoreCasesMethod.storeCase(this.caseBaseChasing, bCase);
	}

	public void close() {
		for(CBRCase oldCase: this.bufferBlinky)
		{
			reviseCase(oldCase);
			retainCase(oldCase);
		}
		for(CBRCase oldCase: this.bufferInky)
		{
			reviseCase(oldCase);
			retainCase(oldCase);
		}
		for(CBRCase oldCase: this.bufferPinky)
		{
			reviseCase(oldCase);
			retainCase(oldCase);
		}
		for(CBRCase oldCase: this.bufferSue)
		{
			reviseCase(oldCase);
			retainCase(oldCase);
		}
		this.bufferBlinky.removeAllElements();
		this.bufferInky.removeAllElements();
		this.bufferPinky.removeAllElements();
		this.bufferSue.removeAllElements();
	}

	public int getPendingChasingCases() {
		return pendingChasingCases;
	}

	public int getPendingEdibleCases() {
		return pendingEdibleCases;
	}
}

package es.ucm.fdi.ici.c2324.practica3.grupo01.ghosts;

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
		
		if(edible) pendingEdibleCases += 1;
		else pendingChasingCases += 1;
		
		
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
		if(edible) pendingEdibleCases -= 1;
		else pendingChasingCases -= 1;
		
		
		reviseCase(bCase);
		retainCase(bCase);
	}
	
	private void reviseCase(CBRCase bCase) {
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
	
	private void retainCase(CBRCase bCase)
	{
		boolean edible = ((GhostDescription) bCase.getDescription()).getEdible();
		// TODO aplicar el documento de diseño al retain

		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
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

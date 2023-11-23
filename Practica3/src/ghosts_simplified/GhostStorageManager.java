package ghosts_simplified;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.CachedLinearCaseBase;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GhostStorageManager {
	
	// vamos a necesitar tener 2 case base, uno para el ghosts edible y otro para el chasing
	// las case base generales no es necesario tenerlas aqui porque no almacenaremos ahi movimientos mientras jugamos contra un oponente
	Game game;
	CachedLinearCaseBase caseBaseEdible;
	CachedLinearCaseBase caseBaseChasing;
	
	// Tendremos un buffer por cada ghost, aunque recuperando casos no tengamos en cuenta el ghost que era.
	Vector<CBRCase> bufferBlinky;
	Vector<CBRCase> bufferPinky;
	Vector<CBRCase> bufferInky;
	Vector<CBRCase> bufferSue;
	
	int pendingEdibleCases;
	int pendingChasingCases;
	
	Map<CBRCase, CBRCase> oldCases;

	private final static int TIME_WINDOW = 3;
	
	public GhostStorageManager()
	{
		this.bufferBlinky = new Vector<CBRCase>();
		this.bufferPinky = new Vector<CBRCase>();
		this.bufferInky = new Vector<CBRCase>();
		this.bufferSue = new Vector<CBRCase>();
		pendingEdibleCases = 0;
		pendingChasingCases = 0;
		this.oldCases =  new HashMap<>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBaseEdible(CachedLinearCaseBase caseBase)
	{
		this.caseBaseEdible = caseBase;
	}
	
	public void setCaseBaseChasing(CachedLinearCaseBase caseBase)
	{
		this.caseBaseChasing = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase, CBRCase oldCase)
	{
		if(oldCase!=null) 
			oldCases.put(newCase, oldCase);
		
		GhostDescription desc = (GhostDescription) newCase.getDescription();
		boolean edible = desc.getEdible();
		GHOST type = desc.getType();
		
		if(!oldCases.containsKey(newCase)) {
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
		if(!oldCases.containsKey(bCase)) {
			if(edible) pendingEdibleCases -= 1;
			else pendingChasingCases -= 1;
		}
		
		reviseCase(bCase);
		retainCase(bCase);
	}
	
	private void reviseCase(CBRCase bCase) {
		// En caso de que el CBRCase est� en el mapa de oldCases, no queremos hacer ni el revise ni el retain.
		//if(oldCases.containsKey(bCase)) return;
		// Si lo hacemos, porque GhostResult del bCase no esta calculado del todo ?
		GhostDescription description = (GhostDescription)bCase.getDescription();
		int oldLives = description.getMspacmanLives();
		int currentLives = game.getPacmanNumberOfLivesRemaining();
		
		int ghost = game.getGhostCurrentNodeIndex(description.getType());
		int mspacman = game.getPacmanCurrentNodeIndex();
		MOVE lastMove = game.getGhostLastMoveMade(description.getType());
		
		if(currentLives > oldLives) {
			currentLives = oldLives;
		}
		
		int resultScore;
		if(currentLives < oldLives) {
			resultScore = 80;
		}
		else {
			resultScore = description.getMspacmanDistance() - game.getShortestPathDistance(ghost, mspacman, lastMove);
		}
		
		GhostResult result = (GhostResult)bCase.getResult();
		result.setScore(resultScore);	
		result.initializeCounter();
		bCase.setResult(result);
	}
	
	public void removeOldCase(CBRCase bCase) {
		
		boolean edible = ((GhostDescription) bCase.getDescription()).getEdible();
		
		Collection<CBRCase> caseToModify = new ArrayList<>(1);
		caseToModify.add(bCase);
		
		if(edible)	
			this.caseBaseEdible.forgetCases(caseToModify);
		else
			this.caseBaseChasing.forgetCases(caseToModify);
	}

	
	private void retainCase(CBRCase bCase)
	{
		boolean edible = ((GhostDescription) bCase.getDescription()).getEdible();
		
		// EN caso de que el map oldCases contenga el bCase, queremos NO guardarlo, e incrementar el caso oldCase mapeado.
		if(oldCases.containsKey(bCase)) {
			CBRCase oldCase = oldCases.get(bCase);
			
			// Eliminamos de la base de casos el NN con mayor reut
			removeOldCase(oldCase);
			
			// Incrementamos el contador (numReps) y hacemos la media de la revision y del anterior caso.
			GhostResult resultToModify = (GhostResult) oldCase.getResult();
			GhostResult revisedResult = (GhostResult) bCase.getResult();
			
			int newScore = (resultToModify.getScore() + revisedResult.getScore()) / 2;
			resultToModify.setScore(newScore);
			
			resultToModify.incrementCounter();
			
			oldCase.setResult(resultToModify);
			// Lo a�adimos de vuelta a la base de casos
			if(edible)	
				StoreCasesMethod.storeCase(this.caseBaseEdible, oldCase);
			else
				StoreCasesMethod.storeCase(this.caseBaseChasing, oldCase);
			
			oldCases.remove(bCase);
		}
		else {
			if(edible)	
				StoreCasesMethod.storeCase(this.caseBaseEdible, bCase);
			else
				StoreCasesMethod.storeCase(this.caseBaseChasing, bCase);
		}
		
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

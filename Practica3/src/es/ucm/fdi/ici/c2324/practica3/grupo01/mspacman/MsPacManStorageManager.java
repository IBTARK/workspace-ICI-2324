package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import java.util.Map;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import pacman.game.Game;

public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	Map<CBRCase, CBRCase> chosenReusedCaseMap;
	
	public static final double SCORE_TH = 10;
	public static final double SIM_TH = 0.9;

	private final static int TIME_WINDOW = 3;
	
	public MsPacManStorageManager()
	{
		this.buffer = new Vector<CBRCase>();
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setCaseBase(CBRCaseBase caseBase)
	{
		this.caseBase = caseBase;
	}
	
	public void reviseAndRetain(CBRCase newCase, Map<CBRCase, CBRCase> chosenReusedCaseMap) {
		this.chosenReusedCaseMap = chosenReusedCaseMap;
		
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		
		if(((MsPacManResult)bCase.getResult()).getScore() > SCORE_TH)
			retainCase(bCase);
		
	}
	
	public void reviseAndRetainGeneric(CBRCase newCase) {
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		
		StoreCasesMethod.storeCase(this.caseBase, bCase);
	}
	
	private void reviseCase(CBRCase bCase) {
		MsPacManDescription description = (MsPacManDescription)bCase.getDescription();
		
		//Information of the case being revised
		int oldScore = description.getScore(), oldLives = description.getLives();
		//Information of the current state of the game
		int currentScore = game.getScore(), currentLives = description.getLives();
		
	
		int resultValue = (currentScore - oldScore) / (oldLives - currentLives + 1);
		
		MsPacManResult result = (MsPacManResult)bCase.getResult();
		result.setScore(resultValue);
		bCase.setResult(result);
	}
	
	/**
	 * The 
	 * @param bCase
	 */
	private void retainCase(CBRCase bCase)
	{
		//Store the old case right now into the case base
		//Alternatively we could store all them when game finishes in close() method
		
		//here you should also check if the case must be stored into persistence (too similar to existing ones, etc.)
		
		if(chosenReusedCaseMap.get(bCase) == null) StoreCasesMethod.storeCase(this.caseBase, bCase);
		else {
			SimPacman sim = new SimPacman();
			//TODO 	QUITAR TRUE
			if(true || sim.compute(bCase.getDescription(), chosenReusedCaseMap.get(bCase).getDescription(), bCase, chosenReusedCaseMap.get(bCase), null) < SIM_TH) {
				StoreCasesMethod.storeCase(this.caseBase, bCase);
			}
			else {
				MsPacManResult res = (MsPacManResult) chosenReusedCaseMap.get(bCase).getResult();
				MsPacManResult resAct = (MsPacManResult) bCase.getResult();
				//Mean of the scores of the similar cases
				res.setScore((res.getScore() * res.getNumReps() + resAct.getScore() ) / (res.getNumReps() + 1));
				res.setNumReps(res.getNumReps() + 1);
				
				//TODO sustituir en caseBase
			}
		}
		chosenReusedCaseMap.remove(bCase);
	}

	public void close() {
		for(CBRCase oldCase: this.buffer)
		{
			reviseCase(oldCase);
			retainCase(oldCase);
		}
		this.buffer.removeAllElements();
	}

	public int getPendingCases() {
		return this.buffer.size();
	}
}

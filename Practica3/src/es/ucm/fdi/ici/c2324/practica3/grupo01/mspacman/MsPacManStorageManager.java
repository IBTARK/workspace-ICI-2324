package es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman;

import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.method.retain.StoreCasesMethod;
import es.ucm.fdi.ici.c2324.practica3.grupo01.Pair;
import pacman.game.Game;

public class MsPacManStorageManager {

	Game game;
	CBRCaseBase caseBase;
	Vector<CBRCase> buffer;
	Map<CBRCase, Pair<CBRCase,Boolean>> chosenReusedCaseMap;

	public static final double SIM_TH = 0.999;
	private final static int TIME_WINDOW = 2;
	
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
	
	public void reviseAndRetain(CBRCase newCase, Map<CBRCase, Pair<CBRCase,Boolean>> chosenReusedCaseMap) {
		this.chosenReusedCaseMap = chosenReusedCaseMap;
		
		this.buffer.add(newCase);
		
		//Buffer not full yet.
		if(this.buffer.size()<TIME_WINDOW)
			return;
		
		
		CBRCase bCase = this.buffer.remove(0);
		reviseCase(bCase);
		
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
		
	
		int resultValue = 1 + (currentScore - oldScore) / ((oldLives - currentLives)*4 + 1);
		
		((MsPacManResult)bCase.getResult()).setScore(resultValue);
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
		
		//Random generated action
		if(chosenReusedCaseMap.get(bCase) == null) 
			StoreCasesMethod.storeCase(this.caseBase, bCase);
		//Case reused from generic case base
		else if (chosenReusedCaseMap.get(bCase).getSecond()) {
			CBRCase reusedCase = chosenReusedCaseMap.get(bCase).getFirst();
			MsPacManResult res = (MsPacManResult)reusedCase.getResult();
			
			//if (res.getScore() < ((MsPacManResult)bCase.getResult()).getScore())
				StoreCasesMethod.storeCase(this.caseBase, reusedCase);
		}
		//Case reused from specific case base
		else {
			CBRCase reusedCase = chosenReusedCaseMap.get(bCase).getFirst();
			MsPacManResult res = (MsPacManResult)reusedCase.getResult();
			MsPacManResult resAct = (MsPacManResult) bCase.getResult();
			double sim = new SimPacman().compute(bCase.getDescription(), reusedCase.getDescription(), bCase, reusedCase, null);
			
			if(sim < SIM_TH) {
				StoreCasesMethod.storeCase(this.caseBase, bCase);
			}
			else {
				//Mean of the scores of the similar cases
				res.setScore((res.getScore() * res.getNumReps() + resAct.getScore() ) / (res.getNumReps() + 1));
				res.setNumReps(res.getNumReps() + 1);
				
				//Substitute in caseBase
				this.caseBase.forgetCases(Arrays.asList(reusedCase));
				StoreCasesMethod.storeCase(this.caseBase, reusedCase);
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

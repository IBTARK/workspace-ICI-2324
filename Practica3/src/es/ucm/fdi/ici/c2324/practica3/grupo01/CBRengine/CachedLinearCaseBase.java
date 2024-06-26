package es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine;

import java.util.ArrayList;
import java.util.Collection;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CaseBaseFilter;
import es.ucm.fdi.gaia.jcolibri.cbrcore.Connector;
import es.ucm.fdi.gaia.jcolibri.exception.InitializingException;

import es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman.MsPacManResult;

/**
 * Cached case base that only persists cases when closing.
 * learn() and forget() are not synchronized with the persistence until close() is invoked.
 * <p>
 * This class presents better performance that LinelCaseBase as only access to the persistence once.
 * This case base is used for evaluation.
 * 
 * @author Juan A. Recio-García
 */
public class CachedLinearCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> originalCases;
	private Collection<CBRCase> workingCases;
	private Collection<CBRCase> casesToRemove;
	
	private Integer nextId;
	private int finalReward;
	private boolean reward = false;
	
	/**
	 * Closes the case base saving or deleting the cases of the persistence media
	 */
	public void close() {
		originalCases.removeAll(casesToRemove);
		Collection<CBRCase> casesToStore = new ArrayList<>(workingCases);
		casesToStore.removeAll(originalCases);

		connector.deleteCases(casesToRemove);
		//If a final score has been give, applies it to the cases to store
		if (reward) rewardCases(casesToStore);
		connector.storeCases(casesToStore);
		connector.close();
	}

	/**
	 * Forgets cases. It only removes the cases from the storage media when closing.
	 */
	public void forgetCases(Collection<CBRCase> cases) {
		workingCases.removeAll(cases);
		casesToRemove.addAll(cases);
	}

	/**
	 * Returns working cases.
	 */
	public Collection<CBRCase> getCases() {
		return originalCases;
	}

	public Collection<CBRCase> getCases(CaseBaseFilter filter) {
		return null;
	}

	/**
	 * Initializes the Case Base with the cases read from the given connector.
	 */
	public void init(Connector connector) throws InitializingException {
		this.connector = connector;
		originalCases = this.connector.retrieveAllCases();	
		workingCases = new java.util.ArrayList<CBRCase>(originalCases);
		casesToRemove = new ArrayList<>();
		nextId = workingCases.size();
	}
	

	public Integer getNextId()
	{
		return nextId;
	}
	
	/**
	 * Learns cases that are only saved when closing the Case Base.
	 */
	public void learnCases(Collection<CBRCase> cases) {
		workingCases.addAll(cases);
		nextId += cases.size();
	}

	/**
		Assigns a score given to the final score of the game, which will be applied to every new case's result
	*/
	public void setReward(int score) {
		this.finalReward = score;
		reward = true;
	}
	
	/**
		Applies the final score given with setReward to every new/modified case's result
	*/
	private void rewardCases(Collection<CBRCase> casesToStore) {
		casesToStore.forEach((c) -> { 
			MsPacManResult res = (MsPacManResult) c.getResult();
			if (res.getFinalScore() == null)
				res.setFinalScore(finalReward);	//If the case is new, sets the final score
			else
				res.setFinalScore((res.getFinalScore() * res.getNumReps() + finalReward) / (res.getNumReps() + 1));
			});		//If the case already existed, sets the mean of all of its results
	}
}

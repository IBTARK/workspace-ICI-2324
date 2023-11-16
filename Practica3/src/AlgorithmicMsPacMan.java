

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCase;
import es.ucm.fdi.gaia.jcolibri.cbrcore.CBRCaseBase;
import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.CachedLinearCaseBase;
import es.ucm.fdi.ici.c2324.practica3.grupo01.CBRengine.CustomPlainTextConnector;
import es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman.MsPacManDescription;
import es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman.MsPacManResult;
import es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman.MsPacManSolution;
import es.ucm.fdi.ici.c2324.practica3.grupo01.mspacman.MsPacManStorageManager;
import pacman.controllers.PacmanController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class AlgorithmicMsPacMan extends PacmanController{
	
	private Game game;
	private double distMax;
	private static final String NAME = "I+D";
	private static final double RAND_LIM = 1;
	private static final int SECURITY_EDIBLE_TIME = 20;
	private static final double k1 = 18000.0; //Constant of edible ghost
	private static final double k2 = 10000.0; //Constant of nearest chasing ghost
	private static final double k3 = 8; //Constant of power pills
	private static final double k4 = 1.0/20; //Constant of pills
	private static final double k5 = 8500.0;   //Constant of other chasing ghosts

	private MsPacManStorageManager storageManager;
	private CustomPlainTextConnector connectorGeneric;
	private CBRCaseBase genericCaseBase;
	final static String CONNECTOR_FILE_PATH = "es/ucm/fdi/ici/c2324/practica3/grupo01/mspacman/plaintextconfig.xml";
	final static String CASE_BASE_PATH = "cbrdata"+File.separator+"grupo01"+File.separator+"mspacman"+File.separator;
	
	public AlgorithmicMsPacMan() throws Exception {
		initCaseBase();
	}

	//Name of the team
	public String getName() {
		return NAME;
	}
	
	private void initCaseBase() throws Exception {
		connectorGeneric = new CustomPlainTextConnector();
		genericCaseBase = new CachedLinearCaseBase();
		
		connectorGeneric.initFromXMLfile(FileIO.findFile(CONNECTOR_FILE_PATH));
		
		//Do not use default case base path in the xml file. Instead use custom file path for each opponent.
		//Note that you can create any subfolder of files to store the case base inside your "cbrdata/grupoXX" folder.
		connectorGeneric.setCaseBaseFile(CASE_BASE_PATH, "generic.csv");
		
		storageManager = new MsPacManStorageManager();
		this.storageManager.setCaseBase(genericCaseBase);
		
		genericCaseBase.init(connectorGeneric);
	}
	
	public void closeCaseBase() {
		genericCaseBase.close();
		connectorGeneric.close();
	}
	
	private void storeCase(Game game, MOVE action) {
		CBRCase newCase = createNewCase(game, action);
		
		storageManager.reviseAndRetainGeneric(newCase);
	}
	
	private CBRCase createNewCase(Game game, MOVE action) {
		//Code before cycle in MsPacMan.java
		MsPacManInput input = new MsPacManInput(game);
		input.parseInput();
		storageManager.setGame(game);
		//createNewCase from MsPacManCBREngine.java, using input query
		CBRCase newCase = new CBRCase();
		MsPacManDescription newDescription = (MsPacManDescription) input.getQuery().getDescription();
		MsPacManResult newResult = new MsPacManResult();
		MsPacManSolution newSolution = new MsPacManSolution();
		int newId = this.genericCaseBase.getCases().size();
		newId+= storageManager.getPendingCases();
		newDescription.setId(newId);
		newResult.setId(newId);
		newSolution.setId(newId);
		newSolution.setAction(action);
		newCase.setDescription(newDescription);
		newCase.setResult(newResult);
		newCase.setSolution(newSolution);
		return newCase;
	}
	
	//Gets the best move of MsPacMan
    public MOVE getMove(Game game, long timeDue) {
    	this.game = game;
    	distMax = Math.sqrt(2 * game.getNumberOfNodes());
    	
    	int pos = game.getPacmanCurrentNodeIndex(); //MsPacMans position 
    	MOVE[] possibleMoves = game.getPossibleMoves(pos, game.getPacmanLastMoveMade()); //Possible moves of MsPacMan
    	
    	//If there is more than one possible movement
    	if (possibleMoves.length > 1) {
    		double newScore; 
    		ArrayList<MOVE> bestMoves = new ArrayList<>();
    		ArrayList<Double> bestScores = new ArrayList<>();
    		
    		//The best movement of the ones possible is selected
    		for(MOVE m : possibleMoves) {
    			newScore = calcScore(pos, m);
    			
    			//If the actual movement is better, is saved as the best one until this moment
    			if(bestMoves.isEmpty()) {
    				bestMoves.add(m);
    				bestScores.add(newScore);
    			}
    			else if (newScore + RAND_LIM >= bestScores.get(0)) {
    				if (newScore - RAND_LIM > bestScores.get(bestScores.size()-1)) {
    					bestScores.clear();
    					bestMoves.clear();
    				}
    				else if (newScore - RAND_LIM > bestScores.get(0)) {
    					bestScores.remove(0);
    					bestMoves.remove(0);
    				}
    				bestScores.add(newScore);
    				bestMoves.add(m);
    			}
    		}
    		int bestIdx = bestMoves.size() == 1 ? 0 : new Random().nextInt(bestMoves.size());
    		
    		storeCase(game, bestMoves.get(bestIdx));
    		
    		return bestMoves.get(bestIdx);
    	}
    	
    	
        return possibleMoves[0];
    }
    
    //Computes the score of a given movement
    private double calcScore(int pos, MOVE move) {
    	int newPos = game.getNeighbour(pos, move);
    	
    	double scoreGhost = scoreDistGhost(pos, newPos, move);
    	double scorePPill = scorePPill(pos, newPos, move);
    	double scorePill = scorePill(pos, newPos, move);
    
    	
    	return scoreGhost + scorePPill + scorePill;
    }
    
    //Score associated to the ghosts 
    private double scoreDistGhost(int pos, int newPos, MOVE m) {
    	double score = 0;
    	GHOST nearestEdible = getNearestEdibleGhost((int) distMax), nearestChasing = getNearestChasingGhost();
    	
    	//If there is an edible ghost and the remaining edible time is superior than 10 ticks
    	if(nearestEdible != null && game.getGhostEdibleTime(nearestEdible) > SECURITY_EDIBLE_TIME) {
    		int distNow = game.getShortestPathDistance(pos, game.getGhostCurrentNodeIndex(nearestEdible), game.getPacmanLastMoveMade());
    		int distNext = game.getShortestPathDistance(newPos, game.getGhostCurrentNodeIndex(nearestEdible), m);
    		//Score associated to the nearest edible ghost
    		score += -k1 / (distNow +10);
    		score += k1 / (distNext +10);
    	}

    	
    	//If there is a chasing ghost
    	if(nearestChasing != null) {
    		int distNearestNow = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(nearestChasing), pos);
    		int distNearestNext = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(nearestChasing), newPos);
    		//Score associated to the nearest chasing ghost
    		score +=  k2 / (distNearestNow + 5);
    		score += -k2 / (distNearestNext + 5);
    		
    		//The rest of the chasing ghost are taken into consideration
			for (GHOST g : GHOST.values()) {
				int ghostNode = game.getGhostCurrentNodeIndex(g); 
				
				//It the ghost is not the nearest chasing one and its edible and its not on the lair, is taken into consideration
				if (g != nearestChasing && !game.isGhostEdible(g) && game.getGhostLairTime(g) <= 0){
					int distNow = game.getShortestPathDistance(ghostNode, pos);
					int distNext = game.getShortestPathDistance(ghostNode, newPos);
					score +=  k5 / (distNow + 10);
		    		score += -k5 / (distNext + 10);	
		    	}
			}	
		}
    	
    	return score;
    }
    
    //Score associated to the power pills
    private double scorePPill(int pos, int newPos, MOVE m) {
    	if (game.getNumberOfActivePowerPills() < 1)
    		return 0;
    	
    	double closenessGhost;
    	try {
    		closenessGhost = game.getShortestPathDistance(pos, 
	    			game.getGhostCurrentNodeIndex(getNearestChasingGhost()));
    	} catch (NullPointerException e) {
    		return 0;
    	}
    	//Gets the nearest power pill
    	int nearestPPill = getNearestPowerPill();
    	//Distance from MsPacMan to the nearest power pill in the actual position
    	int distPPillNow = game.getShortestPathDistance(pos, nearestPPill, game.getPacmanLastMoveMade());
    	//Distance from MsPacMan to the nearest power pill in the next position
		int distPPillNext = (game.getShortestPathDistance(newPos, nearestPPill, m));
		
		int distPPill = distPPillNext- distPPillNow;
    	
		//Score
		double punt = k3 / closenessGhost * (-(distPPill - 60) ^ 3);
    	return punt;
    }
    
    //Score associated to the pills
    private double scorePill(int pos, int newPos, MOVE m) {
    	Integer nearestPill = getNearestPill(), distPill = 0;
    	//If the are remaining power pills
    	if(nearestPill != null) {
    		int distPillNow = game.getShortestPathDistance(pos, nearestPill, game.getPacmanLastMoveMade());
    		int distPillNext = (game.getShortestPathDistance(newPos, nearestPill, m));
    		//Score associated to the nearest power pill
    		distPill = distPillNow - distPillNext;
       	}
    	
    	//The fewer remaining pills there are more important it is to take them
		double score = (game.getNumberOfPills() - game.getNumberOfActivePills()) * distPill * k4;
    	return Math.max(score, 0);
    }
	
    //Get the nearest chasing ghost
	private GHOST getNearestChasingGhost() {
		double minDist = Integer.MAX_VALUE;
		GHOST nearest = null;
		for (GHOST g : GHOST.values()) {
			//Only those that are not on the lair
			if(game.getGhostLairTime(g) <= 0) {
				double dist = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(g), game.getPacmanCurrentNodeIndex());
				if (dist < minDist && !game.isGhostEdible(g))
					nearest = g;
			}
		}
		return nearest;
	}
	
	//Gets the nearest edible ghost
	private GHOST getNearestEdibleGhost(int limit) {
		double minDist = limit + 1;
		GHOST nearest = null;
		for (GHOST g : GHOST.values()) {
			if(game.getGhostLairTime(g) <= 0) {
				double dist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(g), game.getPacmanLastMoveMade());
				if (dist < minDist && game.isGhostEdible(g))
					nearest = g;
			}
		}
		return nearest;
	}
	
	//Gets the nearest pill
	private Integer getNearestPill() {
		Integer nearest = null, dist = Integer.MAX_VALUE;
		for (int i : game.getActivePillsIndices()) {
			int aux = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), i, game.getPacmanLastMoveMade());
			if (dist > aux) {
				nearest = i;
				dist = aux;
			}
		}
		return nearest;
	}
	
	//Gets the nearest power pill
	private Integer getNearestPowerPill() {
		int distNearestPPill = Integer.MAX_VALUE, dist;
		Integer nearestPPill = null;
		
		for(int pp : game.getActivePowerPillsIndices()) {
			dist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), pp, game.getPacmanLastMoveMade());
			if(dist < distNearestPPill) {
				distNearestPPill = dist;
				nearestPPill = pp;
			}
		}
		
		return nearestPPill;
	}
}
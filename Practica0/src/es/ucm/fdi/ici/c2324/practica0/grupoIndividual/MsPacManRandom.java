package es.ucm.fdi.ici.c2324.practica0.grupoIndividual;

import java.awt.Color;
import java.util.Random;

import pacman.controllers.PacmanController;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.GameView;

public class MsPacManRandom extends PacmanController{
	
    private Random rnd = new Random();
    private MOVE[] allMoves = MOVE.values();

    @Override
    public MOVE getMove(Game game, long timeDue) {
    	
    	//Show lines to PPills
    	int[] activePowerPills=game.getActivePowerPillsIndices();
    	for(int i=0;i<activePowerPills.length;i++)
    	GameView.addLines(game,Color.CYAN,game.getPacmanCurrentNodeIndex(),activePowerPills[i]);
    	
    	//Show way to ghosts
    	Color[] colors = {Color.RED, Color.PINK, Color.CYAN, Color.ORANGE};
    	
    	for(Constants.GHOST g: Constants.GHOST.values()) {
    		int ghost = game.getGhostCurrentNodeIndex(g);
    		int mspacman = game.getPacmanCurrentNodeIndex();
    		if(game.getGhostLairTime(g)<=0)
    			GameView.addPoints(game,colors[g.ordinal()],game.getShortestPath(ghost,mspacman));
    	}
    	
        return allMoves[rnd.nextInt(allMoves.length)];
    }

}

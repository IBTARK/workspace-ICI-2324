package es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class RunAwayAction implements RulesAction {

    GHOST ghost;
    enum STRATEGY {AWAY, GOTO_CHASING};
    STRATEGY runAwayStrategy; 
    GHOST nearestChasing;
	public RunAwayAction(GHOST ghost) {
		this.ghost = ghost;
	}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			// RUNAWAYSTRATEGY
			Value value = actionFact.getSlotValue("runawaystrategy");
			if(value == null)
				return;
			String stringValue = value.stringValue(null);
			runAwayStrategy = STRATEGY.valueOf(stringValue);
			
			// NEARESTCHASING ( en caso de que la strategy sea GOTO_CHASING )
			value = actionFact.getSlotValue("nearestChasing");
			if(value == null)
				return;
			stringValue = value.stringValue(null);
			nearestChasing = GHOST.valueOf(stringValue);
		} catch (JessException e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			switch (runAwayStrategy) {
			case AWAY:
				nextMove = actHuirDeMsPacman(game);
				break;
			case GOTO_CHASING:
				nextMove = actHuirHaciaChasing(game);
				break;
			}
        }
            
        return nextMove;	
	}
	
	private MOVE actHuirHaciaChasing(Game game) {
		return game.getApproximateNextMoveTowardsTarget(
				game.getGhostCurrentNodeIndex(ghost), 
				game.getGhostCurrentNodeIndex(nearestChasing), 
				game.getGhostLastMoveMade(ghost), 
				DM.PATH);
	}

	private MOVE actHuirDeMsPacman(Game game) {
		return game.getApproximateNextMoveAwayFromTarget(
				game.getGhostCurrentNodeIndex(ghost), 
				game.getPacmanCurrentNodeIndex(), 
				game.getGhostLastMoveMade(ghost), 
				DM.PATH);
	}

	@Override
	public String getActionId() {
		return ghost+ "runsAway";
	}
}

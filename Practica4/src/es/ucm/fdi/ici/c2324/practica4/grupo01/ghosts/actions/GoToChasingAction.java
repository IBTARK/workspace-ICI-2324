package es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class GoToChasingAction implements RulesAction {

	GHOST ghost;
	int nearestChasing;
	
	public GoToChasingAction() {
		
	}
	@Override
	public void parseFact(Fact actionFact) {
		try {
			// GHOST_TYPE (NECESARIO EN TODAS LAS ACTIONS)
			Value value = actionFact.getSlotValue("ghostType");
			if(value == null)
				return;
			String stringValue = value.stringValue(null);
			ghost = GHOST.valueOf(stringValue);
			
			// NEARESTCHASING ( en caso de que la strategy sea GOTO_CHASING )
			value = actionFact.getSlotValue("nearestChasing");
			if(value == null)
				return;
			stringValue = value.stringValue(null);
			nearestChasing = Integer.parseInt(stringValue);
			
		} catch (JessException e) {
			e.printStackTrace();
		}
	
	}
	
	@Override
	public MOVE execute(Game game) {
		MOVE nextMove = MOVE.NEUTRAL;
		
		if (game.doesGhostRequireAction(ghost))        //if it requires an action
        {
			nextMove = game.getApproximateNextMoveTowardsTarget(
					game.getGhostCurrentNodeIndex(ghost), 
					nearestChasing, 
					game.getGhostLastMoveMade(ghost), 
					DM.PATH);
        }
            
        return nextMove;	
	}

	@Override
	public String getActionId() {
		return "GoToChasingAction";
	}

}

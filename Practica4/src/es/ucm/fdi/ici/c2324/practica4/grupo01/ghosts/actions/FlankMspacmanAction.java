package es.ucm.fdi.ici.c2324.practica4.grupo01.ghosts.actions;

import es.ucm.fdi.ici.rules.RulesAction;
import jess.Fact;
import jess.JessException;
import jess.Value;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class FlankMspacmanAction implements RulesAction {

    GHOST ghost;
    int junction;
    
	public FlankMspacmanAction() {}

	@Override
	public void parseFact(Fact actionFact) {
		try {
			// GHOST_TYPE (NECESARIO EN TODAS LAS ACTIONS)
			Value value = actionFact.getSlotValue("ghostType");
			if(value == null)
				return;
			String stringValue = value.stringValue(null);
			ghost = GHOST.valueOf(stringValue);
			
			value = actionFact.getSlotValue("junction");
			if(value == null)
				return;
			int intValue;
			intValue = value.intValue(null);
			junction = intValue;
			
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
					junction, 
					game.getGhostLastMoveMade(ghost), 
					DM.PATH);
        }
            
        return nextMove;	
	}

	@Override
	public String getActionId() {
		return "flankMspacman";
	}
}

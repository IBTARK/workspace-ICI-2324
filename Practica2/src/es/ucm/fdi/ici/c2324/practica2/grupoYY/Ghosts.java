package es.ucm.fdi.ici.c2324.practica2.grupoYY;

import java.awt.BorderLayout;
import java.util.EnumMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsCoordination;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.GhostsInput;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActCubrirPPill;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActDispersarse;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActEvitarPPill;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActFlanquear;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActHuirDirectamente;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActIrAChasing;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActIrAEdible;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActMantenerDistancias;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActMuerto;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActPerseguirDirectamente;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.actions.ActSepararseFantasma;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.HTDispersarseHuirDirecto;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.HTDispersarseMantenerDistancias;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.HTHuirDirectoDispersarse;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.HTHuirDirectoIrAChasing;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.HTIrAChasingHuirDirecto;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.HTMantenerDistanciasDispersarse;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTCubrirPPillPerseguirDirecto;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTEvitarPPillPerseguirDirecto;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTFlanquearPerseguirDirecto;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTFlanquearSepararseFantasma;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTIrAEdiblePerseguirDirecto;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTPerseguirDirectoCubrirPPill;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTPerseguirDirectoEvitarPPill;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTPerseguirDirectoFlanquear;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTPerseguirDirectoIrAEdible;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTPerseguirDirectoSepararseFantasma;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTSepararseFantasmaFlanquear;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.PTSepararseFantasmaPerseguirDirecto;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.THuirMuerto;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.THuirPerseguir;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.TMuertoPerseguir;
import es.ucm.fdi.ici.c2324.practica2.grupoYY.ghosts.transitions.TPerseguirHuir;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.Transition;
import es.ucm.fdi.ici.fsm.observers.ConsoleFSMObserver;
import es.ucm.fdi.ici.fsm.observers.GraphFSMObserver;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Ghosts extends GhostController {

	EnumMap<GHOST,FSM> fsms;
	GhostsCoordination coord;
	
	public Ghosts()
	{
		setName("Ghosts XX");
		coord = new GhostsCoordination();
		
		fsms = new EnumMap<GHOST,FSM>(GHOST.class);
		for(GHOST ghost: GHOST.values()) {
			FSM fsm = new FSM(ghost.name());
			
			fsm.addObserver(new ConsoleFSMObserver(ghost.name()));
			GraphFSMObserver graphObserver = new GraphFSMObserver(ghost.name());
			fsm.addObserver(graphObserver);
			
			FSM cfsmHuir = new FSM("Huir");
			GraphFSMObserver obsHuir = new GraphFSMObserver(cfsmHuir.toString());
			cfsmHuir.addObserver(obsHuir);
			SimpleState hStateHuirDirectamente = new SimpleState(new ActHuirDirectamente(ghost));
			SimpleState hStateIrAChasing = new SimpleState(new ActIrAChasing(ghost));
			SimpleState hStateDispersarse = new SimpleState(new ActDispersarse(ghost));
			SimpleState hStateMantenerDistancias = new SimpleState(new ActMantenerDistancias(ghost));
			Transition tranH1 = new HTHuirDirectoDispersarse(ghost);
			Transition tranH2 = new HTDispersarseHuirDirecto(ghost);
			Transition tranH3 = new HTMantenerDistanciasDispersarse(ghost);
			Transition tranH4 = new HTDispersarseMantenerDistancias(ghost);
			Transition tranH5 = new HTHuirDirectoIrAChasing(ghost);
			Transition tranH6 = new HTIrAChasingHuirDirecto(ghost);
			cfsmHuir.add(hStateHuirDirectamente, tranH1, hStateDispersarse);
			cfsmHuir.add(hStateDispersarse, tranH2, hStateHuirDirectamente);
			cfsmHuir.add(hStateMantenerDistancias, tranH3, hStateDispersarse);
			cfsmHuir.add(hStateDispersarse, tranH4, hStateMantenerDistancias);
			cfsmHuir.add(hStateHuirDirectamente, tranH5, hStateIrAChasing);
			cfsmHuir.add(hStateIrAChasing, tranH6, hStateHuirDirectamente);
			cfsmHuir.ready(hStateHuirDirectamente);
			
			FSM cfsmPerseguir = new FSM("Perseguir");
			GraphFSMObserver obsPerseguir = new GraphFSMObserver(cfsmPerseguir.toString());
			cfsmPerseguir.addObserver(obsPerseguir);
			SimpleState pStatePerseguirDirectamente = new SimpleState(new ActPerseguirDirectamente(ghost));
			SimpleState pStateSeperarseDeFantasma = new SimpleState(new ActSepararseFantasma(ghost));
			SimpleState pStateFlanquear = new SimpleState(new ActFlanquear(ghost));
			SimpleState pStateCubrirPPill = new SimpleState(new ActCubrirPPill(ghost)); 			
			SimpleState pStateEvitarPPill = new SimpleState(new ActEvitarPPill(ghost));
			SimpleState pStateIrAEdible = new SimpleState(new ActIrAEdible(ghost, coord));
			Transition tranP1 = new PTSepararseFantasmaPerseguirDirecto(ghost);
			Transition tranP2 = new PTPerseguirDirectoSepararseFantasma(ghost);
			Transition tranP3 = new PTPerseguirDirectoFlanquear(ghost);
			Transition tranP4 = new PTFlanquearPerseguirDirecto(ghost);
			Transition tranP5 = new PTPerseguirDirectoCubrirPPill(ghost);
			Transition tranP6 = new PTCubrirPPillPerseguirDirecto(ghost);
			Transition tranP7 = new PTEvitarPPillPerseguirDirecto(ghost);
			Transition tranP8 = new PTPerseguirDirectoEvitarPPill(ghost);
			Transition tranP9 = new PTPerseguirDirectoIrAEdible(ghost);
			Transition tranP10 = new PTIrAEdiblePerseguirDirecto(ghost);
			Transition tranP11 = new PTSepararseFantasmaFlanquear(ghost);
			Transition tranP12 = new PTFlanquearSepararseFantasma(ghost);
			cfsmPerseguir.add(pStateSeperarseDeFantasma, tranP1, pStatePerseguirDirectamente);
			cfsmPerseguir.add(pStatePerseguirDirectamente, tranP2, pStateSeperarseDeFantasma);
			cfsmPerseguir.add(pStatePerseguirDirectamente, tranP3, pStateFlanquear);
			cfsmPerseguir.add(pStateFlanquear, tranP4, pStatePerseguirDirectamente);
			cfsmPerseguir.add(pStatePerseguirDirectamente, tranP5, pStateCubrirPPill);
			cfsmPerseguir.add(pStateCubrirPPill, tranP6, pStatePerseguirDirectamente);
			cfsmPerseguir.add(pStateEvitarPPill, tranP7, pStatePerseguirDirectamente);
			cfsmPerseguir.add(pStatePerseguirDirectamente, tranP8, pStateEvitarPPill);
			cfsmPerseguir.add(pStatePerseguirDirectamente, tranP9, pStateIrAEdible);
			cfsmPerseguir.add(pStateIrAEdible, tranP10, pStatePerseguirDirectamente);
			cfsmPerseguir.add(pStateSeperarseDeFantasma, tranP11, pStateFlanquear);
			cfsmPerseguir.add(pStateFlanquear, tranP12, pStateSeperarseDeFantasma);
			cfsmPerseguir.ready(pStatePerseguirDirectamente);
			
			CompoundState compoundHuir = new CompoundState("Huir", cfsmHuir);
			CompoundState compoundPerseguir = new CompoundState("Perseguir", cfsmPerseguir);
			SimpleState stateMuerto = new SimpleState(new ActMuerto(ghost));

			Transition tran1 = new TPerseguirHuir(ghost);
			Transition tran2 = new THuirPerseguir(ghost);
			Transition tran3 = new THuirMuerto(ghost);
			Transition tran4 = new TMuertoPerseguir(ghost);

			fsm.add(compoundPerseguir, tran1, compoundHuir);
			fsm.add(compoundHuir, tran2, compoundPerseguir);
			fsm.add(compoundHuir, tran3, stateMuerto);
			fsm.add(stateMuerto, tran4, compoundPerseguir);
			
			fsm.ready(stateMuerto);
			
			//obsHuir.showInFrame(new Dimension(300,200));
			JFrame frame = new JFrame();
	    	JPanel main = new JPanel();
	    	main.setLayout(new BorderLayout());
	    	main.add(graphObserver.getAsPanel(true, null), BorderLayout.CENTER);
	    	main.add(obsHuir.getAsPanel(true, null), BorderLayout.EAST);
	    	main.add(obsPerseguir.getAsPanel(true, null), BorderLayout.WEST);
	    	frame.getContentPane().add(main);
	    	frame.pack();
	    	frame.setVisible(true);
			
			fsms.put(ghost, fsm);
		}
	}
	
	public void preCompute(String opponent) {
    	for(FSM fsm: fsms.values())
    		fsm.reset();
    }
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
		EnumMap<GHOST,MOVE> result = new EnumMap<GHOST,MOVE>(GHOST.class);
		
		coord.update(game);
		GhostsInput in = new GhostsInput(game, coord);
		
		for(GHOST ghost: GHOST.values())
		{
			FSM fsm = fsms.get(ghost);
			MOVE move = fsm.run(in);
			result.put(ghost, move);
		}
		
		return result;
		
	
		
	}

}

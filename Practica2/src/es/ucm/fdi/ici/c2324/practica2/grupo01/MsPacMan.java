package es.ucm.fdi.ici.c2324.practica2.grupo01;

import es.ucm.fdi.ici.Input;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.MsPacManInput;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActBuscarPills;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActEvitarPPill;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActFlanquearFantasma;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActHuirDeFantasma;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActHuirHaciaPPill;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActHuirRodeandoPPill;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActHuirVariosFantasmas;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActKamikazeAPill;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActKamikazeFantasma;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.actions.ActPerseguirFantasma;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.HTHuirFantasmaHuirVarios;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.HTHuirHaciaPPillHuirVarios;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.HTHuirHaciaPPillRodearAPPill;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.HTHuirVariosHuirFantasma;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.HTHuirVariosHuirHaciaPPill;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.HTRodearAPPillHuirVarios;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.HTRodearPPillHuirHaciaPPill;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.NTBuscarPillsEvitarPPill;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.NTEvitarPPillBuscarPills;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.PTFlanquearPerseguir;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.PTPerseguirFlanquear;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.THuirKamikazePill;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.THuirNeutral;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.THuirPerseguir;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.TKamikazeFantasmaHuir;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.TKamikazePillHuir;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.TNeutralHuir;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.TNeutralPerseguir;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.TPerseguirHuir;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.TPerseguirKamikazeFantasma;
import es.ucm.fdi.ici.c2324.practica2.grupo01.mspacman.transitions.TPerseguirNeutral;
import es.ucm.fdi.ici.fsm.CompoundState;
import es.ucm.fdi.ici.fsm.FSM;
import es.ucm.fdi.ici.fsm.SimpleState;
import es.ucm.fdi.ici.fsm.Transition;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/*
 * The Class NearestPillPacMan.
 */
public class MsPacMan extends PacmanController {

	FSM fsm;
	
	public String getName() {
		return "YI - DJ";
	}
	
	public MsPacMan() {
		setName("MsPacMan XX");
		
    	fsm = new FSM("MsPacMan");
    	
    	//fsm.addObserver(new ConsoleFSMObserver("MsPacMan"));
//    	GraphFSMObserver observer = new GraphFSMObserver(fsm.toString());
//    	fsm.addObserver(observer);
    	
    	//Huir FSM
    	FSM cfsmHuir = new FSM("Huir");
//    	GraphFSMObserver huirObserver = new GraphFSMObserver(cfsmHuir.toString());
//    	cfsmHuir.addObserver(huirObserver);
    	SimpleState hStateHuirDeUnFantasma = new SimpleState("Huir de un fantasma", new ActHuirDeFantasma());
    	SimpleState hStateHuirDeVariosFantasmas = new SimpleState("Huir de varios fantasmas", new ActHuirVariosFantasmas());
    	SimpleState hStateHaciaPPill = new SimpleState("Huir hacia PPill", new ActHuirHaciaPPill());
    	SimpleState hStateHuirRodeandoHaciaPPill = new SimpleState("Huir rodeando hacia PPill", new ActHuirRodeandoPPill());
    	Transition tranH1 = new HTHuirFantasmaHuirVarios();
    	Transition tranH2 = new HTHuirVariosHuirFantasma();
    	//Transition tranH3 = null; 
    	Transition tranH4 = new HTHuirVariosHuirHaciaPPill();
    	Transition tranH5 = new HTHuirHaciaPPillHuirVarios();
    	Transition tranH6 = new HTRodearAPPillHuirVarios();
    	Transition tranH7 = new HTHuirHaciaPPillRodearAPPill();
    	Transition tranH8 = new HTRodearPPillHuirHaciaPPill();
    	cfsmHuir.add(hStateHuirDeUnFantasma, tranH1, hStateHuirDeVariosFantasmas);
    	cfsmHuir.add(hStateHuirDeVariosFantasmas, tranH2, hStateHuirDeUnFantasma);
    	//cfsmHuir.add(hStateHuirDeUnFantasma, tranH3, hStateHaciaPPill);
    	cfsmHuir.add(hStateHuirDeVariosFantasmas, tranH4, hStateHaciaPPill);
    	cfsmHuir.add(hStateHaciaPPill, tranH5, hStateHuirDeVariosFantasmas);
    	cfsmHuir.add(hStateHuirRodeandoHaciaPPill, tranH6, hStateHuirDeVariosFantasmas);
    	cfsmHuir.add(hStateHaciaPPill, tranH7, hStateHuirRodeandoHaciaPPill);
    	cfsmHuir.add(hStateHuirRodeandoHaciaPPill, tranH8, hStateHaciaPPill);
    	cfsmHuir.ready(hStateHuirDeUnFantasma);
    	
    	//Perseguir FSM
    	FSM cfsmPerseguir = new FSM("Perseguir");
//    	GraphFSMObserver perseguirObserver = new GraphFSMObserver(cfsmPerseguir.toString());
//    	cfsmPerseguir.addObserver(perseguirObserver);
    	SimpleState pStatePerseguirFantasma = new SimpleState("Perseguir fantasma", new ActPerseguirFantasma()); 
    	SimpleState pStateFlanquearFantasma = new SimpleState("Flanquear fantasma", new ActFlanquearFantasma());
    	Transition tranP1 = new PTPerseguirFlanquear();
    	Transition tranP2 = new PTFlanquearPerseguir();
    	cfsmPerseguir.add(pStatePerseguirFantasma, tranP1, pStateFlanquearFantasma);
    	cfsmPerseguir.add(pStateFlanquearFantasma, tranP2, pStatePerseguirFantasma);
    	cfsmPerseguir.ready(pStatePerseguirFantasma);
    	
    	//Neutral FSM
    	FSM cfsmNeutral = new FSM("Neutral");
//    	GraphFSMObserver neutralObserver = new GraphFSMObserver(cfsmNeutral.toString());
//    	cfsmNeutral.addObserver(neutralObserver);
    	SimpleState nStateBuscarPills = new SimpleState("Buscar pills", new ActBuscarPills());
    	SimpleState nStateEvitarPPill = new SimpleState("Evitar PPill", new ActEvitarPPill());
    	Transition tranN1 = new NTBuscarPillsEvitarPPill();
    	Transition tranN2 = new NTEvitarPPillBuscarPills();
    	cfsmNeutral.add(nStateBuscarPills, tranN1, nStateEvitarPPill);
    	cfsmNeutral.add(nStateEvitarPPill, tranN2, nStateBuscarPills);
    	cfsmNeutral.ready(nStateBuscarPills);
    	
    	CompoundState compoundHuir = new CompoundState("Huir", cfsmHuir);
    	CompoundState compoundPerseguir = new CompoundState("Perseguir", cfsmPerseguir);
    	CompoundState compoundNeutral = new CompoundState("Neutral", cfsmNeutral);
    	
    	//Kamikaze fantasma state
    	SimpleState stateKamikazeFant = new SimpleState("Kamikaze a fantasma", new ActKamikazeFantasma());
    	//Kamikaze pill state
    	SimpleState stateKamikazePill = new SimpleState("Kamikaze a pills", new ActKamikazeAPill());
    	
    	Transition tran1 = new TPerseguirHuir();
    	Transition tran2 = new THuirPerseguir();
    	Transition tran3 = new TNeutralHuir();
    	Transition tran4 = new THuirNeutral();
    	Transition tran5 = new TNeutralPerseguir();
    	Transition tran6 = new TPerseguirNeutral(); 
    	Transition tran7 = new TPerseguirKamikazeFantasma();
    	Transition tran8 = new TKamikazeFantasmaHuir();
    	Transition tran9 = new TKamikazePillHuir();
    	Transition tran10 = new THuirKamikazePill();
    	
    	fsm.add(compoundPerseguir, tran1, compoundHuir);
    	fsm.add(compoundHuir, tran2, compoundPerseguir);
    	fsm.add(compoundNeutral, tran3, compoundHuir);
    	fsm.add(compoundHuir, tran4, compoundNeutral);
    	fsm.add(compoundNeutral, tran5, compoundPerseguir);
    	fsm.add(compoundPerseguir, tran6, compoundNeutral);
    	fsm.add(compoundPerseguir, tran7, stateKamikazeFant);
    	fsm.add(stateKamikazeFant, tran8, compoundHuir);
    	fsm.add(stateKamikazePill, tran9, compoundHuir);
    	fsm.add(compoundHuir, tran10, stateKamikazePill);

    	fsm.ready(compoundHuir);
    	
    	
//    	JFrame frame = new JFrame();
//    	JPanel main = new JPanel();
//    	//main.setLayout(new BorderLayout());
//    	main.add(observer.getAsPanel(true, null));//, BorderLayout.CENTER);
//    	main.add(huirObserver.getAsPanel(true, null));//, BorderLayout.CENTER);
//    	main.add(neutralObserver.getAsPanel(true, null));//, BorderLayout.CENTER);
//    	main.add(perseguirObserver.getAsPanel(true, null));//, BorderLayout.CENTER);
//    	frame.getContentPane().add(main);
//    	frame.pack();
//    	frame.setVisible(true);
    	
	}
	
	
	public void preCompute(String opponent) {
    		fsm.reset();
    }
	
	
	
    /* (non-Javadoc)
     * @see pacman.controllers.Controller#getMove(pacman.game.Game, long)
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {
    	Input in = new MsPacManInput(game); 
    	return fsm.run(in);
    }
    
    
}
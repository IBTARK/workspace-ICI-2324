import es.ucm.fdi.ici.c2324.practica1.grupoID.MsPacMan;
import es.ucm.fdi.ici.c2324.practica3.grupo01.GhostsSimplified;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class ExecutorTestGhostSimplified {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        for(int i = 0; i < 10; i++) {
        	 try {
             	PacmanController pacMan = new MsPacMan();
     		    GhostController ghosts = new GhostsSimplified();
     		    System.out.println( 
     		        executor.runGame(pacMan, ghosts, 4) //last parameter defines speed
     		    );
     		    
             } catch (Exception e) {
             	e.printStackTrace();
             }
    	}
   	
    }
	
}

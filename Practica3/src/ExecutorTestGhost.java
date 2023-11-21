import es.ucm.fdi.ici.c2324.practica1.grupoID.MsPacMan;
import es.ucm.fdi.ici.c2324.practica3.grupo01.Ghosts;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class ExecutorTestGhost {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        for(int i = 0; i < 2; i++) {
        	 try {
             	PacmanController pacMan = new MsPacMan();
     		    GhostController ghosts = new Ghosts();
     		    
     		    System.out.println( 
     		        executor.runGame(pacMan, ghosts, 10) //last parameter defines speed
     		    );
     		    
     		    
     		    
             } catch (Exception e) {
             	e.printStackTrace();
             }
    	}
    }
	
}

import es.ucm.fdi.ici.c2324.practica4.grupo01.Ghosts;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class ExecutorTestGhost {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setGhostPO(false)
                .setPacmanPO(false)
                .setVisual(true)
                .setScaleFactor(3.0)
                .build();

        PacmanController pacMan = new PacManRandom();
        GhostController ghosts = new Ghosts();
        
        System.out.println( 
        		executor.runGame(pacMan, ghosts, 10)
        );
        
    }
}


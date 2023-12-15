
import es.ucm.fdi.ici.c2324.practica5.grupo01.Ghosts;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;
import pacman.game.internal.POType;


public class ExecutorTestGhost {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setGhostPO(true)
                .setPacmanPO(false)
                .setPacmanPOvisual(false)
                .setGhostsPOvisual(true)
                .setPOType(POType.RADIUS)
                .setSightLimit(30)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        PacmanController pacMan = new PacManRandom();
        GhostController ghosts = new Ghosts();
        
        System.out.println( 
        		executor.runGame(pacMan, ghosts, 40)
        );
        
    }
}

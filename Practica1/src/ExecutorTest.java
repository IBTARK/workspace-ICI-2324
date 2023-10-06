import es.ucm.fdi.ici.c2324.practica1.grupoYY.Ghosts;
import es.ucm.fdi.ici.c2324.practica1.grupoYY.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;

public class ExecutorTest {
	
	private static final int N = 50;

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        PacmanController pacMan = new MsPacMan();
        GhostController ghosts = new Ghosts();
        
        int score, total = 0;
        for (int i = 0; i < N; ++i) {
        	score = executor.runGame(pacMan, ghosts, 1); //last parameter defines speed
	        System.out.println(score);   
	        total += score;
        }
        System.out.println(String.format("Puntuación media: %d", total / N));
    }
	
}

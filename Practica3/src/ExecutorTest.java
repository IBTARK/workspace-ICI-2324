import pacman.Executor;
import pacman.controllers.GhostController;

public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        while (true)
        try {
        	AlgorithmicMsPacMan pacMan = new AlgorithmicMsPacMan();
		    GhostController ghosts = new GhostsRandom();
		    
		    System.out.println( 
		        executor.runGame(pacMan, ghosts, 1) //last parameter defines speed
		    );     
		    pacMan.closeCaseBase();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
	
}

import pacman.Executor;
import pacman.controllers.GhostController;

public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        for(int i = 0; i < 25; i++)
	        try {
	        	AlgorithmicMsPacMan pacMan = new AlgorithmicMsPacMan();
			    GhostController ghosts = new es.ucm.fdi.ici.c2223.practica1.grupo01.Ghosts();
			    
			    System.out.println( 
			        executor.runGame(pacMan, ghosts, 1) //last parameter defines speed
			    );     
			    pacMan.closeCaseBase();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
        for(int i = 0; i < 50; i++)
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
        
        for(int i = 0; i < 25; i++)
	        try {
	        	AlgorithmicMsPacMan pacMan = new AlgorithmicMsPacMan();
	        	AlgorithmicGhosts ghosts = new AlgorithmicGhosts();
			    
			    System.out.println( 
			        executor.runGame(pacMan, ghosts, 1) //last parameter defines speed
			    );     
			    pacMan.closeCaseBase();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
    }
	
}

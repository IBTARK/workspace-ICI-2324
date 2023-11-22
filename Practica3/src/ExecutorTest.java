import java.util.LinkedList;
import java.util.Queue;

import es.ucm.fdi.ici.c2324.practica3.grupo01.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;

public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        try {
        	Queue<Integer> cola = new LinkedList<>();
        	for(int i = 0; i < 300; i++) {
        		System.out.println(i);
        		MsPacMan pacMan = new MsPacMan();
    		    GhostController ghosts = new AlgorithmicGhosts();
    		    
    		    int score = executor.runGame(pacMan, ghosts, 5); //last parameter defines speed
    		    System.out.println();
    		    
    		    cola.add(score);
    		    if (cola.size() > 20) cola.remove();
    		    System.out.println(String.format("Media: %d", cola.stream().mapToInt(Integer::intValue).sum() / cola.size()));
        	}  
        } catch (Exception e) {
        	e.printStackTrace();
        }
	    
        /*
         try {
        	for(int i = 0; i < 100; i++) {
        		System.out.println(i);
        		AlgorithmicMsPacMan pacMan = new AlgorithmicMsPacMan();
    		    GhostController ghosts = new GhostsRandom();
    		    
    		    System.out.println( 
    		        executor.runGame(pacMan, ghosts, 0) //last parameter defines speed
    		    );
    		    pacMan.closeCaseBase();
        	} 
        	for(int i = 0; i < 50; i++) {
        		System.out.println(i);
        		AlgorithmicMsPacMan pacMan = new AlgorithmicMsPacMan();
    		    GhostController ghosts = new AlgorithmicGhosts();
    		    
    		    System.out.println( 
    		        executor.runGame(pacMan, ghosts, 0) //last parameter defines speed
    		    );
    		    pacMan.closeCaseBase();
        	}  
        } catch (Exception e) {
        	e.printStackTrace();
        }
         */
    }
}

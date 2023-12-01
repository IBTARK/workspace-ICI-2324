

import java.util.LinkedList;
import java.util.Queue;

import es.ucm.fdi.ici.c2324.practica4.grupo01.Ghosts;
import es.ucm.fdi.ici.c2324.practica4.grupo01.MsPacMan;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.PacmanController;


public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setGhostPO(false)
                .setPacmanPO(false)
                .setVisual(true)
                .setScaleFactor(3.0)
                .build();

        try {
        	Queue<Integer> cola = new LinkedList<>();
        	for(int i = 0; i < 300; i++) {
        		System.out.println(i);
        		PacmanController pacMan = new MsPacMan();
    		    GhostController ghosts = new Ghosts();
    		    
    		    int score = executor.runGame(pacMan, ghosts, 10); //last parameter defines speed
    		    System.out.println();
    		    
    		    cola.add(score);
    		    if (cola.size() > 20) cola.remove();
    		    System.out.println(String.format("Media: %d", cola.stream().mapToInt(Integer::intValue).sum() / cola.size()));
        	}  
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
}

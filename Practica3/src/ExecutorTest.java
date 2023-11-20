import pacman.Executor;
import pacman.controllers.GhostController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import es.ucm.fdi.gaia.jcolibri.util.FileIO;
import es.ucm.fdi.ici.c2324.practica3.grupo01.MsPacMan;

public class ExecutorTest {

    public static void main(String[] args) {
        Executor executor = new Executor.Builder()
                .setTickLimit(4000)
                .setVisual(true)
                .setScaleFactor(2.5)
                .build();

        try {
        	for(int i = 0; i < 475; i++) {
        		System.out.println(i);
        		MsPacMan pacMan = new MsPacMan();
    		    GhostController ghosts = new AlgorithmicGhosts();
    		    
    		    System.out.println( 
    		        executor.runGame(pacMan, ghosts, 1) //last parameter defines speed
    		    );
        	}  
        } catch (Exception e) {
        	e.printStackTrace();
        }
	    
    }
}

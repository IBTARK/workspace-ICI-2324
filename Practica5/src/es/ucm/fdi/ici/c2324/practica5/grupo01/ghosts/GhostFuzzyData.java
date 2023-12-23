package es.ucm.fdi.ici.c2324.practica5.grupo01.ghosts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pacman.game.Constants.MOVE;

public class GhostFuzzyData {

	private int mspacman;	
	private MOVE msLastMove;
	private List<Integer> level2Junctions;
	private Map<Integer, List<Integer>> level3Junctions;
	
	public GhostFuzzyData() {
		level2Junctions = new ArrayList<Integer>();
		level3Junctions = new HashMap<Integer,List<Integer>>();
	}
	
	public int getMspacman() {
		return mspacman;
	}
	public void setMspacman(int mspacman) {
		this.mspacman = mspacman;
	}
	public MOVE getMsLastMove() {
		return msLastMove;
	}
	public void setMsLastMove(MOVE msLastMove) {
		this.msLastMove = msLastMove;
	}

	public List<Integer> getLevel2Junctions() {
		return level2Junctions;
	}

	public void setLevel2Junctions(List<Integer> level2Junctions) {
		this.level2Junctions = new ArrayList<Integer>(level2Junctions);
	}
	
	public void deleteLevel2Junction(Integer level2Junction) {
		level2Junctions.remove(level2Junction);
		level3Junctions.remove(level2Junction);
	}

	public Map<Integer, List<Integer>> getLevel3Junctions() {
		return level3Junctions;
	}

	public void setLevel3Junctions(Map<Integer, List<Integer>> level3Junctions) {
		this.level3Junctions = level3Junctions;
	}
	
	public void deleteLevel2Junction(Integer level2Junction, Integer level3Junction) {
		List<Integer> level3s = level3Junctions.get(level2Junction);
		level3s.remove(level3Junction);
	}
}

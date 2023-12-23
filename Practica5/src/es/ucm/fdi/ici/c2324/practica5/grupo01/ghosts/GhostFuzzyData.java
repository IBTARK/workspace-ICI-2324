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
}

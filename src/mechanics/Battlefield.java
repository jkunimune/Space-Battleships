package mechanics;

import java.util.ArrayList;

/**
 * @author jkunimune
 * The class that handles the game and physics-engine, keeping track of all bodies, and updating things.
 */
public class Battlefield {

	public ArrayList<Body> bodies;
	
	
	public Battlefield() {
		double time = (double)System.currentTimeMillis();
		bodies = new ArrayList<Body>();
		bodies.add(new Battleship(-400000000*Univ.m, 0, true, time, this));
	}
	
	
	
	public ArrayList<Body> getBodies() {
		return bodies;
	}
	
	
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	
	public void spawn(Body b) {	// adds a new body to the battlefield
		bodies.add(b);
	}

}

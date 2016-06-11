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
		bodies.add(new Planet(0,0,6378100*Univ.m,"Earth",time));
		bodies.add(new Planet(0,384400000*Univ.m,1737400*Univ.m,"Moon",time));
		bodies.add(new Battleship(-400000000*Univ.m, 0, true, time));
	}
	
	
	
	public ArrayList<Body> getBodies() {
		return bodies;
	}
	
	
	public void update() {
		// TODO Auto-generated method stub
		
	}

}

package mechanics;

import java.util.ArrayList;

/**
 * @author jkunimune
 * The class that handles the game and physics-engine, keeping track of all bodies, and updating things.
 */
public class Battlefield {

	public ArrayList<Body> bodies;
	
	
	public Battlefield() {
		bodies = new ArrayList<Body>();
		bodies.add(new Planet(0,0,"Earth"));
		bodies.add(new Planet(0,384400000,"Moon"));
	}

}

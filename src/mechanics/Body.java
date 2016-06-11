package mechanics;

import java.util.ArrayList;

/**
 * @author jkunimune
 * An object with mass, position, velocity, appearance, and ability to collide with things.
 */
public class Body {

	ArrayList<double[]> pos;	// the set of positions that define the movement of this body over the course of the map
	
	
	
	Body(double x0, double y0, double vx0, double vy0, double t0) {
		double[] init = new double[5];	// each entry in pos must have five entries:
		init[0] = x0;	// x position
		init[1] = y0;	// y position
		init[2] = vx0;	// x velocity
		init[3] = vy0;	// y velocity
		init[4] = t0;	// time
		pos = new ArrayList<double[]>(1);
		pos.add(init);
	}
	
	
	
	public String spriteName() {	// gives the name of the current sprite for the GameScreen to reference
		return "_";
	}
	
	
	public double xValAt(double t) {	// returns x in pixels at time t
		for (int i = pos.size()-1; i >= 0; i --) {	// iterate through pos to find the correct motion segment
			final double[] position = pos.get(i);
			if (position[4] <= t) {	// they should be sorted chronologically
				return position[0] + position[2]*(t-position[4]);	// calculate position based on this
			}
		}
		return pos.get(0)[0];	// if it didn't find anything, just use the initial position
	}
	
	
	public double yValAt(double t) {	// returns y in pixels at time t
		for (int i = pos.size()-1; i >= 0; i --) {	// iterate through pos to find the correct motion segment
			final double[] position = pos.get(i);
			if (position[4] <= t) {	// they should be sorted chronologically
				return position[1] + position[3]*(t-position[4]);	// calculate position based on this
			}
		}
		return pos.get(0)[1];	// if it didn't find anything, just use the initial position
	}

}

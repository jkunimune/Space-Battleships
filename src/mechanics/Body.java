package mechanics;

import java.util.ArrayList;

/**
 * @author jkunimune
 * An object with mass, position, velocity, appearance, and ability to collide with things.
 */
public class Body {

	public static final double[] DEFAULT_TRANSFORM = {0.0, 1.0, 1.0};	// the default transformation (zero rotation and scale of 1)
	
	private ArrayList<double[]> pos;	// the set of positions that define the movement of this body over the course of the map
	public Battlefield space;
	
	
	
	Body(double x0, double y0, double vx0, double vy0, double t0, Battlefield field) {
		double[] init = new double[5];	// each entry in pos must have five entries:
		init[0] = x0;	// x position
		init[1] = y0;	// y position
		init[2] = vx0;	// x velocity
		init[3] = vy0;	// y velocity
		init[4] = t0;	// time
		pos = new ArrayList<double[]>(1);
		pos.add(init);
		space = field;
	}
	
	
	
	public String spriteName() {	// gives the name of the current sprite for the GameScreen to reference
		return "_";
	}
	
	
	public double[] spriteTransform(double t) {	// gives the rotation and scale factors for this object's sprite
		return DEFAULT_TRANSFORM;
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
	
	
	public double vxValAt(double t) {	// returns x in pixels at time t
		for (int i = pos.size()-1; i >= 0; i --) {	// iterate through pos to find the correct motion segment
			final double[] position = pos.get(i);
			if (position[4] <= t) {	// they should be sorted chronologically
				return position[2];	// calculate position based on this
			}
		}
		return pos.get(0)[0];	// if it didn't find anything, just use the initial position
	}
	
	
	public double vyValAt(double t) {	// returns y in pixels at time t
		for (int i = pos.size()-1; i >= 0; i --) {	// iterate through pos to find the correct motion segment
			final double[] position = pos.get(i);
			if (position[4] <= t) {	// they should be sorted chronologically
				return position[3];	// calculate position based on this
			}
		}
		return pos.get(0)[1];	// if it didn't find anything, just use the initial position
	}
	
	
	public double age(double t) {	// returns the number of milliseconds since this has been created
		return t-pos.get(0)[4];
	}

}

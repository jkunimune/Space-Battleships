package mechanics;

/**
 * @author jkunimune
 * An object with mass, position, velocity, appearance, and ability to collide with things.
 */
public class Body {

	double x,y;
	double vx,vy;
	
	
	Body(double x0, double y0, double vx0, double vy0) {
		x = x0;
		y = y0;
		vx = vx0;
		vy = vy0;
	}
	
	
	
	public String spriteName() {	// gives the name of the current sprite for the GameScreen to reference
		return "_";
	}
	
	
	public double xValAt(double t) {	// returns x in meters at time t
		return x;
	}
	
	
	public double yValAt(double t) {	// returns y in meters at time t
		return y;
	}

}

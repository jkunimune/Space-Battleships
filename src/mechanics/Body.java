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

}

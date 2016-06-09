package mechanics;

/**
 * @author jkunimune
 * A body with no control that travels with constant velocity at the speed of light and interacts with ships.
 */
public class Laser extends Body {

	Laser(double x0, double y0, double tht) {
		super(x0, y0, Math.cos(tht), Math.sin(tht));
		// TODO Auto-generated constructor stub
	}

}

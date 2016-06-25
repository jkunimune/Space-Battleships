/**
 * A signal that controls the behaviour of ships
 */
package mechanics;

/**
 * @author jkunimune
 *
 */
public class Order extends Body {

	private byte orderType;		// the type of order
	private byte targetShip;	// the ship being ordered
	
	
	Order(double x0, double y0, double t0, byte order, byte ship, Battlefield field) {
		super(x0, y0, 0, 0, t0, field);
		
		orderType = order;
		targetShip = ship;
	}
	
	
	@Override
	public String spriteName() {
		return "order"+orderType;
	}
	
	
	@Override
	public double[] spriteTransform(double t) {
		final double r = rValAt(t);
		double[] res = {0,r/250.0,r/250.0};	// 250 is the sprite radius, so dividing by 250 yields the scale factor
		if (res[1] < 2)
			return res;	// once it gets a certain size, terminate the sprite for heap space's sake
		res[1] = 0;
		res[2] = 0;
		return res;
	}
	
	
	private double rValAt(double t) {
		return age(t)*Univ.c;
	}

}

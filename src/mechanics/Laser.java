package mechanics;

/**
 * @author jkunimune
 * A body with no control that travels with constant velocity at the speed of light and interacts with ships.
 */
public class Laser extends Body {

	private double heading;
	
	
	
	Laser(double x0, double y0, double tht, double time, Battlefield space) {
		super(x0, y0, Univ.c*Math.cos(tht), Univ.c*Math.sin(tht), time, space);
		heading = tht;
	}
	
	
	
	@Override
	public String spriteName() {
		return "laser";
	}
	
	
	@Override
	public double[] spriteTransform() {
		final double[] res = {heading, 1.0, 1.0};
		return res;
	}

}

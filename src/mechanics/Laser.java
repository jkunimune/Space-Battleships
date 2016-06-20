package mechanics;

/**
 * @author jkunimune
 * A body with no control that travels with constant velocity at the speed of light and interacts with ships.
 */
public class Laser extends Body {

	private double heading;	// the direction
	protected double E;		// the energy
	
	
	
	Laser(double x0, double y0, double tht, double time, Battlefield space) {
		super(x0, y0, Univ.c*Math.cos(tht), Univ.c*Math.sin(tht), time, space);
		heading = tht;
		E = 1*Univ.MJ;
	}
	
	
	Laser(double x0, double y0, double tht, double time, Battlefield space, double energy) {
		super(x0, y0, Univ.c*Math.cos(tht), Univ.c*Math.sin(tht), time, space);
		heading = tht;
		E = energy;
	}
	
	
	
	@Override
	public String spriteName() {
		return "laser";
	}
	
	
	@Override
	public double[] spriteTransform(double t) {
		final double[] res = {heading, E/(1*Univ.MJ), E/(1*Univ.MJ)};
		return res;
	}

}

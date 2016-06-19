package mechanics;

/**
 * @author jkunimune
 * A body with no control that travels with constant velocity at the speed of light and interacts with ships.
 */
public class UVLaser extends Body {

	private double heading;	// the direction
	private double E;		// the energy
	
	
	
	UVLaser(double x0, double y0, double tht, double time, Battlefield space) {
		super(x0, y0, Univ.c*Math.cos(tht), Univ.c*Math.sin(tht), time, space);
		heading = tht;
		E = 1*Univ.MJ;
	}
	
	
	UVLaser(double x0, double y0, double tht, double time, Battlefield space, double energy) {
		super(x0, y0, Univ.c*Math.cos(tht), Univ.c*Math.sin(tht), time, space);
		heading = tht;
		E = energy;
	}
	
	
	
	@Override
	public String spriteName() {
		return "laserUV";
	}
	
	
	@Override
	public double[] spriteTransform(double t) {
		final double[] res = {heading, E/(1*Univ.MJ), E/(1*Univ.MJ)};
		return res;
	}

}

/**
 * A body with no control that travels with constant velocity at the speed of light and interacts with ships.
 */
package mechanics;

/**
 * @author jkunimune
 * 
 */
public class Laser extends Body {

	public static final double energyDens = Math.pow(10,-13)*Univ.MJ/Univ.km3;
	
	private double heading;	// the direction
	protected double E;		// the energy
	protected double r;		// the radius of effect
	
	private double collidedTime;
	
	
	
	Laser(double x0, double y0, double tht, double time, Battlefield space) {
		super(x0, y0, Univ.c*Math.cos(tht), Univ.c*Math.sin(tht), time, space);
		heading = tht;
		E = 1*Univ.MJ;
		r = Math.pow(E/energyDens*0.75/Math.PI, 1/3.0);	// approximate laser as a sphere
		collidedTime = Double.MAX_VALUE;
	}
	
	
	Laser(double x0, double y0, double tht, double time, Battlefield space, double energy) {
		super(x0, y0, Univ.c*Math.cos(tht), Univ.c*Math.sin(tht), time, space);
		heading = tht;
		E = energy;
		r = Math.pow(E/energyDens*0.75/Math.PI, 1/3.0);
		collidedTime = Double.MAX_VALUE;
	}
	
	
	
	@Override
	public void interactWith(Body that, double t) {
		if (t < collidedTime && that instanceof Ship && this.dist(that,t) < r) {
			((Ship) that).damaged(E, t);
			this.collidedTime = t;
			if (!that.existsAt(t))
				playSound("boom", t);
		}
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
	
	
	@Override
	public boolean existsAt(double t) {
		return super.existsAt(t) && t <= collidedTime;
	}

}

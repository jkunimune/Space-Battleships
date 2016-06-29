/**
 * A body with no control that travels with constant velocity at the speed of light and interacts with ships.
 */
package mechanics;

/**
 * @author jkunimune
 * @version 1.0
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
		r = rValFor(E);	// approximate laser as a sphere
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
			this.collide(t);
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
		final double scale = Math.pow(E/(1*Univ.MJ), 1/3.0);
		final double[] res = {heading, scale, scale};
		return res;
	}
	
	
	@Override
	public boolean existsAt(double t) {
		return super.existsAt(t) && t <= collidedTime;
	}
	
	
	public double EValAt(double t) {	// the energy
		return E;
	}
	
	
	public void collide(double t) {	// tells the game that this laser no longer exists
		collidedTime = t;
	}
	
	
	public static double rValFor(double E) {
		return Math.pow(E/energyDens*0.75/Math.PI, 1/3.0);	// gives the radius of a laser of energy E
	}

}

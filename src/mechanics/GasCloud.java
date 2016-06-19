/**
 * 
 */
package mechanics;

/**
 * @author jkunimune
 * A large body that absorbs and confounds lasers.
 */
public class GasCloud extends Body {

	GasCloud(double x0, double y0, double vx0, double vy0, double time, Battlefield space) {
		super(x0, y0, vx0, vy0, time, space);
	}
	
	
	
	@Override
	public String spriteName() {
		return "gas";
	}
	
	
	@Override
	public double[] spriteTransform(double t) {
		final double r = rValAt(t);
		final double[] res = {0,r/100.0,r/100.0};	// 100 is the sprite radius, so dividing by 100 yields the scale factor
		return res;
	}
	
	
	public double rValAt(double t) {
		return Math.sqrt(age(t)*1*Univ.s)*Univ.c/100;
	}

}

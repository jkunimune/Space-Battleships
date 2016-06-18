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
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "gas";
	}
	
	
	@Override
	public double[] spriteTransform() {
		final double[] res = {0,5,5};
		return res;
	}

}

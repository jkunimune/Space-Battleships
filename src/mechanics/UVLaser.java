package mechanics;

/**
 * @author jkunimune
 * A body with no control that travels with constant velocity at the speed of light and interacts with ships.
 */
public class UVLaser extends Laser {

	UVLaser(double x0, double y0, double tht, double time, Battlefield space) {
		super(x0, y0, tht, time, space);
	}
	
	
	UVLaser(double x0, double y0, double tht, double time, Battlefield space, double energy) {
		super(x0, y0, tht, time, space);
		E = energy;
	}
	
	
	
	@Override
	public String spriteName() {
		return "laserUV";
	}

}

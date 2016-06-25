package mechanics;

/**
 * @author jkunimune
 * A ship with a powerful radar, capable of detecting ships anywhere on the map, given time.
 */
public class Radar extends Ship {

	public Radar(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, time, pin, blue, space);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "radar"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {
		shoot(x, y, t);
	}

}

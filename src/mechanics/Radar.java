package mechanics;

/**
 * @author jkunimune
 * A ship with a powerful radar, capable of detecting ships anywhere on the map, given time.
 */
public class Radar extends Ship {

	public Radar(double newX, double newY, boolean blue, double time) {
		super(newX, newY, blue, time);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "radar"+super.spriteName();
	}

}

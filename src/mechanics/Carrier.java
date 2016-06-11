package mechanics;

/**
 * @author jkunimune
 * The command ship, most important of any fleet, which can't do much but give orders and move.
 */
public class Carrier extends Ship {

	public Carrier(double newX, double newY, boolean blue, double time) {
		super(newX, newY, blue, time);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "carrier"+super.spriteName();
	}

}

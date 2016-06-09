package mechanics;

/**
 * @author jkunimune
 * The command ship, most important of any fleet, which can't do much but give orders and move.
 */
public class Carrier extends Ship {

	public Carrier(double newX, double newY, boolean blue) {
		super(newX, newY, 1, blue);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "carrier"+super.spriteName();
	}

}

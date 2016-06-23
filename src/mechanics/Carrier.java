package mechanics;

/**
 * @author jkunimune
 * The command ship, most important of any fleet, which can't do much but give orders and move.
 */
public class Carrier extends Ship {

	public Carrier(double newX, double newY, boolean blue, double time, Battlefield space) {
		super(newX, newY, blue, time, space);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "carrier"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {}
	
	
	public void issueOrder(byte[] orderArr) {	// a Carrier-unique order
		System.out.println(orderArr[0]);
	}

}

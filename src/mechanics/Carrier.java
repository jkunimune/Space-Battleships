package mechanics;

import java.nio.ByteBuffer;

/**
 * The command ship, most important of any fleet, which can't do much but give orders and move.
 * @author jkunimune
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
	
	
	public void issueOrder(byte[] orderArr) {	// a Carrier-unique method
		ByteBuffer bb = ByteBuffer.wrap(orderArr);
		byte order = bb.get();
		byte ship = bb.get();
		long t = bb.getLong();
		space.spawn(new Order(xValAt(t), yValAt(t), t, order, ship, space));	// and does it
	}

}

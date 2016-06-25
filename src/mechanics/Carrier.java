package mechanics;

import java.nio.ByteBuffer;

/**
 * The command ship, most important of any fleet, which can't do much but give orders and move.
 * @author jkunimune
 */
public class Carrier extends Ship {

	public Carrier(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, time, pin, blue, space);
	}
	
	
	
	@Override
	public String spriteName() {
		return "carrier"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {}
	
	
	public void issueOrder(byte[] orderArr) {	// a Carrier-unique method
		ByteBuffer bb = ByteBuffer.wrap(orderArr);	// parses an array of bytes
		byte order = bb.get();						// for the important information about
		byte ship = bb.get();						// the order
		double x = bb.getDouble();
		double y = bb.getDouble();
		double t = bb.getDouble();
		space.spawn(new Order(xValAt(t), yValAt(t), t, order, ship, x, y, space));	// send the order!
	}

}

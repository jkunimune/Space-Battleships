package mechanics;

/**
 * @author jkunimune
 * A ship with powerful guns, long range, and a special ultraviolet laser that bypasses gas clouds.
 */
public class Battleship extends Ship {

	public Battleship(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, time, pin, blue, space);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "battleship"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {
		space.spawn(new UVLaser(xValAt(t), yValAt(t), Math.atan2(y-yValAt(t),x-xValAt(t)), t, space, 1*Univ.MJ));
		playSound("pew", t);	// play the pew pew sound
	}

}

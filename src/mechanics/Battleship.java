package mechanics;

/**
 * @author jkunimune
 * A ship with powerful guns, long range, and a special ultraviolet laser that bypasses gas clouds.
 */
public class Battleship extends Ship {

	public Battleship(double newX, double newY, boolean blue, double time) {
		super(newX, newY, blue, time);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "battleship"+super.spriteName();
	}

}

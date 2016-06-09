package mechanics;

/**
 * @author jkunimune
 * An object that can move around and produce lasers on command from the player or the client.
 */
public class Ship extends Body {

	private int type;		// the id for this particular kind of ship
	private boolean isBlue;	// whether it is blue or red
	
	public Ship(double newX, double newY, int newType, boolean blue) {
		super(newX, newY, 0, 0);
		type = newType;
		isBlue = blue;
	}
	
	
	
	@Override
	public String spriteName() {
		if (isBlue)	return "_b";
		else		return "_r";
	}

}

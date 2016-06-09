package mechanics;

/**
 * @author jkunimune
 * An object that can move around and produce lasers on command from the player or the client.
 */
public class Ship extends Body {

	private double x,y;
	private int type;
	
	public Ship(double newX, double newY, int newType) {
		super(newX, newY, 0, 0);
		type = newType;
	}

}

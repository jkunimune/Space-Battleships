package mechanics;

/**
 * @author jkunimune
 * An object that can move around and produce lasers on command from the player or the client.
 */
public class Ship extends Body {

	private boolean isBlue;	// whether it is blue or red
	private double health, energy;	// HP and PP (in pokemon terms)
	
	public Ship(double newX, double newY, boolean blue, double time) {
		super(newX, newY, Univ.c, 0, time);
		isBlue = blue;
		health = 1.0;
		energy = 1.0;
	}
	
	
	
	@Override
	public String spriteName() {
		if (isBlue)	return "_b";
		else		return "_r";
	}

}

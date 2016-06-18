package mechanics;

/**
 * @author jkunimune
 * An object that can move around and produce lasers on command from the player or the client.
 */
public class Ship extends Body {

	private boolean isBlue;	// whether it is blue or red
	private double health, energy;	// HP and PP (in pokemon terms)
	
	
	
	public Ship(double newX, double newY, boolean blue, double time, Battlefield space) {
		super(newX, newY, Univ.c/10, 0, time, space);
		isBlue = blue;
		health = 1.0;
		energy = 1.0;
		shoot(time);
	}
	
	
	
	@Override
	public String spriteName() {
		if (isBlue)	return "_b";
		else		return "_r";
	}
	
	
	public void shoot(double t) {
		space.spawn(new Laser(xValAt(t), yValAt(t), Math.random()*2*Math.PI, t, space));
	}

}

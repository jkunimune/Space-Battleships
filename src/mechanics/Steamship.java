package mechanics;

/**
 * @author jkunimune
 * A ship that creates gas clouds, which confound lasers.
 */
public class Steamship extends Ship {

	private double energy, health;
	
	
	
	public Steamship(double newX, double newY, boolean blue) {
		super(newX, newY, 1, blue);
		energy = 1.0;
		health = 1.0;
	}
	
	
	
	@Override
	public String spriteName() {
		return "steamship"+super.spriteName();
	}

}

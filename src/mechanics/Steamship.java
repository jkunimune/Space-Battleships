package mechanics;

/**
 * @author jkunimune
 * A ship that creates gas clouds, which confound lasers.
 */
public class Steamship extends Ship {

	public Steamship(double newX, double newY, boolean blue, double time, Battlefield space) {
		super(newX, newY, blue, time, space);
	}
	
	
	
	@Override
	public String spriteName() {
		return "steamship"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {
		space.spawn(new GasCloud(xValAt(t), yValAt(t), vxValAt(t), vyValAt(t), t, space));
		playSound("woosh",t);
	}

}

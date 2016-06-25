package mechanics;

/**
 * @author jkunimune
 * A small and quick ship capable of withstanding considerable damage.
 */
public class Scout extends Ship {

	public Scout(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, time, pin, blue, space);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "scout"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {
		shoot(x, y, t);
	}

}

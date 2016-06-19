package mechanics;

/**
 * @author jkunimune
 * A small and quick ship capable of withstanding considerable damage.
 */
public class Scout extends Ship {

	public Scout(double newX, double newY, boolean blue, double time, Battlefield space) {
		super(newX, newY, blue, time, space);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "scout"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {
		shoot(t, 0.5*Univ.MJ);
	}

}

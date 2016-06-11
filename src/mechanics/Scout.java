package mechanics;

/**
 * @author jkunimune
 * A small and quick ship capable of withstanding considerable damage.
 */
public class Scout extends Ship {

	public Scout(double newX, double newY, boolean blue, double time) {
		super(newX, newY, blue, time);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "scout"+super.spriteName();
	}

}

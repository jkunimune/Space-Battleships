package mechanics;

/**
 * @author jkunimune
 * A small and quick ship capable of withstanding considerable damage.
 */
public class Scout extends Ship {

	public Scout(double newX, double newY, boolean blue) {
		super(newX, newY, 1, blue);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "scout"+super.spriteName();
	}

}

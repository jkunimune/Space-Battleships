package mechanics;

/**
 * @author jkunimune
 * A ship capable of self-destruction.
 */
public class Bomb extends Ship {

	public Bomb(double newX, double newY, boolean blue) {
		super(newX, newY, 1, blue);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "bomb"+super.spriteName();
	}

}

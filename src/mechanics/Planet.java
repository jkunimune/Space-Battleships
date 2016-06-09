package mechanics;

/**
 * @author jkunimune
 * An inert sphere that destroys other bodies that get too close.
 */
public class Planet extends Body {

	private String name;
	
	
	Planet(double x0, double y0, String newName) {
		super(x0, y0, 0, 0);
		name = newName;
		// TODO Auto-generated constructor stub
	}

}

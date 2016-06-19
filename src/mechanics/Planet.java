package mechanics;

/**
 * @author jkunimune
 * An inert sphere that destroys other bodies that get too close.
 */
public class Planet extends Body {

	private String name;
	private double radius;
	
	
	
	Planet(double x0, double y0, double r, String newName, double time, Battlefield space) {
		super(x0, y0, 0, 0, time, space);
		name = newName;
		radius = r;
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return name;
	}

}

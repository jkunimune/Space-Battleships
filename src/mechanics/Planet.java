/**
 * An inert sphere that destroys other bodies that get too close.
 */
package mechanics;

/**
 * @author jkunimune
 * @version 1.0
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
	public void interactWith(Body that, double t) {
		if (this.dist(that, t) < radius) {
			if (that instanceof Ship)
				((Ship) that).damaged(((Ship) that).hValAt(t), t);
			else if (that instanceof Laser)
				((Laser) that).collide(t);
		}
	}
	
	
	@Override
	public String spriteName() {
		return name;
	}

}

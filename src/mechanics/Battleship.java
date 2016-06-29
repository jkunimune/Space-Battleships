/**
 * A ship with powerful guns, long range, and a special ultraviolet laser that bypasses gas clouds.
 */
package mechanics;

/**
 * @author jkunimune
 * @version 1.0
 */
public class Battleship extends Ship {

	public Battleship(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, time, pin, blue, space);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public String spriteName() {
		return "battleship"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {
		final double theta = Math.atan2(y-yValAt(t),x-xValAt(t));
		final double nrg = 1.5*Univ.MJ;
		final double spawnDist = Laser.rValFor(nrg);	// make sure you spawn it in front of the ship so it doesn't shoot itself
		space.spawn(new UVLaser(xValAt(t) + spawnDist*Math.cos(theta),
								yValAt(t) + spawnDist*Math.sin(theta),
								theta, t, space, nrg));
		playSound("pew", t);	// play the pew pew sound
	}

}

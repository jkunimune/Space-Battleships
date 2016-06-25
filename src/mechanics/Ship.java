package mechanics;

/**
 * @author jkunimune
 * An object that can move around and produce lasers on command from the player or the client.
 */
public abstract class Ship extends Body {

	private boolean isBlue;	// whether it is blue or red
	
	protected byte id;		// an identifier for this particular ship
	
	
	
	public Ship(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, 0, 0, time, space);
		id = pin;
		isBlue = blue;
	}
	
	
	
	@Override
	public String spriteName() {
		if (isBlue)	return "_b";
		else		return "_r";
	}
	
	
	public void shoot(double x, double y, double t) {	// shoot a 1 megajoule laser at time t
		space.spawn(new Laser(xValAt(t), yValAt(t), Math.atan2(y-yValAt(t),x-xValAt(t)), t, space, 1*Univ.MJ));
		playSound("pew", t);	// play the pew pew sound
	}
	
	
	public void move(double x, double y, double t) {	// move to the point x,y at a speed of c/10
		for (int i = pos.size()-1; i >= 0; i --) {	// first, clear any movement after this order
			if (pos.get(i)[0] >= t)	pos.remove(i);
			else					break;
		}
		final double x0 = xValAt(t);	// calculate the initial coordinates
		final double y0 = yValAt(t);
		final double delT = Math.hypot(x-x0, y-y0)/(Univ.c/10);	// the duration of the trip
		
		double[] newPos = {t, x0, y0, (x-x0)/delT, (y-y0)/delT};	// add a segment for the motion
		pos.add(newPos);
		double[] newnewPos = {t+delT, x, y, 0, 0};	// and have it stop afterward
		pos.add(newnewPos);
		
		playSound("blast", t);			// then make it play the blast sound at the beginning and end
		clearSoundsAfter(t);
		playSound("blast", t+delT);
	}
	
	
	public abstract void special(double x, double y, double t);
	
	
	public byte getID() {
		return id;
	}

}

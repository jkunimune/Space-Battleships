/**
 * Copyright (c) 2016 Kunimune
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mechanics;

/**
 * A body with no control that travels with constant velocity at the speed of
 * light and interacts with ships.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Laser extends PhysicalBody {

	public static final double ENERGY_DENS = Math.pow(10,-13)*Univ.MJ/Univ.km3;	// a pretty absurd value, but eh
	public static final double HALF_LIFE = 10*Univ.s;	// not really a half-life
	
	private double heading;	// the direction
	protected double E;		// the energy
	protected double r;		// the radius of effect
	
	private double collidedTime;
	
	
	
	public Laser(double x0, double y0, double tht, double time, Battlefield space) {
		super(x0, y0, Univ.c*Math.cos(tht), Univ.c*Math.sin(tht), time, space);
		heading = tht;
		E = 1*Univ.MJ;
		r = rValFor(E);	// approximate laser as a sphere
		collidedTime = Double.MAX_VALUE;
	}
	
	
	public Laser(double x0, double y0, double tht, double time, Battlefield space, double energy) {
		super(x0, y0, Univ.c*Math.cos(tht), Univ.c*Math.sin(tht), time, space);
		heading = tht;
		E = energy;
		r = rValFor(E);
		collidedTime = Double.MAX_VALUE;
	}
	
	
	
	@Override
	public void interactWith(PhysicalBody that, double t) {	// lasers interact with ships by killing them
		if (t < collidedTime && that instanceof Ship && space.dist(this,that,t) < r) {
			((Ship) that).damaged(E, t);
			for (int i = 0; i < E/(100*Univ.kJ); i ++)
				space.spawn(Debris.debris(xValAt(t), yValAt(t), heading, t, space));
			this.collide(t);
		}
	}
	
	
	@Override
	public String spriteName() {
		return "laser";
	}
	
	
	@Override
	public double[] spriteTransform(double t) {
		final double scale = Math.pow(E/(1*Univ.MJ), 1/3.0);
		final double[] res = {heading, scale, scale};
		return res;
	}
	
	
	@Override
	public boolean existsAt(double t) {
		return super.existsAt(t) && t <= collidedTime;
	}
	
	
	@Override
	public double tprime(Body observer, double to) {	// lasers need a different algorithm for this,
		final double c2 = Univ.c*Univ.c;				// because they travel at the speed of light
		final double[] position = pos.get(0);
		final double dt = position[0] - to;
		final double dx = position[1] - observer.xValAt(to);
		final double dy = position[2] - observer.yValAt(to);
		final double vx = position[3];
		final double vy = position[4];
		final double ts = -(dx*dx + dy*dy - c2*dt*dt) / (2*vx*dx + 2*vy*dy - 2*c2*dt) + position[0];
		if (ts < to)	return ts;
		else			return Double.NaN;
	}
	
	
	@Override
	public double luminosityAt(double t) {	// lasers are extremely bright
		return E/HALF_LIFE;	// (for gameplay reasons, not scientific ones)
	}
	
	
	public double EVal() {	// the energy value
		return E;
	}
	
	
	public void collide(double t) {	// tells the game that this laser no longer exists
		collidedTime = t;
	}
	
	
	public static double rValFor(double E) {
		return Math.pow(E/ENERGY_DENS*0.75/Math.PI, 1/3.0);	// gives the radius of a laser of energy E
	}

}

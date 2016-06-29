/**
 * A large body that absorbs and confounds lasers.
 */
package mechanics;

import java.util.ArrayList;

/**
 * @author jkunimune
 * @version 1.0
 */
public class GasCloud extends Body {

	public static final double halfLife = 5*Univ.s;	// the half-life of energy in this cloud
	public static final double laserEnergy = 0.1*Univ.MJ;	// the energy required to form a laser
	
	protected ArrayList<double[]> energy;	// an ArrayList that keeps track of the ever-changing energy value
	
	
	
	GasCloud(double x0, double y0, double vx0, double vy0, double time, Battlefield space) {
		super(x0, y0, vx0, vy0, time, space);
		
		energy = new ArrayList<double[]>(1);
		final double[] init = {time, 0, 0};	// zeroth value is time, first value is internal energy, and second
		energy.add(init);				// value is 'built up energy', a quantity which decides when to emit lasers
	}
	
	
	
	@Override
	public void update(double t) {	// a GasCloud with energy will gradually release lasers
		if (EsValAt(t) >= laserEnergy) {
			double tht = 2*Math.PI*Math.random();	// pick a random direction
			double r = this.rValAt(t) + Laser.rValFor(laserEnergy);	// pick a safe distance
			space.spawn(new Laser(xValAt(t) + r*Math.cos(tht),		// fire it
								  yValAt(t) + r*Math.sin(tht),
								  tht, t, space, laserEnergy));
			
			final double[] newE = {t, EValAt(t)-laserEnergy, EsValAt(t)-laserEnergy};
			energy.add(newE);
		}
	}
	
	
	@Override
	public void interactWith(Body that, double t) {	// GasClouds interact with lasers by absorbing them
		if (that instanceof Laser && this.dist(that, t) < rValAt(t)) {
			((Laser) that).collide(t);	// absorb the laser

			final double[] newE = {t, EValAt(t)+((Laser) that).EValAt(t), EsValAt(t)};	// and deal with 
			energy.add(newE);
		}
	}
	
	
	@Override
	public String spriteName() {
		return "gas";
	}
	
	
	@Override
	public double[] spriteTransform(double t) {
		final double r = rValAt(t);
		final double[] res = {0,r/100.0,r/100.0};	// 150 is the sprite radius, so dividing by 100 draws it a little bigger
		return res;
	}
	
	
	public double rValAt(double t) {
		return Math.sqrt(age(t)*1*Univ.s)*Univ.c/10;
	}
	
	
	public double EValAt(double t) {
		for (int i = energy.size()-1; i >= 0; i --) {
			if (energy.get(i)[0] <= t)
				return energy.get(i)[1];
		}
		return 0;
	}
	
	
	public double EsValAt(double t) {
		for (int i = energy.size()-1; i >= 0; i --) {
			final double[] state = energy.get(i);
			if (state[0] <= t)
				return state[2] + state[1]*(t-state[0])/halfLife;
		}
		return 0;
	}
	
	
	private void addEnergy(double E, double t) {	// increases the energy at time t by E
		final double[] newE = {t, EValAt(t)+E, EsValAt(t)};
		
		/*for (int i = energy.size()-1; i >= 0; i --)
			if (energy.get(i)[0] >= t)		// start by removing anything that was about to happen before this
				energy.remove(i);*/
		
		
	}

}

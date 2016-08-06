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

import java.util.ArrayList;

/**
 * An object that can move around and produce <code>Laser</code> objects on
 * command from the <code>Controller</code> or the <code>Client</code>.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public abstract class Ship extends PhysicalBody {

	public static final double MAX_H_VALUE = 1.5*Univ.MJ;	// maximum health
	public static final double MAX_E_VALUE = 2.0*Univ.MJ;	// maximum energy
	
	public static final double RECHARGE_RATE = 50*Univ.kW;
	
	public static final double MOVEMENT_COST = 0.5*Univ.MJ;
	public static final double LASER_ENERGY = 1.0*Univ.MJ;
	
	private boolean isBlue;	// whether it is blue or red
	
	protected byte id;		// an identifier for this particular ship
	
	protected ArrayList<double[]> health;
	protected ArrayList<double[]> energy;
	
	
	
	public Ship(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, 0, 0, time, space);
		id = pin;
		isBlue = blue;
		
		double[] hInit = {time, 1.5*Univ.MJ};
		double[] eInit = {time, 5*Univ.MJ};
		health = new ArrayList<double[]>(1);
		energy = new ArrayList<double[]>(1);
		health.add(hInit);
		energy.add(eInit);
		
		scales = false;	// ships don't scale because they're sprites are icons
	}
	
	
	
	@Override
	public String spriteName() {
		if (isBlue)	return "_b";
		else		return "_r";
	}
	
	
	@Override
	public boolean existsAt(double t) {
		return super.existsAt(t) && hValAt(t) > 0;
	}
	
	
	public void shoot(double x, double y, double t) {	// shoots a 1 megajoule laser at time t
		if (canExpend(LASER_ENERGY, t)) {
			final double theta = Math.atan2(y-yValAt(t),x-xValAt(t));
			final double spawnDist = Laser.rValFor(LASER_ENERGY) + 1*Univ.m;	// make sure you spawn it in front of the ship so it doesn't shoot itself
			space.spawn(new Laser(xValAt(t) + spawnDist*Math.cos(theta),
								  yValAt(t) + spawnDist*Math.sin(theta),
								  theta, t, space, LASER_ENERGY));
			
			playSound("pew", t);	// play the pew pew sound
		}
	}
	
	
	public void move(double x, double y, double t) {	// moves to the point x,y at a speed of c/10
		if (canExpend(MOVEMENT_COST, t)) {
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
	}
	
	
	public abstract void special(double x, double y, double t);	// executes a special attack
	
	
	public double hValAt(double t) {	// returns the health at time t
		for (int i = health.size()-1; i >= 0; i --) {	// iterate through health to find the correct health value
			final double[] timehealth = health.get(i);
			if (timehealth[0] <= t) {	// they should be sorted chronologically
				return timehealth[1];	// calculate health based on this
			}
		}
		return health.get(0)[1];	// if it didn't find anything, just use the first one
	}
	
	
	public void damaged(double amount, double t) {	// takes some out of your health
		double[] newHVal = {t, hValAt(t)-amount};
		health.add(newHVal);
		if (newHVal[1] <= 0) {
			clearSoundsAfter(t);	// if the ship just died
			playSound("boom"+(int)(Math.random()*2.001), t);	// cancel any later sounds with a boom
		}
	}
	
	
	public double eValAt(double t) {	// returns the energy at time t
		for (int i = energy.size()-1; i >= 0; i --) {	// iterate through energy to find the correct energy segment
			final double[] timeenergy = energy.get(i);
			if (timeenergy[0] <= t) {	// they should be sorted chronologically
				return Math.min(MAX_E_VALUE, timeenergy[1] + RECHARGE_RATE*(t-timeenergy[0]));	// calculate energy based on this
			}
		}
		return energy.get(0)[1];	// if it didn't find anything, just use the first one
	}
	
	
	public boolean canExpend(double amount, double t) {	// takes some out of your energy
		double[] newEVal = {t, eValAt(t)-amount};
		if (newEVal[1] >= 0) {
			energy.add(newEVal);
			return true;	// returns true if it successfully spent energy
		}
		else {
			playSound("blip", t);
			return false;	// returns false if there was not enough energy
		}
	}
	
	
	public byte getID() {	// returns the id number of this ship
		return id;
	}

}

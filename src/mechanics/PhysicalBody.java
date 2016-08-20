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
 * An object with mass, position, velocity, appearance, and ability to collide
 * with things.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public abstract class PhysicalBody implements Body {

	private static final double[] DEFAULT_TRANSFORM = {0.0, 1.0, 1.0, 1.0};	// the default transformation (zero rotation, scale of 1, 100% opacity)
	private static final double DEFAULT_LUMINOSITY = 1*Univ.kW;		// the default luminosity
	
	protected ArrayList<double[]> pos;	// the set of positions that define the movement of this body over the course of the map
	
	private ArrayList<Double> soundt;	// when it want to play sounds
	private ArrayList<String> sound;	// the sounds it wants to play
	protected Battlefield space;
	
	protected boolean scales;	// whether the icon scales when you zoom in and out
	
	
	
	protected PhysicalBody(double x0, double y0, double vx0, double vy0, double t0, Battlefield field) {
		double[] init = new double[5];	// each array in pos must have five entries:
		init[0] = t0;	// time
		init[1] = x0;	// x position
		init[2] = y0;	// y position
		init[3] = vx0;	// x velocity
		init[4] = vy0;	// y velocity
		pos = new ArrayList<double[]>(1);
		pos.add(init);
		space = field;
		soundt = new ArrayList<Double>();
		sound = new ArrayList<String>();
		
		scales = true;	// scales is true by default
	}
	
	
	
	public abstract String spriteName();	// gives the name of the current sprite for the GameScreen to reference
	
	
	public double[] spriteTransform(double t) {	// gives the rotation and scale factors for this object's sprite
		return DEFAULT_TRANSFORM;
	}
	
	
	public void update(double t) {}	// carry out actions that need to be handled continuously
	
	
	public void interactWith(PhysicalBody that, double t) {}	// determine if this should interact with that, and do it
	
	
	public boolean existsAt(double t) {	// determines whether this sprite should be drawn
		return age(t) >= 0;
	}
	
	
	protected final void playSound(String sfx, double t) {	// play sound at time t
		for (int i = soundt.size()-1; i >= 0; i --) {	// find the correct index
			if (soundt.get(i) < t) {
				soundt.add(i+1, new Double(t));		// and insert the sound into sound and soundt
				sound.add(i+1, sfx);
				return;
			}
		}
		soundt.add(0, new Double(t));	// insert it at slot zero if you haven't found a spot by now
		sound.add(0, sfx);
	}
	
	
	protected final void clearSoundsAfter(double t) {	// deletes all planned sounds after a certain time
		for (int i = soundt.size()-1; i >= 0; i --) {
			if (soundt.get(i) > t) {
				soundt.remove(i);
				sound.remove(i);
			}
			else
				return;
		}
	}
	
	
	public final String soundName(double t) {
		if (!soundt.isEmpty() && soundt.get(0) <= t) {	// if there is a sound that needs to be played
			soundt.remove(0);			// take it off the list
			return sound.remove(0);	// and play it
		}
		return null;	// otherwise, stay silent
	}
	
	
	public double tprime(Body observer, double to) {	// calculates the time the observer sees this at
		if (this.equals(observer) ||
				(observer.xValAt(to) == this.xValAt(to) && observer.yValAt(to) == this.yValAt(to)))
			return to;	// no calculations necessary when you're observing yourself
		
		int i;
		for (i = pos.size()-1; i > 0; i --)	// iterate through pos to find the correct motion segment
			if (pos.get(i)[0] <= to)	// they should be sorted chronologically
				break;	// calculate time based on this
		
		final double c2 = Univ.c*Univ.c;
		
		double[] position;
		double ts;
		do {
			position = pos.get(i);
			final double dt = position[0] - to;
			final double dx = position[1] - observer.xValAt(to);
			final double dy = position[2] - observer.yValAt(to);
			final double vx = position[3];
			final double vy = position[4];
			final double vs2   = vx*vx + vy*vy - c2;
			final double b = vx*dx + vy*dy - c2*dt;
			final double ds2   = dx*dx + dy*dy - c2*dt*dt;
			ts = (-b + Math.sqrt(b*b - vs2*ds2))/vs2 + position[0];
			
			i --;
		} while (i >= 0 && ts < position[0]);
		
		return ts;
	}
	
	
	public double luminosityAt(double t) {	// how bright is this object?
		return DEFAULT_LUMINOSITY;
	}
	
	
	public final double xValAt(double t) {	// returns x in pixels at time t
		for (int i = pos.size()-1; i >= 0; i --) {	// iterate through pos to find the correct motion segment
			final double[] position = pos.get(i);
			if (position[0] <= t) {	// they should be sorted chronologically
				return position[1] + position[3]*(t-position[0]);	// calculate position based on this
			}
		}
		return pos.get(0)[1];	// if it didn't find anything, just use the initial position
	}
	
	
	public final double yValAt(double t) {	// returns y in pixels at time t
		for (int i = pos.size()-1; i >= 0; i --) {	// iterate through pos to find the correct motion segment
			final double[] position = pos.get(i);
			if (position[0] <= t) {	// they should be sorted chronologically
				return position[2] + position[4]*(t-position[0]);	// calculate position based on this
			}
		}
		return pos.get(0)[2];	// if it didn't find anything, just use the initial position
	}
	
	
	public final double vxValAt(double t) {	// returns x in pixels at time t
		for (int i = pos.size()-1; i >= 0; i --) {	// iterate through pos to find the correct motion segment
			final double[] position = pos.get(i);
			if (position[0] <= t) {	// they should be sorted chronologically
				return position[3];	// calculate position based on this
			}
		}
		return 0;	// if it didn't find anything, just assume motionless
	}
	
	
	public final double vyValAt(double t) {	// returns y in pixels at time t
		for (int i = pos.size()-1; i >= 0; i --) {	// iterate through pos to find the correct motion segment
			final double[] position = pos.get(i);
			if (position[0] <= t) {	// they should be sorted chronologically
				return position[4];	// calculate position based on this
			}
		}
		return 0;	// if it didn't find anything, just assume motionless
	}
	
	
	public final double age(double t) {	// returns the number of milliseconds since this has been created
		return t-pos.get(0)[0];
	}
	
	
	public boolean doesScale() {
		return scales;
	}

}

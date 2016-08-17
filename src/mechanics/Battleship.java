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
 * A ship with powerful guns, long range, and a special ultraviolet laser that
 * bypasses gas clouds.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Battleship extends Ship {

	public static final double SPECIAL_ENERGY = 1.5*Univ.MJ;
	
	
	
	public Battleship(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, time, pin, blue, space);
	}
	
	
	
	@Override
	public String spriteName() {
		return "ship_battleship"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {	// for its special attack, the Battleship shoots a super-laser
		final double nrg = SPECIAL_ENERGY;
		if (expend(nrg, t)) {
			final double theta = Math.atan2(y-yValAt(t),x-xValAt(t));
			
			final double spawnDist = Laser.rValFor(nrg);	// make sure you spawn it in front of the ship so it doesn't shoot itself
			space.spawn(new UVLaser(xValAt(t) + spawnDist*Math.cos(theta),
									yValAt(t) + spawnDist*Math.sin(theta),
									theta, t, space, nrg));
			
			playSound("pew", t);	// play the pew pew sound
		}
	}

}

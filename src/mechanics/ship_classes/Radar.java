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
package mechanics.ship_classes;

import mechanics.Battlefield;
import mechanics.Ping;
import mechanics.Ship;
import mechanics.Univ;

/**
 * A ship with a powerful radar, capable of detecting ships anywhere on the map,
 * given time.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Radar extends Ship {

	public static final double RADAR_FLUX = Math.pow(10, 11)*Univ.MW*Univ.km*Univ.km;	// how strong it is
	public static final double PING_DURATION = 10*Univ.s;
	
	
	
	public Radar(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, time, pin, blue, space);
		visibilityBand = 1;
	}
	
	
	
	@Override
	public String spriteName() {
		return "ship_radar"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {
		if (expend(1*Univ.MJ, t)) {
			if (this.isBlue())	// the Ping is only visible when your own radar is scanning
				space.spawn(new Ping(xValAt(t), yValAt(t), t, RADAR_FLUX, PING_DURATION, space));
			else {				// for the other team, it makes your Radar visible
				this.illuminate(Double.POSITIVE_INFINITY, PING_DURATION, 0, t);
				this.illuminate(Double.POSITIVE_INFINITY, PING_DURATION, 1, t);
			}
		}
	}
	
	
	

}

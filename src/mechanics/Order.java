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
 * A signal that controls the behaviour of ships.
 * 
 * @author jkunimune
 * @version 1.0
 */
public class Order extends Body {

	private byte orderType;		// the type of order
	private byte targetShip;	// the ship being ordered
	private double xTarg;		// the x value this order refers to
	private double yTarg;		// the y value this order refers to
	
	private boolean orderReceived;	// becomes true whenever it meets its target ship
	
	
	Order(double x0, double y0, double t0, byte order, byte ship, double xr, double yr, Battlefield field) {
		super(x0, y0, 0, 0, t0, field);
		
		orderType = order;
		targetShip = ship;
		xTarg = xr;
		yTarg = yr;
		
		orderReceived = false;
	}
	
	
	@Override
	public void interactWith(Body that, double t) {
		if (!orderReceived && that instanceof Ship && this.dist(that,t) < this.rValAt(t)) {
			if (((Ship) that).getID() == this.targetShip) {
				switch (orderType) {
				case -2:
					((Ship) that).move(xTarg, yTarg, t);
					break;
				case -3:
					((Ship) that).shoot(xTarg, yTarg, t);
					break;
				case -4:
					((Ship) that).special(xTarg, yTarg, t);
					break;
				}
				
				orderReceived = true;
			}
		}
	}
	
	
	@Override
	public String spriteName() {
		return "order"+orderType;
	}
	
	
	@Override
	public double[] spriteTransform(double t) {
		final double r = rValAt(t);
		double[] res = {0,r/500.0,r/500.0};	// 250 is the sprite radius, so dividing by 250 yields the scale factor
		if (res[1] < 2)
			return res;	// once it gets a certain size, terminate the sprite for heap space's sake
		res[1] = 0;
		res[2] = 0;
		return res;
	}
	
	
	private double rValAt(double t) {
		return age(t)*Univ.c;
	}

}

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
 * A signal that controls the behaviour of <code>Ship</code> objects.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Order extends PhysicalBody {

	private byte orderType;		// the type of order
	private byte targetShip;	// the ship being ordered
	private double xTarg;		// the x value this order refers to
	private double yTarg;		// the y value this order refers to
	
	private double receiptTime;	// the time this order met its target ship
	
	
	public Order(double x0, double y0, double t0, byte order, byte ship, double xr, double yr, Battlefield field) {
		super(x0, y0, 0, 0, t0, field);
		
		orderType = order;
		targetShip = ship;
		xTarg = xr;
		yTarg = yr;
		
		receiptTime = Double.POSITIVE_INFINITY;
	}
	
	
	@Override
	public void interactWith(PhysicalBody that, double t) {
		if (that instanceof Ship && space.dist(this,that,t) < this.rValAt(t)) {
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
				
				receiptTime = t;
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
	
	
	@Override
	public boolean existsAt(double t) {
		return super.existsAt(t) && t < receiptTime;
	}
	
	
	@Override
	public double tprime(Body observer, double to) {	// orders are weird in that their position is different
		return to - age(to)/2;							// from where they appear to be, so we can skip this method
	}
	
	
	private double rValAt(double t) {
		return age(t)*Univ.c;
	}

}

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
import mechanics.Order;
import mechanics.Ship;
import network.Protocol;

/**
 * The command ship, most important of any fleet, which can't do much but give
 * orders and move.
 * 
 * @author	jkunimune
 * @version	1.0.0
 */
public class Carrier extends Ship {

	public Carrier(double newX, double newY, double time, byte pin, boolean blue, Battlefield space) {
		super(newX, newY, time, pin, blue, space);
	}
	
	
	
	@Override
	public String spriteName() {
		return "ship_carrier"+super.spriteName();
	}
	
	
	@Override
	public void special(double x, double y, double t) {}
	
	
	@Override
	protected void die(double t) {	// a carrier death is no ordinary death
		super.die(t);
		space.receive(Protocol.denoteVictory(0));	// it signals the end of the game
	}
	
	
	public void issueOrder(String data) {	// a Carrier-unique method
		byte order = Protocol.getOOrder(data);						// for the important information about
		byte ship = Protocol.getOShip(data);						// the order
		double x = Protocol.getOX(data);
		double y = Protocol.getOY(data);
		double t = Protocol.getOT(data, space.getOffset());
		space.spawn(new Order(xValAt(t), yValAt(t), t, order, ship, x, y, space));	// send the order!
	}

}

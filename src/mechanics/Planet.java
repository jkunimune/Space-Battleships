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
 * An inert sphere that destroys other bodies that get too close.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Planet extends Body {

	private String name;
	private double radius;
	
	
	
	public Planet(double x0, double y0, double r, String newName, double time, Battlefield space) {
		super(x0, y0, 0, 0, time, space);
		name = newName;
		radius = r;
	}
	
	
	
	@Override
	public void interactWith(Body that, double t) {
		if (space.dist(this, that, t) < radius) {
			if (that instanceof Ship)
				((Ship) that).damaged(((Ship) that).hValAt(t), t);
			else if (that instanceof Laser)
				((Laser) that).collide(t);
		}
	}
	
	
	@Override
	public String spriteName() {
		return name;
	}
	
	
	@Override
	public double luminosityAt(double t) {	// chances are, you already know where the planet is
		return Double.POSITIVE_INFINITY;
	}

}

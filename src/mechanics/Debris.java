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
 * A meaningless speck with random velocity that exists solely for aesthetic appeal
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Debris extends Body {

	public static Debris debris(double x, double y, double th0, double t, Battlefield field) {
		final double v0 = Univ.c/4;							// given the position, and the direction from which
		final double vx0 = v0*Math.cos(th0);				// a ship was hit, generate some debris that could
		final double vy0 = v0*Math.sin(th0);				// have been produced from the collision
		final double th1 = (2*Math.PI)*Math.random();
		final double v1 = (Univ.c/4)*Math.random();
		final double vx1 = v1*Math.cos(th1);
		final double vy1 = v1*Math.sin(th1);
		return new Debris(x, y, vx0+vx1, vy0+vy1, t, field);
	}
	
	
	
	private Debris(double x0, double y0, double vx0, double vy0, double t0, Battlefield field) {
		super(x0, y0, vx0, vy0, t0, field);
	}
	
	
	
	@Override
	public String spriteName() {
		return "debris";
	}

}

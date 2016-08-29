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
 * A signal that illuminates objects briefly
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Ping extends Body {

	public static final double FLUX = Math.pow(10, 11)*Univ.MW*Univ.km*Univ.km;	// how strong it is
	public static final double DURATION = 10*Univ.s;
	
	
	private double lastUpdateRadius;	// the radius the last time update() was called
	
	
	
	public Ping(double x, double y, double t, Battlefield field) {
		super(x, y, 0, 0, t, field);
	}
	
	
	
	@Override
	public String spriteName() {
		return "ping";
	}
	
	
	@Override
	public double[] spriteTransform(double t) {
		final double r = rValAt(t);
		double[] res = {0,r/500.0,r/500.0,1.0};	// 250 is the sprite radius, so dividing by 250 yields the scale factor
		if (res[1] < 2)
			return res;	// once it gets a certain size,
		res[1] = 0;
		res[2] = 0;
		return res;		// terminate the sprite for the heap's sake
	}
	
	
	@Override
	public void update(double t) {	// save the last update time
		lastUpdateRadius = rValAt(t);
	}
	
	
	@Override
	public void interactWith(Body b, double t) {
		final double r = space.dist(this, b, t);
		if (r <= rValAt(t) && r > lastUpdateRadius)		// if we just hit this object
			b.illuminate(FLUX/(r*r), DURATION, t);	// light it up
	}
	
	
	@Override
	public boolean existsAt(double t) {
		return super.existsAt(t);//rValAt(t) <
	}
	
	
	private double rValAt(double t) {
		return age(t)*Univ.c;
	}

}

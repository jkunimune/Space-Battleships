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

	private double flux;
	private double duration;
	
	private double lastUpdateRadius;	// the radius the last time update() was called
	
	
	
	public Ping(double x, double y, double t, double Phi, double T, Battlefield field) {
		super(x, y, 0, 0, t, field);
		flux = Phi;
		duration = T;
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
			b.illuminate(flux/(r*r), duration, 1, t);	// light it up in the radio band
	}
	
	
	@Override
	public boolean existsAt(double t) {
		return super.existsAt(t);//rValAt(t) <
	}
	
	
	@Override
	public double seenBy(Body observer, double to) {	// pings are weird in that their position is different
		return to - age(to)/2;							// from where they appear to be, so we can skip this method
	}
	
	
	@Override
	public double luminosityAt(int band, double t) {
		if (band == 0)
			return 0;
		else if (band == 1)
			return super.luminosityAt(band, t);
		else
			return 0;
	}
	
	
	private double rValAt(double t) {
		return age(t)*Univ.c;
	}

}

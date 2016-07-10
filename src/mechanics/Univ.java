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
 * A simple class to hold universal values and constants.
 * 
 * @author jkunimune
 * @version 1.0
 */
public class Univ {

	/*length*/
	public static final double pix = 1.0;			// pixel (base unit)
	public static final double m = 0.0000008;		// meter
	public static final double km = 1000*m;			// kilometer
	public static final double ft = 0.3048*m;		// foot
	public static final double mi = 5280*ft;		// mile
	/*time*/
	public static final double ms = 1.0;			// millisecond (base unit)
	public static final double s = 1000.0;			// second
	/*speed*/
	public static final double c = 299792458*m/s;	// speed of light
	/*mass*/
	public static final double kg = Math.pow(2,55);	// kilogram
	/*energy*/
	public static final double J = kg*m*m/(s*s);	// Joule
	public static final double MJ = 1000000*J;		// Megajoule
	/*power*/
	public static final double W = J/s;				// Watt
	public static final double kW = 1000*W;			// kiloWatt
	public static final double MW = 1000000*W;		// Megawatt
	/*volume*/
	public static final double m3 = m*m*m;			// cubic meter
	public static final double L = m3/1000;			// Litre
	public static final double km3 = m3*1000000000;	// cubic kilometer

}

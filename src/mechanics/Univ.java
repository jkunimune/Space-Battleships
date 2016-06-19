package mechanics;
/**
 * 
 */

/**
 * @author jkunimune
 * A simple class to hold universal values and constants.
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

}

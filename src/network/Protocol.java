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
package network;

/**
 * A static <code>class</code> to interpret information sent over the <code>Socket</code>.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Protocol {

	private static final char ORDER = 'o';
	private static final char COLLISION = 'c';
	
	
	public static String composeOrder(byte order, byte ship, double x, double y, double t) {	// create an order String to send over the Socket
		return Character.toString(ORDER) +
				String.format("%02x", order+128) + String.format("%02x", ship) +
				String.format("%1$" + 16 + "s", Double.toHexString(x)) +
				String.format("%1$" + 16 + "s", Double.toHexString(y)) +
				String.format("%1$" + 16 + "s", Double.toHexString(t));
	}
	
	
	public static char getType(String data) {	// returns the character representing the type of informatio nthis is
		return data.charAt(0);
	}
	
	
	public static boolean isOrder(String data) {	// is this an order?
		return data.charAt(0) == ORDER; 
	}
	
	/* METHODS THAT WORK ONLY FOR ORDER-TYPE DATA STRINGS */
	
	public static byte getOrder(String data) {	// what kind of order is it?
		return (byte) (Byte.parseByte(data.substring(1, 3), 16) - 128);
	}
	
	
	public static byte getShip(String data) {	// which ship is it directed toward?
		return Byte.parseByte(data.substring(3, 5), 16);
	}
	
	
	public static double getX(String data) {	// where is it aimed (x)?
		return Double.parseDouble(data.substring(5, 21));
	}
	
	
	public static double getY(String data) {	// where is it aimed (y)?
		return Double.parseDouble(data.substring(21, 37));
	}
	
	
	public static double getT(String data) {	// when was the order given?
		return Double.parseDouble(data.substring(37));
	}
	
	/* END ORDER-SPECIFIC METHODS */
	
	public static boolean isCollision(String data) {	// is this a collision?
		return data.charAt(0) == COLLISION;
	}
	
	/* METHODS THAT ONLY WORK WITH COLLISION-TYPE DATA STRINGS */
	/* END COLLISION-SPECIFIC METHODS */

}

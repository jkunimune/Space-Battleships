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
public final class Protocol {

	private static final char PLACE = 'p';		// placing a ship
	private static final char READY = 'r';		// finishing placing ships
	private static final char ORDER = 'o';		// giving an order
	private static final char COLLISION = 'c';	// two bodies colliding
	private static final char VICTORY = 'v';
	
	
	
	public static String writePlacement(byte id, byte type, double x, double y) {	// create a placement String to send over the Socket
		return String.valueOf(PLACE) +
				String.format("%02x", id) +
				String.format("%02x", type) +
				String.format("%1$21s", Double.toHexString(x)) +
				String.format("%1$21s", Double.toHexString(y));
	}
	
	
	public static String announceReadiness() {	// tell people that this player is ready
		return String.valueOf(READY);
	}
	
	
	public static String composeOrder(byte order, byte ship, double x, double y, double t) {	// create an order String to send over the Socket
		return String.valueOf(ORDER) +
				String.format("%02x", order+128) + String.format("%02x", ship) +
				String.format("%1$21s", Double.toHexString(x)) +
				String.format("%1$21s", Double.toHexString(y)) +
				String.format("%1$21s", Double.toHexString(t));
	}
	
	
	public static String describeCollision() {	// create a collision String to send over the Socket
		return null;
	}
	
	
	public static String denoteVictory(int condition) {
		return denoteVictory((byte) condition);
	}
	
	
	public static String denoteVictory(byte condition) {	// create a victory String to send over the Socket
		return String.valueOf(VICTORY) +
				String.format("%02x", condition);
	}
	
	
	public static char getType(String data) {	// returns the character representing the type of information this is
		return data.charAt(0);
	}
	
	
	public static boolean isPlacement(String data) {	// is this a ship placement?
		return data.charAt(0) == PLACE;
	}
	
	/* METHODS THAT WORK ONLY FOR PLACE-TYPE DATA STRINGS */
	
	public static byte getPID(String data) {
		return Byte.parseByte(data.substring(1, 3), 16);
	}
	
	
	public static byte getPType(String data) {	// what kind of ship is it?
		return Byte.parseByte(data.substring(3,5), 16);
	}
	
	
	public static double getPX(String data) {	// where was it placed (x)?
		return Double.parseDouble(data.substring(5, 26));
	}
	
	
	public static double getPY(String data) {	// where was it placed (y)?
		return Double.parseDouble(data.substring(26, 47));
	}
	
	/* END PLACEMENT-SPECIFIC METHODS */
	
	public static boolean isReadiness(String data) {	// is this a readiness?
		return data.charAt(0) == READY;
	}
	
	/* METHODS THAT WORK ONLY FOR READY-TYPE DATA STRINGS */
	/* END READY-SPECIFIC METHODS */
	
	public static boolean isOrder(String data) {	// is this an order?
		return data.charAt(0) == ORDER; 
	}
	
	/* METHODS THAT WORK ONLY FOR ORDER-TYPE DATA STRINGS */
	
	public static byte getOOrder(String data) {	// what kind of order is it?
		return (byte) (Byte.parseByte(data.substring(1, 3), 16) - 128);
	}
	
	
	public static byte getOShip(String data) {	// which ship is it directed toward?
		return Byte.parseByte(data.substring(3, 5), 16);
	}
	
	
	public static double getOX(String data) {	// where is it aimed (x)?
		return Double.parseDouble(data.substring(5, 26));
	}
	
	
	public static double getOY(String data) {	// where is it aimed (y)?
		return Double.parseDouble(data.substring(26, 47));
	}
	
	
	public static double getOT(String data, double offset) {	// when was the order given?
		return Double.parseDouble(data.substring(47)) + offset;
	}
	
	/* END ORDER-SPECIFIC METHODS */
	
	public static boolean isCollision(String data) {	// is this a collision?
		return data.charAt(0) == COLLISION;
	}
	
	/* METHODS THAT ONLY WORK WITH COLLISION-TYPE DATA STRINGS */
	/* END COLLISION-SPECIFIC METHODS */
	
	public static boolean isVictory(String data) {	// is this a victory condition?
		return data.charAt(0) == VICTORY;
	}
	
	/* METHODS THAT ONLY WORK WITH VICTORY-TYPE DATA STRINGS */
	
	public static byte getVCondition(String data) {	// what caused the end of the game?
		return Byte.parseByte(data.substring(1), 16);
	}
	
	/* END VICTORY-SPECIFIC METHODS */

}

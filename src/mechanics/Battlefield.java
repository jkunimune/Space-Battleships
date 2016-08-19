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

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import mechanics.ship_classes.Carrier;
import network.Protocol;

/**
 * The class that handles the game and physics-engine, keeping track of all
 * bodies, and updating things.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Battlefield {

	private static final double VICTORY_DELAY = 5*Univ.s;	// the time between game end and return to menu
	
	
	private ArrayList<PhysicalBody> bodies;	// the list of game elements
	private Carrier myCarrier;		// the main carrier
	private Carrier yourCarrier;		// the opponent carrier
	private DataOutputStream out;		// the stream to write all events to
	
	private double endGame;		// the time the game ended
	private byte[] blueIDs;		// the IDs of the ships we own
	private double offset;		// the time offset for network conversions
	
	public String message;				// a public phrase for the GameScreen to print out
	
	
	public Battlefield(DataOutputStream dos, double dt, boolean host) {
		bodies = new ArrayList<PhysicalBody>();
		out = dos;
		endGame = Double.POSITIVE_INFINITY;
		message = "";
		
		double time = (double)System.currentTimeMillis();	// the current time
		bodies.add(new Planet(0*Univ.km,0*Univ.km,43441*Univ.mi,"Jupiter",time,this));
		
		if (host) {
			byte[] temp = {0, 1, 2, 3, 4};
			blueIDs = temp;
		}
		else {
			byte[] temp = {5, 6, 7, 8, 9};
			blueIDs = temp;
		}
	}
	
	
	
	public void receive(String data) {	// if in doubt, transmit should be true
		receive(data, true);
	}
	
	
	public void receive(String data, boolean transmit) {		// receives and interprets some data
		if (Protocol.isPlacement(data)) {	// if a ship got placed
			spawnShip(data, transmit);
		}
		else if (Protocol.isOrder(data)) {		// if it was an order
			if (transmit)
				myCarrier.issueOrder(data);	// execute it
			else
				yourCarrier.issueOrder(data);
		}
		else if (Protocol.isCollision(data)) {	// if it was a collision
			System.err.println("I haven't programmed that yet.");
		}
		else if (Protocol.isVictory(data)) {	// if someone won
			endGame = (double)System.currentTimeMillis() + VICTORY_DELAY;
			message = getMessage(Protocol.getVCondition(data));
		}
		else {
			System.err.println("Wait, what does '"+data+"' mean?");
		}
		
		if (transmit && out != null) {	// if you got it from a non-network source
			try {
				out.writeUTF(data);		// broadcast it
			} catch (IOException e) {}
		}
	}
	
	
	public void update() {	// updates all bodies that need to be updated and collides anything close enough to collide
		double t = (double)System.currentTimeMillis();
		
		for (int i = bodies.size()-1; i > 0; i --) {
			final PhysicalBody b1 = bodies.get(i);
			if (b1.existsAt(t)) {
				for (int j = 0; j < i; j ++) {
					final PhysicalBody b2 = bodies.get(j);
					if (b2.existsAt(t)) {
						b1.interactWith(b2, t);
						b2.interactWith(b1, t);
					}
				}
			}
		}
		
		for (int i = bodies.size()-1; i >= 0; i --)
			bodies.get(i).update(t);
	}
	
	
	public boolean active() {	// is the game still going?
		return System.currentTimeMillis() < endGame;
	}
	
	
	public double observedTime(Body b, double t) {	// the time at which you see this object
		final double I = b.luminosityAt(t)/(4*Math.PI*Math.pow(dist(myCarrier, b, t), 2));
		if (I >= Ship.VISIBILITY)	// if b is in scanner range
			return b.tprime(myCarrier, t);
		
		return Double.NaN;		// return
	}
	
	
	public final double dist(Body b1, Body b2, double t) {	// calculates between two bodies
		return Math.hypot(b1.xValAt(t)-b2.xValAt(t), b1.yValAt(t)-b2.yValAt(t));
	}
	
	
	public String getMessage(byte condition) {
		switch (condition) {
		case 0:
			if (myCarrier.existsAt(endGame))
				return "You're Winner !";
			else
				return "You're Loser :(";
		case 1:
			return "Connection Lost !";
		default:
			return "ERROR: Unrecognized error code "+condition;
		}
	}
	
	
	public void spawnShip(String info, boolean blue) {	// create a ship based on a placement string
		final byte type = Protocol.getPType(info);
		final double x = Protocol.getPX(info);
		final double y = Protocol.getPY(info);
		final byte id = Protocol.getPID(info);
		final double t = System.currentTimeMillis();
		Ship s = Ship.buildShip(type, x, y, t, id, blue, this);
		
		if (s instanceof Carrier) {
			if (s.isBlue())	myCarrier = (Carrier) s;
			else			yourCarrier = (Carrier) s;
		}
		spawn(s);
	}
	
	
	public void spawn(PhysicalBody b) {	// adds a new body to the battlefield
		bodies.add(b);
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<PhysicalBody> getBodies() {
		return (ArrayList<PhysicalBody>) bodies.clone();
	}
	
	
	public Ship getShipByID(byte id) {	// searches for the ship that has the matching id
		for (Body b: bodies)	// TODO: make this faster (maybe)
			if (b instanceof Ship)
				if (((Ship) b).getID() == id)
					return (Ship) b;
		return null;
	}
	
	
	public byte[] getIDs() {
		return blueIDs;
	}
	
	
	public double getOffset() {
		return offset;
	}

}

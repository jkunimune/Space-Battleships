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
	
	
	private ArrayList<Body> bodies;		// the list of game elements
	private ArrayList<Ship> myShips;	// the list of blue ships
	private ArrayList<Order> orders;	// the list of orders in effect
	private Carrier myCarrier;		// the main carrier
	private Carrier yourCarrier;		// the opponent carrier
	private PlacementRegion validRegion;	// where we can place ships
	private DataOutputStream out;		// the stream to write all events to
	
	private double endGame;		// the time the game ended
	private byte[] blueIDs;		// the IDs of the ships we own
	private double offset;		// the time offset for network conversions
	
	private boolean opponentReady;
	private boolean usReady;
	public boolean started;		// whether both players are ready
	public String message;				// a public phrase for the GameScreen to print out
	
	
	public Battlefield(DataOutputStream dos, double dt, boolean host) {
		bodies = new ArrayList<Body>();
		myShips = new ArrayList<Ship>();
		orders = new ArrayList<Order>();
		out = dos;
		endGame = Double.POSITIVE_INFINITY;
		
		opponentReady = false;
		usReady = false;
		started = false;
		message = "";
		
		double time = (double)System.currentTimeMillis();	// the current time
		validRegion = PlacementRegion.placementRegion(time, this, host);
		bodies.add(new Planet(0, 0, 43441*Univ.mi, "Jupiter", time, this));
		
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
		if (Protocol.isReadiness(data)) {	// if someone is ready
			if (transmit)	usReady = true;
			else			opponentReady = true;	// keep track of who is ready to play
			started = (usReady && opponentReady);
		}
		else if (Protocol.isPlacement(data)) {	// if a ship got placed
			spawnShip(data, transmit);
		}
		else if (Protocol.isOrder(data)) {		// if it was an order
			if (transmit)
				orders.add(myCarrier.issueOrder(data));	// execute it
			else
				orders.add(yourCarrier.issueOrder(data));
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
			final Body b1 = bodies.get(i);
			if (b1.existsAt(t)) {
				for (int j = 0; j < i; j ++) {
					final Body b2 = bodies.get(j);
					if (b2.existsAt(t)) {
						b1.interactWith(b2, t);
						b2.interactWith(b1, t);
					}
				}
			}
		}
		
		for (int i = bodies.size()-1; i >= 0; i --)
			if (bodies.get(i).existsAt(t))
				bodies.get(i).update(t);
	}
	
	
	public boolean active() {	// is the game still going?
		return System.currentTimeMillis() < endGame;
	}
	
	
	public double observedTime(Body b, double t) {	// the time at which you see this object
		double to = Double.NaN;
		for (Ship s: getShips()) {		// check each ship
			final double ts = s.sees(b, myCarrier.sees(s, t));	// when would you see that ship see b?
			if ((Double.isNaN(to) && !Double.isNaN(ts)) || ts > to)	// if that ship has the best observation time (and is not NaN)
				to = ts;		// then choose that time
		}
		
		return to;		// return the best time
	}
	
	
	public final double dist(Body b1, Body b2, double t) {
		return dist(b1, b2, t, t);
	}
	
	
	public final double dist(Body b1, Body b2, double t1, double t2) {	// calculates distance between two bodies
		return Math.hypot(b1.xValAt(t1)-b2.xValAt(t2), b1.yValAt(t1)-b2.yValAt(t2));
	}
	
	
	public String getMessage(byte condition) {
		switch (condition) {
		case 0:
			if (myCarrier.existsAt(endGame))
				return "You're Winner :D";
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
		
		if (s.isBlue()) {
			myShips.add(s);
			if (s instanceof Carrier)
				myCarrier = (Carrier) s;
		}
		else {
			if (s instanceof Carrier)
				yourCarrier = (Carrier) s;
		}
		spawn(s);
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Ship> getShips() {	// returns a List of blue Ships
		return (ArrayList<Ship>) myShips.clone();
	}
	
	
	public void spawn(Body b) {	// adds a new body to the battlefield
		bodies.add(b);
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Body> getBodies() {
		return (ArrayList<Body>) bodies.clone();
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<Order> getOrders() {
		return (ArrayList<Order>) orders.clone();
	}
	
	
	public Ship getShipByID(byte id) {	// searches for the ship that has the matching id
		for (Ship s: myShips)
			if (s.getID() == id)
				if (s.existsAt(myCarrier.sees(s, System.currentTimeMillis())))
					return s;
		return null;
	}
	
	
	public byte[] getIDs() {
		return blueIDs;
	}
	
	
	public double getOffset() {
		return offset;
	}
	
	
	public PlacementRegion getRegion() {
		return validRegion;
	}
	
	
	public boolean isValid(double x, double y) {
		return validRegion.contains(x, y);
	}

}

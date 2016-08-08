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

import network.Protocol;

/**
 * The class that handles the game and physics-engine, keeping track of all
 * bodies, and updating things.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Battlefield {

	protected ArrayList<PhysicalBody> bodies;	// the list of game elements
	protected Carrier myCarrier;		// the main carrier
	protected Carrier yourCarrier;		// the opponent carrier
	protected DataOutputStream out;		// the stream to write all events to
	
	private byte[] blueIDs;				// the ID of the next ship we place
	
	public String message;				// a public phrase for the GameScreen to print out
	
	
	public Battlefield(DataOutputStream dos, boolean host) {
		bodies = new ArrayList<PhysicalBody>();
		out = dos;
		message = "";
		
		double time = (double)System.currentTimeMillis();	// the current time
		bodies.add(new Planet(0*Univ.km,0*Univ.km,43441*Univ.mi,"Jupiter",time,this));
		
		// TODO: The player will eventually place the ships him/herself. This is mostly temporary
		bodies.add(new Carrier(		-300000*Univ.km, 0, time, (byte)0, host, this));
		bodies.add(new Battleship(	-240000*Univ.km, 0, time, (byte)1, host, this));
		bodies.add(new Scout(		-270000*Univ.km, 0, time, (byte)2, host, this));
		bodies.add(new Radar(		-330000*Univ.km, 0, time, (byte)3, host, this));
		bodies.add(new Steamship(	-360000*Univ.km, 0, time, (byte)4, host, this));
		bodies.add(new Carrier(		 300000*Univ.km, 0, time, (byte)5, !host, this));
		bodies.add(new Battleship(	 240000*Univ.km, 0, time, (byte)6, !host, this));
		bodies.add(new Scout(		 270000*Univ.km, 0, time, (byte)7, !host, this));
		bodies.add(new Radar(		 330000*Univ.km, 0, time, (byte)8, !host, this));
		bodies.add(new Steamship(	 360000*Univ.km, 0, time, (byte)9, !host, this));
		if (host) {
			byte[] temp = {0, 1, 2, 3, 4};
			blueIDs = temp;
			myCarrier = (Carrier) bodies.get(1);
			yourCarrier = (Carrier) bodies.get(6);
		}
		else {
			byte[] temp = {5, 6, 7, 8, 9};
			blueIDs = temp;
			myCarrier = (Carrier) bodies.get(6);
			yourCarrier = (Carrier) bodies.get(1);
		}
	}
	
	
	
	public void receive(String data) {	// if in doubt, transmit should be true
		receive(data, true);
	}
	
	
	public void receive(String data, boolean transmit) {		// receives and interprets some data
		if (Protocol.isPlacement(data)) {
			System.err.println("Uh, I haven't programmed that, yet.");
		}
		else if (Protocol.isOrder(data)) {		// if it was an order
			if (transmit)
				myCarrier.issueOrder(data);	// execute it
			else
				yourCarrier.issueOrder(data);
		}
		else if (Protocol.isCollision(data)) {
			System.err.println("I haven't programmed that yet, either");
		}
		else if (Protocol.isVictory(data)) {
			if (myCarrier.existsAt(System.currentTimeMillis()))
				message = "You're Winner !";
			else
				message = "You're Loser. :(";
		}
		else {
			System.err.println("Wait, what does '"+data+"' mean?");
		}
		if (transmit && out != null) {	// if you got it from a non-network source
			try {
				out.writeUTF(data);		// broadcast it
			} catch (IOException e) {
				System.err.println("I don't know if this will ever print");
			}
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
	
	
	public void spawn(PhysicalBody b) {	// adds a new body to the battlefield
		bodies.add(b);
	}
	
	
	public ArrayList<PhysicalBody> getBodies() {
		return bodies;
	}
	
	
	public Carrier getBlueCarrier() {
		return (Carrier) getShipByID(blueIDs[0]);
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

}

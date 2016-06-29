/**
 * The class that handles the game and physics-engine, keeping track of all bodies, and updating things.
 */
package mechanics;

import java.util.ArrayList;

/**
 * @author jkunimune
 * @version 1.0
 */
public class Battlefield {

	public ArrayList<Body> bodies;
	
	
	public Battlefield() {
		final double bluRC = -300000*Univ.km+Math.random()*100000*Univ.km;
		final double bluTC = 2*Math.random()-1;	// the coordinates for the blue ships
		final double[] bluR = new double[4];
		final double[] bluT = new double[4];
		for (int i = 0; i < 4; i ++) {
			bluR[i] = (0.5+Math.random())*100000*Univ.km;
			bluT[i] = i*2*Math.PI/4 - Math.PI/4 + (Math.random()-0.5);
		}
		
		double time = (double)System.currentTimeMillis();	// the current time
		bodies = new ArrayList<Body>();
		bodies.add(new Planet(70000*Univ.km,-200000*Univ.km,43441*Univ.mi,"Jupiter",time,this));
		bodies.add(new Planet(-170000*Univ.km,110000*Univ.km,58232*Univ.km,"Saturn",time,this));
		
		bodies.add(new Carrier(		bluRC*Math.cos(bluTC),
									bluRC*Math.sin(bluTC), time, (byte)0, true, this));
		bodies.add(new Battleship(	bluRC*Math.cos(bluTC)+bluR[0]*Math.cos(bluT[0]),
									bluRC*Math.sin(bluTC)+bluR[0]*Math.sin(bluT[0]), time, (byte)1, true, this));
		bodies.add(new Scout(		bluRC*Math.cos(bluTC)+bluR[1]*Math.cos(bluT[1]),
									bluRC*Math.sin(bluTC)+bluR[1]*Math.sin(bluT[1]), time, (byte)2, true, this));
		bodies.add(new Radar(		bluRC*Math.cos(bluTC)+bluR[2]*Math.cos(bluT[2]),
									bluRC*Math.sin(bluTC)+bluR[2]*Math.sin(bluT[2]), time, (byte)3, true, this));
		bodies.add(new Steamship(	bluRC*Math.cos(bluTC)+bluR[3]*Math.cos(bluT[3]),
									bluRC*Math.sin(bluTC)+bluR[3]*Math.sin(bluT[3]), time, (byte)4, true, this));
	}
	
	
	
	public ArrayList<Body> getBodies() {
		return bodies;
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
			bodies.get(i).update(t);
	}
	
	
	public void spawn(Body b) {	// adds a new body to the battlefield
		bodies.add(b);
	}
	
	
	public Carrier getBlueCarrier() {
		return (Carrier) bodies.get(2);
	}

}

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
package interaction;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import mechanics.Battlefield;
import mechanics.Ship;
import network.Protocol;

/**
 * The class that handles user input during the pre-game
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class ShipPlacer extends Controller {

	private byte heldShip;			// the type of ship we hold
	private int numShips;			// the number of ships we have placed
	private boolean[] available;	// which ship classes are still available
	
	
	
	public ShipPlacer(GameScreen gs, Battlefield bf) {
		super(gs, bf);
		
		heldShip = -1;
		numShips = 0;
		available = new boolean[Ship.ALL_TYPES.length];
		for (int i = 0; i < available.length; i ++)
			available[i] = true;	// start with all ships available
		
		setHeldShip(Ship.CARRIER);	// pick up the carrier, since everyone must have a carrier
	}
	
	
	
	public byte getHeldShip() {
		return heldShip;
	}
	
	
	public void setHeldShip(byte type) {	// set the held ship to a certain type
		setHeldShip(type2typeIdx(type));
	}
	
	
	public void setHeldShip(int typeIdx) {	// set the held ship to the type at a certain index
		if (available[typeIdx])
			heldShip = Ship.ALL_TYPES[typeIdx];
		available[typeIdx] = false;
	}
	
	
	public void replaceShip() {	// set the heldShip back to -1 and set this ship available again
		if (heldShip == Ship.CARRIER)	return;	// you can't put down a carrier
		
		if (heldShip >= 0) {
			available[type2typeIdx(heldShip)] = true;
		}
		heldShip = -1;
	}
	
	
	public boolean isAvailable(byte type) {
		return isAvailable(type2typeIdx(type));
	}
	
	
	public boolean isAvailable(int typeIdx) {	// has this type of ship been placed yet?
		return available[typeIdx];
	}
	
	
	private int type2typeIdx(byte type) {	// perform a tiny search for this type in Ship.ALL_TYPES
		for (int i = 0; i < Ship.ALL_TYPES.length; i ++) 
			if (Ship.ALL_TYPES[i] == type)
				return i;	//XXX: I'm thinking there's probably a better way to do this
		return -1;	// if the type does not exist
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		final byte mpos = view.getMousePos(x, y, System.currentTimeMillis());
		if (mpos < -1) {
			replaceShip();
		}
		else if (mpos > -1) {
			setHeldShip(mpos);
		}
		else if (heldShip != -1) {
			final double sx = view.spaceXFscreenX(x);
			final double sy = view.spaceYFscreenY(y);
			final byte id = game.getIDs()[numShips];
			
			game.receive(Protocol.writePlacement(id, getHeldShip(), sx, sy));
			heldShip = -1;
			numShips ++;
			
			if (numShips == 5)
				view.startGame();
		}
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			replaceShip();
		else if (e.getKeyCode() == KeyEvent.VK_A)
			setHeldShip(0);
		else if (e.getKeyCode() == KeyEvent.VK_S)
			setHeldShip(1);
		else if (e.getKeyCode() == KeyEvent.VK_D)
			setHeldShip(2);
		else if (e.getKeyCode() == KeyEvent.VK_F)
			setHeldShip(3);
		else if (e.getKeyCode() == KeyEvent.VK_G)
			setHeldShip(4);
	}

}

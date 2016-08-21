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
		available = new boolean[max(Ship.ALL_TYPES)+1];
		for (int i = 0; i < available.length; i ++)
			available[i] = true;	// start with all ships available
		
		setHeldShip(Ship.CARRIER);	// pick up the carrier, since everyone must have a carrier
	}
	
	
	
	public byte getHeldShip() {
		return heldShip;
	}
	
	
	public void setHeldShip(byte type) {	// set the held ship to the type at a certain index
		if (heldShip == Ship.CARRIER)	return;	// you can't put down a carrier
		
		if (heldShip != -1)
			available[heldShip] = true;	// put back the current ship
		
		if (type == -1 || available[type])
			heldShip = type;	// and take the new one
		if (type != -1)
			available[type] = false;	// remove the selected ship
	}
	
	
	public boolean isAvailable(byte type) {
		return available[type];
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		final byte mpos = view.getMousePos(x, y, System.currentTimeMillis());
		if (mpos != -2) {		// if you clicked anywhere but empty space
			if (heldShip == mpos)	// if you clicked on the place where you picked up this one
				setHeldShip((byte) -1);
			else
				setHeldShip(mpos);	// pick up a ship
		}
		else if (heldShip >= 0) {	// if you clicked on empty space and are holding a ship
			game.receive(Protocol.writePlacement(game.getIDs()[numShips],
												 heldShip,
												 view.spaceXFscreenX(x),
												 view.spaceYFscreenY(y)));	// place it
			heldShip = -1;
			numShips ++;
			
			if (numShips == 5)
				view.startGame();
		}
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			setHeldShip((byte) -1);
		else if (e.getKeyCode() == KeyEvent.VK_A)
			setHeldShip(Ship.ALL_TYPES[0]);
		else if (e.getKeyCode() == KeyEvent.VK_S)
			setHeldShip(Ship.ALL_TYPES[1]);
		else if (e.getKeyCode() == KeyEvent.VK_D)
			setHeldShip(Ship.ALL_TYPES[2]);
		else if (e.getKeyCode() == KeyEvent.VK_F)
			setHeldShip(Ship.ALL_TYPES[3]);
		else if (e.getKeyCode() == KeyEvent.VK_G)
			setHeldShip(Ship.ALL_TYPES[4]);
	}
	
	
	
	private final static byte max(byte[] arr) {
		byte max = Byte.MIN_VALUE;
		for (byte b: arr)
			if (b > max)
				max = b;
		return max;
	}

}

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

	private byte heldShip;		// the type of ship we hold
	private int numShips;		// the number of ships we have placed
	
	
	
	public ShipPlacer(GameScreen gs, Battlefield bf) {
		super(gs, bf);
		
		heldShip = -1;
		numShips = 0;
	}
	
	
	
	public byte getHeldShip() {
		return heldShip;
	}
	
	
	public void setHeldShip(byte newShip) {
		heldShip = newShip;
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		final byte mpos = view.getMousePos(x, y, System.currentTimeMillis());
		if (mpos != -1) {
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
			heldShip = -1;
		else if (e.getKeyCode() == KeyEvent.VK_A)
			heldShip = Ship.CARRIER;
		else if (e.getKeyCode() == KeyEvent.VK_S)
			heldShip = Ship.BATTLESHIP;
		else if (e.getKeyCode() == KeyEvent.VK_D)
			heldShip = Ship.SCOUT;
		else if (e.getKeyCode() == KeyEvent.VK_F)
			heldShip = Ship.RADAR;
		else if (e.getKeyCode() == KeyEvent.VK_G)
			heldShip = Ship.STEAMSHIP;
	}

}

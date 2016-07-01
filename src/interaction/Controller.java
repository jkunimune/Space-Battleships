/**
 * A class to take mouse input and interact with the game
 */
package interaction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.ByteBuffer;

import mechanics.Battlefield;
import mechanics.Carrier;

/**
 * @author jkunimune
 * @version 1.0
 */
public class Controller implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

	private GameScreen view;	// the GameScreen this listens to
	private Carrier ship;		// the ship that this interacts through
	
	byte orderMode;				// the type of order being given (-1 for none, -2 for move, -3 for shoot, -4 for special)
	byte activeShip;				// the ship id being ordered (-1 for none, 0-4 for respective indices)
	
	
	
	public Controller(GameScreen gs, Battlefield bf) {
		view = gs;
		ship = bf.getBlueCarrier();
		
		orderMode = -1;
		activeShip = 4;
	}
	
	
	
	private byte[] composeOrder(byte order, byte ship, int mx, int my, long t) {	// composes a byte[] that includes critical information about an order
		ByteBuffer output = ByteBuffer.wrap(new byte[26]);
		output.put(0, order);
		output.put(1, ship);
		output.putDouble(2, view.spaceXFscreenX(mx));
		output.putDouble(10, view.spaceYFscreenY(my));
		output.putDouble(18, (double)t);
		return output.array();
	}
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		view.zoom(e.getWheelRotation());
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {	// when the mouse is released...
		byte mPos = view.getMousePos(e.getLocationOnScreen());
		if (orderMode < -1 && activeShip >= 0) {		// if an order and a ship were active
			ship.issueOrder(composeOrder(orderMode, activeShip, e.getX(), e.getY(), System.currentTimeMillis()));
			orderMode = -1;
			activeShip = 4;
		}
		else if (mPos < -1)			// if a button was clicked on
			orderMode = mPos;
		else if (mPos >= 0)			// if a ship was clicked on
			activeShip = mPos;
		else if (orderMode < -1)	// if there is an active order
			orderMode = mPos;
		//else if (activeShip >= 0)	// if there is an active ship
		//	activeShip = mPos;
	}
	
	
	@Override
	public void mousePressed(MouseEvent arg0) {	// when the mouse is pressed...
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

}

package interaction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.ByteBuffer;

import mechanics.Battlefield;
import mechanics.Carrier;

/**
 * A class to take mouse input and interact with the game
 * @author jkunimune
 */
public class Controller implements MouseListener, KeyListener {

	private GameScreen view;	// the GameScreen this listens to
	private Carrier ship;		// the ship that this interacts through
	
	byte orderMode;				// the type of order being given (-1 for none, -2 for move, -3 for shoot, -4 for special)
	byte activeShip;				// the ship id being ordered (-1 for none, 0-4 for respective indicies)
	
	
	
	public Controller(GameScreen gs, Battlefield bf) {
		view = gs;
		ship = bf.getBlueCarrier();
		
		orderMode = -1;
		activeShip = 1;
	}
	
	
	
	private byte[] composeOrder(byte order, byte ship, long t) {	// composes a byte[] that includes critical information about an order
		ByteBuffer output = ByteBuffer.wrap(new byte[10]);
		output.put(0, order);
		output.put(1, ship);
		output.putLong(2, t);
		return output.array();
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {	// when the mouse is released...
		byte mPos = view.getMousePos(e.getPoint());
		if (orderMode < -1 && activeShip >= 0) {		// if an order and a ship were active
			ship.issueOrder(composeOrder(orderMode, activeShip, System.currentTimeMillis()));
			orderMode = -1;
			activeShip = 1;
		}
		else if (mPos < -1)			// if a button was clicked on
			orderMode = mPos;
		else if (mPos >= 0)			// if a ship was clicked on
			activeShip = mPos;
		else if (orderMode < -1)	// if there is an active order
			orderMode = mPos;
		else if (activeShip >= 0)	// if there is an active ship
			activeShip = mPos;
	}
	
	
	@Override
	public void mousePressed(MouseEvent arg0) {	// when the mouse is pressed...
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent arg0) {System.out.println("B");}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}

}

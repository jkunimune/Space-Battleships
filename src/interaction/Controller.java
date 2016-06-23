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
	
	int orderMode;				// the type of order being given (-1 for none, -2 for move, -3 for shoot, -4 for special)
	int activeShip;				// the ship id being ordered (-1 for none, 0-4 for respective indicies)
	
	
	
	public Controller(GameScreen gs, Battlefield bf) {
		view = gs;
		ship = bf.getBlueCarrier();
		
		orderMode = -1;
		activeShip = -1;
	}
	
	
	
	private byte[] composeOrder(int order, int ship, int x, int y, long t) {	// composes a byte[] that includes critical information about an order
		ByteBuffer output = ByteBuffer.wrap(new byte[17]);
		output.put(0, (byte)(8*ship-order));
		output.putInt(1, x);
		output.putInt(5, y);
		output.putLong(9, t);
		return output.array();
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {	// when the mouse is released...
		int mPos = view.getMousePos(e.getPoint());
		if (orderMode < -1) {				// if an order was active
			ship.issueOrder(composeOrder(orderMode, activeShip, e.getX(), e.getY(), System.currentTimeMillis()));
			orderMode = -1;
			activeShip = -1;
		}
		else if (mPos < 0)	// if a button was clicked on and no order is active
			orderMode = mPos;
		else if (mPos >= 0)	// if a ship was clicked on and no order is active
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

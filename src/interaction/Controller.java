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
import mechanics.Ship;

/**
 * @author jkunimune
 * @version 1.0
 */
public class Controller implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

	private GameScreen view;	// the GameScreen this listens to
	private Carrier ship;		// the ship that this interacts through
	
	private byte orderMode;				// the type of order being given (-1 for none, -2 for move, -3 for shoot, -4 for special)
	private byte activeShip;				// the ship id being ordered (-1 for none, 0-4 for respective indices)
	
	int x, y;	// mouse location
	
	
	
	public Controller(GameScreen gs, Battlefield bf) {
		view = gs;
		ship = bf.getBlueCarrier();
		
		orderMode = -1;
		activeShip = -1;
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
	
	
	public byte getOrder() {
		return orderMode;
	}
	
	
	public byte getShip() {
		return activeShip;
	}
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		view.zoom(e.getWheelRotation(), e.getX(), e.getY());
	}
	
	
	@Override
	public void mouseDragged(MouseEvent e) {	// shift the view accordingly
		view.pan(e.getX()-x, e.getY()-y);
		x = e.getX();
		y = e.getY();
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {	// when the mouse is released...
		byte mPos = view.getMousePos(e.getX(), e.getY(), e.getWhen());
		if (orderMode < -1 && activeShip >= 0) {		// if an order and a ship were active
			ship.issueOrder(composeOrder(orderMode, activeShip, e.getX(), e.getY(), System.currentTimeMillis()));
			orderMode = -1;
			activeShip = -1;
		}
		else if (mPos < -1)			// if a button was clicked on
			orderMode = mPos;
		else if (mPos >= 0)			// if a ship was clicked on
			activeShip = mPos;//activeShip = mPos;
		else if (orderMode < -1)	// if there is an active order
			orderMode = mPos;
		else if (activeShip >= 0)	// if there is an active ship
			activeShip = mPos;
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_1)	// number keys select ships
			activeShip = ((Ship) view.getField().getBodies().get(0)).getID();
		else if (e.getKeyCode() == KeyEvent.VK_2)
			activeShip = ((Ship) view.getField().getBodies().get(1)).getID();
		else if (e.getKeyCode() == KeyEvent.VK_3)
			activeShip = ((Ship) view.getField().getBodies().get(2)).getID();
		else if (e.getKeyCode() == KeyEvent.VK_4)
			activeShip = ((Ship) view.getField().getBodies().get(3)).getID();
		else if (e.getKeyCode() == KeyEvent.VK_5)
			activeShip = ((Ship) view.getField().getBodies().get(4)).getID();
		else if (e.getKeyCode() == KeyEvent.VK_X)	// X is special
			orderMode = -4;
		else if (e.getKeyCode() == KeyEvent.VK_B)	// B is bombard
			orderMode = -3;
		else if (e.getKeyCode() == KeyEvent.VK_M)	// M is move
			orderMode = -2;
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {	// when the mouse is pressed...
		x = e.getX();
		y = e.getY();
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {}

}

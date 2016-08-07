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

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import mechanics.Battlefield;
import network.Protocol;

/**
 * A class to take mouse input and interact with the game
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Controller implements MouseWheelListener, MouseMotionListener, MouseListener, KeyListener {

	private GameScreen view;	// the GameScreen this listens to
	private Battlefield game;		// the ship that this interacts through
	
	private byte orderMode;				// the type of order being given (-1 for none, -2 for move, -3 for shoot, -4 for special)
	private byte activeShip;				// the ship id being ordered (-1 for none, 0-4 for respective indices)
	
	private int x, y;	// mouse location
	
	
	
	public Controller(GameScreen gs, Battlefield bf) {
		view = gs;
		game = bf;
		
		orderMode = -1;
		activeShip = -1;
		
		x = 0;
		y = 0;
	}
	
	
	
	public byte getOrder() {
		return orderMode;
	}
	
	
	private void setOrder(byte o) {
		orderMode = o;
	}
	
	
	public byte getShip() {
		return activeShip;
	}
	
	
	private void setShip(byte s) {
		activeShip = s;
		view.setShip(s);
	}
	
	
	public Point getMouseLocation() {
		return new Point(x,y);
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
	public void mouseMoved(MouseEvent e) {		// update x and y
		x = e.getX();
		y = e.getY();
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {	// when the mouse is clicked...
		final byte mPos = view.getMousePos(e.getX(), e.getY(), e.getWhen());
		
		if (orderMode < -1 && activeShip >= 0) {		// if an order and a ship were active
			final double sx = view.spaceXFscreenX(e.getX());
			final double sy = view.spaceYFscreenY(e.getY());
			game.receive(Protocol.composeOrder(orderMode, activeShip, sx, sy,	// give the order to the game
					                  (double) System.currentTimeMillis()), true);
			setOrder((byte) -1);
		}
		else if (mPos < -1)			// if a button was clicked on
			setOrder(mPos);
		else if (mPos >= 0)			// if a ship was clicked on
			setShip(mPos);
		else if (orderMode < -1)	// if there is an active order
			setOrder(mPos);
		else if (activeShip >= 0)	// if there is an active ship
			setShip(mPos);
	}
	
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_1)	// number keys select ships
			setShip(game.getIDs()[0]);
		else if (e.getKeyCode() == KeyEvent.VK_2)
			setShip(game.getIDs()[1]);
		else if (e.getKeyCode() == KeyEvent.VK_3)
			setShip(game.getIDs()[2]);
		else if (e.getKeyCode() == KeyEvent.VK_4)
			setShip(game.getIDs()[3]);
		else if (e.getKeyCode() == KeyEvent.VK_5)
			setShip(game.getIDs()[4]);
		else if (e.getKeyCode() == KeyEvent.VK_X)	// X is special
			setOrder((byte) -4);
		else if (e.getKeyCode() == KeyEvent.VK_B)	// B is bombard
			setOrder((byte) -3);
		else if (e.getKeyCode() == KeyEvent.VK_M)	// M is move
			setOrder((byte) -2);
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {	// pressing escape cancels all attacks
			setOrder((byte) -1);
			setShip((byte) -1);
		}
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

}

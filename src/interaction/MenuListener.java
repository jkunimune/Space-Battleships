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

/**
 * A MouseListener explicitly for the Menu
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class MenuListener implements MouseListener, MouseMotionListener, KeyListener {

	private Menu menu;
	
	private boolean isPressed;	// left click state
	private int x, y;			// mouse location
	private String[] textbox;	// input box
	private String lastInput;	// last string to be saved
	
	
	
	public MenuListener(Menu m) {
		menu = m;
		
		isPressed = false;
		x = 0;
		y = 0;
		textbox = null;
		lastInput = null;
	}
	
	
	
	public boolean getMousePressed() {
		return isPressed;
	}
	
	
	public int getX() {
		return x;
	}
	
	
	public int getY() {
		return y;
	}
	
	
	public Point getMouseLocation() {
		return new Point(x,y);
	}
	
	
	public String getInput() {
		return lastInput;
	}
	
	
	public void setTextbox(String[] newTB) {
		textbox = newTB;
	}
	
	
	public void deleteTextbox() {
		if (textbox != null) {
			lastInput = textbox[2];
			textbox = null;
		}
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		menu.interpCommand(menu.getCommand(menu.getMousePos(e.getX(), e.getY())));
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			isPressed = true;
	}
	
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			isPressed = false;
	}
	
	
	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {	// for entering text TODO: Enter?
		if (textbox != null) {
			if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
				if (textbox[2].length() >= 1)					// backspaces space backward
					textbox[2] = textbox[2].substring(0, textbox[2].length()-1);
			}
			else if (e.getKeyChar() != KeyEvent.VK_DELETE &&	// delete and esc should be ignored
					 e.getKeyChar() != KeyEvent.VK_ESCAPE) {
				textbox[2] += e.getKeyChar();	// if it is an ordinary character, then type it
			}
		}
	}
	
	
	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent arg0) {}

}

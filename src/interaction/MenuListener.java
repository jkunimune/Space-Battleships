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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import mechanics.Battlefield;

/**
 * A MouseListener explicitly for the Menu
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class MenuListener implements MouseListener, MouseMotionListener {

	private Menu menu;
	
	private boolean isPressed;
	private int x, y;	// mouse location
	
	
	
	public MenuListener(Menu m) {
		menu = m;
		
		isPressed = false;
		x = 0;
		y = 0;
	}
	
	
	
	public boolean getMousePressed() {
		return isPressed;
	}
	
	
	public Point getMouseLocation() {
		return new Point(x,y);
	}
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(menu);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		isPressed = true;
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		isPressed = false;
	}



	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
	}



	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {}

}

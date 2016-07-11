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

import javax.swing.JFrame;
import javax.swing.JPanel;

import mechanics.Battlefield;

/**
 * The class to handle all high-level display stuff for my game. This class handles
 * user input, <code>JPanel</code> and <code>JFrame</code> objects, and stuff of the sort.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Screen {	// TODO: implement Menu

	private JFrame frame;
	private JPanel panel;
	
	private int width, height;
	
	
	
	public Screen(int w, int h) {
		frame = new JFrame("Space Battleships!");
		panel = new Menu(w,h);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(w,h);
		frame.setResizable(true);
		frame.setFocusable(true);
		
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.pack();
		
		width = w;
		height = h;
	}
	
	
	
	public void lookAt(Battlefield field) {	// sets the panel to a GameScreen focused on field
		frame.remove(panel);
		panel = new GameScreen(width, height, field);
		Controller listener = new Controller((GameScreen) panel, field);
		((GameScreen) panel).addListener(listener);
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.pack();
		((GameScreen) panel).developStrategy();
	}



	public void display() {
		panel.setVisible(true);
	}

}

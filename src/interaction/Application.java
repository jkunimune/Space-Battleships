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
import network.Client;
import network.Connection;

/**
 * The class to handle all high-level display stuff for my game. This class handles
 * user input, <code>JPanel</code> and <code>JFrame</code> objects, and stuff of the sort.
 * 
 * @author	jkunimune
 * @version	1.0
 */
public class Application {

	public static final byte MENU = 0;
	public static final byte GAME = 0;
	
	
	private JFrame frame;
	private JPanel panel;
	
	private int width, height;
	
	
	
	public Application(int w, int h) {
		frame = new JFrame("Space Battleships!");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(w,h);
		frame.setResizable(true);
		frame.setFocusable(true);
		
		width = w;
		height = h;
		
		goToMenu();
	}
	
	
	
	public void startGame(Connection c) {	// either join or host a game
		Battlefield bf = new Battlefield(c.getOutput(), c.isHost());
		lookAt(bf);
		Client.startListening(c.getInput(), bf);
	}
	
	
	public void goToMenu() {	// goes to the main menu
		try {
			frame.remove(panel);
		} catch (NullPointerException e) {}
		panel = new Menu(width, height, "main", this);
		frame.setContentPane(panel);
		frame.pack();
		((Menu) panel).developStrategy();
		frame.setVisible(true);
	}
	
	
	public void lookAt(Battlefield field) {	// sets the panel to a GameScreen focused on field
		frame.remove(panel);
		panel = new GameScreen(width, height, field, this);
		frame.setContentPane(panel);
		frame.pack();
		((GameScreen) panel).developStrategy();
		frame.setVisible(true);
	}
	
	
	public void display() {
		panel.setVisible(true);
	}
	
	
	public String getState() {
		try {
			if (panel instanceof Menu)
				return ((Menu) panel).getState();
			return "Game";
		} catch (NullPointerException e) {
			System.err.println("Look at me! I'm a target!");
			return "ERROR";
		}
	}

}

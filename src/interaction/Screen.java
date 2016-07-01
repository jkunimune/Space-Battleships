/**
 * The class to handle all mouse input, JFrames, JPanels, and other high-level display stuff.
 */
package interaction;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mechanics.Battlefield;

/**
 * @author jkunimune
 * @version 1.0
 */
public class Screen {

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

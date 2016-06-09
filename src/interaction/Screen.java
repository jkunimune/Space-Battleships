package interaction;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mechanics.Battlefield;

/**
 * @author jkunimune
 * The class to handle all mouse input, JFrames, JPanels, and other high-level display stuff.
 */
public class Screen {

	private JFrame frame;
	private JPanel panel;
	
	
	
	public Screen(int w, int h) {
		
		frame = new JFrame("Space Battleships!");
		panel = new Menu(w,h);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(w,h);
		frame.setResizable(true);
		
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.pack();
	}
	
	
	
	public void lookAt(Battlefield field) {	// sets the panel to a GameScreen focused on field
		frame.remove(panel);
		panel = new GameScreen(frame.getWidth(), frame.getHeight(), field);
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.pack();
		((GameScreen) panel).developStrategy();
	}



	public void display() {
		panel.setVisible(true);
	}

}

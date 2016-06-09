package interaction;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JPanel;

import mechanics.Battlefield;

/**
 * @author jkunimune
 * The JPanel that displays the gameplay objects and HUD during a match.
 */
public class GameScreen extends JPanel {

	private static final long serialVersionUID = -4461350953048532763L;
	
	private HashMap<String, BufferedImage> sprites;
	private Battlefield space;
	
	
	public GameScreen(Battlefield field) {
		super();
		space = field;
	}

}
